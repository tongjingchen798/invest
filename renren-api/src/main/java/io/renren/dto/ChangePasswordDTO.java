

package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 修改密码DTO
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@ApiModel(value = "修改密码")
public class ChangePasswordDTO {
    @ApiModelProperty(value = "原密码")
    @NotBlank(message = "原密码不能为空")
    private String oldPassword;

    @ApiModelProperty(value = "新密码")
    @NotBlank(message = "新密码不能为空")
    private String newPassword;

    @ApiModelProperty(value = "确认新密码")
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
}
