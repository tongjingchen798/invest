package io.renren.common.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.TreeMap;

/**
 * QePay签名工具类
 * 
 * @author renren
 * @date 2024-01-01
 */
@Slf4j
public class QePaySignatureUtils {

    /**
     * 生成QePay签名（官方规则：ASCII排序）
     * 
     * 签名规则（官方文档）：
     * 1. 将所有需要签名的字段按照ASCII码从小到大进行排序
     * 2. 按照k=v&k=v的格式拼接字符串
     * 3. 在字符串后面拼接商户私钥用&key=x进行拼接，生成待签名queryString字符串
     * 4. 对生成的queryString字符串进行MD5签名，得到小写签名串
     * 5. 除了sign和sign_type以外不为空的参数都需要参与签名
     *
     * @param params 参数Map
     * @param key    私钥
     * @return 签名
     */
    public static String generateSign(Map<String, Object> params, String key) {
        try {
            // 使用TreeMap进行ASCII排序
            Map<String, Object> sortedMap = new TreeMap<>();
            sortedMap.putAll(params);
            
            StringBuilder signStr = new StringBuilder();
            for (Map.Entry<String, Object> entry : sortedMap.entrySet()) {
                if (entry.getValue() != null && !StrUtil.isBlank(String.valueOf(entry.getValue()))) {
                    // 排除sign和sign_type字段
                    if (!"sign".equals(entry.getKey()) && !"sign_type".equals(entry.getKey()) && !"signType".equals(entry.getKey())) {
                        signStr.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                    }
                }
            }
            
            // 移除末尾的&号
            String stringA = signStr.toString();
            if (stringA.endsWith("&")) {
                stringA = stringA.substring(0, stringA.length() - 1);
            }
            
            // 拼接key
            String stringSignTemp = stringA + "&key=" + key;

            log.info("QePay待签名值：{}", stringSignTemp);

            // MD5加密
            String signValue = DigestUtil.md5Hex(stringSignTemp).toLowerCase();

            log.info("QePay签名结果：{}", signValue);

            return signValue;
        } catch (Exception e) {
            log.error("生成QePay签名失败", e);
            throw new RuntimeException("生成QePay签名失败", e);
        }
    }

    /**
     * 生成签名（从JSON对象）
     *
     * @param jsonObject JSON对象
     * @param key        私钥
     * @return 签名
     */
    public static String generateSign(JSONObject jsonObject, String key) {
        // 移除sign字段
        jsonObject.remove("sign");
        jsonObject.remove("sign_type");
        jsonObject.remove("signType");
        
        // 转换为Map
        Map<String, Object> params = jsonObject.getInnerMap();
        
        return generateSign(params, key);
    }

    /**
     * 验证签名
     *
     * @param params 参数Map
     * @param key    私钥
     * @param sign   待验证的签名
     * @return 是否验证通过
     */
    public static boolean verifySign(Map<String, Object> params, String key, String sign) {
        if (StrUtil.isBlank(sign)) {
            return false;
        }
        
        String generatedSign = generateSign(params, key);
        return sign.equals(generatedSign);
    }

    /**
     * 验证签名（从JSON对象）
     *
     * @param jsonObject JSON对象
     * @param key        私钥
     * @param sign       待验证的签名
     * @return 是否验证通过
     */
    public static boolean verifySign(JSONObject jsonObject, String key, String sign) {
        if (StrUtil.isBlank(sign)) {
            return false;
        }
        
        String generatedSign = generateSign(jsonObject, key);
        return sign.equals(generatedSign);
    }

    /**
     * 验证签名（从JSON字符串）
     *
     * @param jsonString JSON字符串
     * @param key        私钥
     * @param sign       待验证的签名
     * @return 是否验证通过
     */
    public static boolean verifySign(String jsonString, String key, String sign) {
        try {
            JSONObject jsonObject = JSON.parseObject(jsonString);
            return verifySign(jsonObject, key, sign);
        } catch (Exception e) {
            log.error("解析JSON失败", e);
            return false;
        }
    }
}
