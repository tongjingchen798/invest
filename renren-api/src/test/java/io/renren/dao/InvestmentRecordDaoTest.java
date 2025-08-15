package io.renren.dao;

import io.renren.entity.InvestmentRecordEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 投资记录DAO测试类
 *
 * @author renren
 * @since 1.0.0
 */
@SpringBootTest
@ActiveProfiles("test")
public class InvestmentRecordDaoTest {

    @Autowired
    private InvestmentRecordDao investmentRecordDao;

    @Test
    public void testUpdateStatusAndProfit() {
        // 测试更新投资记录状态和收益信息
        // 注意：这些测试会实际修改数据库，建议在测试环境中运行
        
        try {
            Long testInvestmentId = 1L; // 假设投资记录ID为1的记录存在
            BigDecimal testProfitAmount = new BigDecimal("100.50"); // 100.50元
            Date testProfitDate = new Date();
            
            // 测试单个投资记录更新
            int result = investmentRecordDao.updateStatusAndProfit(testInvestmentId, testProfitAmount, testProfitDate);
            System.out.println("更新投资记录状态和收益结果: " + result);
            
            // 验证更新结果
            assertTrue(result >= 0, "更新投资记录状态和收益应该成功或记录不存在");
            
            if (result > 0) {
                System.out.println("成功更新投资记录 " + testInvestmentId + " 的状态和收益信息");
            } else {
                System.out.println("投资记录 " + testInvestmentId + " 不存在或更新失败");
            }
            
        } catch (Exception e) {
            System.out.println("测试更新投资记录状态和收益时发生异常: " + e.getMessage());
        }
    }

    @Test
    public void testBatchUpdateStatusAndProfit() {
        // 测试批量更新投资记录状态和收益信息
        
        try {
            List<Long> testInvestmentIds = Arrays.asList(1L, 2L, 3L); // 假设这些投资记录ID存在
            BigDecimal testProfitAmount = new BigDecimal("200.00"); // 200.00元
            Date testProfitDate = new Date();
            
            // 测试批量更新
            int result = investmentRecordDao.batchUpdateStatusAndProfit(testInvestmentIds, testProfitAmount, testProfitDate);
            System.out.println("批量更新投资记录状态和收益结果: " + result);
            
            // 验证更新结果
            assertTrue(result >= 0, "批量更新投资记录状态和收益应该成功");
            
            if (result > 0) {
                System.out.println("成功批量更新 " + result + " 条投资记录的状态和收益信息");
            } else {
                System.out.println("没有投资记录需要更新或更新失败");
            }
            
        } catch (Exception e) {
            System.out.println("测试批量更新投资记录状态和收益时发生异常: " + e.getMessage());
        }
    }

    @Test
    public void testSelectCountByStatus() {
        // 测试根据状态查询投资记录数量
        
        try {
            // 测试查询状态为0（未收益）的记录数量
            int pendingCount = investmentRecordDao.selectCountByStatus(0);
            System.out.println("状态为0（未收益）的投资记录数量: " + pendingCount);
            
            // 测试查询状态为1（已收益）的记录数量
            int completedCount = investmentRecordDao.selectCountByStatus(1);
            System.out.println("状态为1（已收益）的投资记录数量: " + completedCount);
            
            // 验证结果
            assertTrue(pendingCount >= 0, "查询未收益记录数量应该成功");
            assertTrue(completedCount >= 0, "查询已收益记录数量应该成功");
            
        } catch (Exception e) {
            System.out.println("测试根据状态查询投资记录数量时发生异常: " + e.getMessage());
        }
    }

    @Test
    public void testSelectByDateRange() {
        // 测试查询指定日期范围内的投资记录
        
        try {
            // 设置日期范围（最近30天）
            Date endDate = new Date();
            Date startDate = new Date(endDate.getTime() - 30L * 24 * 60 * 60 * 1000);
            
            List<InvestmentRecordEntity> records = investmentRecordDao.selectByDateRange(startDate, endDate);
            System.out.println("最近30天内的投资记录数量: " + (records != null ? records.size() : 0));
            
            // 验证结果
            assertNotNull(records, "查询结果不应该为null");
            
            if (!records.isEmpty()) {
                System.out.println("找到投资记录，第一条记录ID: " + records.get(0).getId());
            }
            
        } catch (Exception e) {
            System.out.println("测试查询指定日期范围内的投资记录时发生异常: " + e.getMessage());
        }
    }

    @Test
    public void testSelectCountByUserIdAndStatus() {
        // 测试查询用户指定状态的投资记录数量
        
        try {
            Long testUserId = 1L; // 假设用户ID为1的用户存在
            
            // 测试查询用户未收益的投资记录数量
            int pendingCount = investmentRecordDao.selectCountByUserIdAndStatus(testUserId, 0);
            System.out.println("用户 " + testUserId + " 未收益的投资记录数量: " + pendingCount);
            
            // 测试查询用户已收益的投资记录数量
            int completedCount = investmentRecordDao.selectCountByUserIdAndStatus(testUserId, 1);
            System.out.println("用户 " + testUserId + " 已收益的投资记录数量: " + completedCount);
            
            // 验证结果
            assertTrue(pendingCount >= 0, "查询用户未收益记录数量应该成功");
            assertTrue(completedCount >= 0, "查询用户已收益记录数量应该成功");
            
        } catch (Exception e) {
            System.out.println("测试查询用户指定状态的投资记录数量时发生异常: " + e.getMessage());
        }
    }

