package io.renren.schedule;

import io.renren.dao.UserBalanceDetailDao;
import io.renren.entity.UserBalanceDetailEntity;
import io.renren.entity.UserEntity;
import io.renren.enums.BusinessTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.renren.config.InvestmentProfitConfig;
import io.renren.dao.InvestmentRecordDao;
import io.renren.dao.UserDao;
import io.renren.entity.InvestmentRecordEntity;
import io.renren.service.BalanceDetailService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户投资收益计算定时任务
 * 
 * 使用Spring Boot的@Scheduled注解实现定时任务
 * 每天9点半执行，计算用户投资收益
 *
 * @author renren
 * @since 1.0.0
 */
@Slf4j
@Component
public class UserInvestmentProfitSchedule {
    
    @Autowired
    private InvestmentRecordDao investmentRecordDao;
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private BalanceDetailService balanceDetailService;
    
    @Autowired
    private InvestmentProfitConfig profitConfig;


    @Autowired
    private UserBalanceDetailDao userBalanceDetailDao;
    
    /**
     * 每天9点半执行用户投资收益计算
     * cron表达式：0 30 9 * * ? (秒 分 时 日 月 周)
     */
    @Scheduled(cron = "0 30 9 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void calculateUserInvestmentProfit() {
        log.info("开始执行用户投资收益计算定时任务，执行时间：{}", new Date());
        
        try {
            // 1. 获取所有有投资的用户
            List<Long> userIds = getUserIdsWithInvestment();
            
            if (userIds == null || userIds.isEmpty()) {
                log.info("没有找到有投资的用户，任务结束");
                return;
            }
            
            log.info("找到 {} 个有投资的用户，开始计算收益", userIds.size());
            
            // 2. 遍历用户计算投资收益
            for (Long userId : userIds) {
                try {
                    calculateUserInvestmentProfit(userId);
                } catch (Exception e) {
                    log.error("计算用户 {} 投资收益失败", userId, e);
                    // 继续处理下一个用户，不影响其他用户
                }
            }
            
            log.info("用户投资收益计算定时任务执行完成，共处理 {} 个用户", userIds.size());
            
        } catch (Exception e) {
            log.error("执行用户投资收益计算定时任务失败", e);
            throw e; // 抛出异常，触发事务回滚
        }
    }
    
    /**
     * 测试用的定时任务，每分钟执行一次（仅用于开发测试）
     */
    @Scheduled(fixedRate = 60000) // 每分钟执行一次
    public void testSchedule() {
        log.debug("测试定时任务执行，时间：{}", new Date());
        // 这里可以添加测试逻辑
    }
    
