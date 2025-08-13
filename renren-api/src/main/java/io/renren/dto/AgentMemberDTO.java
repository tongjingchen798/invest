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
    private Long id;

    @ApiModelProperty(value = "会员名")
    private String username;

    @ApiModelProperty(value = "vip等级")
    private Integer vip;

    @ApiModelProperty(value = "累计佣金")
    private Long hirstory_amt;

    @ApiModelProperty(value = "累计投资")
    private Long hirstory_tz;

    @ApiModelProperty(value = "今日佣金")
    private Long today_amt;

    @ApiModelProperty(value = "今日投资")
    private Long today_tz;

    @ApiModelProperty(value = "佣金比例")
    private Double yj_rate;

    @ApiModelProperty(value = "我的下级")
    private List<AgentMemberDTO> xj;
}
