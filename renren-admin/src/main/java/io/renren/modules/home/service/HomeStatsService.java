package io.renren.modules.home.service;

import io.renren.modules.home.dto.MainStatsDTO;

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
}
