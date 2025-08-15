package io.renren.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 支付配置类
 * 管理各种支付方式的密钥和配置信息
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@Component
@ConfigurationProperties(prefix = "payment")
public class PaymentConfig {

    /**
     * 银行卡支付配置
     */
    private BankPayment bank = new BankPayment();

    /**
     * 虚拟币支付配置
     */
    private CryptoPayment crypto = new CryptoPayment();

    /**
     * UPI支付配置
     */
    private UpiPayment upi = new UpiPayment();

    /**
     * Paytm支付配置
     */
    private PaytmPayment paytm = new PaytmPayment();

    /**
     * 通用支付配置
     */
    private CommonPayment common = new CommonPayment();

    /**
     * 银行卡支付配置
     */
    @Data
    public static class BankPayment {
        /**
         * 商户ID
         */
        private String merchantId;
        
        /**
         * 商户密钥
         */
        private String secretKey;
        
        /**
         * 支付网关地址
         */
        private String gatewayUrl;
        
        /**
         * 回调地址
         */
        private String callbackUrl;
        
        /**
         * 查询地址
         */
        private String queryUrl;
    }

    /**
     * 虚拟币支付配置
     */
    @Data
    public static class CryptoPayment {
        /**
         * 商户ID
         */
        private String merchantId;
        
        /**
         * 商户密钥
         */
        private String secretKey;
        
        /**
         * 支付网关地址
         */
        private String gatewayUrl;
        
        /**
         * 回调地址
         */
        private String callbackUrl;
        
        /**
         * 查询地址
         */
        private String queryUrl;
        
        /**
         * 支持的币种
         */
        private String[] supportedCoins = {"USDT", "BTC", "ETH"};
        
        /**
         * 网络类型
         */
        private String[] networks = {"TRC20", "ERC20", "BEP20"};
    }

    /**
     * UPI支付配置
     */
    @Data
    public static class UpiPayment {
        /**
         * 商户ID
         */
        private String merchantId;
        
        /**
         * 商户密钥
         */
        private String secretKey;
        
        /**
         * 支付网关地址
         */
        private String gatewayUrl;
        
        /**
         * 回调地址
         */
        private String callbackUrl;
        
        /**
         * 查询地址
         */
        private String queryUrl;
        
        /**
         * UPI应用名称
         */
        private String appName;
        
        /**
         * 商户名称
         */
        private String merchantName;
    }

    /**
     * Paytm支付配置
     */
    @Data
    public static class PaytmPayment {
        /**
         * 商户ID
         */
        private String merchantId;
        
        /**
         * 商户密钥
         */
        private String secretKey;
        
        /**
         * 支付网关地址
         */
        private String gatewayUrl;
        
        /**
         * 回调地址
         */
        private String callbackUrl;
        
        /**
         * 查询地址
         */
        private String queryUrl;
        
        /**
         * 行业类型
         */
        private String industryType;
        
        /**
         * 网站名称
         */
        private String websiteName;
    }

    /**
     * 通用支付配置
     */
    @Data
    public static class CommonPayment {
        /**
         * 默认商户密钥
         */
        private String defaultSecretKey;
        
        /**
         * 签名算法
         */
        private String signAlgorithm = "MD5";
        
        /**
         * 字符编码
         */
        private String charset = "UTF-8";
        
        /**
         * 超时时间（秒）
         */
        private Integer timeout = 30;
        
        /**
         * 重试次数
         */
        private Integer retryCount = 3;
    }
}
