package io.renren.controller;

import com.alibaba.fastjson.JSON;
import io.renren.common.utils.Result;
import io.renren.dto.PaymentResponseDTO;
import io.renren.dto.WePayCallbackDTO;
import io.renren.service.WePayPaymentService;
import io.renren.utils.WePaySignatureUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Enumeration;
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
    private WePayPaymentService wePayPaymentService;
    
    @PostMapping("/notify")
    @ApiOperation("WePay支付结果异步通知")
    public String paymentNotify(HttpServletRequest request) {
        try {
            // 获取所有请求参数
            Map<String, String> params = getAllRequestParams(request);
            logger.info("收到WePay支付回调通知: {}", JSON.toJSONString(params));
            
            // 解析WePay回调数据
            WePayCallbackDTO callbackData = parseWePayCallbackData(params);
            
            if (callbackData == null) {
                logger.error("WePay支付回调数据解析失败");
                return "fail";
            }
            
            // 验证WePay签名
            boolean isValid = verifyWePayCallbackSign(callbackData, params);
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
    
    @GetMapping("/callback")
    @ApiOperation("支付成功页面回调")
    public Result<String> paymentCallback(
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) String status) {
        try {
            logger.info("收到支付页面回调 - 订单号: {}, 状态: {}", orderNo, status);
            
            // TODO: 根据订单号查询支付结果并更新订单状态
            
            return new Result<String>().ok("支付成功");
            
        } catch (Exception e) {
            logger.error("处理支付页面回调异常: {}", e.getMessage(), e);
            return new Result<String>().error("处理失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取所有请求参数
     */
    private Map<String, String> getAllRequestParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String paramValue = request.getParameter(paramName);
            params.put(paramName, paramValue);
        }
        
        return params;
    }
    
    /**
     * 解析WePay回调数据
     */
    private WePayCallbackDTO parseWePayCallbackData(Map<String, String> params) {
        try {
            WePayCallbackDTO callbackData = new WePayCallbackDTO();
            
            // 设置基本参数
            callbackData.setTradeNo(params.get("tradeNo"));
            callbackData.setOrderNo(params.get("orderNo"));
            callbackData.setOrderAmount(parseBigDecimal(params.get("orderAmount")));
            callbackData.setAmount(parseBigDecimal(params.get("amount")));
            callbackData.setPayStatus(parseInteger(params.get("payStatus")));
            callbackData.setPayTime(params.get("payTime"));
            callbackData.setCharge(parseBigDecimal(params.get("charge")));
            callbackData.setOtherData(params.get("otherData"));
            callbackData.setRemark(params.get("remark"));
            callbackData.setSign(params.get("sign"));
            
            // 处理reverse字段的特殊转换
            String reverseStr = params.get("reverse");
            if (reverseStr != null) {
                if ("".equals(reverseStr)) {
                    callbackData.setReverse(false);
                } else if ("1".equals(reverseStr)) {
                    callbackData.setReverse(true);
                } else {
                    callbackData.setReverse(Boolean.parseBoolean(reverseStr));
                }
            }
            
            return callbackData;
            
        } catch (Exception e) {
            logger.error("解析WePay支付回调数据失败: {}", e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * 解析BigDecimal
     */
    private BigDecimal parseBigDecimal(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            logger.warn("解析BigDecimal失败: {}", value);
            return null;
        }
    }
    
    /**
     * 解析Integer
     */
    private Integer parseInteger(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.warn("解析Integer失败: {}", value);
            return null;
        }
    }
    
    /**
     * 验证WePay回调签名
     */
    private boolean verifyWePayCallbackSign(WePayCallbackDTO callbackData, Map<String, String> params) {
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
            
            // TODO: 获取商户密钥进行签名验证
            String secretKey = "YOUR_SECRET_KEY"; // 从数据库或配置中获取
            
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
                    break;
                    
                case 1:
                    // 支付成功
                    logger.info("支付成功 - 订单号: {}, 实际支付金额: {}, 手续费: {}", 
                               orderNo, callbackData.getAmount(), callbackData.getCharge());
                    
                    // TODO: 实现支付成功处理逻辑
                    // 1. 根据订单号查询本地订单
                    // 2. 更新订单状态为已支付
                    // 3. 更新用户余额
                    // 4. 记录支付日志
                    // 5. 发送支付成功通知
                    break;
                    
                case 2:
                    // 支付失败
                    logger.warn("支付失败 - 订单号: {}, 失败原因: {}", orderNo, callbackData.getRemark());
                    
                    // TODO: 实现支付失败处理逻辑
                    // 1. 根据订单号查询本地订单
                    // 2. 更新订单状态为支付失败
                    // 3. 记录失败原因
                    // 4. 发送支付失败通知
                    break;
                    
                default:
                    logger.warn("未知支付状态 - 订单号: {}, 状态: {}", orderNo, payStatus);
                    return false;
            }
            
            // 处理反转订单
            if (callbackData.getReverse() != null && callbackData.getReverse()) {
                logger.warn("订单被反转 - 订单号: {}", orderNo);
                // TODO: 实现订单反转处理逻辑
            }
            
            return true;
            
        } catch (Exception e) {
            logger.error("处理WePay支付结果失败: {}", e.getMessage(), e);
            return false;
        }
    }
}
