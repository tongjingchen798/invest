package io.renren.modules.transfer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

/**
 * 人工转账请求参数
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@ApiModel(value = "人工转账请求参数")
public class WithdrawSHRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "收款账号", required = true)
    @NotBlank(message = "收款账号不能为空")
    private String payNo;

    @ApiModelProperty(value = "收款人姓名", required = true)
    @NotBlank(message = "收款人姓名不能为空")
    private String payName;

    @ApiModelProperty(value = "IFSC代码", required = true)
    @NotBlank(message = "IFSC代码不能为空")
    private String ifsc;

    @ApiModelProperty(value = "转账金额（分）", required = true)
    @NotNull(message = "转账金额不能为空")
    @Positive(message = "转账金额必须大于0")
    private Long amount;
}
