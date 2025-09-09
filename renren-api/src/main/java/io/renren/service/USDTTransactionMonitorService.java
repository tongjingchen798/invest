package io.renren.service;

import java.util.List;
import java.util.Map;

/**
 * USDT转账监控服务接口
 *
 * @author renren
 * @date 2024-01-01
 */
public interface USDTTransactionMonitorService {
    
    /**
     * 定时拉取USDT交易记录并存储到U收款记录表
     *
     * @param usdtAddress USDT地址
     */
    void fetchAndStoreUSDTTransactions(String usdtAddress);
    
    /**
     * 匹配U收款记录与充值订单
     *
     * @param usdtAddress USDT地址
     * @param amount 期望金额
     * @param userId 用户ID
     * @return 匹配结果
     */
    Map<String, Object> matchUSDTRecords(String usdtAddress, Long amount, Long userId);
    
    
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
    
    /**
     * 获取未匹配的U收款记录
     *
     * @param usdtAddress USDT地址
     * @return 未匹配的记录列表
     */
    List<Map<String, Object>> getUnmatchedUSDTRecords(String usdtAddress);
}
