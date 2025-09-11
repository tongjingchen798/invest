package io.renren.service;

import com.alibaba.fastjson.JSON;
import io.renren.common.exception.RenException;
import io.renren.dto.HttpResponse;
import io.renren.dto.PaymentResponseDTO;
import io.renren.entity.PayChannelEntity;
import io.renren.entity.PayMerchantEntity;
import io.renren.entity.UserEntity;
import io.renren.utils.OkHttpUtil;
import io.renren.utils.WePaySignatureUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * WePay支付服务实现类
 * 
 * @author renren
 * @date 2024-01-01
 */
@Service
public class WePayPaymentService {
    
    private static final Logger logger = LoggerFactory.getLogger(WePayPaymentService.class);
    
    @Autowired
    private OkHttpUtil okHttpUtil;
    
    /**
     * 创建支付订单
     * 
     * @param user 用户信息
     * @param amount 金额(分)
     * @param orderNo 商户订单号
     * @param payChannel 支付通道
     * @param payMerchant 支付商户
     * @return 支付响应结果
     */
    public PaymentResponseDTO createPaymentOrder(UserEntity user, Long amount, String orderNo, 
                                                PayChannelEntity payChannel, PayMerchantEntity payMerchant) {
        try {
            // 构建支付请求参数
            Map<String, Object> requestData = buildPaymentRequest(user, amount, orderNo, payChannel, payMerchant);
            
            // 发送支付请求
            String apiUrl = "https://apis.wepayplus.com/client/collect/create";
            HttpResponse<String> response = okHttpUtil.postJson(apiUrl, requestData);
            
            if (!response.isSuccess()) {
                logger.error("WePay支付API请求失败: {}", response.getMessage());
                throw new RenException(500, "WePay支付API请求失败: " + response.getMessage());
            }
            
            // 解析支付响应
            return parsePaymentResponse(response.getData());
            
        } catch (Exception e) {
            logger.error("创建WePay支付订单失败: {}", e.getMessage(), e);
            throw new RenException(500, "创建WePay支付订单失败: " + e.getMessage());
        }
    }
    
    /**
     * 构建WePay支付请求参数
     */
    private Map<String, Object> buildPaymentRequest(UserEntity user, Long amount, String orderNo,
                                                   PayChannelEntity payChannel, PayMerchantEntity payMerchant) {
        Map<String, Object> data = new HashMap<>();
        
        // 必填参数
        data.put("mchId", payMerchant.getMerchantno()); // 商户ID
        data.put("passageId", payChannel.getChannelCode()); // 通道ID (TODO: 先用测试通道)
        data.put("amount", amount); // 金额(法币)
        data.put("orderNo", orderNo); // 商户订单号
        data.put("notifyUrl", "http://206.238.68.208:8082/api/payment/notify"); // 异步通知回调地址
        
        // 可选参数
        data.put("callBackUrl", ""); // 充值成功回跳地址
        data.put("otherData", "user_id:" + user.getId()); // 扩展字段
        data.put("remark", ""); // 备注
        data.put("number", ""); // 号码备注
        data.put("userName", ""); // 名字备注
        data.put("email", ""); // 邮箱
        
        // 生成WePay签名
        data.put("sign", WePaySignatureUtils.generateSign(data, payMerchant.getChannelkey()));
        
        return data;
    }
    
    /**
     * 解析WePay支付响应
     */
    private PaymentResponseDTO parsePaymentResponse(String responseData) {
        try {
            PaymentResponseDTO paymentResponse = JSON.parseObject(responseData, PaymentResponseDTO.class);
            
            if (paymentResponse == null) {
                throw new RenException(500, "WePay支付响应解析失败: 响应数据为空");
            }
            
            // 记录响应日志
            if (paymentResponse.getSuccess() != null && paymentResponse.getSuccess()) {
                logger.info("WePay支付请求成功 - 状态码: {}, 状态描述: {}", 
                           paymentResponse.getCode(), paymentResponse.getDesc());
                
                if (paymentResponse.getData() != null) {
                    logger.info("WePay支付数据 - 支付地址: {}, 商户单号: {}, 系统单号: {}", 
                               paymentResponse.getData().getPayUrl(),
                               paymentResponse.getData().getOrderNo(),
                               paymentResponse.getData().getTradeNo());
                }
            } else {
                logger.error("WePay支付请求失败 - 状态码: {}, 状态描述: {}, 状态信息: {}", 
                           paymentResponse.getCode(), paymentResponse.getDesc(), paymentResponse.getMsg());
            }
            
            return paymentResponse;
            
        } catch (Exception e) {
            logger.error("解析WePay支付响应失败: {}", e.getMessage(), e);
            throw new RenException(500, "解析WePay支付响应失败: " + e.getMessage());
        }
    }
    

}
