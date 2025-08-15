package io.renren.dao;

import io.renren.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UserDao充值金额更新方法测试类
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserDaoRechargeAmountTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void testUpdateAllRechargeFields() {
        // 准备测试数据：创建一个测试用户
        UserEntity testUser = createTestUser();
        Long userId = testUser.getId();
        Long rechargeAmount = 10000L; // 100元
        
        // 记录更新前的充值信息
        Long beforeTodayCount = testUser.getTodayRechargeCnt();
        Long beforeHistoryCount = testUser.getHistorychargecnt();
        Long beforeTodayAmount = testUser.getTodayRecharge();
        Long beforeChargeSum = testUser.getChargeSum();
        
        // 执行综合充值更新（次数+金额）
        int result = userDao.updateAllRechargeFields(userId, rechargeAmount);
        
        // 验证更新结果
        assertEquals(1, result, "综合充值更新应该影响1行");
        
        // 重新查询用户信息，验证充值信息是否都正确更新
        UserEntity updatedUser = userDao.selectById(userId);
        assertNotNull(updatedUser, "更新后的用户信息不应为空");
        
        // 验证充值次数
        assertEquals(beforeTodayCount + 1, updatedUser.getTodayRechargeCnt(), "今日充值次数应该增加1");
        assertEquals(beforeHistoryCount + 1, updatedUser.getHistorychargecnt(), "历史充值次数应该增加1");
        
        // 验证充值金额
        assertEquals(beforeTodayAmount + rechargeAmount, updatedUser.getTodayRecharge(), "今日充值金额应该增加充值金额");
        assertEquals(beforeChargeSum + rechargeAmount, updatedUser.getChargeSum(), "累计充值金额应该增加充值金额");
    }

    @Test
    public void testUpdateAllRechargeAmountFields() {
        // 准备测试数据：创建一个测试用户
        UserEntity testUser = createTestUser();
        Long userId = testUser.getId();
        Long rechargeAmount = 5000L; // 50元
        
        // 记录更新前的充值金额信息
        Long beforeTodayAmount = testUser.getTodayRecharge();
        Long beforeChargeSum = testUser.getChargeSum();
        
        // 执行充值金额更新（仅金额，不更新次数）
        int result = userDao.updateAllRechargeAmountFields(userId, rechargeAmount);
        
        // 验证更新结果
        assertEquals(1, result, "充值金额更新应该影响1行");
        
        // 重新查询用户信息，验证充值金额是否都正确更新
        UserEntity updatedUser = userDao.selectById(userId);
        assertNotNull(updatedUser, "更新后的用户信息不应为空");
        
        // 验证充值金额
        assertEquals(beforeTodayAmount + rechargeAmount, updatedUser.getTodayRecharge(), "今日充值金额应该增加充值金额");
        assertEquals(beforeChargeSum + rechargeAmount, updatedUser.getChargeSum(), "累计充值金额应该增加充值金额");
        
        // 验证充值次数没有变化
        assertEquals(testUser.getTodayRechargeCnt(), updatedUser.getTodayRechargeCnt(), "今日充值次数应该保持不变");
        assertEquals(testUser.getHistorychargecnt(), updatedUser.getHistorychargecnt(), "历史充值次数应该保持不变");
    }

    @Test
    public void testUpdateAllRechargeFieldsWithNullValues() {
        // 准备测试数据：创建一个测试用户，充值相关字段为null
        UserEntity testUser = createTestUserWithNullRechargeFields();
        Long userId = testUser.getId();
        Long rechargeAmount = 2000L; // 20元
        
        // 执行综合充值更新
        int result = userDao.updateAllRechargeFields(userId, rechargeAmount);
        
        // 验证更新结果
        assertEquals(1, result, "综合充值更新应该影响1行");
        
        // 重新查询用户信息，验证充值信息是否设置正确
        UserEntity updatedUser = userDao.selectById(userId);
        assertNotNull(updatedUser, "更新后的用户信息不应为空");
        
        // 验证充值次数
        assertEquals(1L, updatedUser.getTodayRechargeCnt(), "今日充值次数应该设置为1");
        assertEquals(1L, updatedUser.getHistorychargecnt(), "历史充值次数应该设置为1");
        
        // 验证充值金额
        assertEquals(rechargeAmount, updatedUser.getTodayRecharge(), "今日充值金额应该设置为充值金额");
        assertEquals(rechargeAmount, updatedUser.getChargeSum(), "累计充值金额应该设置为充值金额");
    }

    @Test
    public void testUpdateAllRechargeFieldsMultipleTimes() {
        // 准备测试数据：创建一个测试用户
        UserEntity testUser = createTestUser();
        Long userId = testUser.getId();
        Long rechargeAmount = 1000L; // 10元
        
        // 记录更新前的充值信息
        Long beforeTodayCount = testUser.getTodayRechargeCnt();
        Long beforeHistoryCount = testUser.getHistorychargecnt();
        Long beforeTodayAmount = testUser.getTodayRecharge();
        Long beforeChargeSum = testUser.getChargeSum();
        
        // 执行多次充值更新
        int result1 = userDao.updateAllRechargeFields(userId, rechargeAmount);
        int result2 = userDao.updateAllRechargeFields(userId, rechargeAmount);
        int result3 = userDao.updateAllRechargeFields(userId, rechargeAmount);
        
        // 验证更新结果
        assertEquals(1, result1, "第1次更新应该影响1行");
        assertEquals(1, result2, "第2次更新应该影响1行");
        assertEquals(1, result3, "第3次更新应该影响1行");
        
        // 重新查询用户信息，验证充值信息是否累计更新
        UserEntity updatedUser = userDao.selectById(userId);
        assertNotNull(updatedUser, "更新后的用户信息不应为空");
        
        // 验证充值次数累计增加
        assertEquals(beforeTodayCount + 3, updatedUser.getTodayRechargeCnt(), "今日充值次数应该增加3");
        assertEquals(beforeHistoryCount + 3, updatedUser.getHistorychargecnt(), "历史充值次数应该增加3");
        
        // 验证充值金额累计增加
        assertEquals(beforeTodayAmount + (rechargeAmount * 3), updatedUser.getTodayRecharge(), "今日充值金额应该增加3倍充值金额");
        assertEquals(beforeChargeSum + (rechargeAmount * 3), updatedUser.getChargeSum(), "累计充值金额应该增加3倍充值金额");
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
        user.setTodayRecharge(50000L); // 500元
        user.setChargeSum(100000L); // 1000元
        user.setAssets(50000L); // 500元
        
        userDao.insert(user);
        return user;
    }

    /**
     * 创建充值相关字段为null的测试用户
     */
    private UserEntity createTestUserWithNullRechargeFields() {
        UserEntity user = new UserEntity();
        user.setUsername("testUserNull" + System.currentTimeMillis());
        user.setMobile("13800138001");
        user.setPassword("testPassword");
        user.setInviteCode("TESTNULL" + System.currentTimeMillis());
        user.setTodayRechargeCnt(null);
        user.setHistorychargecnt(null);
        user.setTodayRecharge(null);
        user.setChargeSum(null);
        user.setAssets(0L);
        
        userDao.insert(user);
        return user;
    }
}
