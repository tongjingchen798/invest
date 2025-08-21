package io.renren.modules.order.controller;

import io.renren.common.page.PageData;
import io.renren.common.utils.Result;
import io.renren.modules.order.dto.InvestmentRecordDTO;
import io.renren.modules.order.service.InvestmentRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * 购买记录
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@RestController
@RequestMapping("/order")
@Api(tags="购买记录")
public class InvestmentRecordController {
    @Autowired
    private InvestmentRecordService investmentRecordService;

    @GetMapping("list")
    @ApiOperation("查询购买记录")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "当前页码，从1开始", paramType = "query", required = true, dataType="int"),
        @ApiImplicitParam(name = "limit", value = "每页显示记录数", paramType = "query", required = true, dataType="int"),
        @ApiImplicitParam(name = "orderField", value = "排序字段", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "order", value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "abbreviation", value = "订单号简称", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "agent", value = "代理", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "biaoqian", value = "标签筛选：传标签（XML联表查询tb_user表）", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "biaoqianFlag", value = "标签筛选：1有 0无 查全部（XML联表查询tb_user表）", paramType = "query", dataType="int"),
        @ApiImplicitParam(name = "createstarttime", value = "到期开始日期:时间戳（需关联tb_project表）", paramType = "query", dataType="long"),
        @ApiImplicitParam(name = "createendtime", value = "到期结束日期:时间戳（需关联tb_project表）", paramType = "query", dataType="long"),
        @ApiImplicitParam(name = "startTime", value = "开始日期:时间戳（投资日期）", paramType = "query", dataType="long"),
        @ApiImplicitParam(name = "endTime", value = "结束日期:时间戳（投资日期）", paramType = "query", dataType="long"),
        @ApiImplicitParam(name = "liebian", value = "裂变筛选：是=1，否=0（XML联表查询tb_user表）", paramType = "query", dataType="int"),
        @ApiImplicitParam(name = "mobile", value = "会员账号（XML联表查询tb_user表）", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "salesmanid", value = "销售员ID", paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "status", value = "状态 0:未收益 1:已收益", paramType = "query", dataType="int"),
        @ApiImplicitParam(name = "projectId", value = "项目ID", paramType = "query", dataType="long"),
        @ApiImplicitParam(name = "userId", value = "用户ID", paramType = "query", dataType="long")
    })
    public Result<PageData<InvestmentRecordDTO>> list(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<InvestmentRecordDTO> page = investmentRecordService.selectPage(params);
        return new Result<PageData<InvestmentRecordDTO>>().ok(page);
    }
}
