package io.renren.modules.usdtrecord.controller;

import io.renren.common.page.PageData;
import io.renren.common.utils.Result;
import io.renren.modules.usdtrecord.dto.UsdtRecordDTO;
import io.renren.modules.usdtrecord.dto.UsdtRecordMatchOrderDTO;
import io.renren.modules.usdtrecord.service.UsdtRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * USDT钱包收款记录
 *
 * @author renren
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/admin/usdtrecord")
@Api(tags = "USDT钱包收款记录")
public class UsdtRecordController {
    @Autowired
    private UsdtRecordService usdtRecordService;

    @GetMapping("page")
    @ApiOperation("USDT钱包收款列表分页查询")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "当前页码，从1开始", required = true, dataType = "int", paramType = "query"),
        @ApiImplicitParam(name = "limit", value = "每页显示记录数", required = true, dataType = "int", paramType = "query"),
        @ApiImplicitParam(name = "orderField", value = "排序字段", required = false, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "order", value = "排序方式，可选值(asc、desc)", required = false, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "createstarttime", value = "开始日期:时间戳", required = false, dataType = "long", paramType = "query"),
        @ApiImplicitParam(name = "createendtime", value = "结束日期:时间戳", required = false, dataType = "long", paramType = "query"),
        @ApiImplicitParam(name = "fromaddress", value = "转账地址", required = false, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "toaddress", value = "收款地址", required = false, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "transactionId", value = "交易Hash", required = false, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "isprocess", value = "状态 0 未处理 1已处理", required = false, dataType = "int", paramType = "query")
    })
    public Result<PageData<UsdtRecordDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        try {
            // 参数验证
            if (params.get("page") == null || params.get("limit") == null) {
                return new Result<PageData<UsdtRecordDTO>>().error("页码和每页记录数不能为空");
            }
            
            PageData<UsdtRecordDTO> pageData = usdtRecordService.page(params);
            return new Result<PageData<UsdtRecordDTO>>().ok(pageData);
        } catch (Exception e) {
            return new Result<PageData<UsdtRecordDTO>>().error("查询USDT收款记录失败: " + e.getMessage());
        }
    }

    @PutMapping("matchOrder")
    @ApiOperation("手工匹配订单")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "dto", value = "前端匹配订单传参", required = true, dataType = "UsdtRecordMatchOrderDTO", paramType = "body")
    })
    public Result<Object> matchOrder(@RequestBody UsdtRecordMatchOrderDTO dto) {
        try {
            // 参数验证
            if (dto.getId() == null) {
                return new Result<Object>().error("USDT记录ID不能为空");
            }
            if (dto.getOrderno() == null || dto.getOrderno().trim().isEmpty()) {
                return new Result<Object>().error("订单号不能为空");
            }
            
            // 调用服务进行订单匹配
            boolean success = usdtRecordService.matchOrder(dto.getId(), dto.getOrderno());
            
            if (success) {
                return new Result<Object>().ok("订单匹配成功");
            } else {
                return new Result<Object>().error("订单匹配失败");
            }
        } catch (Exception e) {
            return new Result<Object>().error("订单匹配失败: " + e.getMessage());
        }
    }
}
