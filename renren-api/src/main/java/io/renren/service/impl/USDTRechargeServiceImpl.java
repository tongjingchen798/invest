package io.renren.service.impl;

import io.renren.common.exception.ErrorCode;
import io.renren.common.exception.RenException;
import io.renren.dao.UAddressConfigDao;
import io.renren.entity.UAddressConfigEntity;
import io.renren.service.USDTRechargeService;
import io.renren.utils.QRCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * USDT充值服务实现类
 *
 * @author renren
 * @date 2024-01-01
 */
@Service
public class USDTRechargeServiceImpl implements USDTRechargeService {
    

    @Autowired
    private UAddressConfigDao uAddressConfigDao;
    
    @Override
    public Map<String, Object> generateUSDTRechargeQRCode(String amount) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取USDT-TRC20地址
        String usdtAddress = getUSDTAddress();
        if (usdtAddress == null || usdtAddress.isEmpty()) {
            throw new RenException(ErrorCode.USDT_ADDRESS_NOT_CONFIGURED);
        }
        
        // 生成二维码
        String qrCodeBase64 = QRCodeUtils.generateUSDTQRCode(usdtAddress, amount);
        
        result.put("usdtAddress", usdtAddress);
        result.put("qrCode", qrCodeBase64);
        result.put("amount", amount);
        result.put("message", "请使用支持TRC20的钱包扫描二维码进行USDT转账");
        
        return result;
    }
    
    @Override
    public String getUSDTAddress() {
        UAddressConfigEntity uAddressConfigEntity = uAddressConfigDao.selectAddrLimit();
        if (uAddressConfigEntity != null && uAddressConfigEntity.getAddr() != null) {
            return uAddressConfigEntity.getAddr();
        }
        return null;
    }
}
