package io.renren.controller;

import io.renren.annotation.Login;
import io.renren.annotation.LoginUser;
import io.renren.common.utils.Result;
import io.renren.entity.UserEntity;
import io.renren.service.RedPacketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 红包接口
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/api/hb")
@Api(tags = "红包接口")
public class ApiRedPacketController {

    @Resource
    private RedPacketService redPacketService;

    @Login
    @PostMapping("lqhb")
    @ApiOperation("领取红包")
    public Result<Map<String, Object>> receiveRedPacket(
            @ApiParam(value = "口令码", required = true) @RequestParam("password") String password,
            @LoginUser UserEntity user,
            HttpServletRequest request) {
        
        // 获取客户端IP地址
        String ipAddress = getClientIpAddress(request);
        
        // 获取设备信息（可以从请求头中获取）
        String deviceInfo = request.getHeader("User-Agent");
        
        // 领取红包
        Map<String, Object> result = redPacketService.receiveRedPacket(
            password, 
            user.getId(), 
            user.getUsername(), 
            ipAddress, 
            deviceInfo
        );
        
        if ((Boolean) result.get("success")) {
            return new Result<Map<String, Object>>().ok(result);
        } else {
            return new Result<Map<String, Object>>().error((String) result.get("message"));
        }
    }
    
    /**
     * 获取客户端真实IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
