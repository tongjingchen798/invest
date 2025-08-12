

package io.renren.controller;

import io.renren.annotation.LoginUser;
import io.renren.common.utils.Result;
import io.renren.common.validator.ValidatorUtils;
import io.renren.dto.ChangePasswordDTO;
import io.renren.dto.ChangeTwoPasswordDTO;
import io.renren.dto.UpdateUserDTO;
import io.renren.dto.UserInfoDTO;
import io.renren.entity.UserEntity;
import io.renren.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理接口
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/api/user")
@Api(tags = "用户管理接口")
public class ApiUserController {
    @Autowired
    private UserService userService;

    @GetMapping("/userInfo")
    @ApiOperation("获取用户信息")
    public Result<UserInfoDTO> getUserInfo(@LoginUser UserEntity user) {
        // 使用Service方法获取用户信息（包含上级用户信息）
        UserInfoDTO userInfo = userService.getUserInfoWithSuperior(user.getId());
        
        if (userInfo == null) {
            return new Result<UserInfoDTO>().error("用户不存在");
        }
        
        return new Result<UserInfoDTO>().ok(userInfo);
    }

    @PostMapping("update")
    @ApiOperation("更新用户信息")
    public Result updateUserInfo(@LoginUser UserEntity user, @RequestBody UpdateUserDTO dto) {
        // 表单校验
        ValidatorUtils.validateEntity(dto);

        // 更新用户信息
        if (dto.getUsername() != null) {
            user.setUsername(dto.getUsername());
        }
        if (dto.getInviteCode() != null) {
            user.setInviteCode(dto.getInviteCode());
        }
        if (dto.getAgent() != null) {
            user.setAgent(dto.getAgent());
        }
        if (dto.getChannel() != null) {
            user.setChannel(dto.getChannel());
        }
        if (dto.getEquipment() != null) {
            user.setEquipment(dto.getEquipment());
        }

        userService.updateById(user);
        return new Result();
    }

    @PostMapping("changePassword")
    @ApiOperation("修改密码")
    public Result changePassword(@LoginUser UserEntity user, @RequestBody ChangePasswordDTO dto) {
        // 表单校验
        ValidatorUtils.validateEntity(dto);

        // 验证原密码
        if (!user.getPassword().equals(DigestUtils.sha256Hex(dto.getOldPassword()))) {
            return new Result().error("原密码不正确");
        }

        // 验证新密码确认
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            return new Result().error("两次输入的新密码不一致");
        }

        // 更新密码
        user.setPassword(DigestUtils.sha256Hex(dto.getNewPassword()));
        userService.updateById(user);

        return new Result();
    }

    @PostMapping("changeTwoPassword")
    @ApiOperation("修改二级密码")
    public Result changeTwoPassword(@LoginUser UserEntity user, @RequestBody ChangeTwoPasswordDTO dto) {
        // 表单校验
        ValidatorUtils.validateEntity(dto);

        // 验证原二级密码
        if (user.getTwoPwd() != null && !user.getTwoPwd().equals(DigestUtils.sha256Hex(dto.getOldTwoPassword()))) {
            return new Result().error("原二级密码不正确");
        }

        // 验证新二级密码确认
        if (!dto.getNewTwoPassword().equals(dto.getConfirmTwoPassword())) {
            return new Result().error("两次输入的新二级密码不一致");
        }

        // 更新二级密码
        user.setTwoPwd(DigestUtils.sha256Hex(dto.getNewTwoPassword()));
        userService.updateById(user);

        return new Result();
    }

    @PostMapping("logout")
    @ApiOperation("退出登录")
    public Result logout(@LoginUser UserEntity user) {
        // 这里可以添加token失效逻辑
        return new Result();
    }
}
