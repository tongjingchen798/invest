package io.renren.service;

import java.util.Map;

/**
 * USDT充值服务接口
 *
 * @author renren
 * @date 2024-01-01
 */
public interface USDTRechargeService {
    
    /**
     * 生成USDT充值二维码
     *
     * @param amount 充值金额（可选）
     * @return 包含二维码信息的Map
     */
    Map<String, Object> generateUSDTRechargeQRCode(String amount);
    
    /**
     * 获取USDT-TRC20地址
     *
     * @return USDT-TRC20地址
     */
    String getUSDTAddress();
}
