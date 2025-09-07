package io.renren.modules.withdraw.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.renren.modules.withdraw.dto.PayAgentResponse;
import io.renren.modules.withdraw.dto.PayoutRequestDTO;
import io.renren.modules.paymerchant.entity.PayMerchantEntity;
import io.renren.modules.withdraw.entity.WithdrawOrderEntity;
import io.renren.modules.withdraw.service.PayAgentService;
import io.renren.common.utils.HttpUtils;
import io.renren.common.utils.WePaySignatureUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * WePay代付服务实现类
 * 
 * @author renren
 * @date 2024-01-01
 */
@Service
public class WePayPayoutServiceImpl implements PayAgentService {
    
    private static final Logger logger = LoggerFactory.getLogger(WePayPayoutServiceImpl.class);
    
    /**
     * WePay代付API地址
     */
    private static final String WEPAY_PAYOUT_URL = "https://api.wepay.com/pay/create";
    
    /**
     * 代付回调地址
     */
    private static final String PAYOUT_NOTIFY_URL = "http://206.238.68.208:8082/api/payout/notify";
    
    /**
     * WePay渠道标识
     */
    private static final String CHANNEL_CODE = "WePay";
    
    @Override
    public PayAgentResponse createPayoutOrder(WithdrawOrderEntity withdrawOrder, PayMerchantEntity payMerchant) {
        try {
            logger.info("开始创建WePay代付订单 - 订单号: {}, 金额: {}", 
                       withdrawOrder.getOrderno(), withdrawOrder.getAmount());
            
            // 1. 构建请求参数
            PayoutRequestDTO request = buildPayoutRequest(withdrawOrder, payMerchant);
            
            // 2. 发送HTTP请求
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            
            String requestBody = JSON.toJSONString(request);
            logger.info("代付请求参数: {}", requestBody);
            
            String responseBody = HttpUtils.postJson(WEPAY_PAYOUT_URL, requestBody, headers);
            
            if (responseBody != null && !responseBody.isEmpty()) {
                logger.info("代付请求成功 - 订单号: {}, 响应: {}", 
                           withdrawOrder.getOrderno(), responseBody);
                return parsePayoutResponse(responseBody);
            } else {
                logger.error("代付请求失败 - 订单号: {}, 响应为空", withdrawOrder.getOrderno());
                return PayAgentResponse.failure("EMPTY_RESPONSE", "代付请求失败: 响应为空", 
                                              CHANNEL_CODE, null, "响应为空");
            }
            
        } catch (Exception e) {
            logger.error("创建WePay代付订单异常 - 订单号: {}", withdrawOrder.getOrderno(), e);
            return PayAgentResponse.error("创建代付订单失败: " + e.getMessage(), 
                                        CHANNEL_CODE, e.getMessage());
        }
    }
    
    @Override
    public String getChannelCode() {
        return CHANNEL_CODE;
    }
    
    @Override
    public boolean supports(String merchantCode) {
        // WePay支持特定的商户CODE
        // 这里可以根据实际业务逻辑进行判断
        // 例如：检查商户CODE是否以"WEPAY"开头，或者检查是否在WePay支持的商户列表中
        return merchantCode != null && (
            merchantCode.startsWith("WEPAY_") || 
            merchantCode.startsWith("WP_") ||
            "WEPAY001".equals(merchantCode) ||
            "WEPAY002".equals(merchantCode)
        );
    }
    
    /**
     * 构建代付请求参数
     */
    private PayoutRequestDTO buildPayoutRequest(WithdrawOrderEntity withdrawOrder, PayMerchantEntity payMerchant) {
        PayoutRequestDTO request = new PayoutRequestDTO();
        
        // 基本参数
        request.setMchId(payMerchant.getMerchantno());
        request.setPassageId("101"); // TODO: 从配置或数据库获取代付通道ID
        request.setOrderNo(withdrawOrder.getOrderno());
        request.setAccount(withdrawOrder.getPayNo());
        request.setUserName(withdrawOrder.getPayName());
        request.setEmail("");
        
        // 金额转换（分转元）
        BigDecimal amountInYuan = new BigDecimal(withdrawOrder.getAmount()).divide(new BigDecimal("100"));
        request.setAmount(amountInYuan.longValue());
        
        // 回调地址
        request.setNotifyUrl(PAYOUT_NOTIFY_URL);
        
        // 扩展字段
//        request.setOtherData("withdraw_order_id:" + withdrawOrder.getId());
        
        // 银行备注（根据国家/地区设置）
        if (withdrawOrder.getIfsc() != null && !withdrawOrder.getIfsc().isEmpty()) {
            request.setIfsc(withdrawOrder.getIfsc());
        }
        
        // 生成签名
        Map<String, Object> signData = new HashMap<>();
        signData.put("mchId", request.getMchId());
        signData.put("passageId", request.getPassageId());
        signData.put("orderNo", request.getOrderNo());
        signData.put("account", request.getAccount());
        signData.put("userName", request.getUserName());
        if (request.getIfsc() != null) {
            signData.put("ifsc", request.getIfsc());
        }
        if (StringUtils.isNotBlank(request.getNumber())) {
            signData.put("number", request.getNumber());
        }
        if (StringUtils.isNotBlank(request.getEmail())) {
            signData.put("email", request.getEmail());
        }
        signData.put("amount", request.getAmount());
        signData.put("notifyUrl", request.getNotifyUrl());
//        if (StringUtils.isNotBlank(request.getOtherData())) {
//            signData.put("otherData", request.getOtherData());
//        }
        
        String sign = WePaySignatureUtils.generateSign(signData, payMerchant.getChannelkey());
        request.setSign(sign);
        
        logger.info("构建代付请求参数完成 - 订单号: {}, 签名: {}", 
                   withdrawOrder.getOrderno(), sign);
        
        return request;
    }
    
    /**
     * 解析代付响应 - 返回统一格式
     */
    private PayAgentResponse parsePayoutResponse(String responseBody) {
        try {
            JSONObject response = JSON.parseObject(responseBody);
            String code = response.getString("code");
            String desc = response.getString("desc");
            Boolean success = response.getBoolean("success");
            
            if (success != null && success) {
                // 获取第三方订单号
                JSONObject data = response.getJSONObject("data");
                String thirdOrderNo = data != null ? data.getString("id") : null;
                
                return PayAgentResponse.success(code, desc, thirdOrderNo, CHANNEL_CODE, responseBody);
            } else {
                return PayAgentResponse.failure(code, desc, CHANNEL_CODE, responseBody, desc);
            }
            
        } catch (Exception e) {
            logger.error("解析代付响应失败 - 响应体: {}", responseBody, e);
            return PayAgentResponse.error("解析代付响应失败: " + e.getMessage(), 
                                        CHANNEL_CODE, e.getMessage());
        }
    }
}