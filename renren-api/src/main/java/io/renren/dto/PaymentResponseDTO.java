package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 支付响应参数DTO
 * 
 * @author renren
 * @date 2024-01-01
 */
@Data
@ApiModel("支付响应参数")
public class PaymentResponseDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("状态码")
    private Integer code;
    
    @ApiModelProperty("状态描述")
    private String desc;
    
    @ApiModelProperty("状态信息")
    private String msg;
    
    @ApiModelProperty("请求状态")
    private Boolean success;
    
    @ApiModelProperty("返回数据")
    private PaymentData data;
    
    @Data
    @ApiModel("支付数据")
    public static class PaymentData implements Serializable {
        
        private static final long serialVersionUID = 1L;
        
        @ApiModelProperty("支付地址")
        private String payUrl;
        
        @ApiModelProperty("商户单号")
        private String orderNo;
        
        @ApiModelProperty("系统单号")
        private String tradeNo;
    }
}
