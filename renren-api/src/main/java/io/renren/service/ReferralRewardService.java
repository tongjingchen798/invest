package io.renren.service;

/**
 * 推荐返利服务接口
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
public interface ReferralRewardService {

    /**
     * 处理用户注册成功后的推荐返利
     * @param newUserId 新注册用户ID
     * @param referrerInviteCode 推荐人邀请码
     * @return 是否处理成功
     */
    boolean processReferralReward(Long newUserId, String referrerInviteCode);

    /**
     * 计算推荐返利金额
     * @param referralCount 推荐人数
     * @return 返利金额（分）
     */
    Long calculateReferralReward(Long referralCount);

    /**
     * 给推荐人发放返利
     * @param referrerId 推荐人ID
     * @param rewardAmount 返利金额（分）
     * @param newUserId 新用户ID
     * @return 是否发放成功
     */
    boolean grantReferralReward(Long referrerId, Long rewardAmount, Long newUserId);
}
