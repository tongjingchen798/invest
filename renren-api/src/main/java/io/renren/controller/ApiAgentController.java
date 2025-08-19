package io.renren.controller;

import io.renren.annotation.Login;
import io.renren.annotation.LoginUser;
import io.renren.common.utils.Result;
import io.renren.dto.MyAgentDTO;
import io.renren.dto.AgentCenterDTO;
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
            @ApiParam(value = "用户ID") @PathVariable Long userId,
            @LoginUser UserEntity user) {
        MyAgentDTO commission = agentCommissionService.getMyAgentCommission(user.getId());
        return new Result<MyAgentDTO>().ok(commission);
    }

    @Login
    @PostMapping("{userId}")
    @ApiOperation("代理中心")
    public Result<AgentCenterDTO> getAgentCenter(@LoginUser UserEntity user) {
        AgentCenterDTO agentCenter = agentCommissionService.getAgentCenterData(user.getId());
        return new Result<AgentCenterDTO>().ok(agentCenter);
    }
}
