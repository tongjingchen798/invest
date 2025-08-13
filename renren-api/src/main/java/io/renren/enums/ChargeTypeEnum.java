package io.renren.enums;

/**
 * 充值类型枚举
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
public enum ChargeTypeEnum {
    
    BANK_CARD(1, "银行卡", "bank", "bank_transfer"),
    CRYPTO(2, "虚拟币", "crypto", "crypto_payment"),
    UPI(3, "UPI", "upi", "upi_payment"),
    PAYTM(4, "Paytm", "paytm", "paytm_payment");

    private final Integer code;
    private final String name;
    private final String channel;
    private final String channelType;

    ChargeTypeEnum(Integer code, String name, String channel, String channelType) {
        this.code = code;
        this.name = name;
        this.channel = channel;
        this.channelType = channelType;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getChannel() {
        return channel;
    }

    public String getChannelType() {
        return channelType;
    }

    /**
     * 根据代码获取枚举
     */
    public static ChargeTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ChargeTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 验证充值类型是否有效
     */
    public static boolean isValid(Integer code) {
        return getByCode(code) != null;
    }
}
