package io.renren.utils;

import com.alibaba.fastjson.JSON;
import io.renren.dto.HttpResponse;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * OkHttp请求工具类
 * 
 * @author renren
 * @date 2024-01-01
 */
@Component
public class OkHttpUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(OkHttpUtil.class);
    
    private static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType FORM_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    
    private OkHttpClient client;
    
    @PostConstruct
    public void init() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }
    
    /**
     * GET请求
     * 
     * @param url 请求URL
     * @return HttpResponse
     */
    public HttpResponse<String> get(String url) {
        return get(url, null);
    }
    
    /**
     * GET请求
     * 
     * @param url 请求URL
     * @param headers 请求头
     * @return HttpResponse
     */
    public HttpResponse<String> get(String url, Map<String, String> headers) {
        return get(url, headers, null);
    }
    
    /**
     * GET请求
     * 
     * @param url 请求URL
     * @param headers 请求头
     * @param params 请求参数
     * @return HttpResponse
     */
    public HttpResponse<String> get(String url, Map<String, String> headers, Map<String, Object> params) {
        long startTime = System.currentTimeMillis();
        
        try {
            // 构建URL参数
            if (params != null && !params.isEmpty()) {
                url = buildUrlWithParams(url, params);
            }
            
            Request.Builder requestBuilder = new Request.Builder().url(url);
            
            // 添加请求头
            if (headers != null) {
                headers.forEach(requestBuilder::addHeader);
            }
            
            Request request = requestBuilder.build();
            
            try (Response response = client.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";
                long costTime = System.currentTimeMillis() - startTime;
                
                HttpResponse<String> httpResponse = new HttpResponse<>(
                    response.code(),
                    response.message(),
                    responseBody,
                    response.isSuccessful()
                );
                httpResponse.setHeaders(response.headers().toString());
                httpResponse.setCostTime(costTime);
                
                return httpResponse;
            }
        } catch (IOException e) {
            logger.error("GET请求失败: {}", e.getMessage(), e);
            long costTime = System.currentTimeMillis() - startTime;
            HttpResponse<String> errorResponse = HttpResponse.error("请求失败: " + e.getMessage());
            errorResponse.setCostTime(costTime);
            return errorResponse;
        }
    }
    
    /**
     * POST请求（JSON）
     * 
     * @param url 请求URL
     * @param jsonData JSON数据
     * @return HttpResponse
     */
    public HttpResponse<String> postJson(String url, String jsonData) {
        return postJson(url, jsonData, null);
    }
    
    /**
     * POST请求（JSON）
     * 
     * @param url 请求URL
     * @param jsonData JSON数据
     * @param headers 请求头
     * @return HttpResponse
     */
    public HttpResponse<String> postJson(String url, String jsonData, Map<String, String> headers) {
        long startTime = System.currentTimeMillis();
        
        try {
            RequestBody body = RequestBody.create(jsonData, JSON_TYPE);
            Request.Builder requestBuilder = new Request.Builder()
                    .url(url)
                    .post(body);
            
            // 添加请求头
            if (headers != null) {
                headers.forEach(requestBuilder::addHeader);
            }
            
            Request request = requestBuilder.build();
            
            try (Response response = client.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";
                long costTime = System.currentTimeMillis() - startTime;
                
                HttpResponse<String> httpResponse = new HttpResponse<>(
                    response.code(),
                    response.message(),
                    responseBody,
                    response.isSuccessful()
                );
                httpResponse.setHeaders(response.headers().toString());
                httpResponse.setCostTime(costTime);
                
                return httpResponse;
            }
        } catch (IOException e) {
            logger.error("POST JSON请求失败: {}", e.getMessage(), e);
            long costTime = System.currentTimeMillis() - startTime;
            HttpResponse<String> errorResponse = HttpResponse.error("请求失败: " + e.getMessage());
            errorResponse.setCostTime(costTime);
            return errorResponse;
        }
    }
    
    /**
     * POST请求（对象转JSON）
     * 
     * @param url 请求URL
     * @param obj 对象
     * @return HttpResponse
     */
    public HttpResponse<String> postJson(String url, Object obj) {
        return postJson(url, obj, null);
    }
    
    /**
     * POST请求（对象转JSON）
     * 
     * @param url 请求URL
     * @param obj 对象
     * @param headers 请求头
     * @return HttpResponse
     */
    public HttpResponse<String> postJson(String url, Object obj, Map<String, String> headers) {
        String jsonData = JSON.toJSONString(obj);
        return postJson(url, jsonData, headers);
    }
    
    /**
     * POST请求（表单）
     * 
     * @param url 请求URL
     * @param params 表单参数
     * @return HttpResponse
     */
    public HttpResponse<String> postForm(String url, Map<String, Object> params) {
        return postForm(url, params, null);
    }
    
    /**
     * POST请求（表单）
     * 
     * @param url 请求URL
     * @param params 表单参数
     * @param headers 请求头
     * @return HttpResponse
     */
    public HttpResponse<String> postForm(String url, Map<String, Object> params, Map<String, String> headers) {
        long startTime = System.currentTimeMillis();
        
        try {
            FormBody.Builder formBuilder = new FormBody.Builder();
            if (params != null) {
                params.forEach((key, value) -> formBuilder.add(key, String.valueOf(value)));
            }
            
            RequestBody body = formBuilder.build();
            Request.Builder requestBuilder = new Request.Builder()
                    .url(url)
                    .post(body);
            
            // 添加请求头
            if (headers != null) {
                headers.forEach(requestBuilder::addHeader);
            }
            
            Request request = requestBuilder.build();
            
            try (Response response = client.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";
                long costTime = System.currentTimeMillis() - startTime;
                
                HttpResponse<String> httpResponse = new HttpResponse<>(
                    response.code(),
                    response.message(),
                    responseBody,
                    response.isSuccessful()
                );
                httpResponse.setHeaders(response.headers().toString());
                httpResponse.setCostTime(costTime);
                
                return httpResponse;
            }
        } catch (IOException e) {
            logger.error("POST Form请求失败: {}", e.getMessage(), e);
            long costTime = System.currentTimeMillis() - startTime;
            HttpResponse<String> errorResponse = HttpResponse.error("请求失败: " + e.getMessage());
            errorResponse.setCostTime(costTime);
            return errorResponse;
        }
    }
    
    /**
     * PUT请求（JSON）
     * 
     * @param url 请求URL
     * @param jsonData JSON数据
     * @return HttpResponse
     */
    public HttpResponse<String> putJson(String url, String jsonData) {
        return putJson(url, jsonData, null);
    }
    
    /**
     * PUT请求（JSON）
     * 
     * @param url 请求URL
     * @param jsonData JSON数据
     * @param headers 请求头
     * @return HttpResponse
     */
    public HttpResponse<String> putJson(String url, String jsonData, Map<String, String> headers) {
        long startTime = System.currentTimeMillis();
        
        try {
            RequestBody body = RequestBody.create(jsonData, JSON_TYPE);
            Request.Builder requestBuilder = new Request.Builder()
                    .url(url)
                    .put(body);
            
            // 添加请求头
            if (headers != null) {
                headers.forEach(requestBuilder::addHeader);
            }
            
            Request request = requestBuilder.build();
            
            try (Response response = client.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";
                long costTime = System.currentTimeMillis() - startTime;
                
                HttpResponse<String> httpResponse = new HttpResponse<>(
                    response.code(),
                    response.message(),
                    responseBody,
                    response.isSuccessful()
                );
                httpResponse.setHeaders(response.headers().toString());
                httpResponse.setCostTime(costTime);
                
                return httpResponse;
            }
        } catch (IOException e) {
            logger.error("PUT JSON请求失败: {}", e.getMessage(), e);
            long costTime = System.currentTimeMillis() - startTime;
            HttpResponse<String> errorResponse = HttpResponse.error("请求失败: " + e.getMessage());
            errorResponse.setCostTime(costTime);
            return errorResponse;
        }
    }
    
    /**
     * PUT请求（对象转JSON）
     * 
     * @param url 请求URL
     * @param obj 对象
     * @return HttpResponse
     */
    public HttpResponse<String> putJson(String url, Object obj) {
        return putJson(url, obj, null);
    }
    
    /**
     * PUT请求（对象转JSON）
     * 
     * @param url 请求URL
     * @param obj 对象
     * @param headers 请求头
     * @return HttpResponse
     */
    public HttpResponse<String> putJson(String url, Object obj, Map<String, String> headers) {
        String jsonData = JSON.toJSONString(obj);
        return putJson(url, jsonData, headers);
    }
    
    /**
     * DELETE请求
     * 
     * @param url 请求URL
     * @return HttpResponse
     */
    public HttpResponse<String> delete(String url) {
        return delete(url, null);
    }
    
    /**
     * DELETE请求
     * 
     * @param url 请求URL
     * @param headers 请求头
     * @return HttpResponse
     */
    public HttpResponse<String> delete(String url, Map<String, String> headers) {
        long startTime = System.currentTimeMillis();
        
        try {
            Request.Builder requestBuilder = new Request.Builder().url(url).delete();
            
            // 添加请求头
            if (headers != null) {
                headers.forEach(requestBuilder::addHeader);
            }
            
            Request request = requestBuilder.build();
            
            try (Response response = client.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";
                long costTime = System.currentTimeMillis() - startTime;
                
                HttpResponse<String> httpResponse = new HttpResponse<>(
                    response.code(),
                    response.message(),
                    responseBody,
                    response.isSuccessful()
                );
                httpResponse.setHeaders(response.headers().toString());
                httpResponse.setCostTime(costTime);
                
                return httpResponse;
            }
        } catch (IOException e) {
            logger.error("DELETE请求失败: {}", e.getMessage(), e);
            long costTime = System.currentTimeMillis() - startTime;
            HttpResponse<String> errorResponse = HttpResponse.error("请求失败: " + e.getMessage());
            errorResponse.setCostTime(costTime);
            return errorResponse;
        }
    }
    
    /**
     * 异步GET请求
     * 
     * @param url 请求URL
     * @param callback 回调函数
     */
    public void getAsync(String url, Callback callback) {
        getAsync(url, null, callback);
    }
    
    /**
     * 异步GET请求
     * 
     * @param url 请求URL
     * @param headers 请求头
     * @param callback 回调函数
     */
    public void getAsync(String url, Map<String, String> headers, Callback callback) {
        Request.Builder requestBuilder = new Request.Builder().url(url);
        
        // 添加请求头
        if (headers != null) {
            headers.forEach(requestBuilder::addHeader);
        }
        
        Request request = requestBuilder.build();
        client.newCall(request).enqueue(callback);
    }
    
    /**
     * 异步POST请求（JSON）
     * 
     * @param url 请求URL
     * @param jsonData JSON数据
     * @param callback 回调函数
     */
    public void postJsonAsync(String url, String jsonData, Callback callback) {
        postJsonAsync(url, jsonData, null, callback);
    }
    
    /**
     * 异步POST请求（JSON）
     * 
     * @param url 请求URL
     * @param jsonData JSON数据
     * @param headers 请求头
     * @param callback 回调函数
     */
    public void postJsonAsync(String url, String jsonData, Map<String, String> headers, Callback callback) {
        RequestBody body = RequestBody.create(jsonData, JSON_TYPE);
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(body);
        
        // 添加请求头
        if (headers != null) {
            headers.forEach(requestBuilder::addHeader);
        }
        
        Request request = requestBuilder.build();
        client.newCall(request).enqueue(callback);
    }
    
    /**
     * 构建带参数的URL
     * 
     * @param url 原始URL
     * @param params 参数
     * @return 构建后的URL
     */
    private String buildUrlWithParams(String url, Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return url;
        }
        
        StringBuilder urlBuilder = new StringBuilder(url);
        if (!url.contains("?")) {
            urlBuilder.append("?");
        } else {
            urlBuilder.append("&");
        }
        
        boolean first = true;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (!first) {
                urlBuilder.append("&");
            }
            urlBuilder.append(entry.getKey()).append("=").append(entry.getValue());
            first = false;
        }
        
        return urlBuilder.toString();
    }
    
    /**
     * 设置自定义OkHttpClient
     * 
     * @param client 自定义客户端
     */
    public void setClient(OkHttpClient client) {
        this.client = client;
    }
    
    /**
     * 获取当前OkHttpClient
     * 
     * @return OkHttpClient
     */
    public OkHttpClient getClient() {
        return client;
    }
}
