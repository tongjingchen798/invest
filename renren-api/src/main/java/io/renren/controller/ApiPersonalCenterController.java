
package io.renren.controller;

import io.renren.annotation.Login;
import io.renren.annotation.LoginUser;
import io.renren.common.constant.Constant;
import io.renren.common.utils.Result;
import io.renren.dto.BalanceDTO;
import io.renren.dto.TransactionDetailDTO;
import io.renren.dto.TransactionDetailPageData;
import io.renren.entity.UserEntity;
import io.renren.service.TransactionDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 个人中心接口
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/api/grzx")
@Api(tags = "个人中心接口")
public class ApiPersonalCenterController {

    @Autowired
    private TransactionDetailService transactionDetailService;

    @Login
    @PostMapping("BalanceTransfers")
    @ApiOperation("投资账户转让")
    public Result<Map<String, Object>> balanceTransfers(@LoginUser UserEntity user) {
        
        // TODO: 实现投资账户转让逻辑
        // 这里需要根据业务需求实现具体的账户转让逻辑
        // 包括：验证用户权限、检查账户余额、执行转让操作、记录交易日志等
        
        Map<String, Object> result = new HashMap<>();
        result.put("transferId", "TRANSFER_" + System.currentTimeMillis());
        result.put("status", "success");
        result.put("message", "账户转让申请已提交");
        
        return new Result<Map<String, Object>>().ok(result);
    }

    @Login
    @GetMapping("agetBalanceDetail")
    @ApiOperation("资金明细")
    public Result<TransactionDetailPageData<TransactionDetailDTO>> getAgetBalanceDetail(
            @ApiParam(value = "每页显示记录数", required = true) @RequestParam(Constant.LIMIT) Integer limit,
            @ApiParam(value = "当前页码，从1开始", required = true) @RequestParam(Constant.PAGE) Integer page,
            @LoginUser UserEntity user) {
        
        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, page);
        params.put(Constant.LIMIT, limit);
        // 只查询当前用户的账变明细
        params.put("userId", user.getId().toString());

        TransactionDetailPageData<TransactionDetailDTO> pageData = transactionDetailService.queryPageData(params);
        
        return new Result<TransactionDetailPageData<TransactionDetailDTO>>().ok(pageData);
    }

    @Login
    @GetMapping("balanceDetail")
    @ApiOperation("资金明细")
    public Result<TransactionDetailPageData<TransactionDetailDTO>> getBalanceDetail(
            @ApiParam(value = "每页显示记录数", required = true) @RequestParam(Constant.LIMIT) Integer limit,
            @ApiParam(value = "当前页码，从1开始", required = true) @RequestParam(Constant.PAGE) Integer page,
            @LoginUser UserEntity user) {
        
        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, page);
        params.put(Constant.LIMIT, limit);
        // 只查询当前用户的账变明细
        params.put("userId", user.getId().toString());

        TransactionDetailPageData<TransactionDetailDTO> pageData = transactionDetailService.queryPageData(params);
        
        return new Result<TransactionDetailPageData<TransactionDetailDTO>>().ok(pageData);
    }

    @Login
    @GetMapping("balance")
    @ApiOperation("账户概览")
    public Result<BalanceDTO> getBalance(@LoginUser UserEntity user) {
        
        // TODO: 实现获取账户余额逻辑
        // 这里需要根据业务需求实现具体的账户余额查询逻辑
        // 包括：查询用户余额、投资收益、充值提现记录等
        
        BalanceDTO balanceDTO = new BalanceDTO();
        balanceDTO.setId(user.getId());
        balanceDTO.setUserId(user.getId());
        
        // 设置默认值（实际应该从数据库查询）
        balanceDTO.setAssets(0L);           // 可用余额
        balanceDTO.setBalance(0L);          // 账户余额
        balanceDTO.setCashwithdrawable(0L); // 可提现
        balanceDTO.setCumulative(0L);       // 累计收益
        balanceDTO.setCzAmount(0L);         // 累计充值
        balanceDTO.setDsAmount(0L);         // 待收利息
        balanceDTO.setDsbjAmount(0L);       // 待收本金
        balanceDTO.setJrAmount(0L);         // 今日收益
        balanceDTO.setTzAmount(0L);         // 累计投资
        balanceDTO.setYsAmount(0L);         // 已收利息
        balanceDTO.setYsbjAmount(0L);       // 已收本金
        balanceDTO.setYtxAmount(0L);        // 已提现
        balanceDTO.setZztxAmount(0L);       // 正在提现
        balanceDTO.setWheelTimes(0);        // 转盘次数
        balanceDTO.setUpdateDate(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
        
        return new Result<BalanceDTO>().ok(balanceDTO);
    }
}
