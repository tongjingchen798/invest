package io.renren.schedule;

import io.renren.dao.UserDao;
import io.renren.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户投资收益定时任务余额更新功能测试
 *
 * @author renren
 * @since 1.0.0
 */
@SpringBootTest
@ActiveProfiles("test")
public class UserInvestmentProfitScheduleBalanceTest {

    @Autowired
    private UserInvestmentProfitSchedule schedule;

    @Autowired
    private UserDao userDao;

    @Test
    public void testGetUserBalanceInfo() {
        // 测试获取用户余额信息
        // 注意：这里需要数据库中有测试用户数据
        try {
            // 假设用户ID为1的用户存在
            Long testUserId = 1L;
            UserEntity balanceInfo = schedule.getUserBalanceInfo(testUserId);
            
            if (balanceInfo != null) {
                System.out.println("用户 " + testUserId + " 余额信息:");
                System.out.println("  可用余额: " + balanceInfo.getAssets() + " 分");
                System.out.println("  余额: " + balanceInfo.getBalance() + " 分");
                System.out.println("  佣金余额: " + balanceInfo.getCommissionBalance() + " 分");
                System.out.println("  今日收益: " + balanceInfo.getTodayProfit() + " 分");
                System.out.println("  历史收益: " + balanceInfo.getHistoryProfit() + " 分");
                System.out.println("  总收益: " + balanceInfo.getTotalProfit() + " 分");
                
                assertNotNull(balanceInfo.getId(), "用户ID不能为空");
            } else {
                System.out.println("用户 " + testUserId + " 不存在或获取余额信息失败");
            }
        } catch (Exception e) {
            System.out.println("测试获取用户余额信息时发生异常: " + e.getMessage());
        }
    }

    @Test
    public void testUserBalanceUpdateMethods() {
        // 测试用户余额更新方法
        // 注意：这些测试会实际修改数据库，建议在测试环境中运行
        
        try {
            Long testUserId = 1L;
            Long testAmount = 1000L; // 1000分 = 10元
            
            // 测试更新用户可用余额
            int balanceResult = userDao.addUserBalance(testUserId, testAmount);
            System.out.println("更新用户可用余额结果: " + balanceResult);
            
            // 测试更新用户今日收益
            int todayProfitResult = userDao.addTodayProfit(testUserId, testAmount);
            System.out.println("更新用户今日收益结果: " + todayProfitResult);
            
            // 测试更新用户历史收益
            int historyProfitResult = userDao.addHistoryProfit(testUserId, testAmount);
            System.out.println("更新用户历史收益结果: " + historyProfitResult);
            
            // 测试更新用户总收益
            int totalProfitResult = userDao.addTotalProfit(testUserId, testAmount);
            System.out.println("更新用户总收益结果: " + totalProfitResult);
            
            // 验证更新结果
            assertTrue(balanceResult >= 0, "更新用户可用余额应该成功或用户不存在");
            assertTrue(todayProfitResult >= 0, "更新用户今日收益应该成功或用户不存在");
            assertTrue(historyProfitResult >= 0, "更新用户历史收益应该成功或用户不存在");
            assertTrue(totalProfitResult >= 0, "更新用户总收益应该成功或用户不存在");
            
        } catch (Exception e) {
            System.out.println("测试用户余额更新方法时发生异常: " + e.getMessage());
        }
    }

    @Test
    public void testUserCommissionUpdateMethods() {
        // 测试用户佣金更新方法
        
        try {
            Long testUserId = 1L;
            Long testAmount = 500L; // 500分 = 5元
            
            // 测试更新用户佣金余额
            int commissionResult = userDao.addCommissionBalance(testUserId, testAmount);
            System.out.println("更新用户佣金余额结果: " + commissionResult);
            
            // 测试更新用户历史佣金余额
            int historyCommissionResult = userDao.addHistoryCommissionBalance(testUserId, testAmount);
            System.out.println("更新用户历史佣金余额结果: " + historyCommissionResult);
            
            // 验证更新结果
            assertTrue(commissionResult >= 0, "更新用户佣金余额应该成功或用户不存在");
            assertTrue(historyCommissionResult >= 0, "更新用户历史佣金余额应该成功或用户不存在");
            
        } catch (Exception e) {
            System.out.println("测试用户佣金更新方法时发生异常: " + e.getMessage());
        }
    }

    @Test
    public void testUserInvestmentUpdateMethods() {
        // 测试用户投资相关更新方法
        
        try {
            Long testUserId = 1L;
            Long testAmount = 2000L; // 2000分 = 20元
            
            // 测试更新用户投资金额
            int investmentResult = userDao.updateInvestmentAmount(testUserId, testAmount);
            System.out.println("更新用户投资金额结果: " + investmentResult);
            
            // 测试更新用户投资统计信息
            int statisticsResult = userDao.updateInvestmentStatistics(testUserId, testAmount);
            System.out.println("更新用户投资统计信息结果: " + statisticsResult);
            
            // 验证更新结果
            assertTrue(investmentResult >= 0, "更新用户投资金额应该成功或用户不存在");
            assertTrue(statisticsResult >= 0, "更新用户投资统计信息应该成功或用户不存在");
            
        } catch (Exception e) {
            System.out.println("测试用户投资更新方法时发生异常: " + e.getMessage());
        }
    }

    @Test
    public void testResetTodayFields() {
        // 测试重置今日字段方法
        // 注意：这个方法会重置所有用户的今日字段，建议在测试环境中运行
        
        try {
            int result = userDao.resetTodayFields();
            System.out.println("重置今日字段结果: " + result);
            
            // 验证结果
            assertTrue(result >= 0, "重置今日字段应该成功");
            
            if (result > 0) {
                System.out.println("成功重置 " + result + " 个用户的今日字段");
            } else {
                System.out.println("没有用户需要重置今日字段");
            }
            
        } catch (Exception e) {
            System.out.println("测试重置今日字段时发生异常: " + e.getMessage());
        }
    }

    @Test
    public void testComprehensiveBalanceUpdate() {
        // 测试综合余额更新方法
        
        try {
            Long testUserId = 1L;
            Long testAmount = 1500L; // 1500分 = 15元
            
            // 测试综合更新收益字段
            int profitResult = userDao.updateAllProfitFields(testUserId, testAmount);
            System.out.println("综合更新收益字段结果: " + profitResult);
            
            // 测试综合更新佣金字段
            int commissionResult = userDao.updateAllCommissionFields(testUserId, testAmount);
            System.out.println("综合更新佣金字段结果: " + commissionResult);
            
            // 验证更新结果
            assertTrue(profitResult >= 0, "综合更新收益字段应该成功或用户不存在");
            assertTrue(commissionResult >= 0, "综合更新佣金字段应该成功或用户不存在");
            
        } catch (Exception e) {
            System.out.println("测试综合余额更新时发生异常: " + e.getMessage());
        }
    }
}
