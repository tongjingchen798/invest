/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.controller;

import io.renren.common.constant.Constant;
import io.renren.common.utils.Result;
import io.renren.dto.BankDTO;
import io.renren.dto.BankPageData;
import io.renren.service.BankService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 银行管理接口
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/api/balank")
@Api(tags = "银行管理接口")
public class ApiBankController {

    @Autowired
    private BankService bankService;

    @GetMapping("page")
    @ApiOperation("获取银行列表")
    public Result<BankPageData<BankDTO>> page(
            @ApiParam(value = "每页显示记录数", required = true) @RequestParam(Constant.LIMIT) Integer limit,
            @ApiParam(value = "当前页码，从1开始", required = true) @RequestParam(Constant.PAGE) Integer page,
            @ApiParam(value = "排序方式，可选值(asc、desc)") @RequestParam(value = Constant.ORDER, required = false) String order,
            @ApiParam(value = "排序字段") @RequestParam(value = Constant.ORDER_FIELD, required = false) String orderField,
            @ApiParam(value = "状态 0：停用 1：正常") @RequestParam(required = false) Integer state) {
        
        // 直接使用MyBatis-Plus分页，无需构建Map
        BankPageData<BankDTO> pageData = bankService.queryPageData(page, limit, order, orderField, state);
        
        return new Result<BankPageData<BankDTO>>().ok(pageData);
    }
}
