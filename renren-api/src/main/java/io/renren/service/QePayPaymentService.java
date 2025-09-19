package io.renren.service;

import com.alibaba.fastjson.JSON;
import io.renren.common.exception.RenException;
import io.renren.dto.HttpResponse;
import io.renren.dto.PaymentResponseDTO;
import io.renren.dto.QePayResponseDTO;
import io.renren.entity.PayChannelEntity;
import io.renren.entity.PayMerchantEntity;
import io.renren.entity.UserEntity;
import io.renren.utils.OkHttpUtil;
import io.renren.utils.QePaySignatureUtils;
import io.renren.utils.QePayTypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * QePay支付服务实现类
 * 
 * @author nico
 * @date 2024-01-01
 */
@Service
public class QePayPaymentService{
    
    private static final Logger logger = LoggerFactory.getLogger(QePayPaymentService.class);
    
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
            
            // 发送支付请求（使用form格式）
            String apiUrl = "https://pay.qeawapay.com/pay/web";
            HttpResponse<String> response = okHttpUtil.postForm(apiUrl, requestData);
            
            if (!response.isSuccess()) {
                logger.error("QePay支付API请求失败: {}", response.getMessage());
                throw new RenException(500, "QePay支付API请求失败: " + response.getMessage());
            }
            
