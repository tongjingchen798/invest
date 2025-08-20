package io.renren.modules.withdraw.controller;

import io.renren.common.constant.Constant;
import io.renren.common.utils.Result;
import io.renren.modules.withdraw.dto.ManualWithdrawDTO;
import io.renren.modules.withdraw.dto.ManualWithdrawPageData;
import io.renren.modules.withdraw.service.ManualWithdrawService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * 手工提现管理
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@RestController
@RequestMapping("withdraw")
@Api(tags="提现管理")
public class ManualWithdrawController {
    @Autowired
    private ManualWithdrawService manualWithdrawService;

    @GetMapping("page")
    @ApiOperation("手工提现分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = "endTime", value = "结束日期:时间戳", paramType = "query", dataType="long") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = "orderno", value = "平台订单号", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = "pay_no", value = "卡号", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = "startTime", value = "开始日期:时间戳", paramType = "query", dataType="long") ,
        @ApiImplicitParam(name = "state", value = "状态 0 待审核 1审核通过  2 审核失败", paramType = "query", dataType="int") ,
        @ApiImplicitParam(name = "threeorder_no", value = "三方订单号", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = "withdraw_type", value = "提现类型 1 余额提现 2 佣金提现", paramType = "query", dataType="String")
    })
//    @RequiresPermissions("withdraw:manual:page")
    public Result<ManualWithdrawPageData> page(@ApiIgnore @RequestParam Map<String, Object> params){
        try {
            ManualWithdrawPageData pageData = manualWithdrawService.getManualWithdrawPage(params);
            return new Result<ManualWithdrawPageData>().ok(pageData);
        } catch (Exception e) {
            return new Result<ManualWithdrawPageData>().error("查询手工提现列表失败: " + e.getMessage());
        }
    }
}
