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

import javax.validation.constraints.NotBlank;

/**
 * 注册表单
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@ApiModel(value = "注册表单")
public class RegisterDTO {
    @ApiModelProperty(value = "手机号")
    @NotBlank(message="手机号不能为空")
    private String mobile;

    @ApiModelProperty(value = "密码")
    @NotBlank(message="密码不能为空")
    private String password;

    @ApiModelProperty(value = "真实姓名")
    private String username;

    @ApiModelProperty(value = "二级密码")
    private String twoPwd;

    @ApiModelProperty(value = "邀请码")
    private String inviteCode;

    @ApiModelProperty(value = "短信验证码")
    private String code;

    @ApiModelProperty(value = "验证码")
    private String captcha;

    @ApiModelProperty(value = "唯一标识")
    private String uuid;

    @ApiModelProperty(value = "代理信息")
    private String agent;

    @ApiModelProperty(value = "客户渠道号")
    private String channel;

    @ApiModelProperty(value = "登录端口(1:安卓, 2:ios, 3:pc, 4:未知)")
    private Integer equipment;
}
