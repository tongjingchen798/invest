/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.controller;

import io.renren.annotation.Login;
import io.renren.annotation.LoginUser;
import io.renren.common.constant.Constant;
import io.renren.common.utils.Result;
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
}
