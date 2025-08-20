package io.renren.modules.home.service;

import io.renren.modules.home.dto.DailyReportDTO;
import io.renren.modules.home.service.impl.HomeStatsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 日报表服务测试类
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@SpringBootTest
@ActiveProfiles("test")
public class DailyReportServiceTest {

    @Resource
    private HomeStatsService homeStatsService;

    @Test
    public void testGetDailyReportStats() {
        try {
            // 测试获取日报表统计数据
            Long startTime = 1755619200000L; // 2025-05-29 00:00:00
            Long endTime = 1755705599000L;   // 2025-05-29 23:59:59
            String type = "d"; // 日报
            
            List<DailyReportDTO> reportList = homeStatsService.getDailyReportStats(startTime, endTime, type);
            
            // 验证结果不为空
            assertNotNull(reportList, "日报表数据列表不能为空");
            
            System.out.println("日报表统计数据获取成功！");
            System.out.println("时间范围: " + startTime + " - " + endTime);
            System.out.println("统计类型: " + type);
            System.out.println("数据条数: " + reportList.size());
            
            // 打印每条记录
            for (DailyReportDTO report : reportList) {
                System.out.println("日期: " + report.getDate() + 
                                 ", 充值: " + report.getTotalCharges() + 
                                 ", 提现: " + report.getTotalWithdraws() + 
                                 ", 订单: " + report.getTotalOrders());
            }
            
        } catch (Exception e) {
            System.err.println("测试失败: " + e.getMessage());
            e.printStackTrace();
            fail("测试应该成功");
        }
    }

    @Test
    public void testGetDailyReportStatsWithNullValues() {
        try {
            // 测试空值处理
            List<DailyReportDTO> reportList = homeStatsService.getDailyReportStats(null, null, "d");
            
            // 验证结果不为空（可能是空列表）
            assertNotNull(reportList, "日报表数据列表不能为null");
            
            System.out.println("空值测试通过，返回数据条数: " + reportList.size());
            
        } catch (Exception e) {
            System.err.println("空值测试失败: " + e.getMessage());
            e.printStackTrace();
            fail("空值测试应该成功");
        }
    }
}
