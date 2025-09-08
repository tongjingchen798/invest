package io.renren.modules.withdraw.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 提现审核DTO
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@ApiModel(value = "提现审核")
public class WithdrawAuditDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "提现记录id")
    private Long id;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "1审核通过 2已提现 3驳回 4提现失败  5无效订单")
    private Integer state;

    @ApiModelProperty(value = "操作人id")
    private Long sysUpdateUserId;
}
