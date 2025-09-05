package io.renren.config;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * OkHttp配置类
 * 
 * @author renren
 * @date 2024-01-01
 */
@Configuration
@ConfigurationProperties(prefix = "okhttp")
public class OkHttpConfig {
    
    /**
     * 连接超时时间（秒）
     */
    private int connectTimeout = 30;
    
    /**
     * 读取超时时间（秒）
     */
    private int readTimeout = 60;
    
    /**
     * 写入超时时间（秒）
     */
    private int writeTimeout = 60;
    
    /**
     * 连接池最大空闲连接数
     */
    private int maxIdleConnections = 5;
    
    /**
     * 连接池中连接的存活时间（分钟）
     */
    private int keepAliveDuration = 5;
    
    /**
     * 是否重试连接失败
     */
    private boolean retryOnConnectionFailure = true;
    
    /**
     * 最大重试次数
     */
    private int maxRetries = 3;
    
    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(maxIdleConnections, keepAliveDuration, TimeUnit.MINUTES))
                .retryOnConnectionFailure(retryOnConnectionFailure)
                .build();
    }
    
    // Getters and Setters
    public int getConnectTimeout() {
        return connectTimeout;
    }
    
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }
    
    public int getReadTimeout() {
        return readTimeout;
    }
    
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }
    
    public int getWriteTimeout() {
        return writeTimeout;
    }
    
    public void setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }
    
    public int getMaxIdleConnections() {
        return maxIdleConnections;
    }
    
    public void setMaxIdleConnections(int maxIdleConnections) {
        this.maxIdleConnections = maxIdleConnections;
    }
    
    public int getKeepAliveDuration() {
        return keepAliveDuration;
    }
    
    public void setKeepAliveDuration(int keepAliveDuration) {
        this.keepAliveDuration = keepAliveDuration;
    }
    
    public boolean isRetryOnConnectionFailure() {
        return retryOnConnectionFailure;
    }
    
    public void setRetryOnConnectionFailure(boolean retryOnConnectionFailure) {
        this.retryOnConnectionFailure = retryOnConnectionFailure;
    }
    
    public int getMaxRetries() {
        return maxRetries;
    }
    
    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }
}
