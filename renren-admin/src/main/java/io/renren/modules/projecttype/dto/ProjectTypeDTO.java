package io.renren.modules.projecttype.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 投资项目分类表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-20
 */
@Data
@ApiModel(value = "投资项目分类表")
public class ProjectTypeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "项目分类id")
	private Long typeId;

	@ApiModelProperty(value = "分类名称")
	private String typeName;

	@ApiModelProperty(value = "排序")
	private Integer sort;

	@ApiModelProperty(value = "状态 0=禁用 1=启用")
	private Integer status;

	@ApiModelProperty(value = "创建时间")
	private Date createDate;

	@ApiModelProperty(value = "更新时间")
	private Date updateDate;

	@ApiModelProperty(value = "创建者ID")
	private Long sysCreateUserId;

	@ApiModelProperty(value = "更新者ID")
	private Long sysUpdateUserId;


}