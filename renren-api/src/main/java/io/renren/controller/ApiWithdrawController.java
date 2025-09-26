package io.renren.controller;

import io.renren.annotation.Login;
import io.renren.annotation.LoginUser;
import io.renren.common.utils.Result;
import io.renren.dto.FirstWithdrawCheckDTO;
import io.renren.dto.RewardWithdrawRequestDTO;
import io.renren.dto.RewardWithdrawSumDTO;
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
    public Result<FirstWithdrawCheckDTO> checkFirstWithdraw(@LoginUser UserEntity user) {
        try {
            FirstWithdrawCheckDTO result = withdrawService.checkFirstWithdraw(user.getId());
            return new Result<FirstWithdrawCheckDTO>().ok(result);
        } catch (Exception e) {
            return new Result<FirstWithdrawCheckDTO>().error("检查首次提现状态失败: " + e.getMessage());
        }
    }

    @Login
    @GetMapping("page")
    @ApiOperation("提现记录分页查询")
    public Result<WithdrawPageData> getWithdrawPage(
            @ApiParam(value = "每页显示记录数", required = true) @RequestParam Integer limit,
            @ApiParam(value = "当前页码，从1开始", required = true) @RequestParam Integer page,
            @LoginUser UserEntity user) {
        try {
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
            queryDTO.setUserId(user.getId());

            // 执行查询
            WithdrawPageData pageData = withdrawService.getWithdrawPageData(queryDTO);
            return new Result<WithdrawPageData>().ok(pageData);

        } catch (Exception e) {
            return new Result<WithdrawPageData>().error("获取提现分页数据失败: " + e.getMessage());
        }
    }

    @Login
    @GetMapping("rewardWithdraw")
    @ApiOperation("佣金提现记录分页查询")
    public Result<WithdrawPageData> getRewardWithdrawPage(
            @ApiParam(value = "每页显示记录数", required = true) @RequestParam Integer limit,
            @ApiParam(value = "当前页码，从1开始", required = true) @RequestParam Integer page,
            @ApiParam(value = "排序方式，可选值(asc、desc)") @RequestParam(required = false) String order,
            @ApiParam(value = "排序字段") @RequestParam(required = false) String orderField,
            @ApiParam(value = "第三方订单号") @RequestParam(required = false) String orderno,
            @ApiParam(value = "卡号") @RequestParam(required = false) String payNo,
            @ApiParam(value = "状态 0 待审核 1审核通过  2 审核失败") @RequestParam(required = false) Integer state,
            @ApiParam(value = "我方订单号") @RequestParam(required = false) String transNo,
            @LoginUser UserEntity user) {
        try {
            // 验证用户权限
//            if (!user.getId().equals(userId)) {
//                return new Result<WithdrawPageData>().error("无权限查询其他用户的佣金提现记录");
//            }

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
            queryDTO.setUserId(user.getId());
            queryDTO.setOrder(order);
            queryDTO.setOrderField(orderField);
            queryDTO.setOrderno(orderno);
            queryDTO.setPayNo(payNo);
            queryDTO.setState(state);
            queryDTO.setTransNo(transNo);

            // 执行佣金提现查询
            WithdrawPageData pageData = withdrawService.getRewardWithdrawPageData(queryDTO);
            return new Result<WithdrawPageData>().ok(pageData);

        } catch (Exception e) {
            return new Result<WithdrawPageData>().error("获取佣金提现分页数据失败: " + e.getMessage());
        }
    }

    @Login
    @PostMapping("rewardwithdraw")
    @ApiOperation("前端佣金提现")
    public Result<Map<String, Object>> submitRewardWithdraw(
            @ApiParam(value = "提现金额（分）", required = true) @RequestParam Long amount,
            @ApiParam(value = "收款人卡号", required = true) @RequestParam String payNo,
            @LoginUser UserEntity user) throws Exception {
            // 构建请求DTO
            RewardWithdrawRequestDTO requestDTO = new RewardWithdrawRequestDTO();
            requestDTO.setAmount(amount);
            requestDTO.setPayNo(payNo);
            // 执行佣金提现
            Map<String, Object> result = withdrawService.submitRewardWithdraw(user.getId(), requestDTO);
            return new Result<Map<String, Object>>().ok(result);
    }

    @Login
    @PostMapping("withdraw")
    @ApiOperation("前端提现")
    public Result<Map<String, Object>> submitWithdraw(
            @ApiParam(value = "提现金额（分）", required = true) @RequestParam Long amount,
            @ApiParam(value = "收款人卡号", required = true) @RequestParam String payNo,
            @LoginUser UserEntity user) throws Exception {
            // 构建请求DTO
            RewardWithdrawRequestDTO requestDTO = new RewardWithdrawRequestDTO();
            requestDTO.setAmount(amount);
            requestDTO.setPayNo(payNo);

            // 执行盈利提现
            Map<String, Object> result = withdrawService.submitWithdraw(user.getId(), requestDTO);
            return new Result<Map<String, Object>>().ok(result);
    }

    @Login
    @GetMapping("rewardWithdrawSum")
    @ApiOperation("佣金提现统计")
    public Result<RewardWithdrawSumDTO> getRewardWithdrawSum(@LoginUser UserEntity user) {
        try {
            RewardWithdrawSumDTO sumDTO = withdrawService.getRewardWithdrawSum(user.getId());
            return new Result<RewardWithdrawSumDTO>().ok(sumDTO);
        } catch (Exception e) {
            return new Result<RewardWithdrawSumDTO>().error("获取佣金提现统计失败: " + e.getMessage());
        }
    }
}
