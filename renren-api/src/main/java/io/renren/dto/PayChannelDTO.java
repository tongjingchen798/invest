package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 支付通道
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@ApiModel(value = "支付通道")
public class PayChannelDTO {
    @ApiModelProperty(value = "ID")
    private Long channelid;

    @ApiModelProperty(value = "通道名,前端显示用的")
    private String channelName;

    @ApiModelProperty(value = "通道类型 UPI/SWIPE/USDT")
    private String channelType;

    @ApiModelProperty(value = "充值 1 或者提现 2")
    private Integer chargeorwithdraw;

    @ApiModelProperty(value = "商户主键")
    private Long merchantid;

    @ApiModelProperty(value = "上下架 0：下架 1：上架")
    private Integer status;

    @ApiModelProperty(value = "usdt赠送比例")
    private BigDecimal usdtGiftRatio;

    @ApiModelProperty(value = "usdt兑当地货币汇率")
    private BigDecimal usdtLocalCurrencyRate;

    @ApiModelProperty(value = "创建时间")
    private String createDate;

    @ApiModelProperty(value = "更新时间")
    private String updateDate;
}
