package io.renren.schedule;

import io.renren.entity.ProjectEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 项目收益率配置测试类
 *
 * @author renren
 * @since 1.0.0
 */
@SpringBootTest
@ActiveProfiles("test")
public class ProjectProfitRateTest {

    private UserInvestmentProfitSchedule schedule;

    @BeforeEach
    public void setUp() {
        schedule = new UserInvestmentProfitSchedule();
    }

    @Test
    public void testParseProjectRate() throws Exception {
        // 测试百分比格式
        BigDecimal rate1 = schedule.parseProjectRate("12%");
        assertEquals(new BigDecimal("0.12"), rate1);
        
        // 测试小数格式
        BigDecimal rate2 = schedule.parseProjectRate("0.15");
        assertEquals(new BigDecimal("0.15"), rate2);
        
        // 测试带空格的百分比
        BigDecimal rate3 = schedule.parseProjectRate(" 8.5% ");
        assertEquals(new BigDecimal("0.085"), rate3);
        
        // 测试空值
        BigDecimal rate4 = schedule.parseProjectRate(null);
        assertNotNull(rate4); // 应该返回默认值
        
        // 测试空字符串
        BigDecimal rate5 = schedule.parseProjectRate("");
        assertNotNull(rate5); // 应该返回默认值
    }

    @Test
    public void testParseProjectDailyRate() throws Exception {
        // 测试年化收益率转换为日收益率
        BigDecimal dailyRate1 = schedule.parseProjectDailyRate("12%年化");
        BigDecimal expected1 = new BigDecimal("12").divide(new BigDecimal("100"), 6, BigDecimal.ROUND_HALF_UP)
                                                 .divide(new BigDecimal("365"), 6, BigDecimal.ROUND_HALF_UP);
        assertEquals(expected1, dailyRate1);
        
        // 测试日收益率格式
        BigDecimal dailyRate2 = schedule.parseProjectDailyRate("0.1%日");
        assertEquals(new BigDecimal("0.001"), dailyRate2);
        
        // 测试纯数字日收益率
        BigDecimal dailyRate3 = schedule.parseProjectDailyRate("0.0003");
        assertEquals(new BigDecimal("0.0003"), dailyRate3);
    }

    @Test
    public void testGetProjectAnnualRate() throws Exception {
        // 创建测试项目
        ProjectEntity project = new ProjectEntity();
        project.setId(1L);
        project.setConversion("15%");
        
        // 测试获取年化收益率
        BigDecimal rate = schedule.getProjectAnnualRate(project, "maturity");
        assertEquals(new BigDecimal("0.15"), rate);
        
        // 测试项目没有配置收益率时使用默认配置
        ProjectEntity project2 = new ProjectEntity();
        project2.setId(2L);
        project2.setConversion(null);
        
        BigDecimal defaultRate = schedule.getProjectAnnualRate(project2, "maturity");
        assertNotNull(defaultRate);
        assertTrue(defaultRate.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    public void testGetProjectDailyRate() throws Exception {
        // 创建测试项目
        ProjectEntity project = new ProjectEntity();
        project.setId(1L);
        project.setConversion("0.1%日");
        
        // 测试获取日收益率
        BigDecimal rate = schedule.getProjectDailyRate(project, "compound");
        assertEquals(new BigDecimal("0.001"), rate);
        
        // 测试项目没有配置收益率时使用默认配置
        ProjectEntity project2 = new ProjectEntity();
        project2.setId(2L);
        project2.setConversion(null);
        
        BigDecimal defaultRate = schedule.getProjectDailyRate(project2, "compound");
        assertNotNull(defaultRate);
        assertTrue(defaultRate.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    public void testDifferentProjectRateFormats() throws Exception {
        // 测试各种收益率格式
        String[] rateFormats = {
            "12%", "0.12", " 8.5% ", "15.5%年化", "0.1%日", "0.0003"
        };
        
        for (String format : rateFormats) {
            try {
                BigDecimal rate = schedule.parseProjectRate(format);
                assertNotNull(rate, "解析收益率失败: " + format);
                assertTrue(rate.compareTo(BigDecimal.ZERO) >= 0, "收益率应该大于等于0: " + format);
                System.out.println("收益率格式: " + format + " -> " + rate);
            } catch (Exception e) {
                fail("解析收益率格式失败: " + format + ", 错误: " + e.getMessage());
            }
        }
    }

    @Test
    public void testProjectRatePriority() throws Exception {
        // 测试收益率优先级：项目配置 > 系统默认 > 硬编码默认
        
        // 1. 项目配置了收益率
        ProjectEntity project1 = new ProjectEntity();
        project1.setId(1L);
        project1.setConversion("18%");
        
        BigDecimal rate1 = schedule.getProjectAnnualRate(project1, "maturity");
        assertEquals(new BigDecimal("0.18"), rate1);
        
        // 2. 项目没有配置收益率
        ProjectEntity project2 = new ProjectEntity();
        project2.setId(2L);
        project2.setConversion(null);
        
        BigDecimal rate2 = schedule.getProjectAnnualRate(project2, "maturity");
        assertNotNull(rate2);
        // 应该使用系统默认配置，不是18%
        assertNotEquals(new BigDecimal("0.18"), rate2);
        
        System.out.println("项目配置收益率: " + rate1);
        System.out.println("系统默认收益率: " + rate2);
    }
}
