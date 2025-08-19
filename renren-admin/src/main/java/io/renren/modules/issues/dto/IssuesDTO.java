package io.renren.modules.issues.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 广告/图片管理表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Data
@ApiModel(value = "广告/图片管理表")
public class IssuesDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键ID")
	private Long id;

	@ApiModelProperty(value = "名称")
	private String title;

	@ApiModelProperty(value = "描述")
	private String content;

	@ApiModelProperty(value = "链接地址")
	private String imagesAddr;

	@ApiModelProperty(value = "广告类型 1=LOG,2=轮播图，3=个人中心 4=弹窗广告")
	private Integer type;

	@ApiModelProperty(value = "排序")
	private Integer sort;

	@ApiModelProperty(value = "状态 0=禁用 1=启用")
	private Integer status;

	@ApiModelProperty(value = "创建时间")
	private Date createDate;

	@ApiModelProperty(value = "更新时间")
	private Date updateDate;

	@ApiModelProperty(value = "创建者")
	private Long creator;

	@ApiModelProperty(value = "更新者")
	private Long updater;


}