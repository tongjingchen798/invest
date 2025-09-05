package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

/**
 * 支付请求参数DTO
 * 
 * @author renren
 * @date 2024-01-01
 */
@Data
@ApiModel("支付请求参数")
public class PaymentRequestDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value = "商户ID", required = true)
    @NotBlank(message = "商户ID不能为空")
    private String mchId;
    
    @ApiModelProperty(value = "通道ID", required = true)
    @NotBlank(message = "通道ID不能为空")
    private String passageId;
    
    @ApiModelProperty(value = "金额(元)", required = true)
    @NotNull(message = "金额不能为空")
    @Positive(message = "金额必须大于0")
    private Long amount;
    
    @ApiModelProperty(value = "商户订单号", required = true)
    @NotBlank(message = "商户订单号不能为空")
    private String orderNo;
    
    @ApiModelProperty(value = "异步通知回调地址", required = true)
    @NotBlank(message = "异步通知回调地址不能为空")
    private String notifyUrl;
    
    @ApiModelProperty(value = "充值成功回跳地址")
    private String callBackUrl;
    
    @ApiModelProperty(value = "扩展字段，支付中心回调时会原样返回")
    private String otherData;
    
    @ApiModelProperty(value = "备注(巴西必填银行编码，泰国网银必填汇款银行名称)")
    private String remark;
    
    @ApiModelProperty(value = "号码备注(泰国网银必填卡号)")
    private String number;
    
    @ApiModelProperty(value = "名字备注(泰国网银必填汇款人实名)")
    private String userName;
    
    @ApiModelProperty(value = "邮箱")
    private String email;
    
    @ApiModelProperty(value = "参数签名", required = true)
    @NotBlank(message = "参数签名不能为空")
    private String sign;
}
