 

package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 更新用户信息DTO
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@ApiModel(value = "更新用户信息")
public class UpdateUserDTO {
    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "邀请码")
    private String inviteCode;

    @ApiModelProperty(value = "代理信息")
    private Long agent;

    @ApiModelProperty(value = "客户渠道号")
    private Long channel;

    @ApiModelProperty(value = "登录端口(1:安卓, 2:ios, 3:pc, 4:未知)")
    private Integer equipment;
}
