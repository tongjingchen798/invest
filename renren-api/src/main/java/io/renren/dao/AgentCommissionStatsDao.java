package io.renren.dao;

import io.renren.common.dao.BaseDao;
import io.renren.entity.AgentCommissionStatsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 代理兑付收益统计表DAO接口
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Mapper
public interface AgentCommissionStatsDao extends BaseDao<AgentCommissionStatsEntity> {

    /**
     * 根据统计日期查询统计数据
     *
     * @param statisticsDate 统计日期
     * @return 统计数据列表
     */
    List<AgentCommissionStatsEntity> selectByStatisticsDate(@Param("statisticsDate") Date statisticsDate);

    /**
     * 根据代理ID查询统计数据
     *
     * @param agentId 代理ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计数据列表
     */
    List<AgentCommissionStatsEntity> selectByAgentId(@Param("agentId") String agentId, 
                                                    @Param("startDate") Date startDate, 
                                                    @Param("endDate") Date endDate);

    /**
     * 根据业务员ID查询统计数据
     *
     * @param salesmanId 业务员ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计数据列表
     */
    List<AgentCommissionStatsEntity> selectBySalesmanId(@Param("salesmanId") String salesmanId, 
                                                        @Param("startDate") Date startDate, 
                                                        @Param("endDate") Date endDate);

    /**
     * 查询日期范围内的统计数据
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计数据列表
     */
    List<AgentCommissionStatsEntity> selectByDateRange(@Param("startDate") Date startDate, 
                                                       @Param("endDate") Date endDate);

    /**
     * 统计指定日期的兑付总额
     *
     * @param statisticsDate 统计日期
     * @return 兑付总额
     */
    Long selectTotalCommissionAmountByDate(@Param("statisticsDate") Date statisticsDate);

    /**
     * 统计指定日期范围的兑付总额
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 兑付总额
     */
    Long selectTotalCommissionAmountByDateRange(@Param("startDate") Date startDate, 
                                               @Param("endDate") Date endDate);

    /**
     * 批量插入统计数据
     *
     * @param statsList 统计数据列表
     * @return 影响行数
     */
    int batchInsert(@Param("statsList") List<AgentCommissionStatsEntity> statsList);

    /**
     * 根据代理ID、业务员ID和统计日期查询是否存在记录
     *
     * @param agentId 代理ID
     * @param salesmanId 业务员ID
     * @param statisticsDate 统计日期
     * @return 记录数量
     */
    int selectCountByAgentAndSalesmanAndDate(@Param("agentId") String agentId, 
                                            @Param("salesmanId") String salesmanId, 
                                            @Param("statisticsDate") Date statisticsDate);

    /**
     * 获取统计汇总数据
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 汇总数据
     */
    Map<String, Object> selectSummaryStats(@Param("startDate") Date startDate, 
                                          @Param("endDate") Date endDate);
}
