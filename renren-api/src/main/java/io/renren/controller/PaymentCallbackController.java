package io.renren.controller;

import io.renren.dao.ChargeOrderDao;
import io.renren.dao.PayMerchantDao;
import io.renren.dto.WePayCallbackDTO;
import io.renren.dto.QePayCallbackDTO;
import io.renren.entity.ChargeOrderEntity;
import io.renren.entity.PayMerchantEntity;
import io.renren.service.PaymentCallbackService;
import io.renren.utils.WePaySignatureUtils;
import io.renren.utils.QePaySignatureUtils;
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
    
    @PostMapping(value = "/qePay/notify", consumes = "application/x-www-form-urlencoded")
    @ApiOperation("QePay支付结果异步通知")
    public String qePayNotify(@RequestParam Map<String, String> params) {
        try {
            logger.info("收到QePay支付回调通知");
            logger.info("QePay支付回调参数: {}", params);
            
            // 解析QePay回调form数据
            QePayCallbackDTO callbackData = parseQePayCallbackForm(params);
            
            if (callbackData == null) {
                logger.error("QePay支付回调form数据解析失败");
                return "fail";
            }
            
            // 验证QePay签名
            boolean isValid = verifyQePayCallbackSign(callbackData);
            if (!isValid) {
                logger.error("QePay支付回调签名验证失败 - 订单号: {}", callbackData.getOrderNo());
                return "fail";
            }
            
            // 处理支付结果
            boolean success = processQePayPaymentResult(callbackData);
            
            if (success) {
                logger.info("QePay支付回调处理成功 - 商家订单号: {}, 平台订单号: {}, 订单状态: {}", 
                           callbackData.getMchOrderNo(), callbackData.getOrderNo(), callbackData.getTradeResult());
                return "success";
            } else {
                logger.error("QePay支付回调处理失败 - 商家订单号: {}", callbackData.getMchOrderNo());
                return "fail";
            }
            
        } catch (Exception e) {
            logger.error("处理QePay支付回调异常: {}", e.getMessage(), e);
            return "fail";
        }
    }
    
    /**
     * 解析QePay支付回调form数据
     */
    private QePayCallbackDTO parseQePayCallbackForm(Map<String, String> params) {
        try {
            QePayCallbackDTO callbackData = new QePayCallbackDTO();
            
            // 从form参数中提取数据（根据QePay异步通知参数）
            callbackData.setTradeResult(params.get("tradeResult")); // 订单状态
            callbackData.setMchId(params.get("mchId")); // 商户号
            callbackData.setMchOrderNo(params.get("mchOrderNo")); // 商家订单号
            callbackData.setOriAmount(params.get("oriAmount")); // 原始订单金额
            callbackData.setAmount(params.get("amount")); // 交易金额（实际支付金额）
            callbackData.setOrderDate(params.get("orderDate")); // 订单时间
            callbackData.setOrderNo(params.get("orderNo")); // 平台支付订单号
            callbackData.setMerRetMsg(params.get("merRetMsg")); // 透传参数
            callbackData.setSignType(params.get("signType")); // 签名方式
            callbackData.setSign(params.get("sign")); // 签名
            
            logger.info("解析QePay支付回调数据成功 - 商家订单号: {}, 订单状态: {}, 平台订单号: {}", 
                       callbackData.getMchOrderNo(), callbackData.getTradeResult(), callbackData.getOrderNo());
            
            return callbackData;
            
        } catch (Exception e) {
            logger.error("解析QePay支付回调数据失败: {}", e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * 验证QePay回调签名
     */
    private boolean verifyQePayCallbackSign(QePayCallbackDTO callbackData) {
        try {
            // 构建签名参数（排除空参数和sign、signType参数）
            Map<String, Object> signParams = new HashMap<>();
            
            // 添加非空参数（根据QePay异步通知参数）
            if (callbackData.getTradeResult() != null && !callbackData.getTradeResult().isEmpty()) {
                signParams.put("tradeResult", callbackData.getTradeResult());
            }
            if (callbackData.getMchId() != null && !callbackData.getMchId().isEmpty()) {
                signParams.put("mchId", callbackData.getMchId());
            }
            if (callbackData.getMchOrderNo() != null && !callbackData.getMchOrderNo().isEmpty()) {
                signParams.put("mchOrderNo", callbackData.getMchOrderNo());
            }
            if (callbackData.getOriAmount() != null && !callbackData.getOriAmount().isEmpty()) {
                signParams.put("oriAmount", callbackData.getOriAmount());
            }
            if (callbackData.getAmount() != null && !callbackData.getAmount().isEmpty()) {
                signParams.put("amount", callbackData.getAmount());
            }
            if (callbackData.getOrderDate() != null && !callbackData.getOrderDate().isEmpty()) {
                signParams.put("orderDate", callbackData.getOrderDate());
            }
            if (callbackData.getOrderNo() != null && !callbackData.getOrderNo().isEmpty()) {
                signParams.put("orderNo", callbackData.getOrderNo());
            }
            // merRetMsg只有在提交时才参与签名
            if (callbackData.getMerRetMsg() != null && !callbackData.getMerRetMsg().isEmpty()) {
                signParams.put("merRetMsg", callbackData.getMerRetMsg());
            }

            ChargeOrderEntity chargeOrder = chargeOrderDao.selectByOrderno(callbackData.getMchOrderNo());
            if (chargeOrder == null) {
                return false;
            }
            PayMerchantEntity payMerchantEntity = payMerchantDao.selectById(chargeOrder.getMerchantid());
            if (payMerchantEntity == null) {
                return false;
            }
            String secretKey = payMerchantEntity.getChannelkey();
            
            // 使用QePay签名验证
            boolean isValid = QePaySignatureUtils.verifyCallbackSign(signParams, secretKey, callbackData.getSign());
            
            if (!isValid) {
                logger.warn("QePay回调签名验证失败 - 期望签名: {}, 实际签名: {}", 
                           QePaySignatureUtils.generateSign(signParams, secretKey), callbackData.getSign());
            }
            
            return isValid;
            
        } catch (Exception e) {
            logger.error("QePay回调签名验证异常: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 处理QePay支付结果
     */
    private boolean processQePayPaymentResult(QePayCallbackDTO callbackData) {
        try {
            String mchOrderNo = callbackData.getMchOrderNo();
            String tradeResult = callbackData.getTradeResult();
            String orderNo = callbackData.getOrderNo();
            
            logger.info("处理QePay支付结果 - 商家订单号: {}, 平台订单号: {}, 订单状态: {}", 
                       mchOrderNo, orderNo, tradeResult);
            
            // 根据订单状态处理（QePay只有支付成功状态为"1"）
            if ("1".equals(tradeResult)) {
                // 支付成功
                logger.info("支付成功 - 商家订单号: {}, 平台订单号: {}, 原始金额: {}, 实际支付金额: {}", 
                           mchOrderNo, orderNo, callbackData.getOriAmount(), callbackData.getAmount());
                return paymentCallbackService.handlePaymentSuccess(convertToWePayCallback(callbackData));
            } else {
                // 其他状态（非支付成功）
                logger.warn("订单状态异常 - 商家订单号: {}, 平台订单号: {}, 状态: {}", 
                           mchOrderNo, orderNo, tradeResult);
                return paymentCallbackService.handlePaymentFailure(convertToWePayCallback(callbackData));
            }

        } catch (Exception e) {
            logger.error("处理QePay支付结果失败: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 将QePayCallbackDTO转换为WePayCallbackDTO，复用现有的处理逻辑
     */
    private WePayCallbackDTO convertToWePayCallback(QePayCallbackDTO qePayCallback) {
        WePayCallbackDTO wePayCallback = new WePayCallbackDTO();
        
        // 映射字段
        wePayCallback.setTradeNo(qePayCallback.getOrderNo()); // 平台支付订单号 -> 系统订单号
        wePayCallback.setOrderNo(qePayCallback.getMchOrderNo()); // 商家订单号 -> 商户订单号
        wePayCallback.setOrderAmount(qePayCallback.getOriAmount() != null ? 
            new java.math.BigDecimal(qePayCallback.getOriAmount()) : null); // 原始订单金额
        wePayCallback.setAmount(qePayCallback.getAmount() != null ? 
            new java.math.BigDecimal(qePayCallback.getAmount()) : null); // 实际支付金额
        
        // 根据tradeResult设置支付状态
        if ("1".equals(qePayCallback.getTradeResult())) {
            wePayCallback.setPayStatus(1); // 支付成功
        } else {
            wePayCallback.setPayStatus(2); // 支付失败
        }
        
        wePayCallback.setPayTime(qePayCallback.getOrderDate()); // 订单时间 -> 支付时间
        wePayCallback.setCharge(null); // QePay回调中没有手续费信息
        wePayCallback.setOtherData(qePayCallback.getMerRetMsg()); // 透传参数
        wePayCallback.setReverse(null); // QePay回调中没有反转信息
        wePayCallback.setRemark(null); // QePay回调中没有备注信息
        wePayCallback.setSign(qePayCallback.getSign()); // 签名
        
        return wePayCallback;
    }
    
}
