package io.renren.task;

import io.renren.dao.ChargeOrderDao;
import io.renren.dao.SysParamsDao;
import io.renren.dao.UAddressConfigDao;
import io.renren.dao.UserDao;
import io.renren.entity.UAddressConfigEntity;
import io.renren.entity.ChargeOrderEntity;
import io.renren.service.USDTTransactionMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * USDT转账监控定时任务
 *
 * @author renren
 * @date 2024-01-01
 */
@Slf4j
@Component
public class USDTTransactionMonitorTask {
    
    @Autowired
    private USDTTransactionMonitorService usdtTransactionMonitorService;
    
    @Autowired
    private ChargeOrderDao chargeOrderDao;
    
    @Autowired
    private UAddressConfigDao uAddressConfigDao;
    
    // 缓存USDT地址配置，避免重复查询数据库
    private UAddressConfigEntity cachedUAddressConfig;
    private long lastCacheTime = 0;
    private static final long CACHE_DURATION = 300000; // 5分钟缓存时间
    
    /**
     * 获取USDT地址配置（带缓存）
     * @return USDT地址配置实体
     */
    private UAddressConfigEntity getUAddressConfig() {
        long currentTime = System.currentTimeMillis();
        
        // 如果缓存为空或已过期，重新查询数据库
        if (cachedUAddressConfig == null || (currentTime - lastCacheTime) > CACHE_DURATION) {
            try {
                cachedUAddressConfig = uAddressConfigDao.selectAddrLimit();
                lastCacheTime = currentTime;
                log.debug("刷新USDT地址配置缓存");
            } catch (Exception e) {
                log.error("查询USDT地址配置失败: {}", e.getMessage(), e);
                return null;
            }
        }
        
        return cachedUAddressConfig;
    }
    
    /**
     * 每30秒拉取USDT交易记录
     */
    @Scheduled(fixedRate = 300000)
    public void fetchUSDTTransactions() {
        try {
            // 获取USDT地址（使用缓存）
            UAddressConfigEntity uAddressConfigEntity = getUAddressConfig();
            if (uAddressConfigEntity == null || uAddressConfigEntity.getAddr() == null) {
                return;
            }
            String usdtAddress = uAddressConfigEntity.getAddr();
            
            // 拉取并存储USDT交易记录
            usdtTransactionMonitorService.fetchAndStoreUSDTTransactions(usdtAddress);
            
        } catch (Exception e) {
            log.error("拉取USDT交易记录失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 每10秒匹配U收款记录与充值订单
     */
    @Scheduled(fixedRate = 10000)
    public void matchUSDTRecords() {
        try {
            // 获取USDT地址（使用缓存）
            UAddressConfigEntity uAddressConfigEntity = getUAddressConfig();
            if (uAddressConfigEntity == null || uAddressConfigEntity.getAddr() == null) {
                log.warn("未找到可用的USDT地址配置");
                return;
            }
            String usdtAddress = uAddressConfigEntity.getAddr();

            // 获取待处理的充值订单
            List<ChargeOrderEntity> pendingOrders = chargeOrderDao.selectPendingUSDTOrders();

            for (ChargeOrderEntity order : pendingOrders) {
                try {
                    // 匹配U收款记录
                    usdtTransactionMonitorService.matchUSDTRecords(
                            usdtAddress,
                            order.getAmount(),
                            order.getUserId()
                    );
                } catch (Exception e) {
                    // 记录错误日志，但不中断其他订单的处理
                    log.error("匹配U收款记录失败，订单ID: {}, 错误: {}", order.getOrderno(), e.getMessage(), e);
                }
            }

        } catch (Exception e) {
            log.error("匹配U收款记录任务执行失败: {}", e.getMessage(), e);
        }
    }


    /**
     * 每十分钟检查一次USDT余额
     */
    @Scheduled(fixedRate = 600000)
    public void checkUSDTBalance() {
        try {
            UAddressConfigEntity uAddressConfigEntity = getUAddressConfig();
            if (uAddressConfigEntity == null || uAddressConfigEntity.getAddr() == null) {
                log.warn("未找到可用的USDT地址配置");
                return;
            }
            String usdtAddress = uAddressConfigEntity.getAddr();

            // 检查USDT余额
            Map<String, Object> balanceResult = usdtTransactionMonitorService.checkUSDTBalance(usdtAddress);
            
            // 如果检查成功，更新余额到数据库
            if (balanceResult != null && (Boolean) balanceResult.get("success")) {
                String balanceStr = (String) balanceResult.get("balance");
                if (balanceStr != null && !balanceStr.isEmpty()) {
                    try {
                        BigDecimal balance = new BigDecimal(balanceStr);
                        uAddressConfigEntity.setBalance(balance);
                        uAddressConfigEntity.setUpdateDate(new Date());
                        
                        int updateResult = uAddressConfigDao.updateById(uAddressConfigEntity);
                        if (updateResult > 0) {
                            log.info("USDT地址 {} 余额更新成功: {} USDT", usdtAddress, balance);
                        } else {
                            log.warn("USDT地址 {} 余额更新失败", usdtAddress);
                        }
                    } catch (NumberFormatException e) {
                        log.error("解析USDT余额失败: {}", balanceStr, e);
                    }
                }
            } else {
                String errorMsg = balanceResult != null ? (String) balanceResult.get("message") : "未知错误";
                log.warn("获取USDT地址 {} 余额失败: {}", usdtAddress, errorMsg);
                
                // 如果是地址格式错误，记录更详细的日志
                if (errorMsg.contains("无效的TRON地址格式") || errorMsg.contains("USDT地址不存在")) {
                    log.error("USDT地址配置可能有问题，请检查数据库中的地址配置: {}", usdtAddress);
                }
            }

        } catch (Exception e) {
            log.error("检查USDT余额失败: {}", e.getMessage(), e);
        }
    }
}
