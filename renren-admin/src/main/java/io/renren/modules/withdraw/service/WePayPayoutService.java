package io.renren.modules.withdraw.service;

import io.renren.modules.withdraw.dto.PayoutRequestDTO;
import io.renren.modules.withdraw.dto.PayoutResponseDTO;
import io.renren.modules.paymerchant.entity.PayMerchantEntity;
import io.renren.modules.withdraw.entity.WithdrawOrderEntity;

/**
 * WePay代付服务接口
 * 
 * @author renren
 * @date 2024-01-01
 */
public interface WePayPayoutService {
    
    /**
     * 创建代付订单
     * 
     * @param withdrawOrder 提现订单
     * @param payMerchant 支付商户信息
     * @return 代付响应结果
     */
    PayoutResponseDTO createPayoutOrder(WithdrawOrderEntity withdrawOrder, PayMerchantEntity payMerchant);
    
    /**
     * 构建代付请求参数
     * 
     * @param withdrawOrder 提现订单
     * @param payMerchant 支付商户信息
     * @return 代付请求参数
     */
    PayoutRequestDTO buildPayoutRequest(WithdrawOrderEntity withdrawOrder, PayMerchantEntity payMerchant);
    
    /**
     * 解析代付响应
     * 
     * @param responseBody 响应体
     * @return 代付响应DTO
     */
    PayoutResponseDTO parsePayoutResponse(String responseBody);
    
    /**
     * 验证代付响应签名
     * 
     * @param response 代付响应
     * @param secretKey 密钥
     * @return 是否验证通过
     */
    boolean verifyPayoutResponse(PayoutResponseDTO response, String secretKey);
}
