package io.renren.modules.paychannel.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;

/**
 * 支付渠道表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Data
@ApiModel(value = "支付渠道表")
public class PayChannelDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "渠道ID")
	private Long channelid;

	@ApiModelProperty(value = "渠道名称")
	private String channelName;

	@ApiModelProperty(value = "渠道类型")
	private String channelType;

	@ApiModelProperty(value = "商户ID")
	private String merchantid;

	@ApiModelProperty(value = "商户名称")
	private String merchantname;

	@ApiModelProperty(value = "状态（0:禁用,1:启用）")
	private Integer status;

	@ApiModelProperty(value = "usdt赠送比例")
	private BigDecimal usdtGiftRatio;

	@ApiModelProperty(value = "usdt兑当地货币汇率	")
	private BigDecimal usdtLocalCurrencyRate;

	@ApiModelProperty(value = "充提类型（1:充值,2:提现,3:充提）")
	private Integer chargeorwithdraw;

	@ApiModelProperty(value = "创建时间")
	private Date createDate;

	@ApiModelProperty(value = "更新时间")
	private Date updateDate;


}