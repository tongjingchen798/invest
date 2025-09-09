package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 充值订单详情DTO
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@ApiModel(value = "充值订单详情")
public class ChargeOrderDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "u兑换法币汇率(1U=多少法币)")
    private BigDecimal uprice;

    @ApiModelProperty(value = "实际充值成功得到的金额")
    private Long amount;

    @ApiModelProperty(value = "充值u数量")
    private BigDecimal uamount;

    @ApiModelProperty(value = "实际支付u数量(后台有个充值折扣比例, 每次会扣减)")
    private BigDecimal uRealAmout;

    @ApiModelProperty(value = "钱包地址")
    private String walletAddr;

    @ApiModelProperty(value = "钱包ID")
    private Long walletId;
}
