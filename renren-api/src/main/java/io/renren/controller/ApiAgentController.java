package io.renren.controller;

import io.renren.annotation.Login;
import io.renren.annotation.LoginUser;
import io.renren.common.utils.Result;
import io.renren.dto.MyAgentDTO;
import io.renren.entity.UserEntity;
import io.renren.service.AgentCommissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 代理佣金接口
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@RestController
@RequestMapping("/api/agent")
@Api(tags = "代理佣金接口")
public class ApiAgentController {
    
    @Autowired
    private AgentCommissionService agentCommissionService;
    
    @Login
    @PostMapping("myAgent/{userId}")
    @ApiOperation("我的佣金")
    public Result<MyAgentDTO> getMyAgentCommission(
            @ApiParam(value = "用户ID", required = true) @PathVariable Long userId,
            @LoginUser UserEntity user) {
        try {
            // 验证用户权限（只能查询自己的佣金信息）
            if (!user.getId().equals(userId)) {
                return new Result<MyAgentDTO>().error("无权限查询其他用户的佣金信息");
            }
            
            // 获取佣金信息
            MyAgentDTO commission = agentCommissionService.getMyAgentCommission(userId);
            
            return new Result<MyAgentDTO>().ok(commission);
            
        } catch (Exception e) {
            return new Result<MyAgentDTO>().error("获取佣金信息失败: " + e.getMessage());
        }
    }
}
