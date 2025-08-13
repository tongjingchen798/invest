package io.renren.controller;

import io.renren.common.utils.Result;
import io.renren.dto.DownloadPageDataDTO;
import io.renren.service.DownloadPageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 下载页数据接口
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@RestController
@RequestMapping("/api")
@Api(tags = "下载页数据接口")
public class ApiDownloadController {
    
    @Autowired
    private DownloadPageService downloadPageService;
    
    @GetMapping("tgcount")
    @ApiOperation("获取下载页统计数据")
    public Result<Map<String, Object>> getDownloadPageData() {
        try {
            // 获取下载页统计数据
            DownloadPageDataDTO data = downloadPageService.getDownloadPageData();
            
            // 构建返回数据，按照接口要求只返回fwl和xzl
            Map<String, Object> result = new HashMap<>();
            result.put("fwl", data.getFwl());
            result.put("xzl", data.getXzl());
            
            return new Result<Map<String, Object>>().ok(result);
            
        } catch (Exception e) {
            return new Result<Map<String, Object>>().error("获取数据失败: " + e.getMessage());
        }
    }
    
    @GetMapping("tgcount/detail")
    @ApiOperation("获取下载页详细统计数据")
    public Result<DownloadPageDataDTO> getDownloadPageDetailData() {
        try {
            // 获取详细的下载页统计数据
            DownloadPageDataDTO data = downloadPageService.getDownloadPageData();
            return new Result<DownloadPageDataDTO>().ok(data);
            
        } catch (Exception e) {
            return new Result<DownloadPageDataDTO>().error("获取详细数据失败: " + e.getMessage());
        }
    }
    
    @PostMapping("tgcount/visit")
    @ApiOperation("增加访问量")
    public Result<String> incrementVisitCount() {
        try {
            boolean success = downloadPageService.incrementVisitCount();
            if (success) {
                return new Result<String>().ok("访问量增加成功");
            } else {
                return new Result<String>().error("访问量增加失败");
            }
        } catch (Exception e) {
            return new Result<String>().error("访问量增加失败: " + e.getMessage());
        }
    }
    
    @PostMapping("tgcount/download")
    @ApiOperation("增加下载量")
    public Result<String> incrementDownloadCount() {
        try {
            boolean success = downloadPageService.incrementDownloadCount();
            if (success) {
                return new Result<String>().ok("下载量增加成功");
            } else {
                return new Result<String>().error("下载量增加失败");
            }
        } catch (Exception e) {
            return new Result<String>().error("下载量增加失败: " + e.getMessage());
        }
    }
}
