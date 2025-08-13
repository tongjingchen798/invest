package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 签到请求
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@ApiModel(value = "签到请求")
public class SignInRequestDTO {
    @ApiModelProperty(value = "用户ID")
    private Long userId;
}
