

package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 广告/图片管理DTO
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@ApiModel(value = "广告/图片管理")
public class IssuesDTO {
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
}