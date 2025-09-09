package io.renren.service;

import java.util.Map;

/**
 * USDT转账监控服务接口
 *
 * @author renren
 * @date 2024-01-01
 */
public interface USDTTransactionMonitorService {
    
    /**
     * 监控USDT转账
     *
     * @param usdtAddress USDT地址
     * @param amount  平台币
     * @param userId 用户ID
     * @return 监控结果
     */
    Map<String, Object> monitorUSDTTransaction(String usdtAddress, Long amount, Long userId);
    
    /**
     * 检查特定地址的USDT余额
     *
     * @param usdtAddress USDT地址
     * @return 余额信息
     */
    Map<String, Object> checkUSDTBalance(String usdtAddress);
    
    /**
     * 验证USDT转账
     *
     * @param txHash 交易哈希
     * @param usdtAddress 目标地址
     * @param amount 期望金额
     * @return 验证结果
     */
    Map<String, Object> verifyUSDTTransaction(String txHash, String usdtAddress, Long amount);
}
