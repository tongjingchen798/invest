

package io.renren.controller;

import io.renren.common.constant.Constant;
import io.renren.common.utils.Result;
import io.renren.dto.IssuesDTO;
import io.renren.dto.IssuesPageData;
import io.renren.service.IssuesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 广告/图片管理接口
 *
 * @author NICO
 */
@RestController
@RequestMapping("/api/issues")
@Api(tags = "广告/图片管理接口")
public class ApiIssuesController {
    @Autowired
    private IssuesService issuesService;

    @GetMapping("page")
    @ApiOperation("分页查询广告/图片")
    public Result<IssuesPageData<IssuesDTO>> page(
            @ApiParam(value = "每页显示记录数", required = true) @RequestParam(Constant.LIMIT) Integer limit,
            @ApiParam(value = "当前页码，从1开始", required = true) @RequestParam(Constant.PAGE) Integer page,
            @ApiParam(value = "排序方式，可选值(asc、desc)") @RequestParam(value = Constant.ORDER, required = false) String order,
            @ApiParam(value = "排序字段") @RequestParam(value = Constant.ORDER_FIELD, required = false) String orderField,
            @ApiParam(value = "广告类型 1=LOG,2=轮播图，3=个人中心 4=弹窗广告") @RequestParam(required = false) String type) {
        
        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, page);
        params.put(Constant.LIMIT, limit);
        params.put(Constant.ORDER, order);
        params.put(Constant.ORDER_FIELD, orderField);
        params.put("type", type);

        IssuesPageData<IssuesDTO> pageData = issuesService.queryPageData(params);
        
        return new Result<IssuesPageData<IssuesDTO>>().ok(pageData);
    }


}