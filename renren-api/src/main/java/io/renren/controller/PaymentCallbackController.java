package io.renren.controller;

import io.renren.common.utils.Result;
import io.renren.dao.ChargeOrderDao;
import io.renren.dao.PayMerchantDao;
import io.renren.dao.UserDao;
import io.renren.dao.UserBalanceDetailDao;
import io.renren.dto.WePayCallbackDTO;
import io.renren.entity.ChargeOrderEntity;
import io.renren.entity.PayMerchantEntity;
import io.renren.service.WePayPaymentService;
import io.renren.service.PaymentCallbackService;
import io.renren.utils.WePaySignatureUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付回调处理控制器
 * 
 * @author renren
 * @date 2024-01-01
 */
@RestController
@RequestMapping("/api/payment")
@Api(tags = "支付回调接口")
public class PaymentCallbackController {
    
    private static final Logger logger = LoggerFactory.getLogger(PaymentCallbackController.class);
    
    @Autowired
    private PayMerchantDao payMerchantDao;

    @Autowired
    private ChargeOrderDao chargeOrderDao;
    

    @Autowired
    private PaymentCallbackService paymentCallbackService;
    
    @PostMapping(value = "/notify", consumes = "application/json")
    @ApiOperation("WePay支付结果异步通知")
    public String paymentNotify(@RequestBody String requestBody) {
        try {
            logger.info("收到WePay支付回调通知");
            logger.info("支付回调JSON数据: {}", requestBody);
            
            // 解析WePay回调JSON数据
            WePayCallbackDTO callbackData = parseWePayCallbackJson(requestBody);
            
            if (callbackData == null) {
                logger.error("WePay支付回调JSON数据解析失败");
                return "fail";
            }
            
            // 验证WePay签名
            boolean isValid = verifyWePayCallbackSign(callbackData);
            if (!isValid) {
                logger.error("WePay支付回调签名验证失败 - 订单号: {}", callbackData.getOrderNo());
                return "fail";
            }
            
            // 处理支付结果
            boolean success = processWePayPaymentResult(callbackData);
            
            if (success) {
                logger.info("WePay支付回调处理成功 - 订单号: {}, 系统单号: {}, 支付状态: {}", 
                           callbackData.getOrderNo(), callbackData.getTradeNo(), callbackData.getPayStatus());
                return "success";
            } else {
                logger.error("WePay支付回调处理失败 - 订单号: {}", callbackData.getOrderNo());
                return "fail";
            }
            
        } catch (Exception e) {
            logger.error("处理WePay支付回调异常: {}", e.getMessage(), e);
            return "fail";
        }
    }
    
