package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * QePay支付响应DTO
 * 根据QePay支付接口返回的JSON结构定义
 * 
 * @author nico
 * @date 2024-01-01
 */
@Data
@ApiModel("QePay支付响应")
public class QePayResponseDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("响应状态: SUCCESS-成功, FAIL-失败")
    private String respCode;
    
    @ApiModelProperty("响应消息")
    private String tradeMsg;
    
    @ApiModelProperty("订单状态: 1-下单成功")
    private String tradeResult;
    
    @ApiModelProperty("商户号")
    private String mchId;
    
    @ApiModelProperty("商家订单号")
    private String mchOrderNo;
    
    @ApiModelProperty("原始订单金额")
    private String oriAmount;
    
    @ApiModelProperty("订单金额")
    private String tradeAmount;
    
    @ApiModelProperty("订单时间")
    private String orderDate;
    
    @ApiModelProperty("平台订单号")
    private String orderNo;
    
    @ApiModelProperty("付款链接")
    private String payInfo;
    
    @ApiModelProperty("签名方式")
    private String signType;
    
    @ApiModelProperty("签名值")
    private String sign;
    
    /**
     * 判断响应是否成功
     */
    public boolean isSuccess() {
        return "SUCCESS".equals(this.respCode) && "1".equals(this.tradeResult);
    }
    
    /**
     * 获取支付链接
     */
    public String getPayUrl() {
        return this.payInfo;
    }
}
