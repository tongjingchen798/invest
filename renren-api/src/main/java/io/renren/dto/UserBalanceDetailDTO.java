package io.renren.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 账变详情DTO
 *
 * @author renren
 * @since 2024-01-01
 */
@Data
public class UserBalanceDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 交易时间
     */
    private Date transactionDate;

    /**
     * 代理ID
     */
    private Long agentId;

    /**
     * 代理名称
     */
    private String agentName;

    /**
     * 标签
     */
    private String label;

    /**
     * 业务类型
     */
    private Integer businessType;

    /**
     * 渠道
     */
    private String channel;

    /**
     * 表单用户ID
     */
    private Long formUserId;

    /**
     * 邀请码状态
     */
    private Integer inviteCodeStatus;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 原始金额（分）
     */
    private Long originalAmount;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 业务员姓名
     */
    private String salesmanName;

    /**
     * 业务员ID
     */
    private Long salesmanId;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 流水ID
     */
    private String streamId;

    /**
     * 交易后金额（分）
     */
    private Long amountAfterTransaction;

    /**
     * 交易金额（分）
     */
    private Long transactionAmount;

    /**
     * 优惠券金额（分）
     */
    private Long couponAmount;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新时间
     */
    private Date updateDate;
}
