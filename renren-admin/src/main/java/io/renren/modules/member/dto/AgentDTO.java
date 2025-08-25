package io.renren.modules.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 代理DTO
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@ApiModel(value = "代理")
public class AgentDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "代理ID")
    private Long id;

    @ApiModelProperty(value = "账号")
    private String mobile;

    @ApiModelProperty(value = "类型")
    private Integer type;

    @ApiModelProperty(value = "姓名")
    private String username;
}