    /**
     * 解析WePay支付回调JSON数据
     */
    private WePayCallbackDTO parseWePayCallbackJson(String requestBody) {
        try {
            // 使用FastJSON解析JSON数据
            com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(requestBody);
            
            WePayCallbackDTO callbackData = new WePayCallbackDTO();
            
            // 直接使用JSON对象，避免字符串转换
            callbackData.setTradeNo(jsonObject.getString("tradeNo"));
            callbackData.setOrderNo(jsonObject.getString("orderNo"));
            callbackData.setOrderAmount(jsonObject.getBigDecimal("orderAmount"));
            callbackData.setAmount(jsonObject.getBigDecimal("amount"));
            callbackData.setPayStatus(jsonObject.getInteger("payStatus"));
            callbackData.setPayTime(jsonObject.getString("payTime"));
            callbackData.setCharge(jsonObject.getBigDecimal("charge"));
            callbackData.setOtherData(jsonObject.getString("otherData"));
            callbackData.setRemark(jsonObject.getString("remark"));
            callbackData.setSign(jsonObject.getString("sign"));
            
            // 直接获取Boolean值，FastJSON会自动处理类型转换
            callbackData.setReverse(jsonObject.getBoolean("reverse"));
            
            logger.info("解析支付回调数据成功 - 订单号: {}, 支付状态: {}", 
                       callbackData.getOrderNo(), callbackData.getPayStatus());
            
            return callbackData;
            
        } catch (Exception e) {
            logger.error("解析WePay支付回调数据失败: {}", e.getMessage(), e);
            return null;
        }
    }
    
    
    /**
     * 验证WePay回调签名
     */
    private boolean verifyWePayCallbackSign(WePayCallbackDTO callbackData) {
        try {
            // 构建签名参数（排除空参数和sign参数）
            Map<String, Object> signParams = new HashMap<>();
            
            // 添加非空参数
            if (callbackData.getTradeNo() != null && !callbackData.getTradeNo().isEmpty()) {
                signParams.put("tradeNo", callbackData.getTradeNo());
            }
            if (callbackData.getOrderNo() != null && !callbackData.getOrderNo().isEmpty()) {
                signParams.put("orderNo", callbackData.getOrderNo());
            }
            if (callbackData.getOrderAmount() != null) {
                signParams.put("orderAmount", callbackData.getOrderAmount());
            }
            if (callbackData.getAmount() != null) {
                signParams.put("amount", callbackData.getAmount());
            }
            if (callbackData.getPayStatus() != null) {
                signParams.put("payStatus", callbackData.getPayStatus());
            }
            if (callbackData.getPayTime() != null && !callbackData.getPayTime().isEmpty()) {
                signParams.put("payTime", callbackData.getPayTime());
            }
            if (callbackData.getCharge() != null) {
                signParams.put("charge", callbackData.getCharge());
            }
            if (callbackData.getOtherData() != null && !callbackData.getOtherData().isEmpty()) {
                signParams.put("otherData", callbackData.getOtherData());
            }
            if (callbackData.getReverse() != null) {
                signParams.put("reverse", callbackData.getReverse());
            }
            if (callbackData.getRemark() != null && !callbackData.getRemark().isEmpty()) {
                signParams.put("remark", callbackData.getRemark());
            }

            ChargeOrderEntity chargeOrder= chargeOrderDao.selectByOrderno(callbackData.getOrderNo());
            if (chargeOrder == null) {
                return false;
            }
            PayMerchantEntity payMerchantEntity = payMerchantDao.selectById(chargeOrder.getMerchantid());
            if (payMerchantEntity == null) {
                return false;
            }
            String secretKey = payMerchantEntity.getChannelkey();
            
            // 生成签名
            String expectedSign = WePaySignatureUtils.generateSign(signParams, secretKey);
            
            // 比较签名
            boolean isValid = expectedSign.equals(callbackData.getSign());
            
            if (!isValid) {
                logger.warn("WePay回调签名验证失败 - 期望签名: {}, 实际签名: {}", expectedSign, callbackData.getSign());
            }
            
            return isValid;
            
        } catch (Exception e) {
            logger.error("WePay回调签名验证异常: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 处理WePay支付结果
     */
    private boolean processWePayPaymentResult(WePayCallbackDTO callbackData) {
        try {
            String orderNo = callbackData.getOrderNo();
            Integer payStatus = callbackData.getPayStatus();
            String tradeNo = callbackData.getTradeNo();
            
            logger.info("处理WePay支付结果 - 订单号: {}, 系统单号: {}, 支付状态: {}", 
                       orderNo, tradeNo, payStatus);
            
            // 根据支付状态处理
            switch (payStatus) {
                case 0:
                    // 订单生成
                    logger.info("订单生成 - 订单号: {}", orderNo);
                    return paymentCallbackService.handleOrderCreated(callbackData);
                    
                case 1:
                    // 支付成功
                    logger.info("支付成功 - 订单号: {}, 实际支付金额: {}, 手续费: {}", 
                               orderNo, callbackData.getAmount(), callbackData.getCharge());
                    return paymentCallbackService.handlePaymentSuccess(callbackData);
                    
                case 2:
                    // 支付失败
                    logger.warn("支付失败 - 订单号: {}, 失败原因: {}", orderNo, callbackData.getRemark());
                    return paymentCallbackService.handlePaymentFailure(callbackData);
                    
                default:
                    logger.warn("未知支付状态 - 订单号: {}, 状态: {}", orderNo, payStatus);
                    return false;
            }


        } catch (Exception e) {
            logger.error("处理WePay支付结果失败: {}", e.getMessage(), e);
            return false;
        }
    }
    
}
