package io.renren.modules.home.controller;

import io.renren.common.utils.Result;
import io.renren.modules.home.dto.DailyReportDTO;
import io.renren.modules.home.dto.MainStatsDTO;
import io.renren.modules.home.dto.QuantityAnalysisDTO;
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
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import org.apache.commons.lang3.StringUtils;
import java.util.Arrays;

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
     * @param type 统计类型 (d: 日报, m: 周报, y: 月报)
     * @return 日报表数据列表
     */
    @GetMapping("/ReportAmount")
    @ApiOperation("获取报表统计数据")
    public Result<List<DailyReportDTO>> getDailyReportStats(
            @ApiParam(value = "开始日期时间戳", required = false) @RequestParam(required = false) Long startTime,
            @ApiParam(value = "结束日期时间戳", required = false) @RequestParam(required = false) Long endTime,
            @ApiParam(value = "统计类型", required = true, example = "d") @RequestParam String type) {

        try {
            // 根据统计类型自动设置时间范围
            Long calculatedStartTime;
            Long calculatedEndTime;
            LocalDate now = LocalDate.now();
            switch (type.toLowerCase()) {
                case "d": // 日统计
                    // 今天0点到23:59:59
                    calculatedStartTime = LocalDate.now().atStartOfDay(ZoneOffset.of("+8")).toInstant().toEpochMilli();
                    calculatedEndTime = LocalDate.now().atTime(23, 59, 59).atZone(ZoneOffset.of("+8")).toInstant().toEpochMilli();
                    break;
                case "w": // 周统计
                    // 本周一到周日
                    LocalDate startOfWeek = now.with(DayOfWeek.MONDAY);
                    LocalDate endOfWeek = now.with(DayOfWeek.SUNDAY);
                    calculatedStartTime = startOfWeek.atStartOfDay(ZoneOffset.of("+8")).toInstant().toEpochMilli();
                    calculatedEndTime = endOfWeek.atTime(23, 59, 59).atZone(ZoneOffset.of("+8")).toInstant().toEpochMilli();
                    break;
                case "m": // 月统计
                    // 本月1号到最后一天
                    LocalDate startOfMonth = now.withDayOfMonth(1);
                    LocalDate endOfMonth = now.with(TemporalAdjusters.lastDayOfMonth());
                    calculatedStartTime = startOfMonth.atStartOfDay(ZoneOffset.of("+8")).toInstant().toEpochMilli();
                    calculatedEndTime = endOfMonth.atTime(23, 59, 59).atZone(ZoneOffset.of("+8")).toInstant().toEpochMilli();
                    break;
//                case "y": // 年统计
//                    // 今年1月1号到12月31号
//                    LocalDate startOfYear = now.withDayOfYear(1);
//                    LocalDate endOfYear = now.withDayOfYear(now.lengthOfYear());
//                    calculatedStartTime = startOfYear.atStartOfDay(ZoneOffset.of("+8")).toInstant().toEpochMilli();
//                    calculatedEndTime = endOfYear.atTime(23, 59, 59).atZone(ZoneOffset.of("+8")).toInstant().toEpochMilli();
//                    break;
                default:
                    // 默认今天
                    calculatedStartTime = LocalDate.now().atStartOfDay(ZoneOffset.of("+8")).toInstant().toEpochMilli();
                    calculatedEndTime = LocalDate.now().atTime(23, 59, 59).atZone(ZoneOffset.of("+8")).toInstant().toEpochMilli();
                    break;
            }
            
            List<DailyReportDTO> reportList = homeStatsService.getDailyReportStats(calculatedStartTime, calculatedEndTime, type);
            return new Result().ok(reportList);
        } catch (Exception e) {
            logger.error("获取日报表统计数据失败", e);
            return new Result().ok(Collections.emptyList());
        }
    }

    @GetMapping("/ReportNum")
    @ApiOperation("数量统计分析图")
    public Result<List<QuantityAnalysisDTO>> getReportNum(
            @ApiParam(value = "开始日期时间戳", required = false) @RequestParam(required = false) Long startTime,
            @ApiParam(value = "结束日期时间戳", required = false) @RequestParam(required = false) Long endTime,
            @ApiParam(value = "统计类型", required = true, example = "d", allowableValues = "d,w,m") @RequestParam String type) {
        
        try {
            // 参数验证
            if (StringUtils.isBlank(type)) {
                return new Result<List<QuantityAnalysisDTO>>().error("统计类型不能为空");
            }
            
            // 验证统计类型是否有效
            if (!Arrays.asList("d", "w", "m").contains(type.toLowerCase())) {
                return new Result<List<QuantityAnalysisDTO>>().error("无效的统计类型，支持的类型：d(日)、w(周)、m(月)");
            }
            
            // 计算时间范围
            TimeRange timeRange = calculateTimeRange(type.toLowerCase());
            
            // 调用服务获取数量统计分析数据
            List<QuantityAnalysisDTO> analysisData = homeStatsService.getQuantityAnalysisData(
                timeRange.getStartTime(), 
                timeRange.getEndTime(), 
                type
            );
            
            return new Result<List<QuantityAnalysisDTO>>().ok(analysisData);
            
        } catch (Exception e) {
            logger.error("获取数量统计分析图数据失败，type: {}, startTime: {}, endTime: {}", type, startTime, endTime, e);
            return new Result<List<QuantityAnalysisDTO>>().error("获取统计数据失败：" + e.getMessage());
        }
    }

    /**
     * 计算时间范围
     */
    private TimeRange calculateTimeRange(String type) {
        LocalDate now = LocalDate.now();
        LocalDateTime startDateTime, endDateTime;
        
        switch (type) {
            case "d": // 日统计：今天0点到23:59:59
                startDateTime = now.atStartOfDay();
                endDateTime = now.atTime(23, 59, 59);
                break;
                
            case "w": // 周统计：本周一到周日
                LocalDate startOfWeek = now.with(DayOfWeek.MONDAY);
                LocalDate endOfWeek = now.with(DayOfWeek.SUNDAY);
                startDateTime = startOfWeek.atStartOfDay();
                endDateTime = endOfWeek.atTime(23, 59, 59);
                break;
                
            case "m": // 月统计：本月1号到最后一天
                LocalDate startOfMonth = now.withDayOfMonth(1);
                LocalDate endOfMonth = now.with(TemporalAdjusters.lastDayOfMonth());
                startDateTime = startOfMonth.atStartOfDay();
                endDateTime = endOfMonth.atTime(23, 59, 59);
                break;
                
            default:
                // 默认今天
                startDateTime = now.atStartOfDay();
                endDateTime = now.atTime(23, 59, 59);
                break;
        }
        
        // 转换为时间戳（毫秒）
        ZoneOffset zoneOffset = ZoneOffset.of("+8");
        long startTime = startDateTime.atZone(zoneOffset).toInstant().toEpochMilli();
        long endTime = endDateTime.atZone(zoneOffset).toInstant().toEpochMilli();
        
        return new TimeRange(startTime, endTime);
    }

    /**
     * 时间范围内部类
     */
    private static class TimeRange {
        private final Long startTime;
        private final Long endTime;
        
        public TimeRange(Long startTime, Long endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }
        
        public Long getStartTime() {
            return startTime;
        }
        
        public Long getEndTime() {
            return endTime;
        }
    }


}
