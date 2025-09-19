package io.renren.utils;

import io.renren.enums.QePayTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * QePay支付类型工具类
 * 
 * @author nico
 * @date 2024-01-01
 */
public class QePayTypeUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(QePayTypeUtils.class);
    
    /**
     * 验证支付类型代码是否有效
     * 
     * @param payTypeCode 支付类型代码
     * @return 是否有效
     */
    public static boolean isValidPayType(String payTypeCode) {
        return QePayTypeEnum.getByCode(payTypeCode) != null;
    }
    
    /**
     * 获取支付类型描述
     * 
     * @param payTypeCode 支付类型代码
     * @return 支付类型描述，如果无效返回null
     */
    public static String getPayTypeDescription(String payTypeCode) {
        QePayTypeEnum payType = QePayTypeEnum.getByCode(payTypeCode);
        return payType != null ? payType.getDescription() : null;
    }
    
    /**
     * 获取所有越南支付类型
     * 
     * @return 越南支付类型列表
     */
    public static List<QePayTypeEnum> getVietnamPayTypes() {
        List<QePayTypeEnum> vietnamTypes = new ArrayList<>();
        for (QePayTypeEnum payType : QePayTypeEnum.values()) {
            if (payType.name().startsWith("VN_")) {
                vietnamTypes.add(payType);
            }
        }
        return vietnamTypes;
    }
    
    /**
     * 获取所有印度支付类型
     * 
     * @return 印度支付类型列表
     */
    public static List<QePayTypeEnum> getIndiaPayTypes() {
        List<QePayTypeEnum> indiaTypes = new ArrayList<>();
        for (QePayTypeEnum payType : QePayTypeEnum.values()) {
            if (payType.name().startsWith("IN_")) {
                indiaTypes.add(payType);
            }
        }
        return indiaTypes;
    }
    
    /**
     * 获取所有二类支付类型
     * 
     * @return 二类支付类型列表
     */
    public static List<QePayTypeEnum> getType2PayTypes() {
        List<QePayTypeEnum> type2Types = new ArrayList<>();
        for (QePayTypeEnum payType : QePayTypeEnum.values()) {
            if (payType.name().contains("_TYPE2") || 
                payType.name().contains("_RUN_") || 
                payType.name().contains("_ACCOUNT_")) {
                type2Types.add(payType);
            }
        }
        return type2Types;
    }
    
    /**
     * 根据国家获取支付类型
     * 
     * @param country 国家代码（VN/IN）
     * @return 对应国家的支付类型列表
     */
    public static List<QePayTypeEnum> getPayTypesByCountry(String country) {
        if ("VN".equalsIgnoreCase(country)) {
            return getVietnamPayTypes();
        } else if ("IN".equalsIgnoreCase(country)) {
            return getIndiaPayTypes();
        }
        return new ArrayList<>();
    }
    
    /**
     * 记录支付类型使用日志
     * 
     * @param payTypeCode 支付类型代码
     * @param orderNo 订单号
     */
    public static void logPayTypeUsage(String payTypeCode, String orderNo) {
        QePayTypeEnum payType = QePayTypeEnum.getByCode(payTypeCode);
        if (payType != null) {
            logger.info("使用QePay支付类型 - 订单号: {}, 支付类型: {}, 描述: {}", 
                       orderNo, payType.getCode(), payType.getDescription());
        } else {
            logger.warn("使用未知QePay支付类型 - 订单号: {}, 支付类型代码: {}", orderNo, payTypeCode);
        }
    }
    
    /**
     * 获取支付类型统计信息
     * 
     * @return 统计信息字符串
     */
    public static String getPayTypeStatistics() {
        int totalTypes = QePayTypeEnum.values().length;
        int vietnamTypes = getVietnamPayTypes().size();
        int indiaTypes = getIndiaPayTypes().size();
        int type2Types = getType2PayTypes().size();
        
        return String.format("QePay支付类型统计 - 总计: %d, 越南: %d, 印度: %d, 二类: %d", 
                           totalTypes, vietnamTypes, indiaTypes, type2Types);
    }
}
