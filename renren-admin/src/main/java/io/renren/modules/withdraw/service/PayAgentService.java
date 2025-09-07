package io.renren.modules.withdraw.service;

import io.renren.modules.withdraw.dto.PayAgentResponse;
import io.renren.modules.paymerchant.entity.PayMerchantEntity;
import io.renren.modules.withdraw.entity.WithdrawOrderEntity;

/**
 * 统一代付服务接口
 * 
 * @author renren
 * @date 2024-01-01
 */
public interface PayAgentService {
    
    /**
     * 创建代付订单
     * 
     * @param withdrawOrder 提现订单
     * @param payMerchant 支付商户信息
     * @return 统一代付响应结果
     */
    PayAgentResponse createPayoutOrder(WithdrawOrderEntity withdrawOrder, PayMerchantEntity payMerchant);
    
    /**
     * 获取代付渠道标识
     * 
     * @return 渠道标识
     */
    String getChannelCode();
    
    /**
     * 检查是否支持该商户
     * 
     * @param merchantCode 商户CODE
     * @return 是否支持
     */
    boolean supports(String merchantCode);
}
