package io.renren.service.impl;

import io.renren.common.service.impl.BaseServiceImpl;
import io.renren.dao.AgentCommissionStatsDao;
import io.renren.entity.AgentCommissionStatsEntity;
import io.renren.schedule.AgentCommissionSchedule;
import io.renren.service.AgentCommissionStatsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 代理兑付收益统计表服务实现类
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Slf4j
@Service
public class AgentCommissionStatsServiceImpl extends BaseServiceImpl<AgentCommissionStatsDao, AgentCommissionStatsEntity> 
        implements AgentCommissionStatsService {

    @Autowired
    private AgentCommissionStatsDao agentCommissionStatsDao;
    
    @Autowired
    private AgentCommissionSchedule agentCommissionSchedule;

    @Override
    public List<AgentCommissionStatsEntity> getStatsByDate(Date statisticsDate) {
        log.debug("根据统计日期查询统计数据：{}", statisticsDate);
        try {
            QueryWrapper<AgentCommissionStatsEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("statistics_date", statisticsDate)
                       .orderByAsc("agent_id", "salesman_id");
            return agentCommissionStatsDao.selectList(queryWrapper);
        } catch (Exception e) {
            log.error("根据统计日期查询统计数据失败", e);
            return null;
        }
    }

    @Override
    public List<AgentCommissionStatsEntity> getStatsByAgentId(String agentId, Date startDate, Date endDate) {
        log.debug("根据代理ID查询统计数据：agentId={}, startDate={}, endDate={}", agentId, startDate, endDate);
        try {
            QueryWrapper<AgentCommissionStatsEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("agent_id", agentId);
            if (startDate != null) {
                queryWrapper.ge("statistics_date", startDate);
            }
            if (endDate != null) {
                queryWrapper.le("statistics_date", endDate);
            }
            queryWrapper.orderByDesc("statistics_date", "salesman_id");
            return agentCommissionStatsDao.selectList(queryWrapper);
        } catch (Exception e) {
            log.error("根据代理ID查询统计数据失败", e);
            return null;
        }
    }

    @Override
    public List<AgentCommissionStatsEntity> getStatsBySalesmanId(String salesmanId, Date startDate, Date endDate) {
        log.debug("根据业务员ID查询统计数据：salesmanId={}, startDate={}, endDate={}", salesmanId, startDate, endDate);
        try {
            QueryWrapper<AgentCommissionStatsEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("salesman_id", salesmanId);
            if (startDate != null) {
                queryWrapper.ge("statistics_date", startDate);
            }
            if (endDate != null) {
                queryWrapper.le("statistics_date", endDate);
            }
            queryWrapper.orderByDesc("statistics_date");
            return agentCommissionStatsDao.selectList(queryWrapper);
        } catch (Exception e) {
            log.error("根据业务员ID查询统计数据失败", e);
            return null;
        }
    }

    @Override
    public List<AgentCommissionStatsEntity> getStatsByDateRange(Date startDate, Date endDate) {
        log.debug("查询日期范围内的统计数据：startDate={}, endDate={}", startDate, endDate);
        try {
            QueryWrapper<AgentCommissionStatsEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.between("statistics_date", startDate, endDate)
                       .orderByDesc("statistics_date", "agent_id", "salesman_id");
            return agentCommissionStatsDao.selectList(queryWrapper);
        } catch (Exception e) {
            log.error("查询日期范围内的统计数据失败", e);
            return null;
        }
    }

    @Override
    public Long getTotalCommissionAmountByDate(Date statisticsDate) {
        log.debug("统计指定日期的兑付总额：{}", statisticsDate);
        try {
            QueryWrapper<AgentCommissionStatsEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("statistics_date", statisticsDate)
                       .select("IFNULL(SUM(commission_amount), 0) as total");
            Map<String, Object> result = agentCommissionStatsDao.selectMaps(queryWrapper).get(0);
            return result != null ? Long.valueOf(result.get("total").toString()) : 0L;
        } catch (Exception e) {
            log.error("统计指定日期的兑付总额失败", e);
            return 0L;
        }
    }

    @Override
    public Long getTotalCommissionAmountByDateRange(Date startDate, Date endDate) {
        log.debug("统计指定日期范围的兑付总额：startDate={}, endDate={}", startDate, endDate);
        try {
            QueryWrapper<AgentCommissionStatsEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.between("statistics_date", startDate, endDate)
                       .select("IFNULL(SUM(commission_amount), 0) as total");
            Map<String, Object> result = agentCommissionStatsDao.selectMaps(queryWrapper).get(0);
            return result != null ? Long.valueOf(result.get("total").toString()) : 0L;
        } catch (Exception e) {
            log.error("统计指定日期范围的兑付总额失败", e);
            return 0L;
        }
    }

    @Override
    public Map<String, Object> getSummaryStats(Date startDate, Date endDate) {
        log.debug("获取统计汇总数据：startDate={}, endDate={}", startDate, endDate);
        try {
            QueryWrapper<AgentCommissionStatsEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.between("statistics_date", startDate, endDate)
                       .select("COUNT(DISTINCT agent_id) as total_agents",
                               "COUNT(DISTINCT salesman_id) as total_salesmen",
                               "IFNULL(SUM(total_charge_amount), 0) as total_charge_amount",
                               "IFNULL(SUM(total_withdraw_amount), 0) as total_withdraw_amount",
                               "IFNULL(SUM(net_amount), 0) as total_net_amount",
                               "IFNULL(SUM(commission_amount), 0) as total_commission_amount",
                               "IFNULL(SUM(user_count), 0) as total_users");
            Map<String, Object> result = agentCommissionStatsDao.selectMaps(queryWrapper).get(0);
            return result != null ? result : new HashMap<>();
        } catch (Exception e) {
            log.error("获取统计汇总数据失败", e);
            return new HashMap<>();
        }
    }

    @Override
    public void calculateCommissionManually(Date targetDate) {
        log.info("手动执行代理兑付收益统计，目标日期：{}", targetDate);
        try {
            agentCommissionSchedule.calculateCommissionManually(targetDate);
        } catch (Exception e) {
            log.error("手动执行代理兑付收益统计失败", e);
            throw e;
        }
    }

    @Override
    public Map<String, Object> getCommissionReport(Date startDate, Date endDate, String agentId, String salesmanId) {
        log.debug("获取代理兑付收益报表数据：startDate={}, endDate={}, agentId={}, salesmanId={}", 
                startDate, endDate, agentId, salesmanId);
        
        Map<String, Object> reportData = new HashMap<>();
        
        try {
            // 获取统计数据
            List<AgentCommissionStatsEntity> statsList;
            if (agentId != null && !agentId.trim().isEmpty()) {
                if (salesmanId != null && !salesmanId.trim().isEmpty()) {
                    // 按业务员ID查询
                    statsList = getStatsBySalesmanId(salesmanId, startDate, endDate);
                } else {
                    // 按代理ID查询
                    statsList = getStatsByAgentId(agentId, startDate, endDate);
                }
            } else {
                // 查询所有数据
                statsList = getStatsByDateRange(startDate, endDate);
            }
            
            // 获取汇总数据
            Map<String, Object> summaryStats = getSummaryStats(startDate, endDate);
            
            // 构建报表数据
            reportData.put("statsList", statsList);
            reportData.put("summaryStats", summaryStats);
            reportData.put("startDate", startDate);
            reportData.put("endDate", endDate);
            reportData.put("agentId", agentId);
            reportData.put("salesmanId", salesmanId);
            
            // 计算统计信息
            if (statsList != null && !statsList.isEmpty()) {
                long totalChargeAmount = statsList.stream()
                        .mapToLong(AgentCommissionStatsEntity::getTotalChargeAmount)
                        .sum();
                long totalWithdrawAmount = statsList.stream()
                        .mapToLong(AgentCommissionStatsEntity::getTotalWithdrawAmount)
                        .sum();
                long totalNetAmount = statsList.stream()
                        .mapToLong(AgentCommissionStatsEntity::getNetAmount)
                        .sum();
                long totalCommissionAmount = statsList.stream()
                        .mapToLong(AgentCommissionStatsEntity::getCommissionAmount)
                        .sum();
                
                reportData.put("totalChargeAmount", totalChargeAmount);
                reportData.put("totalWithdrawAmount", totalWithdrawAmount);
                reportData.put("totalNetAmount", totalNetAmount);
                reportData.put("totalCommissionAmount", totalCommissionAmount);
                reportData.put("recordCount", statsList.size());
            }
            
            log.debug("代理兑付收益报表数据获取成功，共 {} 条记录", 
                    statsList != null ? statsList.size() : 0);
            
        } catch (Exception e) {
            log.error("获取代理兑付收益报表数据失败", e);
            reportData.put("error", e.getMessage());
        }
        
        return reportData;
    }
}
