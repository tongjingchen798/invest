package io.renren.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import io.renren.dao.SmsMerchantConfigDao;
import io.renren.entity.SmsMerchantConfigEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

/**
 * 颂量ITNIO短信工具类
 * 
 * @author lip
 * @date 2024-01-01
 */
@Component
public class SmsUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(SmsUtils.class);
    
    @Autowired
    private SmsMerchantConfigDao smsMerchantConfigDao;
    
    /**
     * 短信配置缓存
     */
    private SmsMerchantConfigEntity smsConfig;
    
    /**
     * 配置缓存时间戳
     */
    private long configCacheTime = 0;
    
    /**
     * 配置缓存有效期（5分钟）
     */
    private static final long CACHE_VALID_TIME = 5 * 60 * 1000;
    
    /**
     * 获取短信配置
     * 
     * @return 短信配置
     */
    private SmsMerchantConfigEntity getSmsConfig() {
        long currentTime = System.currentTimeMillis();
        
        // 检查缓存是否有效
        if (smsConfig != null && (currentTime - configCacheTime) < CACHE_VALID_TIME) {
            return smsConfig;
        }
        
        try {
            // 从数据库获取启用的配置
            smsConfig = smsMerchantConfigDao.selectByCaptchaName();
            configCacheTime = currentTime;
            
            if (smsConfig == null) {
                logger.error("未找到启用的短信配置");
                return null;
            }
            
            logger.info("获取短信配置成功 - 商户: {}, 商户ID: {}", 
                       smsConfig.getCaptchaName(), smsConfig.getCaptchaId());
            
        } catch (Exception e) {
            logger.error("获取短信配置失败", e);
            return null;
        }
        
        return smsConfig;
    }
    
    /**
     * 发送短信
     * 
     * @param mobile 手机号码
     * @param content 短信内容
     * @return 发送结果
     */
    public SmsResult sendSms(String mobile, String content) {
        return sendSms(mobile, content, null);
    }
    
    /**
     * 发送短信
     * 
     * @param mobile 手机号码
     * @param content 短信内容
     * @param templateId 模板ID（可选）
     * @return 发送结果
     */
    public SmsResult sendSms(String mobile, String content, String templateId) {
        try {
            // 获取短信配置
            SmsMerchantConfigEntity config = getSmsConfig();
            if (config == null) {
                return SmsResult.fail("短信配置未找到或未启用");
            }
            
            // 构建请求参数
            String baseUrl = "https://api.itniotech.com/sms";
            String url = baseUrl.concat("/sendSms");
            
            // 生成时间戳（秒）
            String datetime = String.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond());
            
            // 生成签名：MD5(apiKey + apiPwd + timestamp)
            String sign = SecureUtil.md5(config.getCaptchaKey().concat(config.getApiSecret()).concat(datetime));
            
            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("appId", config.getCaptchaNo()); // 使用商户编号作为appId
            requestBody.put("numbers", mobile);
            requestBody.put("content", content);
            requestBody.put("senderId", config.getCaptchaName()); // 使用商户名称作为senderId
            
            // 发送HTTP请求
            HttpResponse response = HttpRequest.post(url)
                    .header(Header.CONNECTION, "Keep-Alive")
                    .header(Header.CONTENT_TYPE, "application/json;charset=UTF-8")
                    .header("Sign", sign)
                    .header("Timestamp", datetime)
                    .header("Api-Key", config.getCaptchaKey())
                    .body(JSONUtil.toJsonStr(requestBody))
                    .execute();
            
            if (response.isOk()) {
                String result = response.body();
                logger.info("短信发送成功 - 手机号: {}, 响应: {}", mobile, result);
                return parseResponse(result);
            } else {
                logger.error("短信发送失败 - 手机号: {}, 状态码: {}, 响应: {}", mobile, response.getStatus(), response.body());
                return SmsResult.fail("短信发送失败，状态码: " + response.getStatus());
            }
            
        } catch (Exception e) {
            logger.error("发送短信失败 - 手机号: {}, 内容: {}, 错误: {}", mobile, content, e.getMessage(), e);
            return SmsResult.fail("发送短信异常: " + e.getMessage());
        }
    }

    /**
     * 解析响应结果
     * 
     * @param response 响应内容
     * @return 发送结果
     */
    private SmsResult parseResponse(String response) {
        try {
            if (StrUtil.isBlank(response)) {
                return SmsResult.fail("响应内容为空");
            }
            
            // 使用Hutool的JSON解析
            Map<String, Object> jsonResponse = JSONUtil.toBean(response, Map.class);
            
            // 根据颂量ITNIO的响应格式解析
            Object codeObj = jsonResponse.get("code");
            Object messageObj = jsonResponse.get("message");
            Object msgIdObj = jsonResponse.get("msgId");
            
            int code = 0;
            if (codeObj != null) {
                code = Integer.parseInt(codeObj.toString());
            }
            
            String message = messageObj != null ? messageObj.toString() : "未知错误";
            String msgId = msgIdObj != null ? msgIdObj.toString() : null;
            
            if (code == 0 || code == 200) { // 成功状态码
                return SmsResult.success(message, msgId);
            } else {
                return SmsResult.fail(message);
            }
            
        } catch (Exception e) {
            logger.error("解析响应失败 - 响应内容: {}, 错误: {}", response, e.getMessage());
            return SmsResult.fail("解析响应失败: " + e.getMessage());
        }
    }
    
    /**
     * 短信发送结果
     */
    public static class SmsResult {
        private boolean success;
        private String message;
        private String msgId;
        
        private SmsResult(boolean success, String message, String msgId) {
            this.success = success;
            this.message = message;
            this.msgId = msgId;
        }
        
        public static SmsResult success(String message, String msgId) {
            return new SmsResult(true, message, msgId);
        }
        
        public static SmsResult success(String message) {
            return new SmsResult(true, message, null);
        }
        
        public static SmsResult fail(String message) {
            return new SmsResult(false, message, null);
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
        
        public String getMsgId() {
            return msgId;
        }
        
        @Override
        public String toString() {
            return "SmsResult{" +
                    "success=" + success +
                    ", message='" + message + '\'' +
                    ", msgId='" + msgId + '\'' +
                    '}';
        }
    }
}
