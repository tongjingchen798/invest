package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 找回密码表单
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@ApiModel(value = "找回密码表单")
public class RetrievePasswordDTO {
    
    @ApiModelProperty(value = "验证码")
    private String captcha;
    
    @ApiModelProperty(value = "短信验证码")
    @NotBlank(message = "短信验证码不能为空")
    private String code;
    
    @ApiModelProperty(value = "手机号")
    @NotBlank(message = "手机号不能为空")
    private String mobile;
    
    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;
    
    @ApiModelProperty(value = "密码二次验证")
    @NotBlank(message = "确认密码不能为空")
    private String password2;
    
    @ApiModelProperty(value = "真实姓名")
    private String username;
    
    @ApiModelProperty(value = "唯一标识")
    private String uuid;
}
