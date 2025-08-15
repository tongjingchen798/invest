package io.renren.schedule;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户投资收益计算定时任务测试类
 *
 * @author renren
 * @since 1.0.0
 */
@SpringBootTest
@ActiveProfiles("test")
public class UserInvestmentProfitScheduleTest {

    @Autowired
    private UserInvestmentProfitSchedule userInvestmentProfitSchedule;

    @Test
    public void testScheduleBeanInjection() {
        // 测试定时任务Bean是否被正确注入
        assertNotNull(userInvestmentProfitSchedule, "定时任务Bean应该被正确注入");
    }

    @Test
    public void testScheduleExecution() {
        // 测试定时任务执行（手动调用）
        assertDoesNotThrow(() -> {
            userInvestmentProfitSchedule.calculateUserInvestmentProfit();
        }, "定时任务执行不应该抛出异常");
    }

    @Test
    public void testTestSchedule() {
        // 测试测试用的定时任务
        assertDoesNotThrow(() -> {
            userInvestmentProfitSchedule.testSchedule();
        }, "测试定时任务执行不应该抛出异常");
    }
}
