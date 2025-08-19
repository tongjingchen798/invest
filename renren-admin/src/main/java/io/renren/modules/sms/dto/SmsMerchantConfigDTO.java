package io.renren.modules.sms.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 短信商户配置表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Data
@ApiModel(value = "短信商户配置表")
public class SmsMerchantConfigDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键ID")
	private Long id;

	@ApiModelProperty(value = "商户ID")
	private String captchaId;

	@ApiModelProperty(value = "商户名称")
	private String captchaName;

	@ApiModelProperty(value = "商户编号")
	private String captchaNo;

	@ApiModelProperty(value = "商户密钥")
	private String captchaKey;

	@ApiModelProperty(value = "短信模板内容")
	private String msg;

	@ApiModelProperty(value = "状态 0:禁用 1:启用")
	private Integer status;

	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	@ApiModelProperty(value = "更新时间")
	private Date updateTime;


}