            // 解析支付响应
            return parsePaymentResponse(response.getData());
            
        } catch (Exception e) {
            logger.error("创建QePay支付订单失败: {}", e.getMessage(), e);
            throw new RenException(500, "创建QePay支付订单失败: " + e.getMessage());
        }
    }
    
    /**
     * 构建QePay支付请求参数
     */
    private Map<String, Object> buildPaymentRequest(UserEntity user, Long amount, String orderNo,
                                                   PayChannelEntity payChannel, PayMerchantEntity payMerchant) {
        Map<String, Object> data = new HashMap<>();
        
        // 验证支付类型
        String payTypeCode = payChannel.getChannelCode();
        if (!QePayTypeUtils.isValidPayType(payTypeCode)) {
            throw new RenException(500, "无效的QePay支付类型: " + payTypeCode);
        }
        
        // 记录支付类型使用日志
        QePayTypeUtils.logPayTypeUsage(payTypeCode, orderNo);

        // 必填参数
        data.put("goods_name", "充值订单"); // 商品名称

        data.put("mch_id", payMerchant.getMerchantno()); // 商户号
        data.put("mch_order_no", orderNo); // 商家订单号

        data.put("pay_type", payTypeCode); // 支付类型（通道编码）
        data.put("trade_amount", String.valueOf(amount)); // 交易金额（字符串格式，精确到元）
        data.put("notify_url", payMerchant.getNotifyUrl()); // 异步通知地址
        data.put("order_date", getCurrentTimeString()); // 订单时间（北京时间）
        data.put("version","1.0");
        // 可选参数
        // 注意：PayMerchantEntity中没有returnUrl字段，这里可以根据需要添加或使用其他字段
        // data.put("page_url", ""); // 同步跳转地址（暂时留空）
        
        // bank_code参数（网银通道必填，其他类型一定不能填该参数）
        // 根据支付类型判断是否需要bank_code
        // if (isBankChannelRequired(payTypeCode)) {
        //     // 这里可以根据具体需求设置bank_code，暂时留空
        //     // data.put("bank_code", "");
        // }
        
        // 透传参数（用户ID）
//        data.put("mch_return_msg", "user_id:" + user.getId());
        
        // 签名方式（固定值MD5，不参与签名）
        data.put("sign_type", "MD5");
        
        // 验证参数长度和格式
//        validateQePayParameters(data);

        // 生成QePay签名
        data.put("sign", QePaySignatureUtils.generateSign(data, payMerchant.getChannelkey()));
        
        return data;
    }
    
    /**
     * 获取当前时间字符串（北京时间格式：yyyy-MM-dd HH:mm:ss）
     */
    private String getCurrentTimeString() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }
    
    /**
     * 判断支付类型是否需要bank_code参数
     * 根据QePay文档，网银通道必填，其他类型一定不能填该参数
     * 
     * @param payTypeCode 支付类型代码
     * @return 是否需要bank_code
     */
    private boolean isBankChannelRequired(String payTypeCode) {
        // 根据支付类型代码判断是否为网银通道
        // 这里可以根据具体的支付类型规则来判断
        // 暂时返回false，具体实现需要根据QePay的文档来确定哪些类型需要bank_code
        return false;
    }
    
    /**
     * 验证QePay参数长度和格式
     */
    private void validateQePayParameters(Map<String, Object> params) {
        // 验证notify_url长度（不超过200字节）
        String notifyUrl = String.valueOf(params.get("notify_url"));
        if (notifyUrl.length() > 200) {
            throw new RenException(500, "异步通知地址长度不能超过200字节");
        }
        
        // 验证page_url长度（不超过200字节）
        if (params.containsKey("page_url")) {
            String pageUrl = String.valueOf(params.get("page_url"));
            if (pageUrl.length() > 200) {
                throw new RenException(500, "同步跳转地址长度不能超过200字节");
            }
        }
        
        // 验证goods_name长度（不超过50字节）
        String goodsName = String.valueOf(params.get("goods_name"));
        if (goodsName.length() > 50) {
            throw new RenException(500, "商品名称长度不能超过50字节");
        }
        
//        // 验证mch_return_msg长度（不超过200字节）
//        String mchReturnMsg = String.valueOf(params.get("mch_return_msg"));
//        if (mchReturnMsg.length() > 200) {
//            throw new RenException(500, "透传参数长度不能超过200字节");
//        }
        
        // 验证trade_amount格式（必须是数字）
        try {
            String tradeAmount = String.valueOf(params.get("trade_amount"));
            Double.parseDouble(tradeAmount);
        } catch (NumberFormatException e) {
            throw new RenException(500, "交易金额格式不正确");
        }
        
        // 验证order_date格式（yyyy-MM-dd HH:mm:ss）
        String orderDate = String.valueOf(params.get("order_date"));
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime.parse(orderDate, formatter);
        } catch (Exception e) {
            throw new RenException(500, "订单时间格式不正确，应为yyyy-MM-dd HH:mm:ss");
        }
    }
    
    /**
     * 解析QePay支付响应
     */
    private PaymentResponseDTO parsePaymentResponse(String responseData) {
        try {
            // 解析QePay的JSON响应
            QePayResponseDTO qePayResponse = JSON.parseObject(responseData, QePayResponseDTO.class);
            
            if (qePayResponse == null) {
                throw new RenException(500, "QePay支付响应解析失败: 响应数据为空");
            }
            
            // 记录QePay响应日志
            logger.info("QePay支付响应 - 响应状态: {}, 响应消息: {}, 订单状态: {}", 
                       qePayResponse.getRespCode(), qePayResponse.getTradeMsg(), qePayResponse.getTradeResult());
            
            // 验证响应签名（可选）
            // 注意：这里可以根据需要验证QePay返回的签名
            
            // 转换为统一的PaymentResponseDTO格式
            PaymentResponseDTO paymentResponse = new PaymentResponseDTO();
            
            if (qePayResponse.isSuccess()) {
                // 支付成功
                paymentResponse.setSuccess(true);
                paymentResponse.setCode(200);
                paymentResponse.setDesc("支付请求成功");
                paymentResponse.setMsg(qePayResponse.getTradeMsg());
                
                // 设置支付数据
                PaymentResponseDTO.PaymentData data = new PaymentResponseDTO.PaymentData();
                data.setPayUrl(qePayResponse.getPayUrl());
                data.setOrderNo(qePayResponse.getMchOrderNo());
                data.setTradeNo(qePayResponse.getOrderNo());
                paymentResponse.setData(data);
                
                logger.info("QePay支付请求成功 - 支付链接: {}, 商家订单号: {}, 平台订单号: {}", 
                           qePayResponse.getPayUrl(), qePayResponse.getMchOrderNo(), qePayResponse.getOrderNo());
                
            } else {
                // 支付失败
                paymentResponse.setSuccess(false);
                paymentResponse.setCode(500);
                paymentResponse.setDesc("支付请求失败");
                paymentResponse.setMsg(qePayResponse.getTradeMsg());
                
                logger.error("QePay支付请求失败 - 响应状态: {}, 响应消息: {}, 订单状态: {}", 
                           qePayResponse.getRespCode(), qePayResponse.getTradeMsg(), qePayResponse.getTradeResult());
            }
            
            return paymentResponse;
            
        } catch (Exception e) {
            logger.error("解析QePay支付响应失败: {}", e.getMessage(), e);
            throw new RenException(500, "解析QePay支付响应失败: " + e.getMessage());
        }
    }
    

}
