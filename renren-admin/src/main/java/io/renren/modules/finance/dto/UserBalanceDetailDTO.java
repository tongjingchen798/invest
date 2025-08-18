package io.renren.modules.finance.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 账变明细DTO
 *
 * @author renren
 * @since 1.0.0
 */
@Data
@ApiModel(value = "账变明细")
public class UserBalanceDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "代理")
    private String agent;

    @ApiModelProperty(value = "代理名称")
    private String agentName;

    @ApiModelProperty(value = "标签")
    private String biaoqian;

    @ApiModelProperty(value = "交易类型（自定义如1购买流水,2提现流水,3佣金流水,4 签到流水）")
    private Integer busiType;

    @ApiModelProperty(value = "渠道")
    private String channel;

    @ApiModelProperty(value = "返佣来源，谁返给userId的")
    private Long formuserid;

    @ApiModelProperty(value = "邀请码状态")
    private Integer inviteCodeStatus;

    @ApiModelProperty(value = "用户账号")
    private String mobile;

    @ApiModelProperty(value = "原始金额")
    private Long originalAmount;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "业务员名称")
    private String salesmanName;

    @ApiModelProperty(value = "业务员编号")
    private String salesmanid;

    @ApiModelProperty(value = "状态 0：交易失败 1：正常")
    private Integer status;

    @ApiModelProperty(value = "交易流水id")
    private String streamId;

    @ApiModelProperty(value = "交易后金额")
    private Long transactionAmount;

    @ApiModelProperty(value = "交易时间")
    private String transactionDate;

    @ApiModelProperty(value = "使用金额")
    private Long useAmount;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "优惠券金额")
    private Long yhqAmount;
}
