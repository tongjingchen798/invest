package io.renren.controller;

import io.renren.annotation.Login;
import io.renren.annotation.LoginUser;
import io.renren.common.utils.Result;
import io.renren.entity.UserEntity;
import io.renren.service.WithdrawService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 提现接口
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@RestController
@RequestMapping("/api/withdraw")
@Api(tags = "提现接口")
public class ApiWithdrawController {

    @Autowired
    private WithdrawService withdrawService;

    @Login
    @PostMapping("scFalg")
    @ApiOperation("是否首次提现")
    public Result<Map<String, Object>> checkFirstWithdraw(@LoginUser UserEntity user) {
        try {
            Map<String, Object> result = withdrawService.checkFirstWithdraw(user.getId());
            return new Result<Map<String, Object>>().ok(result);
        } catch (Exception e) {
            return new Result<Map<String, Object>>().error("检查首次提现状态失败: " + e.getMessage());
        }
    }
}
