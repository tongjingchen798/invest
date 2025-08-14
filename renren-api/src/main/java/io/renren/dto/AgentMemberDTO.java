package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 代理会员
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@ApiModel(value = "代理会员")
public class AgentMemberDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "会员id")
    private String id;

    @ApiModelProperty(value = "会员名")
    private String username;

    @ApiModelProperty(value = "累计投资")
    private String hirstory_tz;

    @ApiModelProperty(value = "vip等级")
    private Integer vip;

    @ApiModelProperty(value = "我的下级")
    private List<AgentMemberDTO> xj;
}
