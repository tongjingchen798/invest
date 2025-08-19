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

import io.renren.config.InvestmentProfitConfig;
import io.renren.dao.InvestmentRecordDao;
import io.renren.dao.ProjectDao;
import io.renren.dao.UserDao;
import io.renren.dao.InvestmentProfitDetailDao;
import io.renren.entity.InvestmentRecordEntity;
import io.renren.entity.ProjectEntity;
import io.renren.entity.InvestmentProfitDetailEntity;
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
    private InvestmentProfitConfig profitConfig;
    
    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private UserBalanceDetailDao userBalanceDetailDao;

    @Autowired
    private InvestmentProfitDetailDao investmentProfitDetailDao;


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

            // 4. 记录每日投资收益明细
            recordInvestmentProfitDetail(record, profitAmount);

            // 5. 更新用户余额
            updateUserBalance(record.getUserId(), profitAmount);

            // 6. 判断投资是否到期（是否是最后一期）
            boolean isMatured = InvestmentProfitCalculator.isInvestmentMatured(
                record.getOrderDate(), record.getCycle()
            );
            
            if (isMatured) {
                log.debug("投资项目 {} 已到期，更新状态为已收益", record.getOrderId());
                // 更新投资记录状态为已收益
                record.setStatus(1);
                investmentRecordDao.updateById(record);
            } else {
                log.debug("投资项目 {} 未到期，当前为第 {} 期，总周期 {} 天", 
                         record.getOrderId(),
                         InvestmentProfitCalculator.calculateInvestmentDays(record.getOrderDate()) + 1,
                         record.getCycle());
            }

            // 7. 记录账变
            recordProfitDetail(record, profitAmount);
            
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
            // 获取项目信息
            ProjectEntity project = null;
            if (record.getProjectId() != null) {
                project = projectDao.selectProjectById(record.getProjectId());
                if (project == null) {
                    log.warn("投资项目 {} 对应的项目 {} 不存在，使用默认配置", record.getOrderId(), record.getProjectId());
                }
            }
            
            // 使用项目信息或投资记录中的信息
            Integer cycleType = project != null ? project.getCycleType() : record.getCycleType();
            Integer cycle = project != null ? project.getCycle() : record.getCycle();
            
            if (cycleType == null) {
                cycleType = 1; // 默认到期收益含本金
            }
            
            if (cycle == null || cycle <= 0) {
                cycle = 1; // 默认1天
            }
            
            // 计算投资天数
            int investmentDays = calculateInvestmentDays(record.getOrderDate());
            
            log.debug("投资项目 {} 周期类型：{}, 周期：{}天, 投资天数：{}天", 
                     record.getOrderId(), cycleType, cycle, investmentDays);
            
            BigDecimal profitAmount = BigDecimal.ZERO;
            
            switch (cycleType) {
                case 1: // 到期收益含本金
                    profitAmount = calculateMaturityProfit(investmentAmount, cycle, investmentDays, project);
                    break;
                case 2: // 每日返本金到期收益
                    profitAmount = calculateDailyReturnProfit(investmentAmount, cycle, investmentDays, project);
                    break;
                case 3: // 不返本金
                    profitAmount = calculateNoPrincipalReturnProfit(investmentAmount, cycle, investmentDays, project);
                    break;
                case 4: // 复利产品
                    profitAmount = calculateCompoundInterestProfit(investmentAmount, cycle, investmentDays, project);
                    break;
                case 5: // 阶梯日益
                    profitAmount = calculateSteppedDailyProfit(investmentAmount, cycle, investmentDays, project);
                    break;
                case 6: // 拼团
                    profitAmount = calculateGroupBuyProfit(investmentAmount, cycle, investmentDays, project);
                    break;
                default:
                    // 默认使用到期收益含本金的计算方式
                    profitAmount = calculateMaturityProfit(investmentAmount, cycle, investmentDays, project);
                    break;
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
     * 计算到期收益含本金的收益
     * 
     * @param investmentAmount 投资金额
     * @param cycle 项目周期
     * @param investmentDays 投资天数
     * @param project 项目信息
     * @return 收益金额
     */
    private BigDecimal calculateMaturityProfit(BigDecimal investmentAmount, Integer cycle, int investmentDays, ProjectEntity project) {
        // 优先使用项目配置的收益率，如果没有则使用默认配置
        BigDecimal annualRate = getProjectAnnualRate(project, "maturity");
        
        // 计算实际收益率：年化收益率 * 投资天数 / 365
        BigDecimal actualRate = annualRate.multiply(new BigDecimal(investmentDays))
                                         .divide(new BigDecimal(365), 4, BigDecimal.ROUND_HALF_UP);
        
        return investmentAmount.multiply(actualRate);
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
        BigDecimal annualRate = getProjectAnnualRate(project, "dailyReturn");
        return investmentAmount.multiply(annualRate);
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
        // 优先使用项目配置的收益率，如果没有则使用默认配置
        BigDecimal annualRate = getProjectAnnualRate(project, "noPrincipal");
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
     * @param project 项目信息
     * @return 收益金额
     */
    private BigDecimal calculateCompoundInterestProfit(BigDecimal investmentAmount, Integer cycle, int investmentDays, ProjectEntity project) {
        BigDecimal annualRate = getProjectAnnualRate(project, "compound");
        return investmentAmount.multiply(annualRate);
    }
    
    /**
     * 计算阶梯日益的收益
     * 
     * @param investmentAmount 投资金额
     * @param cycle 项目周期
     * @param investmentDays 投资天数
     * @param project 项目信息
     * @return 收益金额
     */
    private BigDecimal calculateSteppedDailyProfit(BigDecimal investmentAmount, Integer cycle, int investmentDays, ProjectEntity project) {
        // 优先使用项目配置的阶梯收益率，如果没有则使用默认配置
        BigDecimal totalProfit = BigDecimal.ZERO;
        
        for (int day = 1; day <= investmentDays; day++) {
            BigDecimal dailyRate;
            if (day <= 7) {
                dailyRate = getProjectDailyRate(project, "steppedFirstWeek");
            } else if (day <= 15) {
                dailyRate = getProjectDailyRate(project, "steppedSecondWeek");
            } else {
                dailyRate = getProjectDailyRate(project, "steppedLater");
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
     * @param project 项目信息
     * @return 收益金额
     */
    private BigDecimal calculateGroupBuyProfit(BigDecimal investmentAmount, Integer cycle, int investmentDays, ProjectEntity project) {
        // 优先使用项目配置的拼团收益率，如果没有则使用默认配置
        // 拼团收益：基础收益 + 拼团奖励
        BigDecimal baseAnnualRate = getProjectAnnualRate(project, "groupBuyBase");
        BigDecimal baseProfit = baseAnnualRate.multiply(new BigDecimal(investmentDays))
                                             .divide(new BigDecimal(365), 4, BigDecimal.ROUND_HALF_UP);
        
        // 拼团奖励
        BigDecimal groupBonus = getProjectAnnualRate(project, "groupBuyBonus");
        
        return investmentAmount.multiply(baseProfit).add(investmentAmount.multiply(groupBonus));
    }
    
    /**
     * 从项目信息中获取年化收益率
     * 
     * @param project 项目信息
     * @param rateType 收益率类型
     * @return 年化收益率
     */
    private BigDecimal getProjectAnnualRate(ProjectEntity project, String rateType) {
        try {
            if (project != null && project.getConversion() != null) {
                // 项目配置了收益率，使用项目配置
                String conversion = project.getConversion();
                log.debug("项目 {} 配置收益率: {}", project.getInvestId(), conversion);
                // 这些类型都使用项目的基础收益率
                return parseProjectRate(conversion);
            }

            // 项目没有配置收益率，使用默认配置
            log.debug("使用默认收益率配置，类型: {}", rateType);
            switch (rateType) {
                case "maturity":
                    return profitConfig.getMaturityAnnualRate();
                case "dailyReturn":
                    return profitConfig.getDailyReturnAnnualRate();
                case "noPrincipal":
                    return profitConfig.getNoPrincipalAnnualRate();
                case "groupBuyBase":
                    return profitConfig.getGroupBuyRate().getBaseAnnualRate();
                case "groupBuyBonus":
                    return profitConfig.getGroupBuyRate().getGroupBonusRate();
                default:
                    return profitConfig.getMaturityAnnualRate();
            }
        } catch (Exception e) {
            log.warn("获取项目收益率失败，使用默认配置，类型: {}", rateType, e);
            return profitConfig.getMaturityAnnualRate();
        }
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
            if (project != null && project.getConversion() != null) {
                // 项目配置了收益率，使用项目配置
                String conversion = project.getConversion();
                log.debug("项目 {} 配置日收益率: {}%", project.getInvestId(), conversion);
                
                // 根据收益率类型和项目配置计算
                switch (rateType) {
                    case "compound":
                        // 复利产品使用项目的日收益率
                        return parseProjectDailyRate(conversion);
                    case "steppedFirstWeek":
                    case "steppedSecondWeek":
                    case "steppedLater":
                        // 阶梯日益使用项目的日收益率
                        return parseProjectDailyRate(conversion);
                    default:
                        break;
                }
            }
            
            // 项目没有配置收益率，使用默认配置
            log.debug("使用默认日收益率配置，类型: {}", rateType);
            switch (rateType) {
                case "compound":
                    return profitConfig.getCompoundDailyRate();
                case "steppedFirstWeek":
                    return profitConfig.getSteppedDailyRate().getFirstWeekRate();
                case "steppedSecondWeek":
                    return profitConfig.getSteppedDailyRate().getSecondWeekRate();
                case "steppedLater":
                    return profitConfig.getSteppedDailyRate().getLaterRate();
                default:
                    return profitConfig.getCompoundDailyRate();
            }
        } catch (Exception e) {
            log.warn("获取项目日收益率失败，使用默认配置，类型: {}", rateType, e);
            return profitConfig.getCompoundDailyRate();
        }
    }
    
    /**
     * 解析项目配置的年化收益率
     * 
     * @param conversion 项目收益率配置字符串
     * @return 年化收益率
     */
    private BigDecimal parseProjectRate(String conversion) {
        try {
            BigDecimal rate = new BigDecimal(conversion);
            rate = rate.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_DOWN);
            return rate;
        } catch (Exception e) {
            log.warn("解析项目收益率失败: {}, 使用默认配置", conversion, e);
            return profitConfig.getMaturityAnnualRate();
        }
    }
    
    /**
     * 解析项目配置的日收益率
     * 
     * @param conversion 项目收益率配置字符串
     * @return 日收益率
     */
    private BigDecimal parseProjectDailyRate(String conversion) {
        try {
            if (conversion == null || conversion.trim().isEmpty()) {
                return profitConfig.getCompoundDailyRate();
            }
            BigDecimal rate = new BigDecimal(conversion);
            rate = rate.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_DOWN);

            return rate;
        } catch (Exception e) {
            log.warn("解析项目日收益率失败: {}, 使用默认配置", conversion, e);
            return profitConfig.getCompoundDailyRate();
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
            String today = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new Date());
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
            // 更新用户可用余额
            int balanceResult = userDao.updateAllProfitFields(userId, profitAmountInCents);
            if (balanceResult > 0) {
                log.debug("用户 {} 可用余额更新成功，增加: {} 分", userId, profitAmountInCents);
            } else {
                log.warn("用户 {} 可用余额更新失败", userId);
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
     */
    private void recordProfitDetail(InvestmentRecordEntity record, BigDecimal profitAmount) {
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
            log.debug("投资项目 {} 投资收益账变记录完成，金额：{}", record.getOrderId(), profitAmount);
            
            // 更新用户余额和收益字段
            updateUserBalance(record.getUserId(), profitAmount);
            
        } catch (Exception e) {
            log.error("记录投资项目 {} 投资收益账变失败", record.getOrderId(), e);
            throw e;
        }
    }
    
    /**
     * 每天0点重置用户今日收益、投资和充值字段
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetTodayFields() {
        log.info("开始重置用户今日收益、投资和充值字段...");
        
        try {
            int result = userDao.resetTodayFields();
            log.info("用户今日字段重置完成，影响用户数: {}", result);
        } catch (Exception e) {
            log.error("重置用户今日字段失败", e);
        }
    }
    
    /**
     * 获取用户余额信息（用于调试和监控）
     * 
     * @param userId 用户ID
     * @return 用户余额信息
     */
    public UserEntity getUserBalanceInfo(Long userId) {
        try {
            return userDao.getUserBalanceInfo(userId);
        } catch (Exception e) {
            log.error("获取用户 {} 余额信息失败", userId, e);
            return null;
        }
    }
}
