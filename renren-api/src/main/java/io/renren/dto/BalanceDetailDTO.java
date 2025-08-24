package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 账变记录DTO
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@ApiModel(value = "账变记录")
public class BalanceDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "记录ID")
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "交易流水ID")
    private Long streamId;

    @ApiModelProperty(value = "交易类型（1购买流水,2提现流水,3佣金流水,4签到流水,39转盘中奖）")
    private Integer busiType;

    @ApiModelProperty(value = "原始金额")
    private Long originalAmount;

    @ApiModelProperty(value = "使用金额")
    private Long useAmount;

    @ApiModelProperty(value = "交易后金额")
    private Long transactionAmount;

    @ApiModelProperty(value = "交易时间")
    private String transactionDate;

    @ApiModelProperty(value = "状态 0：交易失败 1：正常")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "渠道")
    private Long channel;

    @ApiModelProperty(value = "代理信息")
    private String agent;

    @ApiModelProperty(value = "业务员ID")
    private Long salesmanid;

    @ApiModelProperty(value = "返佣来源，谁返给userId的")
    private Long formuserid;

    @ApiModelProperty(value = "优惠券金额")
    private Long yhqAmount;
}
