package io.renren.controller;

import io.renren.common.utils.Result;
import io.renren.dto.WePayCallbackDTO;
import io.renren.utils.WePaySignatureUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * WePay支付回调测试控制器
 * 
 * @author renren
 * @date 2024-01-01
 */
@RestController
@RequestMapping("/api/test/payment/callback")
public class PaymentCallbackTestController {
    
    /**
     * 生成测试用的支付回调JSON数据
     */
    @GetMapping("/generate-json")
    public Result<Map<String, Object>> generateCallbackJson(
            @RequestParam String orderNo,
            @RequestParam String tradeNo,
            @RequestParam Integer payStatus,
            @RequestParam(required = false) String remark) {
        
        try {
            // 构建回调数据
            WePayCallbackDTO callbackData = new WePayCallbackDTO();
            callbackData.setTradeNo(tradeNo);
            callbackData.setOrderNo(orderNo);
            callbackData.setOrderAmount(new BigDecimal("100.00"));
            callbackData.setAmount(new BigDecimal("100.00"));
            callbackData.setPayStatus(payStatus);
            callbackData.setPayTime("2024-01-01 12:00:00");
            callbackData.setCharge(new BigDecimal("2.00"));
            callbackData.setOtherData("charge_order_id:123");
            callbackData.setReverse(false);
            callbackData.setRemark(remark != null ? remark : "支付成功");
            
            // 生成签名（使用测试密钥）
            Map<String, Object> signData = new HashMap<>();
            signData.put("tradeNo", callbackData.getTradeNo());
            signData.put("orderNo", callbackData.getOrderNo());
            signData.put("orderAmount", callbackData.getOrderAmount());
            signData.put("amount", callbackData.getAmount());
            signData.put("payStatus", callbackData.getPayStatus());
            signData.put("payTime", callbackData.getPayTime());
            signData.put("charge", callbackData.getCharge());
            signData.put("otherData", callbackData.getOtherData());
            signData.put("reverse", callbackData.getReverse());
            signData.put("remark", callbackData.getRemark());
            
            String sign = WePaySignatureUtils.generateSign(signData, "test_key_123456");
            callbackData.setSign(sign);
            
            // 转换为JSON字符串
            String jsonString = com.alibaba.fastjson.JSON.toJSONString(callbackData);
            
            Map<String, Object> result = new HashMap<>();
            result.put("callbackData", callbackData);
            result.put("jsonString", jsonString);
            result.put("sign", sign);
            
            return new Result<Map<String, Object>>().ok(result);
            
        } catch (Exception e) {
            return new Result<Map<String, Object>>().error("生成回调JSON失败: " + e.getMessage());
        }
    }
    
    /**
     * 测试支付回调接口
     */
    @PostMapping("/test-notify")
    public String testPaymentNotify(@RequestBody String requestBody) {
        try {
            // 这里可以调用实际的回调处理方法进行测试
            return "测试支付回调处理完成: " + requestBody;
        } catch (Exception e) {
            return "测试支付回调处理失败: " + e.getMessage();
        }
    }
    
    /**
     * 生成支付成功的回调JSON
     */
    @GetMapping("/success-json")
    public Result<String> generateSuccessCallbackJson(
            @RequestParam String orderNo,
            @RequestParam String tradeNo) {
        
        Map<String, Object> result = generateCallbackJson(orderNo, tradeNo, 1, "支付成功").getData();
        String jsonString = (String) result.get("jsonString");
        
        return new Result<String>().ok(jsonString);
    }
    
    /**
     * 生成支付失败的回调JSON
     */
    @GetMapping("/failure-json")
    public Result<String> generateFailureCallbackJson(
            @RequestParam String orderNo,
            @RequestParam String tradeNo,
            @RequestParam(required = false) String remark) {
        
        Map<String, Object> result = generateCallbackJson(orderNo, tradeNo, 2, 
            remark != null ? remark : "支付失败").getData();
        String jsonString = (String) result.get("jsonString");
        
        return new Result<String>().ok(jsonString);
    }
}
