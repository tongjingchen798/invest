package io.renren.controller;

import io.renren.common.utils.Result;
import io.renren.dto.AdvertisementDTO;
import io.renren.service.AdvertisementService;
import io.renren.common.page.PageData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 广告素材相关接口
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2025-07-10 20:05:01
 */
@RestController
@RequestMapping("/api/advertisement")
@Api(tags = "广告素材相关接口")
public class ApiAdvertisementController {
    
    @Resource
    private AdvertisementService advertisementService;

    @GetMapping("page")
    @ApiOperation("首页LOG和轮播图查询和个人中心图片")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
        @ApiImplicitParam(name = "limit", value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
        @ApiImplicitParam(name = "order", value = "排序方式，可选值(asc、desc)", paramType = "query", required = false, dataType = "string"),
        @ApiImplicitParam(name = "orderField", value = "排序字段", paramType = "query", required = false, dataType = "string"),
        @ApiImplicitParam(name = "type", value = "广告类型 1=LOG,2=轮播图，3=个人中心 4=弹窗广告", paramType = "query", required = false, dataType = "string")
    })
    public Result<PageData<AdvertisementDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        try {
            if (!params.containsKey("page")) {
                params.put("page", "1");
            }
            if (!params.containsKey("limit")) {
                params.put("limit", "10");
            }
            PageData<AdvertisementDTO> pageData = advertisementService.getPage(params);
            return new Result<PageData<AdvertisementDTO>>().ok(pageData);
        } catch (Exception e) {
            return new Result<PageData<AdvertisementDTO>>().error("查询失败：" + e.getMessage());
        }
    }

}
