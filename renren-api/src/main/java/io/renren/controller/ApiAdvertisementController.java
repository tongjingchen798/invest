package io.renren.controller;

import io.renren.common.utils.Result;
import io.renren.dto.IssuesDTO;
import io.renren.dto.IssuesPageData;
import io.renren.service.IssuesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 广告相关接口
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@RestController
@RequestMapping("/api/advertisement")
@Api(tags = "广告相关接口")
public class ApiAdvertisementController {
    
    @Resource
    private IssuesService issuesService;

    @GetMapping("page")
    @ApiOperation("首页LOG和轮播图查询和个人中心图片")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
        @ApiImplicitParam(name = "limit", value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
        @ApiImplicitParam(name = "order", value = "排序方式，可选值(asc、desc)", paramType = "query", required = false, dataType = "string"),
        @ApiImplicitParam(name = "orderField", value = "排序字段", paramType = "query", required = false, dataType = "string"),
        @ApiImplicitParam(name = "type", value = "广告类型 1=LOG,2=轮播图，3=个人中心 4=弹窗广告", paramType = "query", required = false, dataType = "string")
    })
    public Result<IssuesPageData<IssuesDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        try {
            // 确保分页参数存在
            if (!params.containsKey("page")) {
                params.put("page", "1");
            }
            if (!params.containsKey("limit")) {
                params.put("limit", "10");
            }
            
            // 调用服务层分页查询
            IssuesPageData<IssuesDTO> pageData = issuesService.queryPageData(params);
            return new Result<IssuesPageData<IssuesDTO>>().ok(pageData);
        } catch (Exception e) {
            return new Result<IssuesPageData<IssuesDTO>>().error("查询失败：" + e.getMessage());
        }
    }

    @GetMapping("listByType")
    @ApiOperation("根据类型查询广告列表")
    @ApiImplicitParam(name = "type", value = "广告类型 1=LOG,2=轮播图，3=个人中心 4=弹窗广告", paramType = "query", required = true, dataType = "int")
    public Result<List<IssuesDTO>> listByType(@RequestParam Integer type) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("type", type.toString());
            params.put("page", "1");
            params.put("limit", "100"); // 设置较大的限制以获取所有该类型的广告
            
            IssuesPageData<IssuesDTO> pageData = issuesService.queryPageData(params);
            return new Result<List<IssuesDTO>>().ok(pageData.getList());
        } catch (Exception e) {
            return new Result<List<IssuesDTO>>().error("查询失败：" + e.getMessage());
        }
    }

    @GetMapping("listEnabled")
    @ApiOperation("查询所有启用的广告")
    public Result<List<IssuesDTO>> listEnabled() {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("page", "1");
            params.put("limit", "100"); // 设置较大的限制以获取所有启用的广告
            
            IssuesPageData<IssuesDTO> pageData = issuesService.queryPageData(params);
            return new Result<List<IssuesDTO>>().ok(pageData.getList());
        } catch (Exception e) {
            return new Result<List<IssuesDTO>>().error("查询失败：" + e.getMessage());
        }
    }
}
