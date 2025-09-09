package io.renren.utils;



import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * HTTP工具类
 *
 * @author renren
 * @date 2024-01-01
 */
public class HttpUtils {
    
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();
    
    /**
     * 发送GET请求
     *
     * @param url 请求URL
     * @return 响应内容
     */
    public static String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new IOException("请求失败: " + response.code() + " " + response.message());
            }
        }
    }
    
    /**
     * 发送POST请求
     *
     * @param url 请求URL
     * @param jsonBody JSON请求体
     * @return 响应内容
     */
    public static String post(String url, String jsonBody) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(
                        MediaType.parse("application/json; charset=utf-8"),
                        jsonBody))
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new IOException("请求失败: " + response.code() + " " + response.message());
            }
        }
    }
}
