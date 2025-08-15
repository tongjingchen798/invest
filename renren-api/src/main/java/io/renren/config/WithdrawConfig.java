package io.renren.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 提现规则配置
 * 
 * @author renren
 * @since 1.0.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "withdraw")
public class WithdrawConfig {
    
    /**
     * 单笔最低提现额度分（卢比*100）
     */
    private BigDecimal minAmount = new BigDecimal("20000");
    
    /**
     * 单笔最高提现额度分（卢比*100）
     */
    private BigDecimal maxAmount = new BigDecimal("10000000");
    
    /**
     * 提现手续费率（百分比）
     */
    private BigDecimal feeRate = new BigDecimal("5");
    
    /**
     * 手续费计算方式：true为按比例，false为固定金额
     */
    private boolean percentageFee = true;
    
    /**
     * 固定手续费金额（卢比），当percentageFee为false时使用
     */
    private BigDecimal fixedFee = new BigDecimal("0");
    
}
