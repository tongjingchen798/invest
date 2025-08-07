/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 账变明细
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Data
@ApiModel(value = "账变明细")
public class TransactionDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "交易时间")
    private String transactionDate;

    @ApiModelProperty(value = "流水号")
    private String streamId;

    @ApiModelProperty(value = "业务类型")
    private Integer busiType;

    @ApiModelProperty(value = "原始金额(分)")
    private Long originalAmount;

    @ApiModelProperty(value = "使用金额(分)")
    private Long useAmount;

    @ApiModelProperty(value = "优惠券金额(分)")
    private Long yhqAmount;

    @ApiModelProperty(value = "交易后金额(分)")
    private Long transactionAmount;

    @ApiModelProperty(value = "状态 1:成功 0:失败")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "渠道")
    private String channel;

    @ApiModelProperty(value = "来源用户ID")
    private String formuserid;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "代理ID")
    private String agent;

    @ApiModelProperty(value = "业务员ID")
    private String salesmanid;

    @ApiModelProperty(value = "代理姓名")
    private String agentName;

    @ApiModelProperty(value = "业务员姓名")
    private String salesmanName;

    @ApiModelProperty(value = "标签")
    private String biaoqian;

    @ApiModelProperty(value = "邀请码状态 1:有效 0:无效")
    private Integer inviteCodeStatus;
}
