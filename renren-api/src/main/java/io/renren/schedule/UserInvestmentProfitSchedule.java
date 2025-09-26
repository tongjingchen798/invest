package io.renren.schedule;

import io.renren.dao.ProjectDao;
import io.renren.dao.UserBalanceDetailDao;
import io.renren.entity.ProjectEntity;
import io.renren.entity.UserBalanceDetailEntity;
import io.renren.entity.UserEntity;
import io.renren.enums.BusinessTypeEnum;
import io.renren.utils.InvestmentProfitCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.renren.dao.InvestmentRecordDao;
import io.renren.dao.UserDao;
import io.renren.dao.InvestmentProfitDetailDao;
import io.renren.entity.InvestmentRecordEntity;
import io.renren.entity.InvestmentProfitDetailEntity;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
    private ProjectDao projectDao;

    @Autowired
    private UserBalanceDetailDao userBalanceDetailDao;

    @Autowired
    private InvestmentProfitDetailDao investmentProfitDetailDao;


    /**
     * 每天凌晨2点30分执行用户投资收益计算
     * cron表达式：0 30 2 * * ? (秒 分 时 日 月 周)
     */
    @Scheduled(cron = "0 30 2 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void calculateUserInvestmentProfit() {
        log.info("开始执行用户投资收益计算定时任务，执行时间：{}", new Date());
        
        try {
            int result = resetTodayFieldsByMybatisPlus();
            log.info("用户今日字段重置完成，影响用户数: {}", result);
            // 1. 获取所有有投资的用户
            List<Long> userIds = getUserIdsWithInvestment();
            
            if (userIds == null || userIds.isEmpty()) {
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
                    log.error("计算投资项目 {} 收益失败，用户ID: {}", record.getOrderId(), userId, e);
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
        log.debug("开始计算投资项目 {} 的收益", record.getOrderId());
        
        try {
            // 1. 检查投资状态，只处理未收益的投资
            if (record.getStatus() != null && record.getStatus() == 1) {
                log.debug("投资项目 {} 已经收益，跳过处理", record.getOrderId());
                return;
            }
            
            // 2. 计算收益金额
            BigDecimal profitAmount = calculateProfitAmount(record);
            
            if (profitAmount.compareTo(BigDecimal.ZERO) <= 0) {
                log.debug("投资项目 {} 没有收益，跳过处理", record.getOrderId());
                return;
            }

            // 3. 判断投资是否到期（是否是最后一期）
            boolean isMatured = InvestmentProfitCalculator.isInvestmentMatured(
                record.getOrderDate(), record.getCycle()
            );
            
            // 4. 在更新余额之前获取原始余额，用于账变记录
            UserEntity userBeforeUpdate = userDao.getUserByUserId(record.getUserId());
            Long originalAssets = userBeforeUpdate.getAssets() != null ? userBeforeUpdate.getAssets() : 0L;
            
            // 5. 计算最终需要更新的金额和备注信息
            BigDecimal finalAmount = profitAmount;
            String remark = "投资收益【"+record.getProjectId()+"】";
            
            if (isMatured) {
                log.debug("投资项目 {} 已到期", record.getOrderId());
                
                // 获取项目信息以判断投资类型
                ProjectEntity project = projectDao.selectProjectById(record.getProjectId());
                Integer cycleType = project != null ? project.getCycleType() : null;
                
                // 根据投资类型设置不同的备注信息
                if (cycleType != null && cycleType == 3) { // 不返本金类型
                    remark = "投资收益【"+record.getProjectId()+"】";
                    log.info("投资项目 {} 到期完成，收益: {} 元（不返本金类型）", record.getOrderId(), finalAmount);
                } else {
                    // 每日返利和复利产品类型已经在计算方法中包含了本金
                    remark = "投资收益+本金返还【"+record.getProjectId()+"】";
                    log.info("投资项目 {} 到期完成，收益（已包含本金）: {} 元", record.getOrderId(), finalAmount);
                }
                
                // 更新投资记录状态为已收益
                record.setStatus(1);
                record.setProfitPrincipal(record.getInvestmentAmount() * (record.getInvestCount() != null ? record.getInvestCount() : 1));
                investmentRecordDao.updateById(record);
                
            } else {
                log.debug("投资项目 {} 未到期，当前为第 {} 期，总周期 {} 天", 
                         record.getOrderId(),
                         InvestmentProfitCalculator.calculateInvestmentDays(record.getOrderDate()) + 1,
                         record.getCycle());
            }
            
            // 6. 统一更新用户余额
            updateUserBalance(record.getUserId(), finalAmount);
            
            // 7. 记录收益账变
            recordProfitDetail(record, finalAmount, remark, originalAssets);
            
            // 8. 记录每日投资收益明细（防止重复记录）
            recordInvestmentProfitDetail(record, finalAmount);
            
            log.debug("投资项目 {} 收益计算完成，收益金额: {}", record.getOrderId(), profitAmount);
            
        } catch (Exception e) {
            log.error("计算投资项目 {} 收益失败", record.getOrderId(), e);
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
        log.debug("计算投资项目 {} 的收益金额", record.getOrderId());
        
        try {
            // 1. 验证投资记录的基本信息
            if (record.getInvestmentAmount() == null || record.getInvestmentAmount() <= 0) {
                log.warn("投资项目 {} 投资金额无效", record.getOrderId());
                return BigDecimal.ZERO;
            }
            
            if (record.getOrderDate() == null) {
                log.warn("投资项目 {} 投资日期无效", record.getOrderId());
                return BigDecimal.ZERO;
            }
            
            // 2. 转换为BigDecimal进行计算
            BigDecimal investmentAmount = new BigDecimal(record.getInvestmentAmount());
            
            // 3. 根据周期类型和投资天数计算收益
            BigDecimal profitAmount = calculateProfitByCycleType(record, investmentAmount);
            
            log.debug("投资项目 {} 收益计算：投资金额={}, 收益金额={}", 
                     record.getOrderId(), investmentAmount, profitAmount);
            
            return profitAmount;
            
        } catch (Exception e) {
            log.error("计算投资项目 {} 收益金额失败", record.getOrderId(), e);
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
            // 必须获取项目信息，没有项目信息则无法计算收益
            ProjectEntity project = null;
            if (record.getProjectId() != null) {
                project = projectDao.selectProjectById(record.getProjectId());
            }
            
            if (project == null) {
                log.error("投资项目 {} 对应的项目 {} 不存在，无法计算收益", record.getOrderId(), record.getProjectId());
                return BigDecimal.ZERO;
            }
            
            // 从项目信息中获取配置
            Integer cycleType = project.getCycleType();
            Integer cycle = project.getCycle();
            
            if (cycleType == null) {
                log.error("项目 {} 的周期类型为空，无法计算收益", project.getInvestId());
                return BigDecimal.ZERO;
            }
            
            if (cycle == null || cycle <= 0) {
                log.error("项目 {} 的周期为空或无效，无法计算收益", project.getInvestId());
                return BigDecimal.ZERO;
            }
            
            // 计算投资天数
            int investmentDays = calculateInvestmentDays(record.getOrderDate());
            
            log.debug("投资项目 {} 周期类型：{}, 周期：{}天, 投资天数：{}天", 
                     record.getOrderId(), cycleType, cycle, investmentDays);
            
            BigDecimal profitAmount;
            
            switch (cycleType) {
                case 2: // 每日返利
                    profitAmount = calculateDailyReturnProfit(investmentAmount, cycle, investmentDays, project);
                    break;
                case 3: // 不返本金
                    profitAmount = calculateNoPrincipalReturnProfit(investmentAmount, cycle, investmentDays, project);
                    break;
                case 4: // 复利产品
                    profitAmount = calculateCompoundInterestProfit(investmentAmount, cycle, investmentDays, project);
                    break;
                case 1: // 到期返还 - 已移除
                case 5: // 阶梯日益 - 已移除
                case 6: // 拼团 - 已移除
                default:
                    log.error("不支持的周期类型: {}, 项目ID: {}。支持的类型：2-每日返利，3-不返本金，4-复利产品", cycleType, project.getInvestId());
                    return BigDecimal.ZERO;
            }
            
            return profitAmount;
            
        } catch (Exception e) {
            log.error("根据周期类型计算收益失败，投资项目ID: {}", record.getOrderId(), e);
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
     * 计算每日盈利
     * 
     * @param investmentAmount 投资金额
     * @param cycle 项目周期
     * @param investmentDays 投资天数
     * @param project 项目信息
     * @return 收益金额
     */
    private BigDecimal calculateDailyReturnProfit(BigDecimal investmentAmount, Integer cycle, int investmentDays, ProjectEntity project) {
        // 获取每日返利的日收益率
        BigDecimal dailyRate = getProjectDailyRate(project, "dailyReturn");
        
        // 计算每日收益金额
        BigDecimal dailyProfit = investmentAmount.multiply(dailyRate);
        
        // 如果是最后一期（投资天数等于项目周期），需要返还本金
        if (investmentDays >= cycle) {
            return dailyProfit.add(investmentAmount);
        }
        
        // 非最后一期，只返回每日收益
        return dailyProfit;
    }
    
    /**
     * 计算不返本金的收益
     * 
     * @param investmentAmount 投资金额
     * @param cycle 项目周期
     * @param investmentDays 投资天数
     * @param project 项目信息
     * @return 收益金额
     */
    private BigDecimal calculateNoPrincipalReturnProfit(BigDecimal investmentAmount, Integer cycle, int investmentDays, ProjectEntity project) {
        // 获取不返本金的日收益率
        BigDecimal dailyRate = getProjectDailyRate(project, "noPrincipal");
        
        // 计算每日收益金额
        BigDecimal dailyProfit = investmentAmount.multiply(dailyRate);
        
        // 不返本金类型：每日收益，最后一期也不返还本金
        return dailyProfit;
    }
    
    /**
     * 计算复利产品的收益（到期一起返回本金和利润）
     * 
     * @param investmentAmount 投资金额
     * @param cycle 项目周期（天）
     * @param investmentDays 投资天数
     * @param project 项目信息
     * @return 收益金额
     */
    private BigDecimal calculateCompoundInterestProfit(BigDecimal investmentAmount, Integer cycle, int investmentDays, ProjectEntity project) {
        // 获取复利产品的日收益率
        BigDecimal dailyRate = getProjectDailyRate(project, "compound");
        
        // 计算总收益率：日收益率 * 项目周期
        BigDecimal totalRate = dailyRate.multiply(new BigDecimal(cycle));
        
        // 计算收益金额
        BigDecimal profitAmount = investmentAmount.multiply(totalRate);
        
        // 如果是到期（投资天数等于项目周期），一起返回本金和利润
        if (investmentDays >= cycle) {
            return profitAmount.add(investmentAmount);
        }
        
        // 未到期，只返回收益
        return profitAmount;
    }
    
    
    /**
     * 从项目信息中获取日收益率
     * 
     * @param project 项目信息
     * @param rateType 收益率类型
     * @return 日收益率
     */
    private BigDecimal getProjectDailyRate(ProjectEntity project, String rateType) {
        try {
            if (project == null) {
                log.error("项目信息为空，无法获取日收益率");
                return BigDecimal.ZERO;
            }
            
            if (project.getConversion() == null || project.getConversion().trim().isEmpty()) {
                log.error("项目 {} 的收益率配置为空，无法计算日收益", project.getInvestId());
                return BigDecimal.ZERO;
            }
            
            // 从项目配置中解析日收益率
            String conversion = project.getConversion();
            log.debug("项目 {} 配置日收益率: {}%", project.getInvestId(), conversion);
            
            // 根据收益率类型解析项目配置
            return parseProjectDailyRateByType(conversion, rateType);
            
        } catch (Exception e) {
            log.error("获取项目日收益率失败，项目ID: {}, 类型: {}", project != null ? project.getInvestId() : "null", rateType, e);
            return BigDecimal.ZERO;
        }
    }
    
    
    /**
     * 根据收益率类型解析项目配置的日收益率
     * 
     * @param conversion 项目收益率配置字符串
     * @param rateType 收益率类型
     * @return 日收益率
     */
    private BigDecimal parseProjectDailyRateByType(String conversion, String rateType) {
        try {
            // 解析项目配置的日收益率
            String[] rates = conversion.split(",");
            
            if (rates.length == 1) {
                // 单一收益率，所有类型都使用这个值
                return parseSingleRate(rates[0]);
            } else if (rates.length >= 2) {
                // 多个收益率，根据类型选择
                switch (rateType) {
                    case "dailyReturn":
                        return parseSingleRate(rates[0]); // 每日返利使用第一个
                    case "noPrincipal":
                        return parseSingleRate(rates[0]); // 不返本金使用第一个
                    case "compound":
                        return parseSingleRate(rates[0]); // 复利使用第一个
                    default:
                        return parseSingleRate(rates[0]);
                }
            } else {
                log.error("无效的日收益率配置格式: {}", conversion);
                return BigDecimal.ZERO;
            }
        } catch (Exception e) {
            log.error("解析项目日收益率失败: {}, 类型: {}", conversion, rateType, e);
            return BigDecimal.ZERO;
        }
    }
    
    /**
     * 解析单个收益率字符串
     * 
     * @param rateStr 收益率字符串
     * @return 收益率（小数形式）
     */
    private BigDecimal parseSingleRate(String rateStr) {
        try {
            String cleanRate = rateStr.replace("%", "").trim();
            BigDecimal rate = new BigDecimal(cleanRate);
            // 转换为小数形式，如10.5% -> 0.105
            return rate.divide(new BigDecimal(100), 4, BigDecimal.ROUND_DOWN);
        } catch (Exception e) {
            log.error("解析单个收益率失败: {}", rateStr, e);
            return BigDecimal.ZERO;
        }
    }
    
    
    /**
     * 记录每日投资收益明细
     * 
     * @param record 投资记录
     * @param profitAmount 收益金额
     */
    private void recordInvestmentProfitDetail(InvestmentRecordEntity record, BigDecimal profitAmount) {
        log.debug("记录投资项目 {} 的每日投资收益明细，金额：{}", record.getOrderId(), profitAmount);
        
        try {
            // 检查今天是否已经派发过收益，防止重复派发
            String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            int existingCount = investmentProfitDetailDao.selectCountByInvestmentIdAndDate(record.getOrderId(), today);
            
            if (existingCount > 0) {
                log.info("投资项目 {} 今天已经派发过收益，跳过记录", record.getOrderId());
                return;
            }
            
            // 将收益金额转换为分
            Long profitAmountInCents = profitAmount.multiply(new BigDecimal("100")).longValue();
            
            // 创建投资收益明细记录
            InvestmentProfitDetailEntity profitDetail = new InvestmentProfitDetailEntity();
            profitDetail.setInvestmentId(record.getOrderId());
            profitDetail.setUserId(record.getUserId());
            profitDetail.setProjectId(record.getProjectId());
            profitDetail.setProfitType(1); // 1:利息
            profitDetail.setProfitAmount(profitAmountInCents);
            profitDetail.setProfitDate(new Date());
            profitDetail.setStatus(1); // 1:已到账
            profitDetail.setRemark("每日投资收益【" + record.getInvestName() + "】");
            profitDetail.setCreateDate(new Date());
            profitDetail.setUpdateDate(new Date());
            
            // 插入投资收益明细记录
            investmentProfitDetailDao.insert(profitDetail);
            
            log.debug("投资项目 {} 每日投资收益明细记录完成，金额：{}", record.getOrderId(), profitAmount);
            
        } catch (Exception e) {
            log.error("记录投资项目 {} 每日投资收益明细失败", record.getOrderId(), e);
            throw e;
        }
    }

    /**
     * 更新用户余额和收益相关字段
     * 
     * @param userId 用户ID
     * @param profitAmount 收益金额
     */
    private void updateUserBalance(Long userId, BigDecimal profitAmount) {
        log.debug("更新用户 {} 余额和收益字段，收益金额: {}", userId, profitAmount);
        
        try {
            Long profitAmountInCents = profitAmount.longValue();
            
            // 1. 更新用户可用余额（assets字段）- 用于投资
            int assetsResult = userDao.addUserBalance(userId, profitAmountInCents);
            if (assetsResult > 0) {
                log.debug("用户 {} 可用余额更新成功，增加: {} 分", userId, profitAmountInCents);
            } else {
                log.warn("用户 {} 可用余额更新失败", userId);
            }

            // 2. 更新用户可提现额度和收益统计字段（cashwithdrawable字段）- 用于提现
            int cashResult = userDao.updateAllCashProfitFields(userId, profitAmountInCents);
            if (cashResult > 0) {
                log.debug("用户 {} 可提现额度更新成功，增加: {} 分", userId, profitAmountInCents);
            } else {
                log.warn("用户 {} 可提现额度更新失败", userId);
            }
            
            log.debug("用户 {} 余额和收益字段更新完成", userId);
            
        } catch (Exception e) {
            log.error("更新用户 {} 余额和收益字段失败", userId, e);
            throw e;
        }
    }
    
    /**
     * 记录投资收益账变
     * 
     * @param record 投资记录
     * @param profitAmount 收益金额
     * @param remark 备注信息
     * @param originalAssets 更新前的原始余额
     */
    private void recordProfitDetail(InvestmentRecordEntity record, BigDecimal profitAmount, String remark, Long originalAssets) {
        log.debug("记录投资项目 {} 的投资收益账变，金额：{}", record.getOrderId(), profitAmount);
        
        try {
            // 金额：收益金额
            Long profitAmountInCents = profitAmount.longValue();

            // 记录账变明细
            UserBalanceDetailEntity userBalanceDetail = new UserBalanceDetailEntity();
            userBalanceDetail.setUserId(record.getUserId());
            Date now = new Date();
            userBalanceDetail.setTransactionDate(now);
            //业务类型：投资收益
            userBalanceDetail.setBusiType(BusinessTypeEnum.INCOME.getCode());
            userBalanceDetail.setChannel("1");

            // 设置交易流水ID
            userBalanceDetail.setStreamId(record.getOrderId().toString());

            // 设置使用金额（收益金额）
            userBalanceDetail.setUseAmount(profitAmountInCents);
            
            // 设置原始金额（收益前的余额）
            userBalanceDetail.setOriginalAmount(originalAssets);
            
            // 设置交易后金额（收益后的余额）
            userBalanceDetail.setTransactionAmount(originalAssets + profitAmountInCents);
            userBalanceDetail.setRemarks(remark);
            userBalanceDetail.setStatus(1);
            userBalanceDetail.setCreateDate(now);
            userBalanceDetail.setUpdateDate(now);

            // 插入账变记录
            userBalanceDetailDao.insert(userBalanceDetail);
            log.debug("投资项目 {} 投资收益账变记录完成，金额：{}", record.getOrderId(), profitAmount);
            
        } catch (Exception e) {
            log.error("记录投资项目 {} 投资收益账变失败", record.getOrderId(), e);
            throw e;
        }
    }
    
    /**
     * 重置所有用户的今日字段
     * 使用四个独立的SQL语句，提高性能和可维护性
     * 
     * @return 总影响行数
     */
    private int resetTodayFieldsByMybatisPlus() {
        log.debug("开始重置所有用户的今日字段");
        
        int totalResult = 0;
        
        try {
            // 1. 重置今日收益
            int profitResult = resetTodayProfit();
            totalResult += profitResult;
            
            // 2. 重置今日提现
            int withdrawResult = resetTodayWithdraw();
            totalResult += withdrawResult;
            
            // 3. 重置今日投资
            int investmentResult = resetTodayInvestment();
            totalResult += investmentResult;
            
            // 4. 重置今日充值
            int rechargeResult = resetTodayRecharge();
            totalResult += rechargeResult;

            //重置今日佣金收入
            int todayCommissionResult = resetTodayCommission();
            totalResult += todayCommissionResult;

            
            log.info("所有用户的今日字段重置完成，总影响行数: {}", totalResult);
            return totalResult;
            
        } catch (Exception e) {
            log.error("重置所有用户的今日字段失败", e);
            throw e;
        }
    }

    private int resetTodayCommission() {
        try {
            LambdaUpdateWrapper<UserEntity> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(UserEntity::getTodayCommission, 0)
                    .gt(UserEntity::getTodayCommission, 0); // 只更新有收益的用户

            int result = userDao.update(null, updateWrapper);
            log.debug("今日佣金字段重置完成，影响行数: {}", result);
            return result;
        } catch (Exception e) {
            log.error("重置今日收益字段失败", e);
            throw e;
        }
    }

    /**
     * 重置今日收益字段
     * 只更新有收益记录的用户，提高性能
     */
    private int resetTodayProfit() {
        try {
            LambdaUpdateWrapper<UserEntity> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(UserEntity::getTodayProfit, 0)
                        .gt(UserEntity::getTodayProfit, 0); // 只更新有收益的用户
            
            int result = userDao.update(null, updateWrapper);
            log.debug("今日收益字段重置完成，影响行数: {}", result);
            return result;
        } catch (Exception e) {
            log.error("重置今日收益字段失败", e);
            throw e;
        }
    }
    
    /**
     * 重置今日提现字段
     * 只更新有提现记录的用户，提高性能
     */
    private int resetTodayWithdraw() {
        try {
            LambdaUpdateWrapper<UserEntity> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(UserEntity::getTodayWithdraw, 0)
                        .gt(UserEntity::getTodayWithdraw, 0); // 只更新有提现的用户
            
            int result = userDao.update(null, updateWrapper);
            log.debug("今日提现字段重置完成，影响行数: {}", result);
            return result;
        } catch (Exception e) {
            log.error("重置今日提现字段失败", e);
            throw e;
        }
    }
    
    /**
     * 重置今日投资字段
     * 只更新有投资记录的用户，提高性能
     */
    private int resetTodayInvestment() {
        try {
            LambdaUpdateWrapper<UserEntity> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(UserEntity::getTodayInvestment, 0)
                        .gt(UserEntity::getTodayInvestment, 0); // 只更新有投资的用户
            
            int result = userDao.update(null, updateWrapper);
            log.debug("今日投资字段重置完成，影响行数: {}", result);
            return result;
        } catch (Exception e) {
            log.error("重置今日投资字段失败", e);
            throw e;
        }
    }
    
    /**
     * 重置今日充值字段
     * 只更新有充值记录的用户，提高性能
     */
    private int resetTodayRecharge() {
        try {
            LambdaUpdateWrapper<UserEntity> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(UserEntity::getTodayRecharge, 0)
                        .set(UserEntity::getTodayRechargeCnt, 0)
                        .and(wrapper -> wrapper.gt(UserEntity::getTodayRecharge, 0)
                                           .or()
                                           .gt(UserEntity::getTodayRechargeCnt, 0)); // 只更新有充值的用户
            
            int result = userDao.update(null, updateWrapper);
            log.debug("今日充值字段重置完成，影响行数: {}", result);
            return result;
        } catch (Exception e) {
            log.error("重置今日充值字段失败", e);
            throw e;
        }
    }
    
//    /**
//     * 每天0点重置用户今日收益、投资和充值字段
//     */
//    @Scheduled(cron = "0 0 0 * * ?")
//    public void resetTodayFields() {
//        log.info("开始重置用户今日收益、投资和充值字段...");
//
//        try {
//            int result = userDao.resetTodayInvestmentAndProfit();
//            log.info("用户今日字段重置完成，影响用户数: {}", result);
//        } catch (Exception e) {
//            log.error("重置用户今日字段失败", e);
//        }
//    }
    
}
