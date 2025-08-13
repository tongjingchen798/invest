package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 我的投资信息
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@ApiModel(value = "我的投资信息")
public class MyInvestmentDTO {
    
    @ApiModelProperty(value = "代理")
    private String agent;
    
    @ApiModelProperty(value = "项目周期")
    private Integer cycle;
    
    @ApiModelProperty(value = "周期类型（1:到期收益含本金,2:每日返本金到期收益）")
    private Integer cycleType;
    
    @ApiModelProperty(value = "等待收益")
    private Integer ddsy;
    
    @ApiModelProperty(value = "ID")
    private Integer id;
    
    @ApiModelProperty(value = "图片")
    private String img;
    
    @ApiModelProperty(value = "投资总数")
    private Integer investCount;
    
    @ApiModelProperty(value = "投资项目id")
    private Integer investId;
    
    @ApiModelProperty(value = "项目名称")
    private String investName;
    
    @ApiModelProperty(value = "投资金额")
    private Integer investmentAmount;
    
    @ApiModelProperty(value = "手机号")
    private String mobile;
    
    @ApiModelProperty(value = "订单号简称")
    private String orderAbbr;
    
    @ApiModelProperty(value = "投资日期")
    private String orderDate;
    
    @ApiModelProperty(value = "下单id")
    private Integer orderId;
    
    @ApiModelProperty(value = "收益金额")
    private Integer profitAmount;
    
    @ApiModelProperty(value = "收益日期")
    private String profitDate;
    
    @ApiModelProperty(value = "收益利息")
    private Integer profitInterest;
    
    @ApiModelProperty(value = "收益本金")
    private Integer profitPrincipal;
    
    @ApiModelProperty(value = "抢购分钟数")
    private Integer rushMinute;
    
    @ApiModelProperty(value = "销售员ID")
    private String salesmanid;
    
    @ApiModelProperty(value = "状态 0：未收益 1：已收益")
    private Integer status;
    
    @ApiModelProperty(value = "用户id")
    private Integer userId;
}
