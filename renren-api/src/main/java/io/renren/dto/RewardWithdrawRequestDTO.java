package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

/**
 * 佣金提现请求DTO
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@ApiModel(value = "佣金提现请求")
public class RewardWithdrawRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "提现金额不能为空")
    @Positive(message = "提现金额必须大于0")
    @ApiModelProperty(value = "提现金额（分）", required = true)
    private Long amount;

    @NotBlank(message = "收款人卡号不能为空")
    @ApiModelProperty(value = "收款人卡号", required = true)
    private String payNo;

    @NotBlank(message = "支付密码不能为空")
    @ApiModelProperty(value = "支付密码", required = true)
    private String payPassword;
}
