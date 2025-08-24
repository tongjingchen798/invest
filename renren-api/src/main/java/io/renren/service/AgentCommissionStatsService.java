package io.renren.service;

import io.renren.common.service.BaseService;
import io.renren.entity.AgentCommissionStatsEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 代理兑付收益统计表服务接口
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
public interface AgentCommissionStatsService extends BaseService<AgentCommissionStatsEntity> {

    /**
     * 根据统计日期查询统计数据
     *
     * @param statisticsDate 统计日期
     * @return 统计数据列表
     */
    List<AgentCommissionStatsEntity> getStatsByDate(Date statisticsDate);

    /**
     * 根据代理ID查询统计数据
     *
     * @param agentId 代理ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计数据列表
     */
    List<AgentCommissionStatsEntity> getStatsByAgentId(String agentId, Date startDate, Date endDate);

    /**
     * 根据业务员ID查询统计数据
     *
     * @param salesmanId 业务员ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计数据列表
     */
    List<AgentCommissionStatsEntity> getStatsBySalesmanId(String salesmanId, Date startDate, Date endDate);

    /**
     * 查询日期范围内的统计数据
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计数据列表
     */
    List<AgentCommissionStatsEntity> getStatsByDateRange(Date startDate, Date endDate);

    /**
     * 统计指定日期的兑付总额
     *
     * @param statisticsDate 统计日期
     * @return 兑付总额
     */
    Long getTotalCommissionAmountByDate(Date statisticsDate);

    /**
     * 统计指定日期范围的兑付总额
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 兑付总额
     */
    Long getTotalCommissionAmountByDateRange(Date startDate, Date endDate);

    /**
     * 获取统计汇总数据
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 汇总数据
     */
    Map<String, Object> getSummaryStats(Date startDate, Date endDate);

    /**
     * 手动执行统计（用于测试或补录）
     *
     * @param targetDate 目标日期
     */
    void calculateCommissionManually(Date targetDate);

    /**
     * 获取代理兑付收益报表数据
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param agentId 代理ID（可选）
     * @param salesmanId 业务员ID（可选）
     * @return 报表数据
     */
    Map<String, Object> getCommissionReport(Date startDate, Date endDate, String agentId, String salesmanId);
}
