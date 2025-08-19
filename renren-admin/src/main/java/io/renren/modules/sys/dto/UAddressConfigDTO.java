package io.renren.modules.sys.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;

/**
 * U地址配置表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Data
@ApiModel(value = "U地址配置表")
public class UAddressConfigDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键ID")
	private Long id;

	@ApiModelProperty(value = "名称")
	private String name;

	@ApiModelProperty(value = "地址")
	private String addr;

	@ApiModelProperty(value = "图片地址")
	private String img;

	@ApiModelProperty(value = "余额")
	private BigDecimal balance;

	@ApiModelProperty(value = "状态：0-禁用，1-启用")
	private Integer state;

	@ApiModelProperty(value = "创建时间")
	private Date createDate;

	@ApiModelProperty(value = "更新时间")
	private Date updateDate;


}