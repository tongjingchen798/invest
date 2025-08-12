package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 上级用户信息DTO
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@ApiModel(value = "上级用户信息")
public class SuperiorUserInfoDTO {
    @ApiModelProperty(value = "上级用户ID")
    private Long superiorId;

    @ApiModelProperty(value = "上级用户名")
    private String superiorUsername;

    @ApiModelProperty(value = "上级邀请码")
    private String superiorInviteCode;

    @ApiModelProperty(value = "上级U级账户余额")
    private Long superiorUa;

    @ApiModelProperty(value = "上级U级账户余额")
    private Long superiorUb;

    @ApiModelProperty(value = "上级U级账户余额")
    private Long superiorUc;

    @ApiModelProperty(value = "上级代理信息")
    private String superiorAgent;

    @ApiModelProperty(value = "上级代理名称")
    private String superiorAgentName;
}


