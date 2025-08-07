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
import io.renren.common.utils.Result;
import io.renren.common.validator.ValidatorUtils;
import io.renren.dto.PayInfoDTO;
import io.renren.entity.UserEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import io.renren.common.page.PageData;
import io.renren.service.PayInfoService;
import io.renren.annotation.LoginUser;
import io.renren.entity.UserEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.ApiParam;
import io.renren.dto.PayInfoPageData;
import io.renren.common.constant.Constant;

/**
 * 支付信息接口
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/api/payinfo")
@Api(tags = "支付信息接口")
public class ApiPayInfoController {

    @Autowired
    private PayInfoService payInfoService;

    @Login
    @PostMapping
    @ApiOperation("新增支付方式")
    public Result<Map<String, Object>> addPayInfo(@RequestBody PayInfoDTO dto, @LoginUser UserEntity user) {
        // 参数校验
        ValidatorUtils.validateEntity(dto);
        
        // TODO: 实现新增支付方式逻辑
        // 这里需要根据业务需求实现具体的支付方式添加逻辑
        // 包括：验证验证码、保存支付信息到数据库、关联用户等
        
        Map<String, Object> result = new HashMap<>();
        result.put("payInfoId", "PAY_" + System.currentTimeMillis());
        result.put("status", "success");
        
        return new Result<Map<String, Object>>().ok(result);
    }

    @Login
    @GetMapping("page")
    @ApiOperation("查询支付列表")
    public Result<PayInfoPageData<PayInfoDTO>> page(
            @ApiParam(value = "每页显示记录数", required = true) @RequestParam(Constant.LIMIT) Integer limit,
            @ApiParam(value = "当前页码，从1开始", required = true) @RequestParam(Constant.PAGE) Integer page,
            @ApiParam(value = "排序方式，可选值(asc、desc)") @RequestParam(value = Constant.ORDER, required = false) String order,
            @ApiParam(value = "排序字段") @RequestParam(value = Constant.ORDER_FIELD, required = false) String orderField,
            @LoginUser UserEntity user) {
        
        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, page);
        params.put(Constant.LIMIT, limit);
        params.put(Constant.ORDER, order);
        params.put(Constant.ORDER_FIELD, orderField);
        params.put("userId", user.getId().toString());

        PayInfoPageData<PayInfoDTO> pageData = payInfoService.queryPageData(params);
        
        return new Result<PayInfoPageData<PayInfoDTO>>().ok(pageData);
    }
}
