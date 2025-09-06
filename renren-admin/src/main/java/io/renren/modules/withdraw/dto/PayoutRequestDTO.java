package io.renren.modules.withdraw.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * WePay代付请求DTO
 * 
 * @author renren
 * @date 2024-01-01
 */
@ApiModel("WePay代付请求参数")
public class PayoutRequestDTO {
    
    @ApiModelProperty(value = "商户ID", required = true)
    private String mchId;
    
    @ApiModelProperty(value = "通道ID", required = true)
    private String passageId;
    
    @ApiModelProperty(value = "商户订单号", required = true)
    private String orderNo;
    
    @ApiModelProperty(value = "银行/UPI账号", required = true)
    private String account;
    
    @ApiModelProperty(value = "银行/UPI用户姓名", required = true)
    private String userName;
    
    @ApiModelProperty(value = "银行备注")
    private String ifsc;
    
    @ApiModelProperty(value = "号码备注")
    private String number;
    
    @ApiModelProperty(value = "邮箱")
    private String email;
    
    @ApiModelProperty(value = "金额(元)", required = true)
    private Double amount;
    
    @ApiModelProperty(value = "商户回调地址", required = true)
    private String notifyUrl;
    
    @ApiModelProperty(value = "扩展字段")
    private String otherData;
    
    @ApiModelProperty(value = "参数签名", required = true)
    private String sign;
    
    // Getters and Setters
    public String getMchId() {
        return mchId;
    }
    
    public void setMchId(String mchId) {
        this.mchId = mchId;
    }
    
    public String getPassageId() {
        return passageId;
    }
    
    public void setPassageId(String passageId) {
        this.passageId = passageId;
    }
    
    public String getOrderNo() {
        return orderNo;
    }
    
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    
    public String getAccount() {
        return account;
    }
    
    public void setAccount(String account) {
        this.account = account;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getIfsc() {
        return ifsc;
    }
    
    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }
    
    public String getNumber() {
        return number;
    }
    
    public void setNumber(String number) {
        this.number = number;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Double getAmount() {
        return amount;
    }
    
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    
    public String getNotifyUrl() {
        return notifyUrl;
    }
    
    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
    
    public String getOtherData() {
        return otherData;
    }
    
    public void setOtherData(String otherData) {
        this.otherData = otherData;
    }
    
    public String getSign() {
        return sign;
    }
    
    public void setSign(String sign) {
        this.sign = sign;
    }
}
