package io.renren.modules.withdraw.service;

import io.renren.modules.withdraw.dto.PayAgentResponse;
import io.renren.modules.paymerchant.entity.PayMerchantEntity;
import io.renren.modules.withdraw.entity.WithdrawOrderEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 代付工厂服务
 * 根据渠道ID和商户ID选择合适的代付服务
 * 
 * @author renren
 * @date 2024-01-01
 */
@Service
public class PayAgentFactory {
    
    private static final Logger logger = LoggerFactory.getLogger(PayAgentFactory.class);
    
    @Autowired
    private List<PayAgentService> payAgentServices;
    
    /**
     * 初始化后检查代付服务
     */
    @PostConstruct
    public void init() {
        if (payAgentServices == null || payAgentServices.isEmpty()) {
            logger.warn("未找到任何代付服务实现，请检查PayAgentService实现类是否正确配置");
        } else {
            logger.info("成功加载 {} 个代付服务: {}", 
                       payAgentServices.size(), 
                       payAgentServices.stream()
                           .map(PayAgentService::getChannelCode)
                           .toArray());
        }
    }
    
    /**
     * 创建代付订单
     * 
     * @param withdrawOrder 提现订单
     * @param payMerchant 支付商户信息
     * @return 代付响应结果
     */
    public PayAgentResponse createPayoutOrder(WithdrawOrderEntity withdrawOrder, PayMerchantEntity payMerchant) {
        try {
            // 检查代付服务列表是否为空
            if (payAgentServices == null || payAgentServices.isEmpty()) {
                logger.error("代付服务列表为空，请检查PayAgentService实现类是否正确配置");
                return PayAgentResponse.error("代付服务未配置", 
                                            withdrawOrder.getChannelid(), 
                                            "代付服务列表为空，请检查配置");
            }
            
            // 根据商户CODE选择合适的代付服务
            PayAgentService payAgentService = selectPayAgentService(payMerchant.getMerchantCode());
            
            if (payAgentService == null) {
                logger.error("未找到支持的代付服务 - 商户CODE: {}, 可用服务: {}", 
                           payMerchant.getMerchantCode(),
                           payAgentServices.stream()
                               .map(PayAgentService::getChannelCode)
                               .toArray());
                return PayAgentResponse.error("未找到支持的代付服务", 
                                            withdrawOrder.getChannelid(), 
                                            "商户CODE: " + payMerchant.getMerchantCode());
            }
            
            logger.info("使用代付服务: {} - 订单号: {}, 商户CODE: {}", 
                       payAgentService.getChannelCode(), withdrawOrder.getOrderno(), payMerchant.getMerchantCode());
            
            // 调用对应的代付服务
            return payAgentService.createPayoutOrder(withdrawOrder, payMerchant);
            
        } catch (Exception e) {
            logger.error("代付工厂处理异常 - 订单号: {}, 商户CODE: {}", 
                        withdrawOrder.getOrderno(), payMerchant.getMerchantCode(), e);
            return PayAgentResponse.error("代付处理异常: " + e.getMessage(), 
                                        withdrawOrder.getChannelid(), 
                                        e.getMessage());
        }
    }
    
    
    /**
     * 获取所有支持的代付渠道
     * 
     * @return 渠道列表
     */
    public String[] getSupportedChannels() {
        if (payAgentServices == null || payAgentServices.isEmpty()) {
            return new String[0];
        }
        return payAgentServices.stream()
                .map(PayAgentService::getChannelCode)
                .toArray(String[]::new);
    }
    
    /**
     * 获取代付服务列表（用于调试）
     * 
     * @return 代付服务列表
     */
    public List<PayAgentService> getPayAgentServices() {
        return payAgentServices;
    }
    
    /**
     * 选择合适的代付服务（用于调试）
     * 
     * @param merchantCode 商户CODE
     * @return 代付服务实例
     */
    public PayAgentService selectPayAgentService(String merchantCode) {
        if (payAgentServices == null || payAgentServices.isEmpty()) {
            return null;
        }
        for (PayAgentService service : payAgentServices) {
            if (service.supports(merchantCode)) {
                return service;
            }
        }
        return null;
    }
}
