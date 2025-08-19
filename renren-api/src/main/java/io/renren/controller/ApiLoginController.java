 

package io.renren.controller;


import io.renren.annotation.Login;
import io.renren.common.utils.Result;
import io.renren.common.validator.ValidatorUtils;
import io.renren.dto.LoginDTO;
import io.renren.dto.CustomerServiceDTO;
import io.renren.dto.RetrievePasswordDTO;
import io.renren.dto.UpdatePasswordDTO;
import io.renren.service.TokenService;
import io.renren.service.UserService;
import io.renren.service.CustomerServiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * 登录接口
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/api")
@Api(tags="登录接口")
public class ApiLoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private CustomerServiceService customerServiceService;


    @PostMapping("login")
    @ApiOperation("登录")
    public Result<Map<String, Object>> login(@RequestBody LoginDTO dto){
        //表单校验
        ValidatorUtils.validateEntity(dto);

        //用户登录
        Map<String, Object> map = userService.login(dto);

        return new Result().ok(map);
    }

    @Login
    @PostMapping("logout")
    @ApiOperation("退出")
    public Result logout(@ApiIgnore @RequestAttribute("userId") Long userId){
        tokenService.expireToken(userId);
        return new Result();
    }

    @GetMapping("nologinwslist")
    @ApiOperation("获取客服号列表")
    public Result<List<CustomerServiceDTO>> nologinwslist(){
        List<CustomerServiceDTO> customerServices = customerServiceService.getAllCustomerServices();
        return new Result().ok(customerServices);
    }

    @Login
    @GetMapping("wslist")
    @ApiOperation("返回对应的客服号(登录)")
    public Result<List<CustomerServiceDTO>> wslist(){
        List<CustomerServiceDTO> customerServices = customerServiceService.getAllCustomerServices();
        return new Result().ok(customerServices);
    }

	@PostMapping("retrieve")
	@ApiOperation("找回密码")
	public Result retrievePassword(@RequestBody RetrievePasswordDTO dto) {
		try {
			// 表单校验
			ValidatorUtils.validateEntity(dto);
			
//			// 验证手机号格式（10位数字）
//			if (!dto.getMobile().matches("^\\d{10}$")) {
//				return new Result().error("手机号格式错误");
//			}
//
			// 验证两次密码是否一致
			if (!dto.getPassword().equals(dto.getPassword2())) {
				return new Result().error("两次输入的密码不一致");
			}
			
			// 验证短信验证码（这里需要根据实际业务逻辑实现）
			// TODO: 调用短信验证码验证服务
			
			// 更新用户密码
			boolean success = userService.updatePasswordByMobile(dto.getMobile(), dto.getPassword());
			
			if (success) {
				return new Result().ok("密码重置成功");
			} else {
				return new Result().error("密码重置失败，请检查手机号是否正确");
			}
			
		} catch (Exception e) {
			return new Result().error("找回密码失败: " + e.getMessage());
		}
	}

	@Login
	@PostMapping("updatePws")
	@ApiOperation("修改密码")
	public Result updatePassword(@RequestBody UpdatePasswordDTO dto, @ApiIgnore @RequestAttribute("userId") Long userId) {
		try {
			// 表单校验
			ValidatorUtils.validateEntity(dto);
			
			// 验证两次密码是否一致
			if (!dto.getPassword().equals(dto.getPassword2())) {
				return new Result().error("两次输入的密码不一致");
			}
			
			// 验证短信验证码（这里需要根据实际业务逻辑实现）
			// TODO: 调用短信验证码验证服务
			
			// 更新用户密码
			boolean success = userService.updatePasswordByUserId(userId, dto.getPassword());
			
			if (success) {
				return new Result().ok("密码修改成功");
			} else {
				return new Result().error("密码修改失败");
			}
			
		} catch (Exception e) {
			return new Result().error("修改密码失败: " + e.getMessage());
		}
	}

}