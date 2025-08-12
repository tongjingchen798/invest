package io.renren.enums;

/**
 * 业务类型枚举
 */
public enum BusinessTypeEnum {

    // 基础业务类型
    PURCHASE_FLOW(1, "购买流水", "Purchase Flow"),
    BALANCE_WITHDRAWAL_FLOW(2, "余额提现流水", "Balance Withdrawal Flow"),
    COMMISSION_A(3, "返佣A", "Commission A"),
    FROZEN_AMOUNT(5, "冻结金额", "Frozen Amount"),
    UNFROZEN_AMOUNT(6, "解冻金额", "Unfrozen Amount"),
    MANUAL_RECHARGE(7, "手工充值", "Manual Recharge"),
    MANUAL_DEDUCTION(8, "手工扣款", "Manual Deduction"),
    INCOME(10, "收益", "Income"),
    ONLINE_RECHARGE(11, "线上充值", "Online Recharge"),
    SALARY(12, "工资", "Salary"),
    CASHBACK(15, "返现", "Cashback"),

    // 投资相关
    TRANSFER_TO_INVESTMENT(21, "转入投资", "Transfer to Investment"),
    SELL_PRODUCT(20, "出售产品", "Sell Product"),
    TRANSFER_FROM_INVESTMENT(23, "投资账户转出", "Transfer from Investment"),
    TRANSFER_TO_INVESTMENT_ACCOUNT(24, "转给投资账户", "Transfer to Investment Account"),

    // 代理相关
    AGENT_TRANSFER_OUT(22, "代理转出", "Agent Transfer Out"),
    PROJECT_COMMISSION_UP(32, "项目返上级", "Project Commission Up"),
    PROJECT_COMMISSION_SELF(31, "项目返自己", "Project Commission Self"),
    COMMISSION_B(30, "返佣B", "Commission B"),
    COMMISSION_WITHDRAWAL_FLOW(33, "佣金提现流水", "Commission Withdrawal Flow"),

    // 奖励相关
    TASK_REWARD(28, "任务奖励", "Task Reward"),
    REGISTRATION_REWARD(27, "注册奖励", "Registration Reward"),
    GROUP_BUY_REWARD(26, "拼团奖励", "Group Buy Reward"),
    RED_PACKET_CLAIM(18, "领取红包", "Red Packet Claim"),
    TODAY_WELFARE(19, "今日福利", "Today Welfare"),
    INVITATION_WELFARE(14, "邀请福利", "Invitation Welfare"),
    SIGN_IN_REWARD(13, "签到奖励", "Sign In Reward");

    private final Integer code;
    private final String chineseName;
    private final String englishName;

    BusinessTypeEnum(Integer code, String chineseName, String englishName) {
        this.code = code;
        this.chineseName = chineseName;
        this.englishName = englishName;
    }

    public Integer getCode() {
        return code;
    }

    public String getChineseName() {
        return chineseName;
    }

    public String getEnglishName() {
        return englishName;
    }

    /**
     * 根据代码获取枚举
     */
    public static BusinessTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (BusinessTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 根据代码获取中文名称
     */
    public static String getChineseNameByCode(Integer code) {
        BusinessTypeEnum type = getByCode(code);
        return type != null ? type.getChineseName() : "未知类型";
    }

    /**
     * 根据代码获取英文名称
     */
    public static String getEnglishNameByCode(Integer code) {
        BusinessTypeEnum type = getByCode(code);
        return type != null ? type.getEnglishName() : "Unknown Type";
    }

    /**
     * 判断是否为有效代码
     */
    public static boolean isValidCode(Integer code) {
        return getByCode(code) != null;
    }
}
