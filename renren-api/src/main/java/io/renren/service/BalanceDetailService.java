package io.renren.service;

import io.renren.dto.BalanceDetailPageData;

/**
 * 资金明细服务接口
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
public interface BalanceDetailService {

    /**
     * 获取用户资金明细分页数据
     * @param userId 用户ID
     * @param page 页码
     * @param limit 每页大小
     * @return 分页数据
     */
    BalanceDetailPageData getBalanceDetailPageData(Long userId, Integer page, Integer limit);

    /**
     * 记录推荐返利到资金明细
     * @param currentAssets
     * @param userId 用户ID
     * @param amount 返利金额（分）
     * @param newUserId 新注册用户ID
     * @return 是否记录成功
     */
    boolean recordReferralReward(Long currentAssets,Long assets,Long userId, Long amount, Long newUserId);

    /**
     * 记录充值成功到资金明细
     * @param userId 用户ID
     * @param amount 充值金额（分）
     * @param orderNo 订单号
     * @param channel 支付通道
     * @param thirdOrderNo 第三方订单号
     * @return 是否记录成功
     */
    boolean recordChargeSuccess(Long userId, Long amount, String orderNo, String channel, String thirdOrderNo);

    /**
     * 获取代理佣金资金明细分页数据
     * @param userId 用户ID
     * @param page 页码
     * @param limit 每页大小
     * @return 分页数据
     */
    BalanceDetailPageData getAgentBalanceDetailPageData(Long userId, Integer page, Integer limit);

//    /**
//     * 记录充值失败到资金明细
//     * @param userId 用户ID
//     * @param amount 充值金额（分）
//     * @param orderNo 订单号
//     * @param channel 支付通道
//     * @param thirdOrderNo 第三方订单号
//     * @param failReason 失败原因
//     * @return 是否记录成功
//     */
//    boolean recordChargeFail(Long userId, Long amount, String orderNo, String channel, String thirdOrderNo, String failReason);
}
