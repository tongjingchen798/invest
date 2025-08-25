package io.renren.modules.sys.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 客服设置DTO
 *
 * @author renren
 * @since 1.0.0
 */
@Data
@ApiModel(value = "客服设置")
public class CustomerServiceSettingDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long id;

    @ApiModelProperty(value = "客服的tg号码")
    private String tgnumber;

    @ApiModelProperty(value = "客服的头像")
    private String wsimage;

    @ApiModelProperty(value = "客服的名字")
    private String wsname;

    @ApiModelProperty(value = "客服的号码")
    private String wsnumber;
}
