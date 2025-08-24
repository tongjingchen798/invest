package io.renren.schedule;

import io.renren.dao.*;
import io.renren.entity.*;
import io.renren.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * 代理兑付收益统计定时任务
 * 
 * 统计维度：代理、代理下的业务员、时间、兑付金额
 * 每天0点统计前一天的业务员兑付金额：计算公式 所有名下用户总充值和提现差
 *
 * @author renren
 * @since 1.0.0
 */
@Slf4j
@Component
public class AgentCommissionSchedule {
    
    @Autowired
    private ChargeOrderDao chargeOrderDao;
    
    @Autowired
    private WithdrawOrderDao withdrawOrderDao;
    
    @Autowired
    private AgentCommissionStatsDao agentCommissionStatsDao;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 每天0点执行代理兑付收益统计
     * cron表达式：0 0 0 * * ? (秒 分 时 日 月 周)
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void calculateAgentCommission() {
        log.info("开始执行代理兑付收益统计定时任务，执行时间：{}", new Date());
        
        try {
            // 获取前一天的日期范围
            Date yesterday = getYesterday();
            Date startTime = getStartOfDay(yesterday);
            Date endTime = getEndOfDay(yesterday);
            
            log.info("统计时间范围：{} 到 {}", 
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime));
            
            // 1. 获取所有代理和业务员信息
            Map<Long, List<Long>> agentSalesmanMap = getAgentSalesmanMapping();
            log.info("获取到 {} 个代理，共 {} 个业务员", 
                    agentSalesmanMap.size(), 
                    agentSalesmanMap.values().stream().mapToInt(List::size).sum());
            
            // 2. 统计前一天的充值数据
            Map<String, Object> chargeStats = getChargeStatistics(startTime, endTime);
            log.info("充值统计完成，总金额：{}，总笔数：{}", 
                    chargeStats.get("totalAmount"), chargeStats.get("totalCount"));
            
            // 3. 统计前一天的提现数据
            Map<String, Object> withdrawStats = getWithdrawStatistics(startTime, endTime);
            log.info("提现统计完成，总金额：{}，总笔数：{}", 
                    withdrawStats.get("totalAmount"), withdrawStats.get("totalCount"));
            
            // 4. 计算每个业务员的兑付金额
            List<AgentCommissionStatsEntity> commissionStatsList = calculateCommissionStats(
                    agentSalesmanMap, startTime, endTime, chargeStats, withdrawStats);
            
            // 5. 保存统计结果到数据库
            if (!commissionStatsList.isEmpty()) {
                int insertCount = agentCommissionStatsDao.batchInsert(commissionStatsList);
                log.info("成功保存 {} 条代理兑付收益统计数据", insertCount);
            } else {
                log.info("没有需要保存的统计数据");
            }
            
            log.info("代理兑付收益统计定时任务执行完成，共处理 {} 个业务员", commissionStatsList.size());
            
        } catch (Exception e) {
            log.error("执行代理兑付收益统计定时任务失败", e);
            throw e; // 抛出异常，触发事务回滚
        }
    }
    
    /**
     * 获取前一天的日期
     */
    private Date getYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTime();
    }
    
    /**
     * 获取指定日期的开始时间（00:00:00）
     */
    private Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    
    /**
     * 获取指定日期的结束时间（23:59:59）
     */
    private Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }
    
    /**
     * 获取代理和业务员的映射关系
     */
    private Map<Long, List<Long>> getAgentSalesmanMapping() {
        log.debug("开始获取代理和业务员映射关系");
        
        try {
            // 这里应该从用户表或其他相关表获取代理和业务员的映射关系
            // 暂时使用模拟数据，实际项目中需要根据具体业务逻辑实现
            
            Map<Long, List<Long>> agentSalesmanMap = new HashMap<>();
            
//            // 模拟数据：实际应该从数据库查询
//            agentSalesmanMap.put("AG001", Arrays.asList("SM001", "SM002"));
//            agentSalesmanMap.put("AG002", Arrays.asList("SM003", "SM004"));
//            agentSalesmanMap.put("AG003", Arrays.asList("SM005"));
            
            log.debug("获取到代理业务员映射关系：{}", agentSalesmanMap);
            return agentSalesmanMap;
            
        } catch (Exception e) {
            log.error("获取代理业务员映射关系失败", e);
            return new HashMap<>();
        }
    }
    
    /**
     * 统计充值数据
     */
    private Map<String, Object> getChargeStatistics(Date startTime, Date endTime) {
        log.debug("开始统计充值数据，时间范围：{} 到 {}", startTime, endTime);
        
        try {
            // 使用MyBatis-Plus的QueryWrapper构建查询条件
            QueryWrapper<ChargeOrderEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("state", 1) // 只统计审核通过的充值
                       .ge("create_time", startTime)
                       .le("create_time", endTime);
            
            // 查询充值订单
            List<ChargeOrderEntity> chargeOrders = chargeOrderDao.selectList(queryWrapper);
            
            // 统计结果
            Map<String, Object> stats = new HashMap<>();
            long totalAmount = 0;
            int totalCount = 0;
            
            // 按业务员分组统计
            Map<Long, Long> salesmanChargeMap = new HashMap<>();
            // 按代理分组统计
            Map<Long, Long> agentChargeMap = new HashMap<>();
            
            for (ChargeOrderEntity order : chargeOrders) {
                if (order.getAmount() != null) {
                    totalAmount += order.getAmount();
                    totalCount++;
                    
                    // 按业务员统计
                    Long salesmanKey = order.getSalesmanid();
                    salesmanChargeMap.merge(salesmanKey, order.getAmount(), Long::sum);
                    
                    // 按代理统计
                    Long agentKey = order.getAgent();
                    agentChargeMap.merge(agentKey, order.getAmount(), Long::sum);
                }
            }
            
            stats.put("totalAmount", totalAmount);
            stats.put("totalCount", totalCount);
            stats.put("salesmanChargeMap", salesmanChargeMap);
            stats.put("agentChargeMap", agentChargeMap);
            stats.put("chargeOrders", chargeOrders);
            
            log.debug("充值统计完成，总金额：{}，总笔数：{}", totalAmount, totalCount);
            return stats;
            
        } catch (Exception e) {
            log.error("统计充值数据失败", e);
            return new HashMap<>();
        }
    }
    
    /**
     * 统计提现数据
     */
    private Map<String, Object> getWithdrawStatistics(Date startTime, Date endTime) {
        log.debug("开始统计提现数据，时间范围：{} 到 {}", startTime, endTime);
        
        try {
            // 使用MyBatis-Plus的QueryWrapper构建查询条件
            QueryWrapper<WithdrawOrderEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("state", Arrays.asList(1, 2)) // 统计审核通过和已提现的
                       .ge("create_time", startTime)
                       .le("create_time", endTime);
            
            // 查询提现订单
            List<WithdrawOrderEntity> withdrawOrders = withdrawOrderDao.selectList(queryWrapper);
            
            // 统计结果
            Map<String, Object> stats = new HashMap<>();
            long totalAmount = 0;
            int totalCount = 0;
            
            // 按业务员分组统计
            Map<Long, Long> salesmanWithdrawMap = new HashMap<>();
            // 按代理分组统计
            Map<Long, Long> agentWithdrawMap = new HashMap<>();
            
            for (WithdrawOrderEntity order : withdrawOrders) {
                if (order.getAmount() != null) {
                    totalAmount += order.getAmount();
                    totalCount++;
                    
                    // 按业务员统计
                    Long salesmanKey = order.getSalesmanid();
                    salesmanWithdrawMap.merge(salesmanKey, order.getAmount(), Long::sum);
                    
                    // 按代理统计
                    Long agentKey = order.getAgent();
                    agentWithdrawMap.merge(agentKey, order.getAmount(), Long::sum);
                }
            }
            
            stats.put("totalAmount", totalAmount);
            stats.put("totalCount", totalCount);
            stats.put("salesmanWithdrawMap", salesmanWithdrawMap);
            stats.put("agentWithdrawMap", agentWithdrawMap);
            stats.put("withdrawOrders", withdrawOrders);
            
            log.debug("提现统计完成，总金额：{}，总笔数：{}", totalAmount, totalCount);
            return stats;
            
        } catch (Exception e) {
            log.error("统计提现数据失败", e);
            return new HashMap<>();
        }
    }
    
    /**
     * 计算每个业务员的兑付金额
     */
    private List<AgentCommissionStatsEntity> calculateCommissionStats(
            Map<Long, List<Long>> agentSalesmanMap,
            Date startTime, Date endTime,
            Map<String, Object> chargeStats,
            Map<String, Object> withdrawStats) {
        
        log.debug("开始计算业务员兑付金额");
        
        List<AgentCommissionStatsEntity> commissionStatsList = new ArrayList<>();
        
        try {
            // 获取充值统计数据
            @SuppressWarnings("unchecked")
            Map<String, Long> salesmanChargeMap = (Map<String, Long>) chargeStats.get("salesmanChargeMap");
//            @SuppressWarnings("unchecked")
//            Map<String, Long> agentChargeMap = (Map<String, Long>) chargeStats.get("agentChargeMap");
            
            // 获取提现统计数据
            @SuppressWarnings("unchecked")
            Map<String, Long> salesmanWithdrawMap = (Map<String, Long>) withdrawStats.get("salesmanWithdrawMap");
//            @SuppressWarnings("unchecked")
//            Map<String, Long> agentWithdrawMap = (Map<String, Long>) withdrawStats.get("agentWithdrawMap");
//
            // 遍历每个代理和其下的业务员
            for (Map.Entry<Long, List<Long>> entry : agentSalesmanMap.entrySet()) {
                Long agentId = entry.getKey();
                List<Long> salesmanList = entry.getValue();
                
                // 获取代理名称
                String agentName = getAgentName(agentId);
                
                for (Long salesmanId : salesmanList) {
                    // 检查是否已经统计过
                    if (isAlreadyCalculated(agentId, salesmanId, startTime)) {
                        log.debug("业务员 {} 在 {} 的统计数据已存在，跳过", salesmanId, startTime);
                        continue;
                    }
                    
                    // 获取业务员名称
                    String salesmanName = getSalesmanName(salesmanId);
                    
                    // 创建统计记录
                    AgentCommissionStatsEntity statsEntity = new AgentCommissionStatsEntity();
                    statsEntity.setAgentId(agentId);
                    statsEntity.setAgentName(agentName);
                    statsEntity.setSalesmanId(salesmanId);
                    statsEntity.setSalesmanName(salesmanName);
                    statsEntity.setStatisticsDate(startTime);
                    statsEntity.setCreateTime(new Date());
                    statsEntity.setUpdateTime(new Date());
                    statsEntity.setStatus(1);
                    statsEntity.setCommissionRate(new BigDecimal("1"));
                    
                    // 计算业务员的充值总额
                    long salesmanChargeAmount = salesmanChargeMap.getOrDefault(salesmanId, 0L);
                    statsEntity.setTotalChargeAmount(salesmanChargeAmount);
                    
                    // 计算业务员的提现总额
                    long salesmanWithdrawAmount = salesmanWithdrawMap.getOrDefault(salesmanId, 0L);
                    statsEntity.setTotalWithdrawAmount(salesmanWithdrawAmount);
                    
                    // 计算净额（充值 - 提现）
                    long netAmount = salesmanChargeAmount - salesmanWithdrawAmount;
                    statsEntity.setNetAmount(netAmount);
                    
                    // 计算兑付金额（净额 * 5%）
                    if (netAmount > 0) {
                        BigDecimal netAmountDecimal = new BigDecimal(netAmount);
                        BigDecimal commissionRate = new BigDecimal("0.05"); // 5%
                        BigDecimal commissionAmount = netAmountDecimal.multiply(commissionRate)
                                .setScale(0, RoundingMode.HALF_UP);
                        statsEntity.setCommissionAmount(commissionAmount.longValue());
                    } else {
                        statsEntity.setCommissionAmount(0L);
                    }
                    
                    // 统计业务员名下的用户数量
                    int userCount = getUserCountBySalesman(salesmanId);
                    statsEntity.setUserCount(userCount);
                    
                    // 设置备注
                    statsEntity.setRemark(String.format("自动统计 - 充值:%d, 提现:%d, 净额:%d, 兑付比例:5%%", 
                            salesmanChargeAmount, salesmanWithdrawAmount, netAmount));
                    
                    commissionStatsList.add(statsEntity);
                    
                    log.debug("业务员 {} 兑付计算完成：充值={}, 提现={}, 净额={}, 兑付金额={}, 用户数={}", 
                             salesmanId, salesmanChargeAmount, salesmanWithdrawAmount, 
                             netAmount, statsEntity.getCommissionAmount(), userCount);
                }
            }
            
            log.debug("业务员兑付金额计算完成，共 {} 个业务员", commissionStatsList.size());
            return commissionStatsList;
            
        } catch (Exception e) {
            log.error("计算业务员兑付金额失败", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 获取代理名称
     */
    private String getAgentName(Long agentId) {
        return sysUserService.getAgentNameById(agentId);
    }
    
    /**
     * 获取业务员名称
     */
    private String getSalesmanName(Long salesmanId) {
        return sysUserService.getSalesmanNameById(salesmanId);
    }
    
    /**
     * 检查是否已经统计过
     */
    private boolean isAlreadyCalculated(Long agentId, Long salesmanId, Date statisticsDate) {
        try {
            QueryWrapper<AgentCommissionStatsEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("agent_id", agentId)
                       .eq("salesman_id", salesmanId)
                       .eq("statistics_date", statisticsDate);
            Long count = agentCommissionStatsDao.selectCount(queryWrapper);
            return count > 0L;
        } catch (Exception e) {
            log.warn("检查统计数据是否存在失败，agentId: {}, salesmanId: {}, date: {}", 
                    agentId, salesmanId, statisticsDate, e);
            return false;
        }
    }
    
    /**
     * 获取业务员名下的用户数量
     */
    private int getUserCountBySalesman(Long salesmanId) {
        try {
            // 这里应该从用户表查询该业务员名下的用户数量
            // 暂时返回默认值
            return 5; // 模拟数据
        } catch (Exception e) {
            log.warn("获取业务员 {} 名下用户数量失败", salesmanId, e);
            return 0;
        }
    }
    
    /**
     * 手动执行统计（用于测试或补录）
     */
    public void calculateCommissionManually(Date targetDate) {
        log.info("手动执行代理兑付收益统计，目标日期：{}", targetDate);
        
        try {
            Date startTime = getStartOfDay(targetDate);
            Date endTime = getEndOfDay(targetDate);
            
            // 获取代理业务员映射
            Map<Long, List<Long>> agentSalesmanMap = getAgentSalesmanMapping();
            
            // 统计充值和提现数据
            Map<String, Object> chargeStats = getChargeStatistics(startTime, endTime);
            Map<String, Object> withdrawStats = getWithdrawStatistics(startTime, endTime);
            
            // 计算兑付金额
            List<AgentCommissionStatsEntity> commissionStatsList = calculateCommissionStats(
                    agentSalesmanMap, startTime, endTime, chargeStats, withdrawStats);
            
            // 保存结果
            if (!commissionStatsList.isEmpty()) {
                int insertCount = agentCommissionStatsDao.batchInsert(commissionStatsList);
                log.info("手动统计完成，成功保存 {} 条数据", insertCount);
            } else {
                log.info("手动统计完成，没有需要保存的数据");
            }
            
        } catch (Exception e) {
            log.error("手动执行代理兑付收益统计失败", e);
            throw e;
        }
    }
}
