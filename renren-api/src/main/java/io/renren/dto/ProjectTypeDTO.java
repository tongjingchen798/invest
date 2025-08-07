

 package io.renren.dto;

 import io.swagger.annotations.ApiModel;
 import io.swagger.annotations.ApiModelProperty;
 import lombok.Data;
 
 import java.io.Serializable;
 
 /**
  * 投资项目分类DTO
  *
  * @author Mark sunlightcs@gmail.com
  * @since 1.0.0
  */
 @Data
 @ApiModel(value = "投资项目分类表")
 public class ProjectTypeDTO implements Serializable {
     private static final long serialVersionUID = 1L;
 
     @ApiModelProperty(value = "项目分类id")
     private Long typeId;
 
     @ApiModelProperty(value = "分类名称")
     private String typeName;
 }