package io.renren.enums;

/**
 * QePay支付类型枚举
 * 
 * @author nico
 * @date 2024-01-01
 */
public enum QePayTypeEnum {
    
    // 越南支付类型
    VN_WEB_SCAN("000", "越南网银扫码"),
    VN_WEB_DIRECT("001", "越南网银直连"),
    VN_WEB_TRANSFER("002", "越南网银转卡"),
    VN_MOMO("003", "越南MOMO"),
    VN_ZALO("004", "越南Zalo"),
    VN_VTPAY("005", "越南Vtpay"),
    
    // 越南二类支付类型
    VN_WEB_SCAN_TYPE2("020", "越南网银扫码二类"),
    VN_WEB_DIRECT_TYPE2("021", "越南网银直连二类"),
    VN_WEB_TRANSFER_TYPE2("022", "越南网银转卡二类"),
    VN_MOMO_TYPE2("023", "越南MOMO二类"),
    VN_ZALO_TYPE2("024", "越南Zalo二类"),
    VN_VTPAY_TYPE2("025", "越南Vtpay二类"),
    
    // 印度支付类型
    IN_WEB_B2C("100", "印度网银B2C"),
    IN_PAYTM_NATIVE_TYPE1("101", "印度Paytm原生一类"),
    IN_UPI_NATIVE_TYPE1("102", "印度UPI原生一类"),
    IN_WEB_TRANSFER("103", "印度网银转卡"),
    IN_PAYTM_ENTERTAINMENT("104", "印度Paytm娱乐"),
    IN_UPI_ENTERTAINMENT("105", "印度UPI娱乐"),
    
    // 印度二类支付类型
    IN_WEB_B2C_TYPE2("120", "印度网银B2C二类"),
    IN_PAYTM_RUN_TYPE2("121", "印度Paytm跑分二类"),
    IN_UPI_RUN_TYPE2("122", "印度UPI跑分二类"),
    IN_WEB_TRANSFER_TYPE2("123", "印度网银转卡二类"),
    
    // 印度跑分一类支付类型
    IN_PAYTM_RUN_TYPE1("131", "印度Paytm跑分一类"),
    IN_UPI_RUN_TYPE1("132", "印度UPI跑分一类"),
    
    // 印度原生二类支付类型
    IN_PAYTM_NATIVE_TYPE2_1("151", "印度Paytm原生二类"),
    IN_PAYTM_NATIVE_TYPE2_2("152", "印度Paytm原生二类"),
    
    // 印度专户T1支付类型
    IN_PAYTM_ACCOUNT_T1("161", "印度Paytm专户T1"),
    IN_UPI_ACCOUNT_T1("162", "印度UPI专户T1"),
    
    // 印度专户T2支付类型
    IN_PAYTM_ACCOUNT_T2("171", "印度Paytm专户T2"),
    IN_UPI_ACCOUNT_T2("172", "印度UPI专户T2");
    
    private final String code;
    private final String description;
    
    QePayTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 根据代码获取支付类型枚举
     * 
     * @param code 支付类型代码
     * @return 支付类型枚举，如果未找到返回null
     */
    public static QePayTypeEnum getByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }
        
        for (QePayTypeEnum payType : values()) {
            if (payType.getCode().equals(code.trim())) {
                return payType;
            }
        }
        return null;
    }
    
    /**
     * 判断是否为越南支付类型
     * 
     * @param code 支付类型代码
     * @return 是否为越南支付类型
     */
    public static boolean isVietnamPayType(String code) {
        QePayTypeEnum payType = getByCode(code);
        if (payType == null) {
            return false;
        }
        
        return payType.name().startsWith("VN_");
    }
    
    /**
     * 判断是否为印度支付类型
     * 
     * @param code 支付类型代码
     * @return 是否为印度支付类型
     */
    public static boolean isIndiaPayType(String code) {
        QePayTypeEnum payType = getByCode(code);
        if (payType == null) {
            return false;
        }
        
        return payType.name().startsWith("IN_");
    }
    
    /**
     * 判断是否为二类支付类型
     * 
     * @param code 支付类型代码
     * @return 是否为二类支付类型
     */
    public static boolean isType2PayType(String code) {
        QePayTypeEnum payType = getByCode(code);
        if (payType == null) {
            return false;
        }
        
        return payType.name().contains("_TYPE2") || 
               payType.name().contains("_RUN_") || 
               payType.name().contains("_ACCOUNT_");
    }
    
    @Override
    public String toString() {
        return code + " - " + description;
    }
}
