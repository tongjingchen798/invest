//package io.renren.utils;
//
//import io.renren.dto.HttpResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * OkHttp使用示例
// *
// * @author renren
// * @date 2024-01-01
// */
//@Component
//public class OkHttpExample {
//
//    @Autowired
//    private OkHttpUtil okHttpUtil;
//
//    /**
//     * GET请求示例
//     */
//    public void getExample() {
//        // 简单GET请求
//        HttpResponse<String> response1 = okHttpUtil.get("https://httpbin.org/get");
//        System.out.println("GET请求结果: " + response1);
//
//        // 带参数的GET请求
//        Map<String, Object> params = new HashMap<>();
//        params.put("name", "张三");
//        params.put("age", 25);
//        HttpResponse<String> response2 = okHttpUtil.get("https://httpbin.org/get", null, params);
//        System.out.println("带参数GET请求结果: " + response2);
//
//        // 带请求头的GET请求
//        Map<String, String> headers = new HashMap<>();
//        headers.put("Authorization", "Bearer token123");
//        headers.put("User-Agent", "MyApp/1.0");
//        HttpResponse<String> response3 = okHttpUtil.get("https://httpbin.org/headers", headers);
//        System.out.println("带请求头GET请求结果: " + response3);
//    }
//
//    /**
//     * POST请求示例
//     */
//    public void postExample() {
//        // POST JSON请求
//        Map<String, Object> data = new HashMap<>();
//        data.put("name", "李四");
//        data.put("email", "lisi@example.com");
//
//        HttpResponse<String> response1 = okHttpUtil.postJson("https://httpbin.org/post", data);
//        System.out.println("POST JSON请求结果: " + response1);
//
//        // POST表单请求
//        Map<String, Object> formData = new HashMap<>();
//        formData.put("username", "admin");
//        formData.put("password", "123456");
//
//        HttpResponse<String> response2 = okHttpUtil.postForm("https://httpbin.org/post", formData);
//        System.out.println("POST表单请求结果: " + response2);
//
//        // 带请求头的POST请求
//        Map<String, String> headers = new HashMap<>();
//        headers.put("Content-Type", "application/json");
//        headers.put("Authorization", "Bearer token123");
//
//        HttpResponse<String> response3 = okHttpUtil.postJson("https://httpbin.org/post", data, headers);
//        System.out.println("带请求头POST请求结果: " + response3);
//    }
//
//    /**
//     * PUT请求示例
//     */
//    public void putExample() {
//        Map<String, Object> data = new HashMap<>();
//        data.put("id", 1);
//        data.put("name", "王五");
//        data.put("status", "active");
//
//        HttpResponse<String> response = okHttpUtil.putJson("https://httpbin.org/put", data);
//        System.out.println("PUT请求结果: " + response);
//    }
//
//    /**
//     * DELETE请求示例
//     */
//    public void deleteExample() {
//        HttpResponse<String> response = okHttpUtil.delete("https://httpbin.org/delete");
//        System.out.println("DELETE请求结果: " + response);
//    }
//
//    /**
//     * 异步请求示例
//     */
//    public void asyncExample() {
//        // 异步GET请求
//        okHttpUtil.getAsync("https://httpbin.org/get", new okhttp3.Callback() {
//            @Override
//            public void onFailure(okhttp3.Call call, java.io.IOException e) {
//                System.err.println("异步GET请求失败: " + e.getMessage());
//            }
//
//            @Override
//            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws java.io.IOException {
//                String responseBody = response.body() != null ? response.body().string() : "";
//                System.out.println("异步GET请求成功: " + responseBody);
//            }
//        });
//
//        // 异步POST请求
//        Map<String, Object> data = new HashMap<>();
//        data.put("message", "Hello Async");
//
//        okHttpUtil.postJsonAsync("https://httpbin.org/post", data, new okhttp3.Callback() {
//            @Override
//            public void onFailure(okhttp3.Call call, java.io.IOException e) {
//                System.err.println("异步POST请求失败: " + e.getMessage());
//            }
//
//            @Override
//            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws java.io.IOException {
//                String responseBody = response.body() != null ? response.body().string() : "";
//                System.out.println("异步POST请求成功: " + responseBody);
//            }
//        });
//    }
//
//    /**
//     * 处理响应结果示例
//     */
//    public void handleResponseExample() {
//        HttpResponse<String> response = okHttpUtil.get("https://httpbin.org/get");
//
//        if (response.isSuccess()) {
//            System.out.println("请求成功!");
//            System.out.println("状态码: " + response.getCode());
//            System.out.println("响应数据: " + response.getData());
//            System.out.println("请求耗时: " + response.getCostTime() + "ms");
//        } else {
//            System.err.println("请求失败!");
//            System.err.println("错误码: " + response.getCode());
//            System.err.println("错误信息: " + response.getMessage());
//        }
//    }
//}
