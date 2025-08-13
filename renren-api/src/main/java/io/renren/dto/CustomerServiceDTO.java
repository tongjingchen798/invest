package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 客服号DTO
 *
 * @author renren
 * @since 2024-01-01
 */
@Data
@ApiModel(value = "客服号信息")
public class CustomerServiceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "客服ID")
    private String id;

    @ApiModelProperty(value = "用户名")
    private String username;
}
