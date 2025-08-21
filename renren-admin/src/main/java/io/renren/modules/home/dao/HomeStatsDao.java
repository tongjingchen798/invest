package io.renren.modules.home.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 首页统计数据DAO接口
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Mapper
public interface HomeStatsDao {

    /**
     * 获取充值订单统计
     * @param startTime 开始时间戳
     * @param endTime 结束时间戳
     * @return 统计结果
     */
    Map<String, Object> getChargeOrderStats(@Param("startTime") Long startTime, @Param("endTime") Long endTime);

    /**
     * 获取提现订单统计
     * @param startTime 开始时间戳
     * @param endTime 结束时间戳
     * @return 统计结果
     */
    Map<String, Object> getWithdrawOrderStats(@Param("startTime") Long startTime, @Param("endTime") Long endTime);

    /**
     * 获取项目统计
     * @param startTime 开始时间戳
     * @param endTime 结束时间戳
     * @return 统计结果
     */
    Map<String, Object> getProjectStats(@Param("startTime") Long startTime, @Param("endTime") Long endTime);

    /**
     * 获取用户注册统计
     * @param startTime 开始时间戳
     * @param endTime 结束时间戳
     * @return 统计结果
     */
    Map<String, Object> getUserRegistrationStats(@Param("startTime") Long startTime, @Param("endTime") Long endTime);

    /**
     * 获取销售总额统计
     * @param startTime 开始时间戳
     * @param endTime 结束时间戳
     * @return 统计结果
     */
    Map<String, Object> getSalesAmountStats(@Param("startTime") Long startTime, @Param("endTime") Long endTime);

    /**
     * 获取日报表统计
     * @param startTime 开始时间戳
     * @param endTime 结束时间戳
     * @param type 统计类型 (d: 日报, m: 月报, y: 年报)
     * @return 日报表数据列表
     */
    List<Map<String, Object>> getDailyReportStats(@Param("startTime") Long startTime, @Param("endTime") Long endTime, @Param("type") String type);

    /**
     * 获取数量统计分析图数据
     * @param startTime 开始时间戳
     * @param endTime 结束时间戳
     * @param type 统计类型 (d: 日, w: 周, m: 月)
     * @return 数量统计分析数据列表
     */
    List<Map<String, Object>> getQuantityAnalysisData(@Param("startTime") Long startTime, @Param("endTime") Long endTime, @Param("type") String type);
}
