/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 用户信息DTO
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@ApiModel(value = "用户信息")
public class UserInfoDTO {
    @ApiModelProperty(value = "用户ID")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "邀请码")
    private String inviteCode;

    @ApiModelProperty(value = "代理信息")
    private String agent;

    @ApiModelProperty(value = "客户渠道号")
    private String channel;

    @ApiModelProperty(value = "登录端口(1:安卓, 2:ios, 3:pc, 4:未知)")
    private Integer equipment;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;
}
