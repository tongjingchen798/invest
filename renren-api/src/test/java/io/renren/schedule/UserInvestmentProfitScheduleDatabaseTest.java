package io.renren.schedule;

import io.renren.dao.InvestmentRecordDao;
import io.renren.entity.InvestmentRecordEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户投资收益计算定时任务数据库查询测试类
 *
 * @author renren
 * @since 1.0.0
 */
@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class UserInvestmentProfitScheduleDatabaseTest {

    @Autowired
    private InvestmentRecordDao investmentRecordDao;

    @Test
    public void testSelectDistinctUserIdsWithInvestment() {
        // 测试查询有投资记录的用户ID列表
        List<Long> userIds = investmentRecordDao.selectDistinctUserIdsWithInvestment();
        
        assertNotNull(userIds, "用户ID列表不应该为null");
        
        if (!userIds.isEmpty()) {
            log.info("查询到 {} 个有投资的用户", userIds.size());
            
            // 验证用户ID的有效性
            for (Long userId : userIds) {
                assertNotNull(userId, "用户ID不应该为null");
                assertTrue(userId > 0, "用户ID应该大于0");
            }
            
            // 验证去重功能
            long distinctCount = userIds.stream().distinct().count();
            assertEquals(userIds.size(), distinctCount, "用户ID应该去重");
            
        } else {
            log.info("数据库中没有找到有投资的用户");
        }
    }

    @Test
    public void testSelectByUserId() {
        // 测试根据用户ID查询投资记录
        List<Long> userIds = investmentRecordDao.selectDistinctUserIdsWithInvestment();
        
        if (!userIds.isEmpty()) {
            Long testUserId = userIds.get(0);
            List<InvestmentRecordEntity> records = investmentRecordDao.selectByUserId(testUserId);
            
            assertNotNull(records, "投资记录列表不应该为null");
            
            if (!records.isEmpty()) {
                log.info("用户 {} 有 {} 条投资记录", testUserId, records.size());
                
                // 验证投资记录的有效性
                for (InvestmentRecordEntity record : records) {
                    assertNotNull(record.getId(), "投资记录ID不应该为null");
                    assertEquals(testUserId, record.getUserId(), "用户ID应该匹配");
                    assertNotNull(record.getInvestmentAmount(), "投资金额不应该为null");
                    assertTrue(record.getInvestmentAmount() > 0, "投资金额应该大于0");
                }
                
            } else {
                log.info("用户 {} 没有投资记录", testUserId);
            }
        }
    }

    @Test
    public void testSelectTotalInvestmentByUserId() {
        // 测试查询用户总投资金额
        List<Long> userIds = investmentRecordDao.selectDistinctUserIdsWithInvestment();
        
        if (!userIds.isEmpty()) {
            Long testUserId = userIds.get(0);
            Long totalInvestment = investmentRecordDao.selectTotalInvestmentByUserId(testUserId);
            
            assertNotNull(totalInvestment, "总投资金额不应该为null");
            assertTrue(totalInvestment >= 0, "总投资金额应该大于等于0");
            
            log.info("用户 {} 总投资金额: {}", testUserId, totalInvestment);
        }
    }

    @Test
    public void testSelectPendingInterestByUserId() {
        // 测试查询用户待收利息金额
        List<Long> userIds = investmentRecordDao.selectDistinctUserIdsWithInvestment();
        
        if (!userIds.isEmpty()) {
            Long testUserId = userIds.get(0);
            Long pendingInterest = investmentRecordDao.selectPendingInterestByUserId(testUserId);
            
            assertNotNull(pendingInterest, "待收利息金额不应该为null");
            assertTrue(pendingInterest >= 0, "待收利息金额应该大于等于0");
            
            log.info("用户 {} 待收利息金额: {}", testUserId, pendingInterest);
        }
    }
}
