package io.renren.modules.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 代理详情DTO
 *
 * @author renren
 * @since 1.0.0
 */
@Data
@ApiModel(value = "代理详情")
public class AgentDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "级别")
    private Integer level;

    @ApiModelProperty(value = "投资次数")
    private Integer tzcnt;

    @ApiModelProperty(value = "投资金额")
    private String tzamount;

    @ApiModelProperty(value = "提现金额")
    private String withdrawamt;

    @ApiModelProperty(value = "余额")
    private String balance;

    @ApiModelProperty(value = "下级列表")
    private List<AgentDetailDTO> listxj;

    @ApiModelProperty(value = "标签")
    private String biaoqian;

    @ApiModelProperty(value = "邀请码状态")
    private Integer inviteCodeStatus;
}
