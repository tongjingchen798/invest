package io.renren.modules.withdraw.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * WePay回调数据DTO
 * 
 * @author renren
 * @date 2024-01-01
 */
@ApiModel("WePay回调数据")
public class WePayCallbackDTO {
    
    @ApiModelProperty(value = "第三方订单号")
    private String tradeNo;
    
    @ApiModelProperty(value = "商户订单号")
    private String orderNo;
    
    @ApiModelProperty(value = "订单金额")
    private BigDecimal orderAmount;
    
    @ApiModelProperty(value = "实际支付金额")
    private BigDecimal amount;
    
    @ApiModelProperty(value = "支付状态")
    private Integer payStatus;
    
    @ApiModelProperty(value = "支付时间")
    private String payTime;
    
    @ApiModelProperty(value = "手续费")
    private BigDecimal charge;
    
    @ApiModelProperty(value = "扩展字段")
    private String otherData;
    
    @ApiModelProperty(value = "是否反转")
    private Boolean reverse;
    
    @ApiModelProperty(value = "备注")
    private String remark;
    
    @ApiModelProperty(value = "签名")
    private String sign;
    
    // Getters and Setters
    public String getTradeNo() {
        return tradeNo;
    }
    
    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }
    
    public String getOrderNo() {
        return orderNo;
    }
    
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    
    public BigDecimal getOrderAmount() {
        return orderAmount;
    }
    
    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public Integer getPayStatus() {
        return payStatus;
    }
    
    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }
    
    public String getPayTime() {
        return payTime;
    }
    
    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }
    
    public BigDecimal getCharge() {
        return charge;
    }
    
    public void setCharge(BigDecimal charge) {
        this.charge = charge;
    }
    
    public String getOtherData() {
        return otherData;
    }
    
    public void setOtherData(String otherData) {
        this.otherData = otherData;
    }
    
    public Boolean getReverse() {
        return reverse;
    }
    
    public void setReverse(Boolean reverse) {
        this.reverse = reverse;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public String getSign() {
        return sign;
    }
    
    public void setSign(String sign) {
        this.sign = sign;
    }
}
