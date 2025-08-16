package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 项目详情DTO
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Data
@ApiModel(value = "项目详情")
public class ProjectDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "项目ID")
    private String investId;

    @ApiModelProperty(value = "项目名称")
    private String investName;

    @ApiModelProperty(value = "项目简称")
    private String abbreviation;

    @ApiModelProperty(value = "项目状态（0:下架,1:上架,2:删除）")
    private Integer status;

    @ApiModelProperty(value = "可买台数")
    private Integer investRepeat;

    @ApiModelProperty(value = "项目类型（0:默认类型,1:固定金额投资,2:众筹）")
    private Integer projectType;

    @ApiModelProperty(value = "回款方式（1:到期返还,2:每日返利,3:不返本金,4:复利产品,5:阶梯日益,6:拼团）")
    private Integer cycleType;

    @ApiModelProperty(value = "项目规模金额")
    private String scaleAmount;

    @ApiModelProperty(value = "投资周期(天)")
    private Integer cycle;

    @ApiModelProperty(value = "日收益率(%)")
    private String conversion;

    @ApiModelProperty(value = "每日收益")
    private String principalProfit;

    @ApiModelProperty(value = "总收益")
    private String totalProfit;

    @ApiModelProperty(value = "总成本(本金+收益)")
    private String totalCost;

    @ApiModelProperty(value = "分类ID")
    private String typeId;

    @ApiModelProperty(value = "项目图片地址")
    private String img;

    @ApiModelProperty(value = "项目描述")
    private String projectDescribe;

    @ApiModelProperty(value = "排序权重")
    private Integer sort;

    @ApiModelProperty(value = "VIP等级要求")
    private Integer vip;
}
