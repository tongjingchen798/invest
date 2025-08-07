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
 * 付息还本
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Data
@ApiModel(value = "ProfitDTO")
public class ProfitDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "投资项目ID")
    private Long investId;

    @ApiModelProperty(value = "项目名称")
    private String investName;

    @ApiModelProperty(value = "投资金额")
    private Long investmentAmount;

    @ApiModelProperty(value = "收益金额")
    private Long profitAmount;

    @ApiModelProperty(value = "收益利息")
    private Long profitInterest;

    @ApiModelProperty(value = "收益本金")
    private Long profitPrincipal;

    @ApiModelProperty(value = "等待收益")
    private Long ddsy;

    @ApiModelProperty(value = "项目周期")
    private Integer cycle;

    @ApiModelProperty(value = "周期类型（1:到期收益含本金,2:每日返本金到期收益）")
    private Integer cycleType;

    @ApiModelProperty(value = "投资日期")
    private String orderDate;

    @ApiModelProperty(value = "收益日期")
    private String profitDate;

    @ApiModelProperty(value = "下单ID")
    private Long orderId;

    @ApiModelProperty(value = "订单号简称")
    private String orderAbbr;

    @ApiModelProperty(value = "投资总数")
    private Integer investCount;

    @ApiModelProperty(value = "图片")
    private String img;

    @ApiModelProperty(value = "状态 0：未收益 1：已收益")
    private Integer status;

    @ApiModelProperty(value = "代理ID")
    private String agent;

    @ApiModelProperty(value = "业务员ID")
    private String salesmanid;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "抢购分钟")
    private Integer rushMinute;
}
