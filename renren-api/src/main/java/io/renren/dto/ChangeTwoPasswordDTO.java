

package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 修改二级密码DTO
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@ApiModel(value = "修改二级密码")
public class ChangeTwoPasswordDTO {
    @ApiModelProperty(value = "原二级密码")
    @NotBlank(message = "原二级密码不能为空")
    private String oldTwoPassword;

    @ApiModelProperty(value = "新二级密码")
    @NotBlank(message = "新二级密码不能为空")
    private String newTwoPassword;

    @ApiModelProperty(value = "确认新二级密码")
    @NotBlank(message = "确认二级密码不能为空")
    private String confirmTwoPassword;
}
