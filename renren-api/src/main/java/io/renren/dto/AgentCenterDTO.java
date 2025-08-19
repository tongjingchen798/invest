package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 代理中心DTO
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@ApiModel(value = "代理中心")
public class AgentCenterDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "历史佣金总额")
    private String hirstory_amt;

    @ApiModelProperty(value = "代理提现总额")
    private String withdraw_amt;

    @ApiModelProperty(value = "昨日提现总额")
    private String yt_withdraw_amt;

    @ApiModelProperty(value = "今日佣金总额")
    private String today_amt;

    @ApiModelProperty(value = "历史工资总额")
    private String hirstory_gzamt;

    @ApiModelProperty(value = "今日工资总额")
    private String today_gzamt;

    @ApiModelProperty(value = "1级佣金")
    private String list1_amt;

    @ApiModelProperty(value = "2级佣金")
    private String list2_amt;

    @ApiModelProperty(value = "2级有效人数本月")
    private String effectiveList2Month;

    @ApiModelProperty(value = "1级有效人数")
    private String effectiveList1;

    @ApiModelProperty(value = "1级有效人数本月")
    private String effectiveList1Month;

    @ApiModelProperty(value = "2级有效人数")
    private String effectiveList2;
}
