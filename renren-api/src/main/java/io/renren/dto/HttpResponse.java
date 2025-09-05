package io.renren.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * HTTP响应结果封装类
 * 
 * @author renren
 * @date 2024-01-01
 */
@Data
public class HttpResponse<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 响应状态码
     */
    private int code;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 是否成功
     */
    private boolean success;
    
    /**
     * 响应头信息
     */
    private String headers;
    
    /**
     * 请求耗时（毫秒）
     */
    private long costTime;
    
    public HttpResponse() {
    }
    
    public HttpResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = code >= 200 && code < 300;
    }
    
    public HttpResponse(int code, String message, T data, boolean success) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = success;
    }
    
    /**
     * 成功响应
     */
    public static <T> HttpResponse<T> success(T data) {
        return new HttpResponse<>(200, "success", data, true);
    }
    
    /**
     * 成功响应
     */
    public static <T> HttpResponse<T> success(String message, T data) {
        return new HttpResponse<>(200, message, data, true);
    }
    
    /**
     * 失败响应
     */
    public static <T> HttpResponse<T> error(int code, String message) {
        return new HttpResponse<>(code, message, null, false);
    }
    
    /**
     * 失败响应
     */
    public static <T> HttpResponse<T> error(String message) {
        return new HttpResponse<>(500, message, null, false);
    }
}
