package io.renren.controller;

import io.renren.annotation.Login;
import io.renren.annotation.LoginUser;
import io.renren.common.utils.Result;
import io.renren.dto.VipClaimStatusDTO;
import io.renren.entity.UserEntity;
import io.renren.service.VipClaimService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * VIP领取状态接口
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@RestController
@RequestMapping("/api")
@Api(tags = "VIP领取状态接口")
public class ApiVipController {
    
    @Autowired
    private VipClaimService vipClaimService;
    
    @Login
    @GetMapping("userVipLQInfo")
    @ApiOperation("查询用户可领取的等级")
    public Result<VipClaimStatusDTO> getUserVipClaimInfo(@LoginUser UserEntity user) {
        try {
            // 获取用户VIP领取状态
            VipClaimStatusDTO status = vipClaimService.getUserVipClaimStatus(user.getId());
            return new Result<VipClaimStatusDTO>().ok(status);
            
        } catch (Exception e) {
            return new Result<VipClaimStatusDTO>().error("获取VIP信息失败: " + e.getMessage());
        }
    }
    
    @Login
    @PostMapping("userVipLQ")
    @ApiOperation("领取等级奖励")
    public Result<Map<String, Object>> claimVipLevel(
            @ApiParam(value = "VIP等级", required = true) @RequestParam Integer vip,
            @LoginUser UserEntity user) {
        try {
            // 检查用户是否可以领取该VIP等级
            if (!vipClaimService.canClaimVipLevel(user.getId(), vip)) {
                return new Result<Map<String, Object>>().error("不满足领取条件");
            }
            
            // 领取VIP等级
            boolean success = vipClaimService.claimVipLevel(user.getId(), vip);
            if (!success) {
                return new Result<Map<String, Object>>().error("领取失败");
            }
            
            // 构建返回数据
            Map<String, Object> result = new HashMap<>();
            result.put("vipLevel", vip);
            result.put("claimTime", System.currentTimeMillis());
            result.put("status", "claimed");
            result.put("userId", user.getId());
            
            return new Result<Map<String, Object>>().ok(result);
            
        } catch (Exception e) {
            return new Result<Map<String, Object>>().error("领取失败: " + e.getMessage());
        }
    }
}
