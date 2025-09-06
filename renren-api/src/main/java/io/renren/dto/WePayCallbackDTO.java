package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * WePay支付回调参数DTO
 * 
 * @author renren
 * @date 2024-01-01
 */
@Data
@ApiModel("WePay支付回调参数")
public class WePayCallbackDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("系统订单号")
    private String tradeNo;
    
    @ApiModelProperty("商户订单号")
    private String orderNo;
    
    @ApiModelProperty("订单金额")
    private BigDecimal orderAmount;
    
    @ApiModelProperty("实际支付金额")
    private BigDecimal amount;
    
    @ApiModelProperty("支付状态: 0-订单生成, 1-支付成功, 2-支付失败")
    private Integer payStatus;
    
    @ApiModelProperty("支付时间")
    private String payTime;
    
    @ApiModelProperty("手续费")
    private BigDecimal charge;
    
    @ApiModelProperty("扩展字段，支付中心回调时会原样返回")
    private String otherData;
    
    @ApiModelProperty("是否反转订单")
    private Boolean reverse;
    
    @ApiModelProperty("付款失败备注")
    private String remark;
    
    @ApiModelProperty("签名值")
    private String sign;
}
