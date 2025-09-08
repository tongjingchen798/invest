package io.renren.utils;

import io.renren.config.WithdrawConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 提现规则验证工具类
 * 
 * @author renren
 * @since 1.0.0
 */
@Slf4j
@Component
public class WithdrawRuleValidator {
    
    @Autowired
    private WithdrawConfig withdrawConfig;
    

    
    /**
     * 计算提现手续费
     * 
     * @param amount 提现金额（卢比）
     * @return 手续费金额（卢比）
     */
    public BigDecimal calculateFee(BigDecimal amount) {
        if (withdrawConfig.isPercentageFee()) {
            // 按比例计算手续费
            return amount.multiply(withdrawConfig.getFeeRate())
                        .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        } else {
            // 固定手续费
            return withdrawConfig.getFixedFee();
        }
    }
    
    /**
     * 计算实际支付金额
     * 
     * @param amount 提现金额（卢比）
     * @return 实际支付金额（卢比）
     */
    public BigDecimal calculateRealAmount(BigDecimal amount) {
        BigDecimal fee = calculateFee(amount);
        return amount.subtract(fee);
    }
    

    

    /**
     * 验证结果内部类
     */
    public static class ValidationResult {
        private boolean valid;
        private String errorMessage;
        
        public boolean isValid() {
            return valid;
        }
        
        public void setValid(boolean valid) {
            this.valid = valid;
        }
        
        public String getErrorMessage() {
            return errorMessage;
        }
        
        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}
