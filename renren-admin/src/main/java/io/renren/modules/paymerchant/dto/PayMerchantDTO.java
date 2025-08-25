package io.renren.modules.paymerchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;

/**
 * 支付商户配置表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Data
@ApiModel(value = "支付商户配置表")
public class PayMerchantDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "商户ID")
	private Long merchantid;

	@ApiModelProperty(value = "商户号")
	private String merchantno;

	@ApiModelProperty(value = "商户名")
	private String merchantname;

	@ApiModelProperty(value = "密钥")
	private String channelkey;

	@ApiModelProperty(value = "密码")
	private String password;

	@ApiModelProperty(value = "代收费率%")
	private BigDecimal dsFree;

	@ApiModelProperty(value = "代付费率%")
	private BigDecimal dfFree;

	@ApiModelProperty(value = "单笔手续费")
	private BigDecimal oneFree;

	@ApiModelProperty(value = "后台管理地址")
	private String houtaiurl;

	@ApiModelProperty(value = "代收-通道代码")
	private String channeltypeds;

	@ApiModelProperty(value = "代付-通道代码")
	private String channeltypedf;

	@ApiModelProperty(value = "优先级")
	private Integer degreeheat;

	@ApiModelProperty(value = "状态（0:下架,1:上架）")
	private Integer status;

	@ApiModelProperty(value = "创建时间")
	private Date createDate;

	@ApiModelProperty(value = "更新时间")
	private Date updateDate;


}