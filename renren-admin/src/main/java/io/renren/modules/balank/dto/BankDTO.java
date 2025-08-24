package io.renren.modules.balank.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 银行管理表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Data
@ApiModel(value = "银行管理表")
public class BankDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "ID")
	private Long id;

	@ApiModelProperty(value = "银行简称")
	private String blankCode;

	@ApiModelProperty(value = "银行名称")
	private String blankName;

	@ApiModelProperty(value = "渠道")
	private String channel;

	@ApiModelProperty(value = "货币")
	private String currency;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "状态 0：停用 1：正常")
	private Integer state;

	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	@ApiModelProperty(value = "状态时间")
	private Date stateTime;


}