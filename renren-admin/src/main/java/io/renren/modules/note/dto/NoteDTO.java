package io.renren.modules.note.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 公告表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Data
@ApiModel(value = "公告表")
public class NoteDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键ID")
	private Long id;

	@ApiModelProperty(value = "公告标题")
	private String title;

	@ApiModelProperty(value = "状态 0:禁用 1:启用")
	private Integer status;

	@ApiModelProperty(value = "创建时间")
	private Date createDate;


}