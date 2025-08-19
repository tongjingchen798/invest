package io.renren.modules.signconfig.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 签到奖励配置表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Data
@ApiModel(value = "签到奖励配置表")
public class SignRewardConfigDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键ID")
	private Long id;

	@ApiModelProperty(value = "第1天奖励金额(分)")
	private Long oneDay;

	@ApiModelProperty(value = "第2天奖励金额(分)")
	private Long twoDay;

	@ApiModelProperty(value = "第3天奖励金额(分)")
	private Long threeDay;

	@ApiModelProperty(value = "第4天奖励金额(分)")
	private Long fourDay;

	@ApiModelProperty(value = "第5天奖励金额(分)")
	private Long fiveDay;

	@ApiModelProperty(value = "第6天奖励金额(分)")
	private Long sixDay;

	@ApiModelProperty(value = "第7天奖励金额(分)")
	private Long sevenDay;

	@ApiModelProperty(value = "首次7天奖励金额(分)")
	private Long firstsevenDay;

	@ApiModelProperty(value = "状态 0:禁用 1:启用")
	private Integer status;

	@ApiModelProperty(value = "第1天签到类型 1:积分 2:余额")
	private Integer oneDayqdtype;

	@ApiModelProperty(value = "第2天签到类型 1:积分 2:余额")
	private Integer twoDayqdtype;

	@ApiModelProperty(value = "第3天签到类型 1:积分 2:余额")
	private Integer threeDayqdtype;

	@ApiModelProperty(value = "第4天签到类型 1:积分 2:余额")
	private Integer fourDayqdtype;

	@ApiModelProperty(value = "第5天签到类型 1:积分 2:余额")
	private Integer fiveDayqdtype;

	@ApiModelProperty(value = "第6天签到类型 1:积分 2:余额")
	private Integer sixDayqdtype;

	@ApiModelProperty(value = "第7天签到类型 1:积分 2:余额")
	private Integer sevenDayqdtype;

	@ApiModelProperty(value = "首次7天签到类型 1:积分 2:余额")
	private Integer firstsevenDayqdtype;

	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	@ApiModelProperty(value = "更新时间")
	private Date updateTime;


}