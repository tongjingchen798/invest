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
@ApiModel(value = "问题记录")
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

	@ApiModelProperty(value = "创建时间")
	private Date createDate;




}