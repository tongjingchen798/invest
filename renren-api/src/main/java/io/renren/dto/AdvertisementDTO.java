package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 广告素材DTO
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2025-07-10 20:05:01
 */
@Data
@ApiModel(value = "广告素材")
public class AdvertisementDTO {
    @ApiModelProperty(value = "主键ID")
    private Integer id;

    @ApiModelProperty(value = "广告类型 1=LOG,2=轮播图，3=个人中心 4=弹窗广告")
    private Integer type;

    @ApiModelProperty(value = "广告标题")
    private String title;

    @ApiModelProperty(value = "图片地址")
    private String logosAddr;

    @ApiModelProperty(value = "链接地址")
    private String logosLinkaddr;

    @ApiModelProperty(value = "备注描述")
    private String remark;

    @ApiModelProperty(value = "生效时间")
    private String createDate;

    @ApiModelProperty(value = "是否弹窗（根据type字段计算：4=弹窗广告）")
    private Integer isPop;

    @ApiModelProperty(value = "生效时间")
    private String sxDate;

    @ApiModelProperty(value = "展示时长（小时）")
    private Integer hour;
}
