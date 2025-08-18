//package io.renren.schedule;
//
//import io.renren.config.CommissionConfig;
//import io.renren.dao.InvestmentRecordDao;
//import io.renren.dao.UserBalanceDetailDao;
//import io.renren.dao.UserDao;
//import io.renren.entity.InvestmentRecordEntity;
//import io.renren.entity.UserBalanceDetailEntity;
//import io.renren.entity.UserEntity;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 每日佣金统计定时任务
// *
// * @author renren
// * @since 1.0.0
// */
//@Slf4j
//@Component
//public class DailyCommissionSchedule {
//
//    @Autowired
//    private InvestmentRecordDao investmentRecordDao;
//
//    @Autowired
//    private UserDao userDao;
//
//    @Autowired
//    private CommissionConfig commissionConfig;
//
//    @Autowired
//    private UserBalanceDetailDao userBalanceDetailDao;
//
//    /**
//     * 每日佣金统计任务
//     * 每天凌晨2点执行
//     */
//    @Scheduled(cron = "${commission.cron-expression:0 0 2 * * ?}")
//    @Transactional(rollbackFor = Exception.class)
//    public void calculateDailyCommission() {
//        log.info("开始执行每日佣金统计任务");
//
//        // 检查是否启用佣金计算
//        if (!commissionConfig.isEnabled()) {
//            log.info("佣金计算功能已禁用，跳过执行");
//            return;
//        }
//
//        try {
//            // 获取昨日日期范围
//            Date yesterdayStart = getYesterdayStart();
//            Date yesterdayEnd = getYesterdayEnd();
//
//            log.info("统计日期范围: {} 到 {}", yesterdayStart, yesterdayEnd);
//
//            // 查询昨日所有投资记录
//            List<InvestmentRecordEntity> investmentRecords = investmentRecordDao.selectByDateRange(yesterdayStart, yesterdayEnd);
//
//            if (investmentRecords.isEmpty()) {
//                log.info("昨日没有投资记录，跳过佣金计算");
//                return;
//            }
//
//            log.info("昨日投资记录数量: {}", investmentRecords.size());
//
//            // 按用户分组统计投资金额
//            Map<Long, Long> userInvestmentMap = new HashMap<>();
//            for (InvestmentRecordEntity record : investmentRecords) {
//                if (record.getUserId() != null && record.getInvestmentAmount() != null) {
//                    Long userId = record.getUserId();
//                    Long currentAmount = userInvestmentMap.getOrDefault(userId, 0L);
//                    userInvestmentMap.put(userId, currentAmount + record.getInvestmentAmount());
//                }
//            }
//
//            log.info("需要计算佣金的用户数量: {}", userInvestmentMap.size());
//
//            // 计算每个投资用户的推荐人佣金
//            for (Map.Entry<Long, Long> entry : userInvestmentMap.entrySet()) {
//                Long investorUserId = entry.getKey();
//                Long totalInvestmentAmount = entry.getValue();
//
//                calculateCommissionForInvestor(investorUserId, totalInvestmentAmount);
//            }
//
//            log.info("每日佣金统计任务执行完成");
//
//        } catch (Exception e) {
//            log.error("每日佣金统计任务执行失败", e);
//            throw new RuntimeException("每日佣金统计任务执行失败: " + e.getMessage());
//        }
//    }
//
//    /**
//     * 为投资用户计算推荐人佣金
//     *
//     * @param investorUserId 投资用户ID
//     * @param investmentAmount 投资金额
//     */
//    private void calculateCommissionForInvestor(Long investorUserId, Long investmentAmount) {
//        try {
//            // 查询投资用户信息
//            UserEntity investor = userDao.selectById(investorUserId);
//            if (investor == null) {
//                log.warn("投资用户不存在，用户ID: {}", investorUserId);
//                return;
//            }
//
//            // 获取1级推荐人
//            String firstLevelInviteCode = investor.getUpinviteCode();
//            if (firstLevelInviteCode == null || firstLevelInviteCode.trim().isEmpty()) {
//                log.debug("投资用户没有1级推荐人，用户ID: {}", investorUserId);
//                return;
//            }
//
//            // 查询1级推荐人
//            UserEntity firstLevelReferrer = userDao.selectByInviteCode(firstLevelInviteCode);
//            if (firstLevelReferrer == null) {
//                log.warn("1级推荐人不存在，邀请码: {}", firstLevelInviteCode);
//                return;
//            }
//
//            // 计算1级佣金
//            BigDecimal firstLevelCommission = calculateCommission(investmentAmount, commissionConfig.getFirstLevelRateDecimal());
//            log.debug("用户ID: {}, 投资金额: {}, 1级推荐人ID: {}, 1级佣金: {}",
//                    investorUserId, investmentAmount, firstLevelReferrer.getId(), firstLevelCommission);
//
//            // 更新1级推荐人佣金
//            updateUserCommission(firstLevelReferrer.getId(), firstLevelCommission);
//
//            // 获取2级推荐人（1级推荐人的上级）
//            String secondLevelInviteCode = firstLevelReferrer.getUpinviteCode();
//            if (secondLevelInviteCode == null || secondLevelInviteCode.trim().isEmpty()) {
//                log.debug("1级推荐人没有上级，跳过2级佣金计算");
//                return;
//            }
//
//            // 查询2级推荐人
//            UserEntity secondLevelReferrer = userDao.selectByInviteCode(secondLevelInviteCode);
//            if (secondLevelReferrer == null) {
//                log.warn("2级推荐人不存在，邀请码: {}", secondLevelInviteCode);
//                return;
//            }
//
//            // 计算2级佣金
//            BigDecimal secondLevelCommission = calculateCommission(investmentAmount, commissionConfig.getSecondLevelRateDecimal());
//            log.debug("用户ID: {}, 投资金额: {}, 2级推荐人ID: {}, 2级佣金: {}",
//                    investorUserId, investmentAmount, secondLevelReferrer.getId(), secondLevelCommission);
//
//            // 更新2级推荐人佣金
//            updateUserCommission(secondLevelReferrer.getId(), secondLevelCommission);
//
//        } catch (Exception e) {
//            log.error("计算用户佣金失败，投资用户ID: {}, 投资金额: {}", investorUserId, investmentAmount, e);
//        }
//    }
//
//    /**
//     * 计算佣金金额
//     *
//     * @param investmentAmount 投资金额（分）
//     * @param rate 佣金比例
//     * @return 佣金金额（分）
//     */
//    private BigDecimal calculateCommission(Long investmentAmount, BigDecimal rate) {
//        // 将分转换为卢比进行计算
//        BigDecimal amountInRupee = new BigDecimal(investmentAmount).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
//
//        // 计算佣金
//        BigDecimal commission = amountInRupee.multiply(rate).setScale(2, RoundingMode.HALF_UP);
//
//        // 将卢比转换回分
//        return commission.multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_UP);
//    }
//
//    /**
//     * 更新用户佣金字段
//     *
//     * @param userId 用户ID
//     * @param commissionAmount 佣金金额（分）
//     */
//    private void updateUserCommission(Long userId, BigDecimal commissionAmount) {
//        try {
//            // 转换为long类型
//            long commissionInCents = commissionAmount.longValue();
//            // 更新用户佣金相关字段
//            userDao.updateCommissionFields(userId, commissionInCents);
//
//            log.debug("用户佣金更新成功，用户ID: {}, 佣金金额: {}", userId, commissionAmount);
//
//        } catch (Exception e) {
//            log.error("更新用户佣金失败，用户ID: {}, 佣金金额: {}", userId, commissionAmount, e);
//        }
//    }
//
//    /**
//     * 获取昨日开始时间
//     */
//    private Date getYesterdayStart() {
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_MONTH, -1);
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//        return calendar.getTime();
//    }
//
//    /**
//     * 获取昨日结束时间
//     */
//    private Date getYesterdayEnd() {
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_MONTH, -1);
//        calendar.set(Calendar.HOUR_OF_DAY, 23);
//        calendar.set(Calendar.MINUTE, 59);
//        calendar.set(Calendar.SECOND, 59);
//        calendar.set(Calendar.MILLISECOND, 999);
//        return calendar.getTime();
//    }
//}
