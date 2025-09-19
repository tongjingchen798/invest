package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * QePay支付回调参数DTO
 * 根据QePay异步通知参数定义
 * 
 * @author nico
 * @date 2024-01-01
 */
@Data
@ApiModel("QePay支付回调参数")
public class QePayCallbackDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("订单状态: 1-支付成功")
    private String tradeResult;
    
    @ApiModelProperty("商户号")
    private String mchId;
    
    @ApiModelProperty("商家订单号")
    private String mchOrderNo;
    
    @ApiModelProperty("原始订单金额")
    private String oriAmount;
    
    @ApiModelProperty("交易金额（实际支付金额）")
    private String amount;
    
    @ApiModelProperty("订单时间")
    private String orderDate;
    
    @ApiModelProperty("平台支付订单号")
    private String orderNo;
    
    @ApiModelProperty("透传参数")
    private String merRetMsg;
    
    @ApiModelProperty("签名方式")
    private String signType;
    
    @ApiModelProperty("签名值")
    private String sign;
}
