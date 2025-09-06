package io.renren.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * HTTP工具类
 * 
 * @author renren
 * @date 2024-01-01
 */
public class HttpUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);
    
    /**
     * 发送POST JSON请求
     * 
     * @param url 请求URL
     * @param jsonBody JSON请求体
     * @param headers 请求头
     * @return 响应体
     */
    public static String postJson(String url, String jsonBody, Map<String, String> headers) {
        HttpURLConnection connection = null;
        try {
            // 创建连接
            URL requestUrl = new URL(url);
            connection = (HttpURLConnection) requestUrl.openConnection();
            
            // 设置请求方法和属性
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setConnectTimeout(30000); // 30秒连接超时
            connection.setReadTimeout(60000);    // 60秒读取超时
            
            // 设置请求头
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            
            // 发送请求体
            if (jsonBody != null && !jsonBody.isEmpty()) {
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }
            }
            
            // 获取响应
            int responseCode = connection.getResponseCode();
            logger.debug("HTTP响应码: {}", responseCode);
            
            // 读取响应体
            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                        responseCode >= 200 && responseCode < 300 ? 
                        connection.getInputStream() : 
                        connection.getErrorStream(), 
                        StandardCharsets.UTF_8))) {
                
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
            
            String responseBody = response.toString();
            logger.debug("HTTP响应体: {}", responseBody);
            
            return responseBody;
            
        } catch (Exception e) {
            logger.error("HTTP请求失败 - URL: {}, 错误: {}", url, e.getMessage(), e);
            throw new RuntimeException("HTTP请求失败: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    
    /**
     * 发送POST JSON请求（无请求头）
     * 
     * @param url 请求URL
     * @param jsonBody JSON请求体
     * @return 响应体
     */
    public static String postJson(String url, String jsonBody) {
        return postJson(url, jsonBody, null);
    }
    
    /**
     * 发送GET请求
     * 
     * @param url 请求URL
     * @param headers 请求头
     * @return 响应体
     */
    public static String get(String url, Map<String, String> headers) {
        HttpURLConnection connection = null;
        try {
            // 创建连接
            URL requestUrl = new URL(url);
            connection = (HttpURLConnection) requestUrl.openConnection();
            
            // 设置请求方法和属性
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setConnectTimeout(30000); // 30秒连接超时
            connection.setReadTimeout(60000);    // 60秒读取超时
            
            // 设置请求头
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            
            // 获取响应
            int responseCode = connection.getResponseCode();
            logger.debug("HTTP响应码: {}", responseCode);
            
            // 读取响应体
            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                        responseCode >= 200 && responseCode < 300 ? 
                        connection.getInputStream() : 
                        connection.getErrorStream(), 
                        StandardCharsets.UTF_8))) {
                
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
            
            String responseBody = response.toString();
            logger.debug("HTTP响应体: {}", responseBody);
            
            return responseBody;
            
        } catch (Exception e) {
            logger.error("HTTP请求失败 - URL: {}, 错误: {}", url, e.getMessage(), e);
            throw new RuntimeException("HTTP请求失败: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    
    /**
     * 发送GET请求（无请求头）
     * 
     * @param url 请求URL
     * @return 响应体
     */
    public static String get(String url) {
        return get(url, null);
    }
}
