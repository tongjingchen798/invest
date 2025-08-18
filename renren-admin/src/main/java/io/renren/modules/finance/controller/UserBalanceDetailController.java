package io.renren.modules.finance.controller;

import io.renren.common.page.PageData;
import io.renren.common.utils.Result;
import io.renren.modules.finance.dto.UserBalanceDetailDTO;
import io.renren.modules.finance.service.UserBalanceDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户余额明细Controller
 *
 * @author renren
 * @since 1.0.0
 */
@RestController
@RequestMapping("/userbalancedetail")
@Api(tags = "财务管理-账变明细")
public class UserBalanceDetailController {

    @Autowired
    private UserBalanceDetailService userBalanceDetailService;

    @GetMapping("page")
    @ApiOperation("分页查询账变明细")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "当前页码，从1开始", required = true, dataType = "int", paramType = "query"),
        @ApiImplicitParam(name = "limit", value = "每页显示记录数", required = true, dataType = "int", paramType = "query"),
        @ApiImplicitParam(name = "biaoqian", value = "标签筛选：传标签", required = false, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "biaoqianFlag", value = "标签筛选：1有 0无 查全部，不传参", required = false, dataType = "int", paramType = "query"),
        @ApiImplicitParam(name = "busiType", value = "账务类型", required = false, dataType = "int", paramType = "query"),
        @ApiImplicitParam(name = "endTime", value = "结束日期:时间戳", required = false, dataType = "long", paramType = "query"),
        @ApiImplicitParam(name = "mobile", value = "用户账号", required = false, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "order", value = "排序方式，可选值(asc、desc)", required = false, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "orderField", value = "排序字段", required = false, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "startTime", value = "开始日期:时间戳", required = false, dataType = "long", paramType = "query")
    })
    // @RequiresPermissions("finance:userbalancedetail:page")
    public Result<PageData<UserBalanceDetailDTO>> page(
            @RequestParam("page") Integer page,
            @RequestParam("limit") Integer limit,
            @RequestParam(value = "biaoqian", required = false) String biaoqian,
            @RequestParam(value = "biaoqianFlag", required = false) Integer biaoqianFlag,
            @RequestParam(value = "busiType", required = false) Integer busiType,
            @RequestParam(value = "endTime", required = false) Long endTime,
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "orderField", required = false) String orderField,
            @RequestParam(value = "startTime", required = false) Long startTime) {
        
        try {
            // 参数验证
            if (page == null || page < 1) {
                return new Result<PageData<UserBalanceDetailDTO>>().error("页码必须大于0");
            }
            if (limit == null || limit < 1 || limit > 1000) {
                return new Result<PageData<UserBalanceDetailDTO>>().error("每页记录数必须在1-1000之间");
            }

            // 调用Service查询
            PageData<UserBalanceDetailDTO> pageData = userBalanceDetailService.getBalanceDetailPage(
                page, limit, biaoqian, biaoqianFlag, busiType, endTime, mobile, order, orderField, startTime
            );

            return new Result<PageData<UserBalanceDetailDTO>>().ok(pageData);
        } catch (Exception e) {
            return new Result<PageData<UserBalanceDetailDTO>>().error("查询账变明细失败: " + e.getMessage());
        }
    }
}
