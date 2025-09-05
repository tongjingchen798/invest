# OkHttp工具类使用说明

## 概述

`OkHttpUtil` 是一个基于OkHttp3的HTTP请求工具类，提供了简单易用的API来发送各种HTTP请求。

## 功能特性

- 支持GET、POST、PUT、DELETE等HTTP方法
- 支持JSON和表单数据格式
- 支持同步和异步请求
- 支持自定义请求头
- 支持请求参数
- 统一的响应封装
- 请求耗时统计
- 连接池管理
- 可配置的超时时间

## 快速开始

### 1. 依赖配置

在 `pom.xml` 中已添加OkHttp依赖：

```xml
<dependency>
    <groupId>com.squareup.okhttp3</groupId>
    <artifactId>okhttp</artifactId>
    <version>4.10.0</version>
</dependency>
```

### 2. 配置参数

在 `application.yml` 中可以配置OkHttp参数：

```yaml
okhttp:
  connect-timeout: 30      # 连接超时时间（秒）
  read-timeout: 60         # 读取超时时间（秒）
  write-timeout: 60        # 写入超时时间（秒）
  max-idle-connections: 5  # 连接池最大空闲连接数
  keep-alive-duration: 5   # 连接存活时间（分钟）
  retry-on-connection-failure: true  # 是否重试连接失败
  max-retries: 3           # 最大重试次数
```

## 使用示例

### 1. 注入工具类

```java
@Autowired
private OkHttpUtil okHttpUtil;
```

### 2. GET请求

```java
// 简单GET请求
HttpResponse<String> response = okHttpUtil.get("https://api.example.com/users");

// 带参数的GET请求
Map<String, Object> params = new HashMap<>();
params.put("page", 1);
params.put("size", 10);
HttpResponse<String> response = okHttpUtil.get("https://api.example.com/users", null, params);

// 带请求头的GET请求
Map<String, String> headers = new HashMap<>();
headers.put("Authorization", "Bearer token123");
HttpResponse<String> response = okHttpUtil.get("https://api.example.com/users", headers);
```

### 3. POST请求

```java
// POST JSON请求
Map<String, Object> data = new HashMap<>();
data.put("name", "张三");
data.put("email", "zhangsan@example.com");
HttpResponse<String> response = okHttpUtil.postJson("https://api.example.com/users", data);

// POST表单请求
Map<String, Object> formData = new HashMap<>();
formData.put("username", "admin");
formData.put("password", "123456");
HttpResponse<String> response = okHttpUtil.postForm("https://api.example.com/login", formData);

// 带请求头的POST请求
Map<String, String> headers = new HashMap<>();
headers.put("Content-Type", "application/json");
headers.put("Authorization", "Bearer token123");
HttpResponse<String> response = okHttpUtil.postJson("https://api.example.com/users", data, headers);
```

### 4. PUT请求

```java
// PUT JSON请求
Map<String, Object> data = new HashMap<>();
data.put("id", 1);
data.put("name", "李四");
HttpResponse<String> response = okHttpUtil.putJson("https://api.example.com/users/1", data);
```

### 5. DELETE请求

```java
// DELETE请求
HttpResponse<String> response = okHttpUtil.delete("https://api.example.com/users/1");

// 带请求头的DELETE请求
Map<String, String> headers = new HashMap<>();
headers.put("Authorization", "Bearer token123");
HttpResponse<String> response = okHttpUtil.delete("https://api.example.com/users/1", headers);
```

### 6. 异步请求

```java
// 异步GET请求
okHttpUtil.getAsync("https://api.example.com/users", new Callback() {
    @Override
    public void onFailure(Call call, IOException e) {
        System.err.println("请求失败: " + e.getMessage());
    }
    
    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String responseBody = response.body() != null ? response.body().string() : "";
        System.out.println("请求成功: " + responseBody);
    }
});

// 异步POST请求
Map<String, Object> data = new HashMap<>();
data.put("name", "王五");
okHttpUtil.postJsonAsync("https://api.example.com/users", data, new Callback() {
    @Override
    public void onFailure(Call call, IOException e) {
        System.err.println("请求失败: " + e.getMessage());
    }
    
    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String responseBody = response.body() != null ? response.body().string() : "";
        System.out.println("请求成功: " + responseBody);
    }
});
```

## 响应处理

### HttpResponse类

所有请求都返回 `HttpResponse<T>` 对象，包含以下属性：

- `code`: HTTP状态码
- `message`: 响应消息
- `data`: 响应数据
- `success`: 是否成功（状态码200-299为成功）
- `headers`: 响应头信息
- `costTime`: 请求耗时（毫秒）

### 处理响应

```java
HttpResponse<String> response = okHttpUtil.get("https://api.example.com/users");

if (response.isSuccess()) {
    System.out.println("请求成功!");
    System.out.println("状态码: " + response.getCode());
    System.out.println("响应数据: " + response.getData());
    System.out.println("请求耗时: " + response.getCostTime() + "ms");
} else {
    System.err.println("请求失败!");
    System.err.println("错误码: " + response.getCode());
    System.err.println("错误信息: " + response.getMessage());
}
```

## 高级用法

### 1. 自定义OkHttpClient

```java
@Autowired
private OkHttpUtil okHttpUtil;

public void customClient() {
    OkHttpClient customClient = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .addInterceptor(new LoggingInterceptor())
            .build();
    
    okHttpUtil.setClient(customClient);
}
```

### 2. 添加拦截器

```java
public class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long startTime = System.currentTimeMillis();
        
        Response response = chain.proceed(request);
        
        long endTime = System.currentTimeMillis();
        System.out.println("请求耗时: " + (endTime - startTime) + "ms");
        
        return response;
    }
}
```

## 注意事项

1. 所有请求都是同步的，除非使用异步方法
2. 响应体在读取后会自动关闭，不能重复读取
3. 建议在生产环境中配置合适的超时时间
4. 大量并发请求时建议使用连接池
5. 异步请求的回调方法在后台线程中执行

## 错误处理

工具类会自动捕获IOException并返回错误响应，建议在业务代码中检查 `response.isSuccess()` 来判断请求是否成功。

## 性能优化

1. 使用连接池复用连接
2. 合理设置超时时间
3. 对于大量请求，考虑使用异步方法
4. 根据实际需求调整连接池参数
