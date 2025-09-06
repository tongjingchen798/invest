//package io.renren.service;
//
//import java.util.Map;
//
///**
// * 充值回调服务接口
// * 处理各种支付方式的回调逻辑
// *
// * @author renren
// * @email renren@gmail.com
// * @date 2024-01-01 00:00:00
// */
//public interface ChargeCallbackService {
//
//    /**
//     * 处理银行卡充值回调
//     *
//     * @param thirdOrderNo 第三方订单号
//     * @param orderNo 平台订单号
//     * @param amount 支付金额
//     * @param status 支付状态
//     * @param payTime 支付时间
//     * @param sign 签名
//     * @param parameterMap 所有回调参数
//     * @return 处理是否成功
//     */
//    boolean processBankCallback(String thirdOrderNo, String orderNo, String amount,
//                               String status, String payTime, String sign,
//                               Map<String, String[]> parameterMap);
//
//    /**
//     * 处理虚拟币充值回调
//     *
//     * @param thirdOrderNo 第三方订单号
//     * @param orderNo 平台订单号
//     * @param uAmount USDT数量
//     * @param status 支付状态
//     * @param txHash 交易哈希
//     * @param walletAddr 钱包地址
//     * @param sign 签名
//     * @param parameterMap 所有回调参数
//     * @return 处理是否成功
//     */
//    boolean processCryptoCallback(String thirdOrderNo, String orderNo, String uAmount,
//                                 String status, String txHash, String walletAddr, String sign,
//                                 Map<String, String[]> parameterMap);
//
//    /**
//     * 处理UPI充值回调
//     *
//     * @param thirdOrderNo 第三方订单号
//     * @param orderNo 平台订单号
//     * @param amount 支付金额
//     * @param status 支付状态
//     * @param upiId UPI ID
//     * @param sign 签名
//     * @param parameterMap 所有回调参数
//     * @return 处理是否成功
//     */
//    boolean processUpiCallback(String thirdOrderNo, String orderNo, String amount,
//                              String status, String upiId, String sign,
//                              Map<String, String[]> parameterMap);
//
//    /**
//     * 处理Paytm充值回调
//     *
//     * @param thirdOrderNo 第三方订单号
//     * @param orderNo 平台订单号
//     * @param amount 支付金额
//     * @param status 支付状态
//     * @param paytmOrderId Paytm订单ID
//     * @param sign 签名
//     * @param parameterMap 所有回调参数
//     * @return 处理是否成功
//     */
//    boolean processPaytmCallback(String thirdOrderNo, String orderNo, String amount,
//                                String status, String paytmOrderId, String sign,
//                                Map<String, String[]> parameterMap);
//
//    /**
//     * 处理通用充值回调
//     *
//     * @param paymentMethod 支付方式
//     * @param thirdOrderNo 第三方订单号
//     * @param orderNo 平台订单号
//     * @param amount 支付金额
//     * @param status 支付状态
//     * @param sign 签名
//     * @param parameterMap 所有回调参数
//     * @return 处理是否成功
//     */
//    boolean processCommonCallback(String paymentMethod, String thirdOrderNo, String orderNo,
//                                 String amount, String status, String sign,
//                                 Map<String, String[]> parameterMap);
//
//    /**
//     * 构建订单状态查询响应
//     *
//     * @param order 充值订单
//     * @return 订单状态信息
//     */
//    Map<String, Object> buildOrderStatusResponse(Object order);
//
//    /**
//     * 验证回调签名
//     *
//     * @param parameterMap 回调参数
//     * @param sign 签名
//     * @param secretKey 密钥
//     * @return 签名是否有效
//     */
//    boolean verifyCallbackSign(Map<String, String[]> parameterMap, String sign, String secretKey);
//
//    /**
//     * 更新充值订单状态
//     *
//     * @param orderNo 订单号
//     * @param status 新状态
//     * @param thirdOrderNo 第三方订单号
//     * @param additionalInfo 额外信息
//     * @return 更新是否成功
//     */
//    boolean updateChargeOrderStatus(String orderNo, Integer status, String thirdOrderNo, Map<String, Object> additionalInfo);
//
//    /**
//     * 处理充值成功逻辑
//     *
//     * @param orderNo 订单号
//     * @param amount 充值金额
//     * @param thirdOrderNo 第三方订单号
//     * @return 处理是否成功
//     */
//    boolean handleChargeSuccess(String orderNo, Long amount, String thirdOrderNo);
//
//    /**
//     * 处理充值失败逻辑
//     *
//     * @param orderNo 订单号
//     * @param failReason 失败原因
//     * @param thirdOrderNo 第三方订单号
//     * @return 处理是否成功
//     */
//    boolean handleChargeFail(String orderNo, String failReason, String thirdOrderNo);
//}
