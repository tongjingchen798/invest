package io.renren.service;

import io.renren.dto.WePayCallbackDTO;
import io.renren.entity.ChargeOrderEntity;
import io.renren.entity.UserEntity;
import io.renren.entity.UserBalanceDetailEntity;

/**
 * 支付回调服务接口
 * 
 * @author renren
 * @date 2024-01-01
 */
public interface PaymentCallbackService {
    
    /**
     * 处理支付成功回调
     * 
     * @param callbackData 回调数据
     * @return 处理结果
     */
    boolean handlePaymentSuccess(WePayCallbackDTO callbackData);
    
    /**
     * 处理支付失败回调
     * 
     * @param callbackData 回调数据
     * @return 处理结果
     */
    boolean handlePaymentFailure(WePayCallbackDTO callbackData);
    
    /**
     * 处理订单生成回调
     * 
     * @param callbackData 回调数据
     * @return 处理结果
     */
    boolean handleOrderCreated(WePayCallbackDTO callbackData);
}
