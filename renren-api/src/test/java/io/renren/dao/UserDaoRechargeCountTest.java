package io.renren.dao;

import io.renren.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UserDao充值次数更新方法测试类
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserDaoRechargeCountTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void testUpdateAllRechargeCountFields() {
        // 准备测试数据：创建一个测试用户
        UserEntity testUser = createTestUser();
        Long userId = testUser.getId();
        
        // 记录更新前的充值次数
        Long beforeTodayCount = testUser.getTodayRechargeCnt();
        Long beforeHistoryCount = testUser.getHistorychargecnt();
        
        // 执行综合充值次数更新
        int result = userDao.updateAllRechargeCountFields(userId);
        
        // 验证更新结果
        assertEquals(1, result, "综合充值次数更新应该影响1行");
        
        // 重新查询用户信息，验证充值次数是否都增加
        UserEntity updatedUser = userDao.selectById(userId);
        assertNotNull(updatedUser, "更新后的用户信息不应为空");
        assertEquals(beforeTodayCount + 1, updatedUser.getTodayRechargeCnt(), "今日充值次数应该增加1");
        assertEquals(beforeHistoryCount + 1, updatedUser.getHistorychargecnt(), "历史充值次数应该增加1");
    }

    /**
     * 创建测试用户
     */
    private UserEntity createTestUser() {
        UserEntity user = new UserEntity();
        user.setUsername("testUser" + System.currentTimeMillis());
        user.setMobile("13800138000");
        user.setPassword("testPassword");
        user.setInviteCode("TEST" + System.currentTimeMillis());
        user.setTodayRechargeCnt(5L);
        user.setHistorychargecnt(20L);
        user.setChargeSum(100000L); // 1000元
        user.setAssets(50000L); // 500元
        
        userDao.insert(user);
        return user;
    }
}
