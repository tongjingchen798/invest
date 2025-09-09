 

package io.renren.controller;


import io.renren.annotation.Login;
import io.renren.common.exception.ErrorCode;
import io.renren.common.exception.RenException;
import io.renren.common.utils.Result;
import io.renren.common.validator.ValidatorUtils;
import io.renren.dto.LoginDTO;
import io.renren.dto.CustomerServiceDTO;
import io.renren.dto.RetrievePasswordDTO;
import io.renren.dto.UpdatePasswordDTO;
import io.renren.service.TokenService;
import io.renren.service.UserService;
import io.renren.service.CustomerServiceService;
import io.renren.common.utils.IpUtils;
import io.renren.dao.UserLogDao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import javax.servlet.http.HttpServletRequest;

import java.util.Date;
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
    @Autowired
    private UserLogDao userLogDao;


    @PostMapping("login")
    @ApiOperation("登录")
    public Result<Map<String, Object>> login(@RequestBody LoginDTO dto, HttpServletRequest request){
        //表单校验
        ValidatorUtils.validateEntity(dto);

        // 获取客户端IP地址
        String clientIp = IpUtils.getIpAddr(request);
        dto.setLoginIp(clientIp);
        
        // 如果没有设置设备类型，默认为未知
        if (dto.getEquipment() == null) {
            dto.setEquipment(1);
        }

        //用户登录
        Map<String, Object> map = userService.login(dto);

        return new Result().ok(map);
    }

    @Login
    @PostMapping("logout")
    @ApiOperation("退出")
    public Result logout(@ApiIgnore @RequestAttribute("userId") Long userId){
        try {
            // 使token失效
            tokenService.expireToken(userId);
            
            // 更新登出时间
            Date logoutTime = new Date();
            userLogDao.updateLogoutTime(userId, logoutTime);
            
            return new Result().ok("退出成功");
        } catch (Exception e) {
            // 即使更新登出时间失败，也要确保token失效
            e.printStackTrace();
            return new Result().ok("退出成功");
        }
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
				throw new RenException(ErrorCode.PASSWORD_NOT_MATCH);
			}
			
			// 验证短信验证码（这里需要根据实际业务逻辑实现）
			// TODO: 调用短信验证码验证服务
			
			// 更新用户密码
			boolean success = userService.updatePasswordByMobile(dto.getMobile(), dto.getPassword());
			
			if (success) {
				return new Result().ok("密码重置成功");
			} else {
				throw new RenException(ErrorCode.PASSWORD_RESET_FAILED);
			}
			
		} catch (Exception e) {
			throw new RenException(ErrorCode.FIND_PASSWORD_FAILED);
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
				throw new RenException(ErrorCode.PASSWORD_NOT_MATCH);
			}
			
			// 验证短信验证码（这里需要根据实际业务逻辑实现）
			// TODO: 调用短信验证码验证服务
			
			// 更新用户密码
			boolean success = userService.updatePasswordByUserId(userId, dto.getPassword());
			
			if (success) {
				return new Result().ok("密码修改成功");
			} else {
				throw new RenException(ErrorCode.PASSWORD_CHANGE_FAILED);
			}
			
		} catch (Exception e) {
			throw new RenException(ErrorCode.PASSWORD_CHANGE_FAILED);
		}
	}

}