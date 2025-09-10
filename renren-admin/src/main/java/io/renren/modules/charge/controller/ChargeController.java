package io.renren.modules.charge.controller;

import io.renren.common.constant.Constant;
import io.renren.common.utils.Result;
import io.renren.modules.charge.dto.ChargePageData;
import io.renren.modules.charge.service.ChargeOrderService;
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
 * 充值订单管理
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@RestController
@RequestMapping("charge")
@Api(tags="充值订单管理")
public class ChargeController {
    @Autowired
    private ChargeOrderService chargeOrderService;

    @GetMapping("page")
    @ApiOperation("充值列表分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = "biaoqian", value = "标签筛选：传标签", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = "biaoqianFlag", value = "标签筛选：1 有 0 无 查全部，不传参", paramType = "query", dataType="int") ,
        @ApiImplicitParam(name = "createendtime", value = "结束日期:时间戳", paramType = "query", dataType="long") ,
        @ApiImplicitParam(name = "createstarttime", value = "开始日期:时间戳", paramType = "query", dataType="long") ,
        @ApiImplicitParam(name = "endTime", value = "结束日期:时间戳", paramType = "query", dataType="long") ,
        @ApiImplicitParam(name = "liebian", value = "裂变筛选：是 =1，否 =0，查全部，不传参", paramType = "query", dataType="int") ,
        @ApiImplicitParam(name = "mobile", value = "用户账号", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = "startTime", value = "开始日期:时间戳", paramType = "query", dataType="long") ,
        @ApiImplicitParam(name = "state", value = "状态 0 待审核 1审核通过  2 审核失败", paramType = "query", dataType="int") ,
        @ApiImplicitParam(name = "threeorder_no", value = "三方订单号", paramType = "query", dataType="String")
    })
//    @RequiresPermissions("charge:order:page")
    public Result<ChargePageData> page(@ApiIgnore @RequestParam Map<String, Object> params){
        try {
            ChargePageData pageData = chargeOrderService.getAdminChargePage(params);
            return new Result<ChargePageData>().ok(pageData);
        } catch (Exception e) {
            return new Result<ChargePageData>().error("查询充值列表失败: " + e.getMessage());
        }
    }
}
