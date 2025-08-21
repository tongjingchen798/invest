package io.renren.modules.home.service;

import io.renren.modules.home.dto.DailyReportDTO;
import io.renren.modules.home.dto.MainStatsDTO;
import io.renren.modules.home.dto.QuantityAnalysisDTO;

import java.util.List;

/**
 * 首页统计服务接口
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
public interface HomeStatsService {

    /**
     * 获取首页统计数据
     * @param startTime 开始时间戳
     * @param endTime 结束时间戳
     * @return 统计数据
     */
    MainStatsDTO getMainStats(Long startTime, Long endTime);

    /**
     * 获取日报表统计数据
     * @param startTime 开始时间戳
     * @param endTime 结束时间戳
     * @param type 统计类型 (d: 日报, m: 月报, y: 年报)
     * @return 日报表数据列表
     */
    List<DailyReportDTO> getDailyReportStats(Long startTime, Long endTime, String type);

    /**
     * 获取数量统计分析图数据
     * @param startTime 开始时间戳
     * @param endTime 结束时间戳
     * @param type 统计类型 (d: 日, w: 周, m: 月)
     * @return 数量统计分析数据列表
     */
    List<QuantityAnalysisDTO> getQuantityAnalysisData(Long startTime, Long endTime, String type);
}
