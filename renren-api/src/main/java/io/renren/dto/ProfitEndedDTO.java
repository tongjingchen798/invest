package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 付息还本记录
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@ApiModel(value = "付息还本记录")
public class ProfitEndedDTO implements Serializable {
    private static final long serialVersionUID = 1L;

//    @ApiModelProperty(value = "待收金额（分）")
//    private Long dsAmount;
//
//    @ApiModelProperty(value = "待收本金（分）")
//    private Long dsbjAmount;
//
//    @ApiModelProperty(value = "待收利息（分）")
//    private Long dslxAmount;

    @ApiModelProperty(value = "项目数")
    private Long items;

//    @ApiModelProperty(value = "今日收益（分）")
//    private Long jrAmount;

    @ApiModelProperty(value = "今日收益（分）")
    private Long jrProfit;

    @ApiModelProperty(value = "本金（分）")
    private Long totalPrincipal;

    @ApiModelProperty(value = "总收益（分）")
    private Long totalProfit;
//
//    @ApiModelProperty(value = "已收利息（分）")
//    private Long ysAmount;
//
//    @ApiModelProperty(value = "已收本金（分）")
//    private Long ysbjAmount;
}
