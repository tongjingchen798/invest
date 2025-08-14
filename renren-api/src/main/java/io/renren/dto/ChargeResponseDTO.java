package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 充值接口返回DTO
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@ApiModel(value = "充值接口返回")
public class ChargeResponseDTO {

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "支付订单号")
    private String pOrderNo;

    @ApiModelProperty(value = "支付链接")
    private String payUrl;

    @ApiModelProperty(value = "商户号")
    private String merchantNo;

    @ApiModelProperty(value = "USDT地址")
    private String addr;

    @ApiModelProperty(value = "u数量")
    private BigDecimal uamount;

    @ApiModelProperty(value = "u兑换法币汇率(1U=多少法币)")
    private BigDecimal uprice;

    @ApiModelProperty(value = "实际充值成功得到的金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "实际支付u数量(后台有个充值折扣比例, 每次会扣减)")
    private BigDecimal uRealAmout;

    @ApiModelProperty(value = "钱包地址")
    private String walletAddr;
}
