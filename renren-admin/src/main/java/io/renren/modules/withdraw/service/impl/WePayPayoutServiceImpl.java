package io.renren.modules.withdraw.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.renren.common.exception.RenException;
import io.renren.modules.paychannel.dao.PayChannelDao;
import io.renren.modules.paychannel.entity.PayChannelEntity;
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
    private static final String WEPAY_PAYOUT_URL = "https://apis.wepayplus.com/client/pay/create";
    
    /**
     * 代付回调地址
     */
    private static final String PAYOUT_NOTIFY_URL = "http://206.238.68.208:8082/api/payout/notify";
    
    /**
     * WePay渠道标识
     */
    private static final String CHANNEL_CODE = "WePay";
    private final PayChannelDao payChannelDao;

    public WePayPayoutServiceImpl(PayChannelDao payChannelDao) {
        this.payChannelDao = payChannelDao;
    }

    @Override
    public PayAgentResponse createPayoutOrder(WithdrawOrderEntity withdrawOrder, PayMerchantEntity payMerchant) {
        try {
            logger.info("开始创建WePay代付订单 - 订单号: {}, 金额: {}", 
                       withdrawOrder.getOrderno(), withdrawOrder.getAmount());
            
            // 1. 构建请求参数
            Map<String, Object> requestData  = buildPayoutRequest(withdrawOrder, payMerchant);

            // 2. 发送HTTP请求
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            
            String requestBody = JSON.toJSONString(requestData);
            logger.info("代付请求参数: {}", requestBody);

            // 使用改进的HTTP请求方法，返回状态码和响应体
            HttpUtils.HttpResponseResult httpResult = HttpUtils.postJsonWithStatus(WEPAY_PAYOUT_URL, requestBody, headers);
            
            if (httpResult.getStatusCode() >= 200 && httpResult.getStatusCode() < 300) {
                // HTTP状态码成功，解析响应内容
                if (!httpResult.getResponseBody().isEmpty()) {
                    logger.info("代付请求成功 - 订单号: {}, HTTP状态码: {}, 响应: {}", 
                               withdrawOrder.getOrderno(), httpResult.getStatusCode(), httpResult.getResponseBody());
                    return parsePayoutResponse(httpResult.getResponseBody());
                } else {
                    logger.error("代付请求失败 - 订单号: {}, HTTP状态码: {}, 响应为空", 
                                withdrawOrder.getOrderno(), httpResult.getStatusCode());
                    return PayAgentResponse.failure("EMPTY_RESPONSE", "代付请求失败: 响应为空", 
                                                  CHANNEL_CODE, null, "响应为空");
                }
            } else {
                // HTTP状态码错误（如400, 500等）
                logger.error("代付请求失败 - 订单号: {}, HTTP状态码: {}, 响应: {}", 
                           withdrawOrder.getOrderno(), httpResult.getStatusCode(), httpResult.getResponseBody());
                
                // 尝试解析错误响应
                if (!httpResult.getResponseBody().isEmpty()) {
                    try {
                        JSONObject errorResponse = JSON.parseObject(httpResult.getResponseBody());
                        String errorCode = errorResponse.getString("code");
                        String errorMsg = errorResponse.getString("msg");
                        return PayAgentResponse.failure(errorCode, errorMsg, CHANNEL_CODE, 
                                                      httpResult.getResponseBody(), errorMsg);
                    } catch (Exception e) {
                        return PayAgentResponse.failure("HTTP_ERROR", "代付请求失败: HTTP " + httpResult.getStatusCode(), 
                                                      CHANNEL_CODE, httpResult.getResponseBody(), 
                                                      "HTTP状态码: " + httpResult.getStatusCode());
                    }
                } else {
                    return PayAgentResponse.failure("HTTP_ERROR", "代付请求失败: HTTP " + httpResult.getStatusCode(), 
                                                  CHANNEL_CODE, null, "HTTP状态码: " + httpResult.getStatusCode());
                }
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
        // 例如：检查商户CODE是否"WEPAY"开头，或者检查是否在WePay支持的商户列表中
        return merchantCode != null && merchantCode.startsWith("WEPAY");
    }
    
    /**
     * 构建代付请求参数
     */
    private Map<String, Object> buildPayoutRequest(WithdrawOrderEntity withdrawOrder, PayMerchantEntity payMerchant) {
            PayChannelEntity payChannelEntity=payChannelDao.selectById(withdrawOrder.getChannelid());
            if(payChannelEntity==null){
                throw new RenException("渠道不存在");
            }
            Map<String, Object> data = new HashMap<>();
            // 必填参数
            data.put("mchId", payMerchant.getMerchantno()); // 商户ID
            data.put("passageId", payChannelEntity.getChannelCode()); // 通道ID (TODO: 先用测试通道)
            // 金额转换（分转元）
            BigDecimal amountInYuan = new BigDecimal(withdrawOrder.getRealAmount()).divide(new BigDecimal("100"));
            data.put("amount", amountInYuan.intValue()); // 金额(法币)
            data.put("orderNo", withdrawOrder.getOrderno()); // 商户订单号
            data.put("account", withdrawOrder.getPayNo()); // 异步通知回调地址
            data.put("userName", withdrawOrder.getPayName());
            data.put("notifyUrl", PAYOUT_NOTIFY_URL); // 异步通知回调地址
            data.put("otherData", "user_id:" + withdrawOrder.getUserId()); // 扩展字段
            data.put("ifsc", withdrawOrder.getIfsc());
            data.put("remark", ""); // 备注
            data.put("number", ""); // 号码备注
            data.put("email", ""); // 邮箱
            // 生成WePay签名
            data.put("sign", WePaySignatureUtils.generateSign(data, payMerchant.getChannelkey()));
        return data;
    }
    
    /**
     * 解析代付响应 - 返回统一格式
     */
    private PayAgentResponse parsePayoutResponse(String responseBody) {
        try {
            JSONObject response = JSON.parseObject(responseBody);
            String code = response.getString("code");
            String desc = response.getString("msg");
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