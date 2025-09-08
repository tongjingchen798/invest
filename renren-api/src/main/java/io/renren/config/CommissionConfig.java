package io.renren.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 佣金配置
 * 
 * @author renren
 * @since 1.0.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "commission")
public class CommissionConfig {
    
    /**
     * 1级直属推荐佣金比例（百分比）
     */
    private BigDecimal firstLevelRate = new BigDecimal("7");
    
    /**
     * 2级推荐佣金比例（百分比）
     */
    private BigDecimal secondLevelRate = new BigDecimal("3");
    
    /**
     * 是否启用佣金计算
     */
    private boolean enabled = true;
    
    /**
     * 佣金计算时间（每天凌晨几点执行）
     */
    private String cronExpression = "0 0 2 * * ?";
    
    /**
     * 获取1级佣金比例（小数形式）
     */
    public BigDecimal getFirstLevelRateDecimal() {
        return firstLevelRate.divide(new BigDecimal("100"), 4, BigDecimal.ROUND_DOWN);
    }
    
    /**
     * 获取2级佣金比例（小数形式）
     */
    public BigDecimal getSecondLevelRateDecimal() {
        return secondLevelRate.divide(new BigDecimal("100"), 4, BigDecimal.ROUND_DOWN);
    }
}
