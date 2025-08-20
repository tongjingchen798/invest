package io.renren.modules.home.service;

import io.renren.modules.home.dto.MainStatsDTO;
import io.renren.modules.home.service.impl.HomeStatsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 首页统计服务测试类
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@SpringBootTest
@ActiveProfiles("test")
public class HomeStatsServiceTest {

    @Resource
    private HomeStatsService homeStatsService;

    @Test
    public void testGetMainStats() {
        try {
            // 测试获取统计数据（无时间范围）
            MainStatsDTO stats = homeStatsService.getMainStats(null, null);
            
            // 验证结果不为空
            assertNotNull(stats, "统计数据不能为空");
            
            // 验证所有字段都有值（即使是0）
            assertNotNull(stats.getChargeOrderAmount(), "充值金额不能为null");
            assertNotNull(stats.getCharge_order_cnt(), "充值订单数不能为null");
            assertNotNull(stats.getOnline_privilege_cnt(), "在线项目数不能为null");
            assertNotNull(stats.getPrivilege_cnt(), "购买项目数不能为null");
            assertNotNull(stats.getWithdrawOrderAmount(), "提现金额不能为null");
            assertNotNull(stats.getWithdraw_order_cnt(), "提现订单数不能为null");
            assertNotNull(stats.getXs_amount(), "销售总额不能为null");
            assertNotNull(stats.getZc_cnt(), "注册会员数不能为null");
            
            System.out.println("统计数据获取成功！");
            System.out.println("充值金额: " + stats.getChargeOrderAmount());
            System.out.println("充值订单数: " + stats.getCharge_order_cnt());
            System.out.println("在线项目数: " + stats.getOnline_privilege_cnt());
            System.out.println("购买项目数: " + stats.getPrivilege_cnt());
            System.out.println("提现金额: " + stats.getWithdrawOrderAmount());
            System.out.println("提现订单数: " + stats.getWithdraw_order_cnt());
            System.out.println("销售总额: " + stats.getXs_amount());
            System.out.println("注册会员数: " + stats.getZc_cnt());
            
        } catch (Exception e) {
            fail("获取统计数据失败: " + e.getMessage());
        }
    }

    @Test
    public void testGetMainStatsWithTimeRange() {
        try {
            // 测试获取统计数据（有时间范围）
            Long startTime = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000L); // 7天前
            Long endTime = System.currentTimeMillis(); // 现在
            
            MainStatsDTO stats = homeStatsService.getMainStats(startTime, endTime);
            
            // 验证结果不为空
            assertNotNull(stats, "统计数据不能为空");
            
            System.out.println("带时间范围的统计数据获取成功！");
            System.out.println("时间范围: " + startTime + " - " + endTime);
            System.out.println("充值金额: " + stats.getChargeOrderAmount());
            System.out.println("充值订单数: " + stats.getCharge_order_cnt());
            System.out.println("购买项目总数: " + stats.getPrivilege_cnt());
            
        } catch (Exception e) {
            fail("获取统计数据失败: " + e.getMessage());
        }
    }

    @Test
    public void testTimeRangeFiltering() {
        try {
            // 测试时间范围过滤功能
            Long startTime = System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000L); // 30天前
            Long endTime = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000L);   // 7天前
            
            MainStatsDTO statsWithRange = homeStatsService.getMainStats(startTime, endTime);
            MainStatsDTO statsAllTime = homeStatsService.getMainStats(null, null);
            
            // 验证结果不为空
            assertNotNull(statsWithRange, "带时间范围的统计数据不能为空");
            assertNotNull(statsAllTime, "全时间统计数据不能为空");
            
            System.out.println("时间范围过滤测试:");
            System.out.println("30天前到7天前 - 购买项目数: " + statsWithRange.getPrivilege_cnt());
            System.out.println("全时间 - 购买项目数: " + statsAllTime.getPrivilege_cnt());
            
            // 验证在线项目数不受时间范围影响（应该相同）
            assertEquals(statsWithRange.getOnline_privilege_cnt(), 
                        statsAllTime.getOnline_privilege_cnt(), 
                        "在线项目数应该不受时间范围影响");
            
        } catch (Exception e) {
            fail("时间范围过滤测试失败: " + e.getMessage());
        }
    }
}