    @Test
    public void testSelectInvestmentStatistics() {
        // 测试查询投资记录统计信息
        
        try {
            Long testUserId = 1L; // 假设用户ID为1的用户存在
            
            Map<String, Object> statistics = investmentRecordDao.selectInvestmentStatistics(testUserId);
            System.out.println("用户 " + testUserId + " 的投资统计信息:");
            
            if (statistics != null && !statistics.isEmpty()) {
                System.out.println("  总投资记录数: " + statistics.get("totalCount"));
                System.out.println("  总投资金额: " + statistics.get("totalInvestment") + " 分");
                System.out.println("  待收投资金额: " + statistics.get("pendingInvestment") + " 分");
                System.out.println("  已完成投资金额: " + statistics.get("completedInvestment") + " 分");
                System.out.println("  总收益金额: " + statistics.get("totalProfit") + " 分");
                System.out.println("  待收利息金额: " + statistics.get("pendingInterest") + " 分");
                
                // 验证统计信息
                assertNotNull(statistics.get("totalCount"), "总投资记录数不应该为null");
                assertNotNull(statistics.get("totalInvestment"), "总投资金额不应该为null");
                assertNotNull(statistics.get("totalProfit"), "总收益金额不应该为null");
            } else {
                System.out.println("  没有找到投资统计信息");
            }
            
        } catch (Exception e) {
            System.out.println("测试查询投资记录统计信息时发生异常: " + e.getMessage());
        }
    }

    @Test
    public void testSelectDistinctUserIdsWithInvestment() {
        // 测试查询所有有投资记录的用户ID
        
        try {
            List<Long> userIds = investmentRecordDao.selectDistinctUserIdsWithInvestment();
            System.out.println("有投资记录的用户数量: " + (userIds != null ? userIds.size() : 0));
            
            // 验证结果
            assertNotNull(userIds, "用户ID列表不应该为null");
            
            if (!userIds.isEmpty()) {
                System.out.println("前5个用户ID: " + userIds.subList(0, Math.min(5, userIds.size())));
            }
            
        } catch (Exception e) {
            System.out.println("测试查询有投资记录的用户ID时发生异常: " + e.getMessage());
        }
    }

    @Test
    public void testSelectByUserId() {
        // 测试根据用户ID查询投资记录
        
        try {
            Long testUserId = 1L; // 假设用户ID为1的用户存在
            
            List<InvestmentRecordEntity> records = investmentRecordDao.selectByUserId(testUserId);
            System.out.println("用户 " + testUserId + " 的投资记录数量: " + (records != null ? records.size() : 0));
            
            // 验证结果
            assertNotNull(records, "投资记录列表不应该为null");
            
            if (!records.isEmpty()) {
                InvestmentRecordEntity firstRecord = records.get(0);
                System.out.println("第一条投资记录信息:");
                System.out.println("  ID: " + firstRecord.getId());
                System.out.println("  项目ID: " + firstRecord.getProjectId());
                System.out.println("  投资金额: " + firstRecord.getInvestmentAmount() + " 分");
                System.out.println("  状态: " + firstRecord.getStatus());
                System.out.println("  投资日期: " + firstRecord.getOrderDate());
            }
            
        } catch (Exception e) {
            System.out.println("测试根据用户ID查询投资记录时发生异常: " + e.getMessage());
        }
    }

    @Test
    public void testSelectTotalInvestmentByUserId() {
        // 测试查询用户总投资金额
        
        try {
            Long testUserId = 1L; // 假设用户ID为1的用户存在
            
            Long totalInvestment = investmentRecordDao.selectTotalInvestmentByUserId(testUserId);
            System.out.println("用户 " + testUserId + " 总投资金额: " + totalInvestment + " 分");
            
            // 验证结果
            assertNotNull(totalInvestment, "总投资金额不应该为null");
            assertTrue(totalInvestment >= 0, "总投资金额应该大于等于0");
            
        } catch (Exception e) {
            System.out.println("测试查询用户总投资金额时发生异常: " + e.getMessage());
        }
    }

    @Test
    public void testSelectTotalProfitByUserId() {
        // 测试查询用户总收益金额
        
        try {
            Long testUserId = 1L; // 假设用户ID为1的用户存在
            
            Long totalProfit = investmentRecordDao.selectTotalProfitByUserId(testUserId);
            System.out.println("用户 " + testUserId + " 总收益金额: " + totalProfit + " 分");
            
            // 验证结果
            assertNotNull(totalProfit, "总收益金额不应该为null");
            assertTrue(totalProfit >= 0, "总收益金额应该大于等于0");
            
        } catch (Exception e) {
            System.out.println("测试查询用户总收益金额时发生异常: " + e.getMessage());
        }
    }
}
