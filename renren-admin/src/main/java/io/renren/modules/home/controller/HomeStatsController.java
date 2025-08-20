package io.renren.modules.home.controller;

import io.renren.common.utils.Result;
import io.renren.modules.home.dto.MainStatsDTO;
import io.renren.modules.home.service.HomeStatsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 首页统计接口
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@RestController
@RequestMapping("/stats")
@Api(tags = "首页统计接口")
public class HomeStatsController {

    private static final Logger logger = LoggerFactory.getLogger(HomeStatsController.class);

    @Autowired
    private HomeStatsService homeStatsService;

    /**
     * 获取首页统计数据
     * 
     * @param startTime 开始日期时间戳 (query参数)
     * @param endTime 结束日期时间戳 (query参数)
     * @return 统计数据
     */
    @GetMapping("/main")
    @ApiOperation("获取首页统计数据")
    public Result<MainStatsDTO> getMainStats(
            @ApiParam(value = "开始日期时间戳") @RequestParam(value = "startTime", required = false) Long startTime,
            @ApiParam(value = "结束日期时间戳") @RequestParam(value = "endTime", required = false) Long endTime) {
        
        logger.info("获取首页统计数据，开始时间: {}, 结束时间: {}", startTime, endTime);
        
        try {
            // 获取统计数据
            MainStatsDTO stats = homeStatsService.getMainStats(startTime, endTime);
            
            logger.info("成功获取统计数据: {}", stats);
            return new Result<MainStatsDTO>().ok(stats);
            
        } catch (Exception e) {
            logger.error("获取统计数据失败", e);
            return new Result<MainStatsDTO>().error("获取统计数据失败: " + e.getMessage());
        }
    }
}
