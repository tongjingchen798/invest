package io.renren.modules.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 结算报表DTO
 *
 * @author renren
 * @since 1.0.0
 */
@Data
@ApiModel(value = "结算报表")
public class SettlementReportDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "代理编号")
    private Long agent;

    @ApiModelProperty(value = "代理名称")
    private String agentName;

    @ApiModelProperty(value = "结算充值金额")
    private Integer czRealAmount;

    @ApiModelProperty(value = "利润=充值-提现")
    private Integer lr;

    @ApiModelProperty(value = "业务员名称")
    private String salesmanName;

    @ApiModelProperty(value = "业务员编号")
    private Long salesmanid;

    @ApiModelProperty(value = "结算提现金额")
    private Integer txRealAmount;
}
