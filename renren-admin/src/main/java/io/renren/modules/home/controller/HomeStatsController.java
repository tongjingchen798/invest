package io.renren.modules.home.controller;

import io.renren.common.utils.Result;
import io.renren.modules.home.dto.DailyReportDTO;
import io.renren.modules.home.dto.MainStatsDTO;
import io.renren.modules.home.service.HomeStatsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

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
     * @param startTime 开始时间戳 (可选)
     * @param endTime 结束时间戳 (可选)
     * @return 统计数据
     */
    @GetMapping("/main")
    @ApiOperation("获取首页统计数据")
    public Result<MainStatsDTO> getMainStats(
            @ApiParam(value = "开始日期时间戳") @RequestParam(value = "startTime", required = false) Long startTime,
            @ApiParam(value = "结束日期时间戳") @RequestParam(value = "endTime", required = false) Long endTime) {
        
        logger.info("获取首页统计数据，开始时间: {}, 结束时间: {}", startTime, endTime);
        
        try {
            MainStatsDTO stats = homeStatsService.getMainStats(startTime, endTime);
            logger.info("成功获取统计数据: {}", stats);
            return new Result<MainStatsDTO>().ok(stats);
            
        } catch (Exception e) {
            logger.error("获取统计数据失败", e);
            return new Result<MainStatsDTO>().error("获取统计数据失败: " + e.getMessage());
        }
    }


    /**
     * 获取报表统计数据
     *
     * @param startTime 开始时间戳
     * @param endTime 结束时间戳
     * @param type 统计类型 (d: 日报, m: 月报, y: 年报)
     * @return 日报表数据列表
     */
    @GetMapping("/ReportAmount")
    @ApiOperation("获取报表统计数据")
    public Result<List<DailyReportDTO>> getDailyReportStats(
            @ApiParam(value = "开始日期时间戳", required = true) @RequestParam Long startTime,
            @ApiParam(value = "结束日期时间戳", required = true) @RequestParam Long endTime,
            @ApiParam(value = "统计类型", required = true, example = "d") @RequestParam String type) {

        logger.info("收到日报表统计数据请求，时间范围: {} - {}, 类型: {}", startTime, endTime, type);

        try {
            List<DailyReportDTO> reportList = homeStatsService.getDailyReportStats(startTime, endTime, type);
            return new Result().ok(reportList);
        } catch (Exception e) {
            logger.error("获取日报表统计数据失败", e);
            return new Result().ok(Collections.emptyList());
        }
    }
}
