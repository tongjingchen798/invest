package io.renren.controller;

import io.renren.dto.WePayCallbackDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * WePay支付回调处理示例
 * 
 * @author renren
 * @date 2024-01-01
 */
@Component
public class WePayCallbackExample {
    
    /**
     * WePay回调JSON数据示例
     */
    public void callbackJsonExample() {
        // 模拟WePay回调JSON数据
        String jsonCallbackData = """
            {
                "tradeNo": "TRADE123456789",
                "orderNo": "ORDER123456789", 
                "orderAmount": 100.00,
                "amount": 100.00,
                "payStatus": 1,
                "payTime": "2024-01-01 12:00:00",
                "charge": 2.00,
                "otherData": "charge_order_id:123",
                "reverse": false,
                "remark": "支付成功",
                "sign": "ABC123DEF456..."
            }
            """;
        
        System.out.println("WePay回调JSON数据示例:");
        System.out.println(jsonCallbackData);
        
        // 解析JSON数据
        WePayCallbackDTO callbackData = parseJsonCallback(jsonCallbackData);
        
        // 处理回调数据
        processCallbackData(callbackData);
    }
    
    /**
     * 解析JSON回调数据（推荐方式：直接使用JSON对象）
     */
    private WePayCallbackDTO parseJsonCallback(String jsonData) {
        // 使用FastJSON解析
        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(jsonData);
        
        WePayCallbackDTO callbackData = new WePayCallbackDTO();
        
        // 直接使用JSON对象的方法，避免字符串转换
        callbackData.setTradeNo(jsonObject.getString("tradeNo"));
        callbackData.setOrderNo(jsonObject.getString("orderNo"));
        callbackData.setOrderAmount(jsonObject.getBigDecimal("orderAmount"));  // 直接获取BigDecimal
        callbackData.setAmount(jsonObject.getBigDecimal("amount"));            // 直接获取BigDecimal
        callbackData.setPayStatus(jsonObject.getInteger("payStatus"));         // 直接获取Integer
        callbackData.setPayTime(jsonObject.getString("payTime"));
        callbackData.setCharge(jsonObject.getBigDecimal("charge"));            // 直接获取BigDecimal
        callbackData.setOtherData(jsonObject.getString("otherData"));
        callbackData.setReverse(jsonObject.getBoolean("reverse"));             // 直接获取Boolean
        callbackData.setRemark(jsonObject.getString("remark"));
        callbackData.setSign(jsonObject.getString("sign"));
        
        return callbackData;
    }
    
    /**
     * 处理回调数据
     */
    private void processCallbackData(WePayCallbackDTO callbackData) {
        System.out.println("处理回调数据:");
        System.out.println("订单号: " + callbackData.getOrderNo());
        System.out.println("系统单号: " + callbackData.getTradeNo());
        System.out.println("支付状态: " + callbackData.getPayStatus());
        System.out.println("支付金额: " + callbackData.getAmount());
    }
    
    /**
     * WePay回调参数示例（兼容旧版本）
     */
    public void callbackParameterExample() {
        // 模拟WePay回调参数
        WePayCallbackDTO callbackData = new WePayCallbackDTO();
        
        // 基本参数
        callbackData.setTradeNo("TRADE123456789"); // 系统订单号
        callbackData.setOrderNo("ORDER123456789"); // 商户订单号
        callbackData.setOrderAmount(new BigDecimal("100.00")); // 订单金额
        callbackData.setAmount(new BigDecimal("100.00")); // 实际支付金额
        callbackData.setPayStatus(1); // 支付状态: 1-支付成功
        callbackData.setPayTime("2024-01-01 12:00:00"); // 支付时间
        callbackData.setCharge(new BigDecimal("2.00")); // 手续费
        callbackData.setOtherData("user_id:12345"); // 扩展字段
        callbackData.setReverse(false); // 是否反转订单
        callbackData.setRemark(""); // 付款失败备注
        callbackData.setSign("ABC123DEF456"); // 签名值
        
        // 处理不同支付状态
        switch (callbackData.getPayStatus()) {
            case 0:
                System.out.println("订单生成状态");
                break;
            case 1:
                System.out.println("支付成功 - 金额: " + callbackData.getAmount());
                break;
            case 2:
                System.out.println("支付失败 - 原因: " + callbackData.getRemark());
                break;
        }
    }
    
    /**
     * 回调URL配置示例
     */
    public void callbackUrlExample() {
        // 异步通知回调地址
        String notifyUrl = "http://206.238.68.208:8082/api/payment/notify";
        
        // 页面回调地址（可选）
        String callbackUrl = "http://206.238.68.208:8082/api/payment/callback";
        
        System.out.println("异步通知地址: " + notifyUrl);
        System.out.println("页面回调地址: " + callbackUrl);
    }
    
    /**
     * 签名验证示例
     */
    public void signatureVerificationExample() {
        // 模拟回调参数
        String tradeNo = "TRADE123456789";
        String orderNo = "ORDER123456789";
        String orderAmount = "100.00";
        String amount = "100.00";
        String payStatus = "1";
        String payTime = "2024-01-01 12:00:00";
        String charge = "2.00";
        String otherData = "user_id:12345";
        String reverse = "false";
        String remark = "";
        String sign = "ABC123DEF456";
        
        // 构建签名参数（排除空参数和sign参数）
        StringBuilder signStr = new StringBuilder();
        signStr.append("amount=").append(amount).append("&");
        signStr.append("charge=").append(charge).append("&");
        signStr.append("orderAmount=").append(orderAmount).append("&");
        signStr.append("orderNo=").append(orderNo).append("&");
        signStr.append("otherData=").append(otherData).append("&");
        signStr.append("payStatus=").append(payStatus).append("&");
        signStr.append("payTime=").append(payTime).append("&");
        signStr.append("reverse=").append(reverse).append("&");
        signStr.append("tradeNo=").append(tradeNo).append("&");
        signStr.append("key=").append("YOUR_SECRET_KEY");
        
        System.out.println("签名字符串: " + signStr.toString());
        System.out.println("期望签名: " + sign);
    }
}
