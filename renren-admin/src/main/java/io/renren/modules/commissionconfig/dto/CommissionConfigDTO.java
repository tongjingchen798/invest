package io.renren.modules.commissionconfig.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;

/**
 * 返佣比例配置表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Data
@ApiModel(value = "返佣比例配置表")
public class CommissionConfigDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键ID")
	private Long id;

	@ApiModelProperty(value = "状态 0:禁用 1:启用")
	private Integer status;

	@ApiModelProperty(value = "创建人")
	private String creator;

	@ApiModelProperty(value = "创建时间")
	private Date createDate;

	@ApiModelProperty(value = "更新人")
	private String updater;

	@ApiModelProperty(value = "更新时间")
	private Date updateDate;

	@ApiModelProperty(value = "一级返佣比例(%)")
	private BigDecimal areward;

	@ApiModelProperty(value = "二级返佣比例(%)")
	private BigDecimal breward;

	@ApiModelProperty(value = "三级返佣比例(%)")
	private BigDecimal creward;


}