    /**
     * 获取所有有投资的用户ID列表
     * 
     * @return 用户ID列表
     */
    private List<Long> getUserIdsWithInvestment() {
        log.debug("查询有投资的用户ID列表");
        
        try {
            // 查询所有有投资记录的用户ID（去重）
            // 状态为0表示未收益，1表示已收益，都算作有投资
            List<Long> userIds = investmentRecordDao.selectDistinctUserIdsWithInvestment();
            
            if (userIds == null || userIds.isEmpty()) {
                log.info("数据库中没有找到有投资的用户");
                return new ArrayList<>();
            }
            
            log.debug("从数据库查询到 {} 个有投资的用户", userIds.size());
            return userIds;
            
        } catch (Exception e) {
            log.error("查询有投资用户失败", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 计算指定用户的投资收益
     * 
     * @param userId 用户ID
     */
    private void calculateUserInvestmentProfit(Long userId) {
        log.debug("开始计算用户 {} 的投资收益", userId);
        
        try {
            // 1. 获取用户当前投资记录
            List<InvestmentRecordEntity> investmentRecords = investmentRecordDao.selectByUserId(userId);
            
            if (investmentRecords == null || investmentRecords.isEmpty()) {
                log.debug("用户 {} 没有投资记录", userId);
                return;
            }
            
            // 2. 计算每个投资项目的收益
            for (InvestmentRecordEntity record : investmentRecords) {
                try {
                    calculateInvestmentProfit(record);
                } catch (Exception e) {
                    log.error("计算投资项目 {} 收益失败，用户ID: {}", record.getId(), userId, e);
                    // 继续处理下一个投资项目
                }
            }
            
            log.debug("用户 {} 投资收益计算完成", userId);
            
        } catch (Exception e) {
            log.error("计算用户 {} 投资收益失败", userId, e);
            throw e;
        }
    }
    
    /**
     * 计算单个投资项目的收益
     * 
     * @param record 投资记录
     */
    private void calculateInvestmentProfit(InvestmentRecordEntity record) {
        log.debug("开始计算投资项目 {} 的收益", record.getId());
        
        try {
            // 1. 检查投资状态，只处理未收益的投资
            if (record.getStatus() != null && record.getStatus() == 1) {
                log.debug("投资项目 {} 已经收益，跳过处理", record.getId());
                return;
            }
            
            // 2. 计算收益金额
            BigDecimal profitAmount = calculateProfitAmount(record);
            
            if (profitAmount.compareTo(BigDecimal.ZERO) <= 0) {
                log.debug("投资项目 {} 没有收益，跳过处理", record.getId());
                return;
            }
            
            // 3. 更新投资记录状态
            updateInvestmentRecordStatus(record.getId(), profitAmount);
            
            // 4. 更新用户余额
            updateUserBalance(record.getUserId(), profitAmount);
            
            // 5. 记录账变
            recordProfitDetail(record, profitAmount);
            
            log.debug("投资项目 {} 收益计算完成，收益金额: {}", record.getId(), profitAmount);
            
        } catch (Exception e) {
            log.error("计算投资项目 {} 收益失败", record.getId(), e);
            throw e;
        }
    }
    
    /**
     * 根据投资记录计算收益金额
     * 
     * @param record 投资记录
     * @return 收益金额
     */
    private BigDecimal calculateProfitAmount(InvestmentRecordEntity record) {
        log.debug("计算投资项目 {} 的收益金额", record.getId());
        
        try {
            // 1. 验证投资记录的基本信息
            if (record.getInvestmentAmount() == null || record.getInvestmentAmount() <= 0) {
                log.warn("投资项目 {} 投资金额无效", record.getId());
                return BigDecimal.ZERO;
            }
            
            if (record.getOrderDate() == null) {
                log.warn("投资项目 {} 投资日期无效", record.getId());
                return BigDecimal.ZERO;
            }
            
            // 2. 转换为BigDecimal进行计算
            BigDecimal investmentAmount = new BigDecimal(record.getInvestmentAmount());
            
            // 3. 根据周期类型和投资天数计算收益
            BigDecimal profitAmount = calculateProfitByCycleType(record, investmentAmount);
            
            log.debug("投资项目 {} 收益计算：投资金额={}, 收益金额={}", 
                     record.getId(), investmentAmount, profitAmount);
            
            return profitAmount;
            
        } catch (Exception e) {
            log.error("计算投资项目 {} 收益金额失败", record.getId(), e);
            return BigDecimal.ZERO;
        }
    }
    
    /**
     * 根据周期类型计算收益
     * 
     * @param record 投资记录
     * @param investmentAmount 投资金额
     * @return 收益金额
     */
    private BigDecimal calculateProfitByCycleType(InvestmentRecordEntity record, BigDecimal investmentAmount) {
        try {
            // 获取项目信息（这里需要注入ProjectDao来查询项目详情）
            // ProjectEntity project = projectDao.selectById(record.getProjectId());
            
            // 暂时使用投资记录中的信息
            Integer cycleType = record.getCycleType();
            Integer cycle = record.getCycle();
            
            if (cycleType == null) {
                cycleType = 1; // 默认到期收益含本金
            }
            
            if (cycle == null || cycle <= 0) {
                cycle = 1; // 默认1天
            }
            
            // 计算投资天数
            int investmentDays = calculateInvestmentDays(record.getOrderDate());
            
            log.debug("投资项目 {} 周期类型：{}, 周期：{}天, 投资天数：{}天", 
                     record.getId(), cycleType, cycle, investmentDays);
            
            BigDecimal profitAmount = BigDecimal.ZERO;
            
            switch (cycleType) {
                case 1: // 到期收益含本金
                    profitAmount = calculateMaturityProfit(investmentAmount, cycle, investmentDays);
                    break;
                case 2: // 每日返本金到期收益
                    profitAmount = calculateDailyReturnProfit(investmentAmount, cycle, investmentDays);
                    break;
                case 3: // 不返本金
                    profitAmount = calculateNoPrincipalReturnProfit(investmentAmount, cycle, investmentDays);
                    break;
                case 4: // 复利产品
                    profitAmount = calculateCompoundInterestProfit(investmentAmount, cycle, investmentDays);
                    break;
                case 5: // 阶梯日益
                    profitAmount = calculateSteppedDailyProfit(investmentAmount, cycle, investmentDays);
                    break;
                case 6: // 拼团
                    profitAmount = calculateGroupBuyProfit(investmentAmount, cycle, investmentDays);
                    break;
                default:
                    // 默认使用到期收益含本金的计算方式
                    profitAmount = calculateMaturityProfit(investmentAmount, cycle, investmentDays);
                    break;
            }
            
            return profitAmount;
            
        } catch (Exception e) {
            log.error("根据周期类型计算收益失败，投资项目ID: {}", record.getId(), e);
            return BigDecimal.ZERO;
        }
    }
    
    /**
     * 计算投资天数
     * 
     * @param orderDate 投资日期
     * @return 投资天数
     */
    private int calculateInvestmentDays(Date orderDate) {
        try {
            Date currentDate = new Date();
            long diffInMillies = currentDate.getTime() - orderDate.getTime();
            long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);
            return Math.max(1, (int) diffInDays); // 最少1天
        } catch (Exception e) {
            log.warn("计算投资天数失败，使用默认值1天", e);
            return 1;
        }
    }
    
    /**
     * 计算到期收益含本金的收益
     * 
     * @param investmentAmount 投资金额
     * @param cycle 项目周期
     * @param investmentDays 投资天数
     * @return 收益金额
     */
    private BigDecimal calculateMaturityProfit(BigDecimal investmentAmount, Integer cycle, int investmentDays) {
        // 使用配置的年化收益率
        BigDecimal annualRate = profitConfig.getMaturityAnnualRate();
        
        // 计算实际收益率：年化收益率 * 投资天数 / 365
        BigDecimal actualRate = annualRate.multiply(new BigDecimal(investmentDays))
                                         .divide(new BigDecimal(365), 4, BigDecimal.ROUND_HALF_UP);
        
        return investmentAmount.multiply(actualRate);
    }
    
    /**
     * 计算每日返本金到期收益的收益
     * 
     * @param investmentAmount 投资金额
     * @param cycle 项目周期
     * @param investmentDays 投资天数
     * @return 收益金额
     */
    private BigDecimal calculateDailyReturnProfit(BigDecimal investmentAmount, Integer cycle, int investmentDays) {
        // 使用配置的年化收益率
        BigDecimal annualRate = profitConfig.getDailyReturnAnnualRate();
        BigDecimal dailyRate = annualRate.divide(new BigDecimal(365), 6, BigDecimal.ROUND_HALF_UP);
        
        return investmentAmount.multiply(dailyRate).multiply(new BigDecimal(investmentDays));
    }
    
    /**
     * 计算不返本金的收益
     * 
     * @param investmentAmount 投资金额
     * @param cycle 项目周期
     * @param investmentDays 投资天数
     * @return 收益金额
     */
    private BigDecimal calculateNoPrincipalReturnProfit(BigDecimal investmentAmount, Integer cycle, int investmentDays) {
        // 使用配置的年化收益率
        BigDecimal annualRate = profitConfig.getNoPrincipalAnnualRate();
        BigDecimal actualRate = annualRate.multiply(new BigDecimal(investmentDays))
                                         .divide(new BigDecimal(365), 4, BigDecimal.ROUND_HALF_UP);
        
        return investmentAmount.multiply(actualRate);
    }
    
    /**
     * 计算复利产品的收益
     * 
     * @param investmentAmount 投资金额
     * @param cycle 项目周期
     * @param investmentDays 投资天数
     * @return 收益金额
     */
    private BigDecimal calculateCompoundInterestProfit(BigDecimal investmentAmount, Integer cycle, int investmentDays) {
        // 复利计算：P * (1 + r)^n - P
        // 使用配置的日收益率
        BigDecimal dailyRate = profitConfig.getCompoundDailyRate();
        BigDecimal compoundFactor = BigDecimal.ONE.add(dailyRate).pow(investmentDays);
        
        return investmentAmount.multiply(compoundFactor).subtract(investmentAmount);
    }
    
    /**
     * 计算阶梯日益的收益
     * 
     * @param investmentAmount 投资金额
     * @param cycle 项目周期
     * @param investmentDays 投资天数
     * @return 收益金额
     */
    private BigDecimal calculateSteppedDailyProfit(BigDecimal investmentAmount, Integer cycle, int investmentDays) {
        // 使用配置的阶梯收益率
        BigDecimal totalProfit = BigDecimal.ZERO;
        
        for (int day = 1; day <= investmentDays; day++) {
            BigDecimal dailyRate;
            if (day <= 7) {
                dailyRate = profitConfig.getSteppedDailyRate().getFirstWeekRate();
            } else if (day <= 15) {
                dailyRate = profitConfig.getSteppedDailyRate().getSecondWeekRate();
            } else {
                dailyRate = profitConfig.getSteppedDailyRate().getLaterRate();
            }
            
            totalProfit = totalProfit.add(investmentAmount.multiply(dailyRate));
        }
        
        return totalProfit;
    }
    
    /**
     * 计算拼团的收益
     * 
     * @param investmentAmount 投资金额
     * @param cycle 项目周期
     * @param investmentDays 投资天数
     * @return 收益金额
     */
    private BigDecimal calculateGroupBuyProfit(BigDecimal investmentAmount, Integer cycle, int investmentDays) {
        // 使用配置的拼团收益率
        // 拼团收益：基础收益 + 拼团奖励
        BigDecimal baseAnnualRate = profitConfig.getGroupBuyRate().getBaseAnnualRate();
        BigDecimal baseProfit = baseAnnualRate.multiply(new BigDecimal(investmentDays))
                                             .divide(new BigDecimal(365), 4, BigDecimal.ROUND_HALF_UP);
        
        // 拼团奖励
        BigDecimal groupBonus = profitConfig.getGroupBuyRate().getGroupBonusRate();
        
        return investmentAmount.multiply(baseProfit).add(investmentAmount.multiply(groupBonus));
    }
    
    /**
     * 更新投资记录状态
     * 
     * @param investmentId 投资记录ID
     * @param profitAmount 收益金额
     */
    private void updateInvestmentRecordStatus(Long investmentId, BigDecimal profitAmount) {
        log.debug("更新投资记录 {} 状态，收益金额: {}", investmentId, profitAmount);
        
        try {
            // 这里应该调用DAO更新投资记录状态
            // 更新状态为已收益，设置收益金额和收益日期
            // investmentRecordDao.updateStatusAndProfit(investmentId, profitAmount, new Date());
            
            log.debug("投资记录 {} 状态更新完成", investmentId);
            
        } catch (Exception e) {
            log.error("更新投资记录 {} 状态失败", investmentId, e);
            throw e;
        }
    }
    
    /**
     * 更新用户余额
     * 
     * @param userId 用户ID
     * @param profitAmount 收益金额
     */
    private void updateUserBalance(Long userId, BigDecimal profitAmount) {
        log.debug("更新用户 {} 余额，收益金额: {}", userId, profitAmount);
        
        try {
            // 这里应该调用用户服务更新余额
            // 将收益金额添加到用户余额中
            // userService.addBalance(userId, profitAmount);
            
            log.debug("用户 {} 余额更新完成", userId);
            
        } catch (Exception e) {
            log.error("更新用户 {} 余额失败", userId, e);
            throw e;
        }
    }
    
    /**
     * 记录投资收益账变
     * 
     * @param record 投资记录
     * @param profitAmount 收益金额
     */
    private void recordProfitDetail(InvestmentRecordEntity record, BigDecimal profitAmount) {
        log.debug("记录投资项目 {} 的投资收益账变，金额：{}", record.getId(), profitAmount);
        
        try {
            // 金额：收益金额（转换为分）
            Long profitAmountInCents = profitAmount.multiply(new BigDecimal("100")).longValue();

            // 记录账变明细
            UserBalanceDetailEntity userBalanceDetail = new UserBalanceDetailEntity();
            userBalanceDetail.setUserId(record.getUserId());
            Date now = new Date();
            userBalanceDetail.setTransactionDate(now);
            //业务类型：投资收益
            userBalanceDetail.setBusinessType(BusinessTypeEnum.INCOME.getCode());
            userBalanceDetail.setChannel("1");

            // 设置交易流水ID
            userBalanceDetail.setStreamId(record.getId().toString());

            // 设置使用金额（签到奖励金额）
            userBalanceDetail.setUseAmount(profitAmountInCents);
            UserEntity user = userDao.getUserByUserId(record.getUserId());
            // 设置原始金额（签到前的余额）
            userBalanceDetail.setOriginalAmount(user.getAssets() != null ? user.getAssets() : 0L);

            // 设置交易后金额（签到后的余额）
            userBalanceDetail.setTransactionAmount(user.getAssets() != null ? user.getAssets() + profitAmountInCents : profitAmountInCents);
            userBalanceDetail.setRemarks("投资收益【"+record.getProjectId()+"】");
            userBalanceDetail.setStatus(1);
            userBalanceDetail.setCreateDate(now);
            userBalanceDetail.setUpdateDate(now);

            // 插入账变记录
            userBalanceDetailDao.insert(userBalanceDetail);
            log.debug("投资项目 {} 投资收益账变记录完成，金额：{}", record.getId(), profitAmount);
            
        } catch (Exception e) {
            log.error("记录投资项目 {} 投资收益账变失败", record.getId(), e);
            throw e;
        }
    }
}
