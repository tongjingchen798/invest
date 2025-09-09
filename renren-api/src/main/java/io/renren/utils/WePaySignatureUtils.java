package io.renren.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * WEPAY签名工具类
 */
@Slf4j
public class WePaySignatureUtils {

    /**
     * 生成签名
     *
     * @param params 参数Map
     * @param key    私钥
     * @return 签名
     */
    public static String generateSign(Map<String, Object> params, String key) {
        try {
            // 第一步：过滤空值并排序
            String stringA = params.entrySet().stream()
                    .filter(item -> item.getValue() != null)
                    .sorted(Map.Entry.comparingByKey())
                    .map(item -> String.format("%s=%s", item.getKey(), item.getValue()))
                    .collect(Collectors.joining("&"));

            // 第二步：拼接key
            String stringSignTemp = stringA + "&key=" + key;
            log.info("待签名值：{}", stringSignTemp);
            // 第三步：MD5加密并转小写
            String signValue = DigestUtil.md5Hex(stringSignTemp).toLowerCase();

            log.info("签名结果：{}", signValue);

            return signValue;
        } catch (Exception e) {
            log.error("生成签名失败", e);
            return "";
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
