/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 账变明细表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Data
@TableName("tb_transaction_detail")
public class TransactionDetailEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private String id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 交易时间
     */
    private Date transactionDate;

    /**
     * 流水号
     */
    private String streamId;

    /**
     * 业务类型
     */
    private Integer busiType;

    /**
     * 原始金额(分)
     */
    private Long originalAmount;

    /**
     * 使用金额(分)
     */
    private Long useAmount;

    /**
     * 优惠券金额(分)
     */
    private Long yhqAmount;

    /**
     * 交易后金额(分)
     */
    private Long transactionAmount;

    /**
     * 状态 1:成功 0:失败
     */
    private Integer status;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 渠道
     */
    private String channel;

    /**
     * 来源用户ID
     */
    private String formuserid;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 代理ID
     */
    private String agent;

    /**
     * 业务员ID
     */
    private String salesmanid;

    /**
     * 代理姓名
     */
    private String agentName;

    /**
     * 业务员姓名
     */
    private String salesmanName;

    /**
     * 标签
     */
    private String biaoqian;

    /**
     * 邀请码状态 1:有效 0:无效
     */
    private Integer inviteCodeStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
