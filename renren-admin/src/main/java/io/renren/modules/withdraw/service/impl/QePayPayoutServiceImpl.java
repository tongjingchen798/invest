package io.renren.modules.withdraw.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.renren.common.exception.RenException;
import io.renren.modules.paychannel.dao.PayChannelDao;
import io.renren.modules.paychannel.entity.PayChannelEntity;
import io.renren.modules.withdraw.dto.PayAgentResponse;
import io.renren.modules.paymerchant.entity.PayMerchantEntity;
import io.renren.modules.withdraw.entity.WithdrawOrderEntity;
import io.renren.modules.withdraw.service.PayAgentService;
import io.renren.common.utils.HttpUtils;
import io.renren.common.utils.QePaySignatureUtils;
import io.renren.common.utils.FormDataUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * QePay代付服务实现类
 * 专门处理印度代付业务
 * 
 * @author renren
 * @date 2024-01-01
 */
@Service
public class QePayPayoutServiceImpl implements PayAgentService {
    
    private static final Logger logger = LoggerFactory.getLogger(QePayPayoutServiceImpl.class);
    
    /**
     * QePay代付API地址
     */
    private static final String QEPAY_PAYOUT_URL = "https://pay.qeawapay.com/pay/transfer";
    
    /**
     * 代付回调地址
     */
    private static final String PAYOUT_NOTIFY_URL = "https://profit-game.com/api/qepay/payout/notify";
    
    /**
     * QePay渠道标识
     */
    private static final String CHANNEL_CODE = "QePay";
    
    private final PayChannelDao payChannelDao;

    public QePayPayoutServiceImpl(PayChannelDao payChannelDao) {
        this.payChannelDao = payChannelDao;
    }

    @Override
    public PayAgentResponse createPayoutOrder(WithdrawOrderEntity withdrawOrder, PayMerchantEntity payMerchant) {
        try {
            logger.info("开始创建QePay代付订单 - 订单号: {}, 金额: {}", 
                       withdrawOrder.getOrderno(), withdrawOrder.getAmount());
            
            // 1. 构建请求参数
            Map<String, Object> requestData = buildPayoutRequest(withdrawOrder, payMerchant);

            // 2. 构建表单数据
            String formData = FormDataUtils.buildSimpleFormData(requestData);
            logger.info("代付请求参数: {}", formData);

            // 3. 发送HTTP请求
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            
            // 使用改进的HTTP请求方法，返回状态码和响应体
            HttpUtils.HttpResponseResult httpResult = HttpUtils.postJsonWithStatus(QEPAY_PAYOUT_URL, formData, headers);
            
            if (httpResult.getStatusCode() >= 200 && httpResult.getStatusCode() < 300) {
                // 请求成功，解析响应
                return parsePayoutResponse(httpResult.getResponseBody(), withdrawOrder.getOrderno());
            } else {
                // HTTP请求失败
                logger.error("QePay代付请求失败 - 状态码: {}, 响应: {}", 
                           httpResult.getStatusCode(), httpResult.getResponseBody());
                return PayAgentResponse.failure(
                    "HTTP_ERROR", 
                    "代付请求失败，状态码: " + httpResult.getStatusCode(),
                    CHANNEL_CODE,
                    httpResult.getResponseBody(),
                    "HTTP请求失败"
                );
            }
            
        } catch (Exception e) {
            logger.error("QePay代付处理异常 - 订单号: {}, 错误: {}", 
                       withdrawOrder.getOrderno(), e.getMessage(), e);
            return PayAgentResponse.error(
                "代付处理异常: " + e.getMessage(),
                CHANNEL_CODE,
                e.getMessage()
            );
        }
    }
    
    @Override
    public String getChannelCode() {
        return CHANNEL_CODE;
    }
    
    @Override
    public boolean supports(String merchantCode) {
        // QePay支持特定的商户CODE
        // 这里可以根据实际业务逻辑进行判断
        return merchantCode != null && merchantCode.startsWith("QEPAY");
    }
    
    /**
     * 构建代付请求参数
     */
    private Map<String, Object> buildPayoutRequest(WithdrawOrderEntity withdrawOrder, PayMerchantEntity payMerchant) {
        PayChannelEntity payChannelEntity = payChannelDao.selectById(withdrawOrder.getChannelid());
        if (payChannelEntity == null) {
            throw new RenException("渠道不存在");
        }
        
        Map<String, Object> data = new HashMap<>();
        
        // 必填参数
        data.put("sign_type", "MD5"); // 固定值MD5，不参与签名
        data.put("mch_id", payMerchant.getMerchantno()); // 商户代码
        data.put("mch_transferId", withdrawOrder.getOrderno()); // 商家转账订单号
        
        // 金额转换（分转元）
        BigDecimal amountInYuan = new BigDecimal(withdrawOrder.getRealAmount()).divide(new BigDecimal("100"));
        data.put("transfer_amount", amountInYuan.toString()); // 转账金额
        
        // 申请时间（北京时间）
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        data.put("apply_date", sdf.format(new Date()));
        
        // 银行信息
        data.put("bank_code", withdrawOrder.getBlankCode()); // 收款银行代码
        data.put("receive_name", withdrawOrder.getPayName()); // 收款银行户名
        data.put("receive_account", withdrawOrder.getPayNo()); // 收款银行账号
        
        // 印度代付必填IFSC码
        if (StringUtils.isNotBlank(withdrawOrder.getIfsc())) {
            data.put("remark", withdrawOrder.getIfsc()); // 备注字段填写IFSC码
        }
        
        // 异步通知地址（可选）
        data.put("back_url", PAYOUT_NOTIFY_URL);
        
        // 生成QePay签名
        String sign = QePaySignatureUtils.generateSign(data, payMerchant.getDfKey());
        data.put("sign", sign);
        
        return data;
    }
    
    
    /**
     * 解析代付响应 - 返回统一格式
     */
    private PayAgentResponse parsePayoutResponse(String responseBody, String orderNo) {
        try {
            logger.info("QePay代付响应: {}", responseBody);
            
            JSONObject responseJson = JSON.parseObject(responseBody);
            String respCode = responseJson.getString("respCode");
            String errorMsg = responseJson.getString("errorMsg");
            
            if ("SUCCESS".equals(respCode)) {
                // 代付请求成功
                String tradeNo = responseJson.getString("tradeNo");
                String tradeResult = responseJson.getString("tradeResult");
                
                logger.info("QePay代付请求成功 - 订单号: {}, 平台订单号: {}, 处理结果: {}", 
                           orderNo, tradeNo, tradeResult);
                
                return PayAgentResponse.success(
                    "SUCCESS",
                    "代付请求成功",
                    tradeNo,
                    CHANNEL_CODE,
                    responseBody
                );
            } else {
                // 代付请求失败
                logger.error("QePay代付请求失败 - 订单号: {}, 错误信息: {}", orderNo, errorMsg);
                
                return PayAgentResponse.failure(
                    respCode,
                    errorMsg != null ? errorMsg : "代付请求失败",
                    CHANNEL_CODE,
                    responseBody,
                    errorMsg
                );
            }
            
        } catch (Exception e) {
            logger.error("解析QePay代付响应失败 - 订单号: {}, 响应: {}, 错误: {}", 
                       orderNo, responseBody, e.getMessage(), e);
            
            return PayAgentResponse.failure(
                "PARSE_ERROR",
                "解析响应失败",
                CHANNEL_CODE,
                responseBody,
                e.getMessage()
            );
        }
    }
}
