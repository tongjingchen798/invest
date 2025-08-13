package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 我的佣金DTO
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@ApiModel(value = "我的佣金")
public class MyAgentDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "累计佣金")
    private Long hirstory_amt;

    @ApiModelProperty(value = "累计工资")
    private Long hirstory_gzamt;

    @ApiModelProperty(value = "今日佣金")
    private Long today_amt;

    @ApiModelProperty(value = "今日工资")
    private Long today_gzamt;

    @ApiModelProperty(value = "可提佣金")
    private Long withdraw_amt;

    @ApiModelProperty(value = "已提佣金")
    private Long yt_withdraw_amt;

    @ApiModelProperty(value = "一级佣金")
    private Long list1_amt;

    @ApiModelProperty(value = "二级佣金")
    private Long list2_amt;

    @ApiModelProperty(value = "有效一级会员数")
    private Long effectiveList1;

    @ApiModelProperty(value = "本月有效一级会员数")
    private Long effectiveList1Month;

    @ApiModelProperty(value = "有效二级会员数")
    private Long effectiveList2;

    @ApiModelProperty(value = "本月有效二级会员数")
    private Long effectiveList2Month;

    @ApiModelProperty(value = "一级会员列表")
    private List<AgentMemberDTO> list1;

    @ApiModelProperty(value = "二级会员列表")
    private List<AgentMemberDTO> list2;

    @ApiModelProperty(value = "三级会员列表")
    private List<AgentMemberDTO> list3;
}
