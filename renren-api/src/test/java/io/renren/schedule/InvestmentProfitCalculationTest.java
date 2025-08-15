package io.renren.schedule;

import io.renren.entity.InvestmentRecordEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 投资收益计算逻辑测试类
 *
 * @author renren
 * @since 1.0.0
 */
@SpringBootTest
@ActiveProfiles("test")
public class InvestmentProfitCalculationTest {

    private UserInvestmentProfitSchedule schedule;
    private InvestmentRecordEntity testRecord;

    @BeforeEach
    public void setUp() {
        schedule = new UserInvestmentProfitSchedule();
        
        // 创建测试投资记录
        testRecord = new InvestmentRecordEntity();
        testRecord.setId(1L);
        testRecord.setUserId(1001L);
        testRecord.setProjectId(2001L);
        testRecord.setInvestmentAmount(100000L); // 1000元（分）
        testRecord.setOrderDate(new Date());
        testRecord.setStatus(0); // 未收益
        testRecord.setCycle(30); // 30天
        testRecord.setCycleType(1); // 到期收益含本金
    }

    @Test
    public void testCalculateProfitAmount() {
        // 测试基本收益计算
        BigDecimal profit = schedule.calculateProfitAmount(testRecord);
        
        assertNotNull(profit, "收益金额不应该为null");
        assertTrue(profit.compareTo(BigDecimal.ZERO) > 0, "收益金额应该大于0");
        
        System.out.println("投资金额: " + testRecord.getInvestmentAmount() + "分");
        System.out.println("收益金额: " + profit + "元");
    }

    @Test
    public void testCalculateProfitByCycleType() {
        // 测试不同周期类型的收益计算
        
        // 1. 到期收益含本金
        testRecord.setCycleType(1);
        BigDecimal profit1 = schedule.calculateProfitAmount(testRecord);
        assertTrue(profit1.compareTo(BigDecimal.ZERO) > 0);
        
        // 2. 每日返本金到期收益
        testRecord.setCycleType(2);
        BigDecimal profit2 = schedule.calculateProfitAmount(testRecord);
        assertTrue(profit2.compareTo(BigDecimal.ZERO) > 0);
        
        // 3. 不返本金
        testRecord.setCycleType(3);
        BigDecimal profit3 = schedule.calculateProfitAmount(testRecord);
        assertTrue(profit3.compareTo(BigDecimal.ZERO) > 0);
        
        // 4. 复利产品
        testRecord.setCycleType(4);
        BigDecimal profit4 = schedule.calculateProfitAmount(testRecord);
        assertTrue(profit4.compareTo(BigDecimal.ZERO) > 0);
        
        // 5. 阶梯日益
        testRecord.setCycleType(5);
        BigDecimal profit5 = schedule.calculateProfitAmount(testRecord);
        assertTrue(profit5.compareTo(BigDecimal.ZERO) > 0);
        
        // 6. 拼团
        testRecord.setCycleType(6);
        BigDecimal profit6 = schedule.calculateProfitAmount(testRecord);
        assertTrue(profit6.compareTo(BigDecimal.ZERO) > 0);
        
        System.out.println("不同周期类型的收益对比:");
        System.out.println("到期收益含本金: " + profit1 + "元");
        System.out.println("每日返本金到期收益: " + profit2 + "元");
        System.out.println("不返本金: " + profit3 + "元");
        System.out.println("复利产品: " + profit4 + "元");
        System.out.println("阶梯日益: " + profit5 + "元");
        System.out.println("拼团: " + profit6 + "元");
    }

    @Test
    public void testCalculateInvestmentDays() {
        // 测试投资天数计算
        Date pastDate = new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000L); // 7天前
        testRecord.setOrderDate(pastDate);
        
        int days = schedule.calculateInvestmentDays(pastDate);
        assertEquals(7, days, "投资天数应该为7天");
        
        System.out.println("投资天数: " + days + "天");
    }

    @Test
    public void testInvalidInvestmentRecord() {
        // 测试无效投资记录
        
        // 投资金额为0
        testRecord.setInvestmentAmount(0L);
        BigDecimal profit1 = schedule.calculateProfitAmount(testRecord);
        assertEquals(BigDecimal.ZERO, profit1, "投资金额为0时收益应该为0");
        
        // 投资金额为null
        testRecord.setInvestmentAmount(null);
        BigDecimal profit2 = schedule.calculateProfitAmount(testRecord);
        assertEquals(BigDecimal.ZERO, profit2, "投资金额为null时收益应该为0");
        
        // 投资日期为null
        testRecord.setInvestmentAmount(100000L);
        testRecord.setOrderDate(null);
        BigDecimal profit3 = schedule.calculateProfitAmount(testRecord);
        assertEquals(BigDecimal.ZERO, profit3, "投资日期为null时收益应该为0");
    }

    @Test
    public void testDifferentInvestmentAmounts() {
        // 测试不同投资金额的收益计算
        testRecord.setCycleType(1); // 到期收益含本金
        
        long[] amounts = {10000L, 50000L, 100000L, 500000L, 1000000L}; // 100元到10000元
        
        for (long amount : amounts) {
            testRecord.setInvestmentAmount(amount);
            BigDecimal profit = schedule.calculateProfitAmount(testRecord);
            
            assertNotNull(profit, "收益金额不应该为null");
            assertTrue(profit.compareTo(BigDecimal.ZERO) > 0, "收益金额应该大于0");
            
            System.out.println("投资金额: " + amount + "分, 收益: " + profit + "元");
        }
    }

    @Test
    public void testDifferentCycles() {
        // 测试不同投资周期的收益计算
        testRecord.setCycleType(1); // 到期收益含本金
        testRecord.setInvestmentAmount(100000L); // 1000元
        
        int[] cycles = {7, 15, 30, 60, 90, 180, 365}; // 不同周期
        
        for (int cycle : cycles) {
            testRecord.setCycle(cycle);
            BigDecimal profit = schedule.calculateProfitAmount(testRecord);
            
            assertNotNull(profit, "收益金额不应该为null");
            assertTrue(profit.compareTo(BigDecimal.ZERO) > 0, "收益金额应该大于0");
            
            System.out.println("投资周期: " + cycle + "天, 收益: " + profit + "元");
        }
    }
}
