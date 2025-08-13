package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户佣金提现统计
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@ApiModel(value = "用户佣金提现统计")
public class RewardWithdrawSumDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "累计提现（分）")
    private Long historyAmount;

    @ApiModelProperty(value = "提现中（分）")
    private Long zztxAmount;
}
