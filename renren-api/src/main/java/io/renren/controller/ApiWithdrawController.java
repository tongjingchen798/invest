package io.renren.controller;

import io.renren.annotation.Login;
import io.renren.annotation.LoginUser;
import io.renren.common.utils.Result;
import io.renren.dto.WithdrawPageData;
import io.renren.dto.WithdrawQueryDTO;
import io.renren.entity.UserEntity;
import io.renren.service.WithdrawService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    @Login
    @GetMapping("page")
    @ApiOperation("余额提现分页查询")
    public Result<WithdrawPageData> getWithdrawPage(
            @ApiParam(value = "每页显示记录数", required = true) @RequestParam Integer limit,
            @ApiParam(value = "当前页码，从1开始", required = true) @RequestParam Integer page,
            @ApiParam(value = "登录用户id", required = true) @RequestParam Long userId,
            @ApiParam(value = "排序方式，可选值(asc、desc)") @RequestParam(required = false) String order,
            @ApiParam(value = "排序字段") @RequestParam(required = false) String orderField,
            @ApiParam(value = "第三方订单号") @RequestParam(required = false) String orderno,
            @ApiParam(value = "卡号") @RequestParam(required = false) String payNo,
            @ApiParam(value = "状态 0 待审核 1审核通过  2 审核失败") @RequestParam(required = false) Integer state,
            @ApiParam(value = "我方订单号") @RequestParam(required = false) String transNo,
            @LoginUser UserEntity user) {
        try {
            // 验证用户权限
            if (!user.getId().equals(userId)) {
                return new Result<WithdrawPageData>().error("无权限查询其他用户的提现记录");
            }

            // 参数验证
            if (page == null || page < 1) {
                return new Result<WithdrawPageData>().error("页码必须大于0");
            }
            if (limit == null || limit < 1 || limit > 100) {
                return new Result<WithdrawPageData>().error("每页记录数必须在1-100之间");
            }

            // 构建查询参数
            WithdrawQueryDTO queryDTO = new WithdrawQueryDTO();
            queryDTO.setLimit(limit);
            queryDTO.setPage(page);
            queryDTO.setUserId(userId);
            queryDTO.setOrder(order);
            queryDTO.setOrderField(orderField);
            queryDTO.setOrderno(orderno);
            queryDTO.setPayNo(payNo);
            queryDTO.setState(state);
            queryDTO.setTransNo(transNo);

            // 执行查询
            WithdrawPageData pageData = withdrawService.getWithdrawPageData(queryDTO);
            return new Result<WithdrawPageData>().ok(pageData);

        } catch (Exception e) {
            return new Result<WithdrawPageData>().error("获取提现分页数据失败: " + e.getMessage());
        }
    }
}
