package io.renren.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 账变详情表
 *
 * @author renren
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_balance_detail")
public class UserBalanceDetailEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId
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
     * 业务类型 1购买流水 2余额提现流水 3返佣A 5冻结金额 6解冻金额  7手工充值 8手工扣款  10收益  11线上充值 12工资 32项目返上级 31项目返自己 30 返佣B
     * 28任务奖励 27注册奖励 26拼团奖励 24转给投资账户 23投资账户转出 22代理转出 21转入投资 20出售产品
     * 18领取红包 19今日福利 14邀请福利  13签到奖励 33佣金提现流水 15返现
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
     * 原始金额
     */
    private BigDecimal originalAmount;

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
     * 交易后金额
     */
    private BigDecimal amountAfterTransaction;

    /**
     * 交易金额
     */
    private BigDecimal transactionAmount;

    /**
     * 优惠券金额
     */
    private BigDecimal couponAmount;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createDate;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;
}
