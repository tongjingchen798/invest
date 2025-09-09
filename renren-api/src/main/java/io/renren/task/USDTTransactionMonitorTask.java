package io.renren.task;

import io.renren.dao.ChargeOrderDao;
import io.renren.dao.SysParamsDao;
import io.renren.dao.UAddressConfigDao;
import io.renren.dao.UserDao;
import io.renren.entity.UAddressConfigEntity;
import io.renren.entity.ChargeOrderEntity;
import io.renren.service.USDTTransactionMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * USDT转账监控定时任务
 *
 * @author renren
 * @date 2024-01-01
 */
@Component
public class USDTTransactionMonitorTask {
    
    @Autowired
    private USDTTransactionMonitorService usdtTransactionMonitorService;
    
    @Autowired
    private ChargeOrderDao chargeOrderDao;
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private SysParamsDao sysParamsDao;
    
    @Autowired
    private UAddressConfigDao uAddressConfigDao;
    
    /**
     * 每30秒检查一次USDT转账
     */
    @Scheduled(fixedRate = 30000)
    public void monitorUSDTTransactions() {
        try {
            // 获取USDT地址
            UAddressConfigEntity uAddressConfigEntity = uAddressConfigDao.selectAddrLimit();
            if (uAddressConfigEntity == null || uAddressConfigEntity.getAddr() == null) {
                return;
            }
            String usdtAddress = uAddressConfigEntity.getAddr();
            
            // 获取待处理的充值订单
            List<ChargeOrderEntity> pendingOrders = chargeOrderDao.selectPendingUSDTOrders();
            
            for (ChargeOrderEntity order : pendingOrders) {
                try {
                    // 监控USDT转账
                    usdtTransactionMonitorService.monitorUSDTTransaction(
                            usdtAddress, 
                            order.getAmount(),
                            order.getUserId()
                    );
                } catch (Exception e) {
                    // 记录错误日志，但不中断其他订单的处理
                    System.err.println("监控USDT转账失败，订单ID: " + order.getOrderno() + ", 错误: " + e.getMessage());
                }
            }
            
        } catch (Exception e) {
            System.err.println("USDT转账监控任务执行失败: " + e.getMessage());
        }
    }
    
    /**
     * 每小时检查一次USDT余额
     */
    @Scheduled(fixedRate = 3600000)
    public void checkUSDTBalance() {
        try {
            UAddressConfigEntity uAddressConfigEntity = uAddressConfigDao.selectAddrLimit();
            if (uAddressConfigEntity == null || uAddressConfigEntity.getAddr() == null) {
                return;
            }
            String usdtAddress = uAddressConfigEntity.getAddr();
            
            // 检查USDT余额
            usdtTransactionMonitorService.checkUSDTBalance(usdtAddress);
            
        } catch (Exception e) {
            System.err.println("检查USDT余额失败: " + e.getMessage());
        }
    }
}
