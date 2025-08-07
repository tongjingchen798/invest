

package io.renren.controller;

import io.renren.common.utils.Result;
import io.renren.common.validator.ValidatorUtils;
import io.renren.entity.UserEntity;
import io.renren.dto.RegisterDTO;
import io.renren.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 注册接口
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/api")
@Api(tags="注册接口")
public class ApiRegisterController {
    @Autowired
    private UserService userService;

    @PostMapping("register")
    @ApiOperation("注册")
    public Result register(@RequestBody RegisterDTO dto){
        //表单校验
        ValidatorUtils.validateEntity(dto);

        UserEntity user = new UserEntity();
        user.setMobile(dto.getMobile());
        // 如果提供了真实姓名，使用真实姓名，否则使用手机号作为用户名
        user.setUsername(dto.getUsername() != null ? dto.getUsername() : dto.getMobile());
        user.setPassword(DigestUtils.sha256Hex(dto.getPassword()));
        
        // 设置新字段
        if (dto.getTwoPwd() != null) {
            user.setTwoPwd(DigestUtils.sha256Hex(dto.getTwoPwd()));
        }
        user.setInviteCode(dto.getInviteCode());
        user.setAgent(dto.getAgent());
        user.setChannel(dto.getChannel());
        user.setEquipment(dto.getEquipment());
        
        user.setCreateDate(new Date());
        userService.insert(user);

        return new Result();
    }
}