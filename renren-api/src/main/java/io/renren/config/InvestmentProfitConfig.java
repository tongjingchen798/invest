package io.renren.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 投资收益配置类
 * 
 * 用于配置不同周期类型的收益率参数
 *
 * @author renren
 * @since 1.0.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "investment.profit")
public class InvestmentProfitConfig {
    
    /**
     * 到期收益含本金的年化收益率
     */
    private BigDecimal maturityAnnualRate = new BigDecimal("0.12"); // 12%
    
    /**
     * 每日返本金到期收益的年化收益率
     */
    private BigDecimal dailyReturnAnnualRate = new BigDecimal("0.08"); // 8%
    
    /**
     * 不返本金的年化收益率
     */
    private BigDecimal noPrincipalAnnualRate = new BigDecimal("0.15"); // 15%
    
    /**
     * 复利产品的日收益率
     */
    private BigDecimal compoundDailyRate = new BigDecimal("0.0003"); // 0.03%
    
    /**
     * 阶梯日益的收益率配置
     */
    private SteppedDailyRate steppedDailyRate = new SteppedDailyRate();
    
    /**
     * 拼团的收益率配置
     */
    private GroupBuyRate groupBuyRate = new GroupBuyRate();
    
    @Data
    public static class SteppedDailyRate {
        /**
         * 前7天的日收益率
         */
        private BigDecimal firstWeekRate = new BigDecimal("0.001"); // 0.1%
        
        /**
         * 8-15天的日收益率
         */
        private BigDecimal secondWeekRate = new BigDecimal("0.0015"); // 0.15%
        
        /**
         * 16天以后的日收益率
         */
        private BigDecimal laterRate = new BigDecimal("0.002"); // 0.2%
    }
    
    @Data
    public static class GroupBuyRate {
        /**
         * 基础年化收益率
         */
        private BigDecimal baseAnnualRate = new BigDecimal("0.10"); // 10%
        
        /**
         * 拼团奖励比例
         */
        private BigDecimal groupBonusRate = new BigDecimal("0.02"); // 2%
    }
}
