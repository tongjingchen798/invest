package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 我的佣金
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@ApiModel(value = "我的佣金")
public class MyAgentDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "1级佣金")
    private Long list1_amt;

    @ApiModelProperty(value = "1级人数")
    private Long effectiveList1;

    @ApiModelProperty(value = "2级佣金")
    private Long list2_amt;

    @ApiModelProperty(value = "2级人数")
    private Long effectiveList2;

    @ApiModelProperty(value = "1级会员列表")
    private List<AgentMemberDTO> list1;
}
