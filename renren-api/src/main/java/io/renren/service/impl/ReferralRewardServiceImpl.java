package io.renren.service.impl;

import io.renren.dao.UserDao;
import io.renren.entity.UserEntity;
import io.renren.service.ReferralRewardService;
import io.renren.service.BalanceDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 推荐返利服务实现类
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Slf4j
@Service("referralRewardService")
public class ReferralRewardServiceImpl implements ReferralRewardService {

    @Autowired
    private UserDao userDao;
    
    @Autowired
    private BalanceDetailService balanceDetailService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean processReferralReward(Long newUserId, String referrerInviteCode) {
        try {
            // 如果没有推荐人邀请码，直接返回成功
            if (referrerInviteCode == null || referrerInviteCode.trim().isEmpty()) {
                log.info("用户 {} 没有推荐人，跳过返利处理", newUserId);
                return true;
            }

            // 根据邀请码查找推荐人
            UserEntity referrer = userDao.getUserByInviteCode(referrerInviteCode);
            if (referrer == null) {
                log.warn("邀请码 {} 对应的推荐人不存在", referrerInviteCode);
                return false;
            }

            // 获取推荐人的推荐人数
            Long referralCount = referrer.getTgrs();
            if (referralCount == null) {
                referralCount = 0L;
            }

            // 计算返利金额
            Long rewardAmount = calculateReferralReward(referralCount);
            if (rewardAmount <= 0) {
                log.info("推荐人 {} 的推荐人数 {} 不满足返利条件", referrer.getId(), referralCount);
                return true;
            }

            // 发放返利
            boolean success = grantReferralReward(referrer.getId(), rewardAmount, newUserId);


            return success;
        } catch (Exception e) {
            log.error("处理推荐返利失败，新用户ID: {}, 推荐人邀请码: {}", newUserId, referrerInviteCode, e);
            throw e;
        }
    }

    @Override
    public Long calculateReferralReward(Long referralCount) {
        if (referralCount == null) {
            referralCount = 0L;
        }

        if (referralCount == 0) {
            // 第1个推荐用户：200卢比 = 20000分
            return 20000L;
        } else if (referralCount == 1 || referralCount == 2) {
            // 第2-3个推荐用户：300卢比 = 30000分
            return 30000L;
        } else if (referralCount == 3 || referralCount == 4) {
            // 第4-5个推荐用户：400卢比 = 40000分
            return 40000L;
        } else if (referralCount == 5 || referralCount == 6) {
            // 第6-7个推荐用户：500卢比 = 50000分
            return 50000L;
        } else if (referralCount >= 7) {
            // 第8-10个推荐用户：600卢比 = 60000分
            return 60000L;
        } else {
            return 0L;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean grantReferralReward(Long referrerId, Long rewardAmount, Long newUserId) {
        try {
            // 获取推荐人信息
            UserEntity referrer = userDao.selectById(referrerId);
            if (referrer == null) {
                log.error("推荐人 {} 不存在", referrerId);
                return false;
            }
            Long originalAmount=referrer.getCommissionBalance();

            // 更新推荐人的佣金余额等字段
            int updateResult = userDao.updateInviteCodeProfitFields(referrer.getId(), rewardAmount);
            if (updateResult <= 0) {
                log.error("更新推荐人 {} 余额失败", referrerId);
                return false;
            }
            // 记录推荐返利到资金明细表
            balanceDetailService.recordReferralReward(originalAmount,referrer.getCommissionBalance()+rewardAmount,referrerId, rewardAmount, newUserId);

            log.info("推荐人 {} 获得推荐返利 {} 分，当前余额: {} 分，佣金余额: {} 分", 
                referrerId, rewardAmount, referrer.getBalance(), referrer.getCommissionBalance());

            return true;
        } catch (Exception e) {
            log.error("发放推荐返利失败，推荐人ID: {}, 返利金额: {}, 新用户ID: {}", 
                referrerId, rewardAmount, newUserId, e);
            throw e;
        }
    }
}
