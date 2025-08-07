package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 余额表单
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Data
@ApiModel(value = "余额表单")
public class BalanceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "可用余额分为单位")
    private Long assets;

    @ApiModelProperty(value = "账户余额/我的资产=余额+可提现+等待收益")
    private Long balance;

    @ApiModelProperty(value = "可提现")
    private Long cashwithdrawable;

    @ApiModelProperty(value = "累计收益 = 代收收益 + 已收收益")
    private Long cumulative;

    @ApiModelProperty(value = "累计充值")
    private Long czAmount;

    @ApiModelProperty(value = "待收利息/等待回收")
    private Long dsAmount;

    @ApiModelProperty(value = "待收本金")
    private Long dsbjAmount;

    @ApiModelProperty(value = "今日收益")
    private Long jrAmount;

    @ApiModelProperty(value = "累计投资")
    private Long tzAmount;

    @ApiModelProperty(value = "已收利息")
    private Long ysAmount;

    @ApiModelProperty(value = "已收本金")
    private Long ysbjAmount;

    @ApiModelProperty(value = "已提现")
    private Long ytxAmount;

    @ApiModelProperty(value = "正在提现")
    private Long zztxAmount;

    @ApiModelProperty(value = "转盘次数")
    private Integer wheelTimes;

    @ApiModelProperty(value = "更新时间")
    private String updateDate;
}
