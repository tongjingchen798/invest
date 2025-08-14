package io.renren.service;

import io.renren.service.ReferralRewardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 推荐返利服务测试类
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@SpringBootTest
public class ReferralRewardServiceTest {

    @Autowired
    private ReferralRewardService referralRewardService;

    @Test
    public void testCalculateReferralReward() {
        // 测试第1个推荐用户：200卢比 = 20000分
        assertEquals(20000L, referralRewardService.calculateReferralReward(0L));
        
        // 测试第2个推荐用户：300卢比 = 30000分
        assertEquals(30000L, referralRewardService.calculateReferralReward(1L));
        
        // 测试第3个推荐用户：300卢比 = 30000分
        assertEquals(30000L, referralRewardService.calculateReferralReward(2L));
        
        // 测试第4个推荐用户：400卢比 = 40000分
        assertEquals(40000L, referralRewardService.calculateReferralReward(3L));
        
        // 测试第5个推荐用户：400卢比 = 40000分
        assertEquals(40000L, referralRewardService.calculateReferralReward(4L));
        
        // 测试第6个推荐用户：500卢比 = 50000分
        assertEquals(50000L, referralRewardService.calculateReferralReward(5L));
        
        // 测试第7个推荐用户：500卢比 = 50000分
        assertEquals(50000L, referralRewardService.calculateReferralReward(6L));
        
        // 测试第8个推荐用户：600卢比 = 60000分
        assertEquals(60000L, referralRewardService.calculateReferralReward(7L));
        
        // 测试第9个推荐用户：600卢比 = 60000分
        assertEquals(60000L, referralRewardService.calculateReferralReward(8L));
        
        // 测试第10个推荐用户：600卢比 = 60000分
        assertEquals(60000L, referralRewardService.calculateReferralReward(9L));
        
        // 测试超过10个推荐用户：不再返利
        assertEquals(0L, referralRewardService.calculateReferralReward(10L));
        assertEquals(0L, referralRewardService.calculateReferralReward(15L));
        assertEquals(0L, referralRewardService.calculateReferralReward(100L));
    }

    @Test
    public void testCalculateReferralRewardWithNull() {
        // 测试null值处理
        assertEquals(20000L, referralRewardService.calculateReferralReward(null));
    }
}
