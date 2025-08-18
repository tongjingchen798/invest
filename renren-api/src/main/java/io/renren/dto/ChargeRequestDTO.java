//package io.renren.dto;
//
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Data;
//
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Min;
//import java.io.Serializable;
//
///**
// * 充值请求DTO
// *
// * @author renren
// * @email renren@gmail.com
// * @date 2024-01-01 00:00:00
// */
//@Data
//@ApiModel(value = "充值请求")
//public class ChargeRequestDTO implements Serializable {
//    private static final long serialVersionUID = 1L;
//
//    @NotNull(message = "充值金额不能为空")
//    @Min(value = 1, message = "充值金额必须大于0")
//    @ApiModelProperty(value = "充值金额", required = true, example = "100000")
//    private Long amount;
//
//    @NotNull(message = "充值类型不能为空")
//    @ApiModelProperty(value = "充值类型 1银行卡 2虚拟币 3 upi 4 Paytm", required = true, example = "1")
//    private Integer charge_type;
//
//    @ApiModelProperty(value = "支付通道主键", example = "1")
//    private Long channelid;
//}
