package io.renren.modules.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 用户登录日志表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-20
 */
@Data
@ApiModel(value = "用户登录日志表")
public class UserLogDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键ID")
	private Long id;

	@ApiModelProperty(value = "用户ID")
	private Long userId;

	@ApiModelProperty(value = "销售员姓名")
	private String salesmanName;

	@ApiModelProperty(value = "手机号")
	private String mobile;

	@ApiModelProperty(value = "登录时间")
	private Date loginTime;

	@ApiModelProperty(value = "登出时间")
	private Date logoutTime;

	@ApiModelProperty(value = "登录IP地址")
	private String loginIp;

	@ApiModelProperty(value = "设备类型 1:PC 2:移动端 3:APP")
	private Integer equipment;

	@ApiModelProperty(value = "销售员ID")
	private Long salesmanid;

	@ApiModelProperty(value = "代理ID")
	private Long agent;

	@ApiModelProperty(value = "标签")
	private String biaoqian;

	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	@ApiModelProperty(value = "更新时间")
	private Date updateTime;


}