package io.renren.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 表单数据工具类
 * 
 * @author renren
 * @date 2024-01-01
 */
public class FormDataUtils {
    
    /**
     * 构建表单数据字符串
     * 
     * @param params 参数Map
     * @return 表单数据字符串
     */
    public static String buildFormData(Map<String, Object> params) throws UnsupportedEncodingException {
        StringBuilder formData = new StringBuilder();
        boolean first = true;
        
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (!first) {
                formData.append("&");
            }
            
            String key = URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString());
            String value = entry.getValue() != null ? 
                URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8.toString()) : "";
            
            formData.append(key).append("=").append(value);
            first = false;
        }
        
        return formData.toString();
    }
    
    /**
     * 构建简单的表单数据字符串（不进行URL编码）
     * 
     * @param params 参数Map
     * @return 表单数据字符串
     */
    public static String buildSimpleFormData(Map<String, Object> params) {
        StringBuilder formData = new StringBuilder();
        boolean first = true;
        
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (!first) {
                formData.append("&");
            }
            formData.append(entry.getKey()).append("=").append(entry.getValue());
            first = false;
        }
        
        return formData.toString();
    }
}
