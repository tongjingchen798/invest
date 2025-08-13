package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 积分记录
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@ApiModel(value = "积分记录")
public class TeamPointsDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "记录ID")
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "交易类型（自定义如1购买流水,2提现流水,3佣金流水,4签到流水）")
    private Integer busiType;

    @ApiModelProperty(value = "积分金额")
    private Long pointsAmount;

    @ApiModelProperty(value = "积分时间")
    private String pointsDate;

    @ApiModelProperty(value = "备注")
    private String remarks;
}
