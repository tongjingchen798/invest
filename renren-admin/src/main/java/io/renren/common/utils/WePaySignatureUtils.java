package io.renren.common.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;

/**
 * WePay签名工具类
 * 
 * @author renren
 * @date 2024-01-01
 */
public class WePaySignatureUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(WePaySignatureUtils.class);
    
    /**
     * 生成WePay签名
     * 
     * @param params 参数Map
     * @param secretKey 密钥
     * @return 签名字符串
     */
    public static String generateSign(Map<String, Object> params, String secretKey) {
        try {
            if (params == null || params.isEmpty()) {
                throw new IllegalArgumentException("参数不能为空");
            }
            
            if (secretKey == null || secretKey.trim().isEmpty()) {
                throw new IllegalArgumentException("密钥不能为空");
            }
            
            // 1. 过滤空值参数（空字符串参与签名，null值不参与）
            Map<String, Object> filteredParams = new TreeMap<>();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (entry.getValue() != null) {
                    filteredParams.put(entry.getKey(), entry.getValue());
                }
            }
            
            // 2. 按字典序排序并拼接参数（key1=value1&key2=value2格式）
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, Object> entry : filteredParams.entrySet()) {
                if (!first) {
                    sb.append("&");
                }
                sb.append(entry.getKey()).append("=").append(entry.getValue());
                first = false;
            }
            
            // 3. 添加密钥（&key=私钥）
            sb.append("&key=").append(secretKey);
            
            String signString = sb.toString();
            logger.debug("待签名值: {}", signString);
            
            // 4. MD5加密并转小写
            String sign = DigestUtils.md5Hex(signString).toLowerCase();
            logger.debug("签名结果: {}", sign);
            
            return sign;
            
        } catch (Exception e) {
            logger.error("生成WePay签名失败", e);
            throw new RuntimeException("生成签名失败: " + e.getMessage());
        }
    }
    
    /**
     * 验证WePay签名
     * 
     * @param params 参数Map
     * @param secretKey 密钥
     * @param sign 待验证的签名
     * @return 是否验证通过
     */
    public static boolean verifySign(Map<String, Object> params, String secretKey, String sign) {
        try {
            if (sign == null || sign.trim().isEmpty()) {
                return false;
            }
            
            String expectedSign = generateSign(params, secretKey);
            boolean isValid = expectedSign.equals(sign.toLowerCase());
            
            logger.debug("签名验证结果: {}, 期望签名: {}, 实际签名: {}", 
                        isValid, expectedSign, sign.toLowerCase());
            
            return isValid;
            
        } catch (Exception e) {
            logger.error("验证WePay签名失败", e);
            return false;
        }
    }
}
