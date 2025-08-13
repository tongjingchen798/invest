package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 提现查询参数
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@ApiModel(value = "提现查询参数")
public class WithdrawQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "每页显示记录数", required = true)
    private Integer limit;

    @ApiModelProperty(value = "当前页码，从1开始", required = true)
    private Integer page;

    @ApiModelProperty(value = "登录用户id", required = true)
    private Long userId;

    @ApiModelProperty(value = "排序方式，可选值(asc、desc)")
    private String order;

    @ApiModelProperty(value = "排序字段")
    private String orderField;

    @ApiModelProperty(value = "第三方订单号")
    private String orderno;

    @ApiModelProperty(value = "卡号")
    private String payNo;

    @ApiModelProperty(value = "状态 0 待审核 1审核通过  2 审核失败")
    private Integer state;

    @ApiModelProperty(value = "我方订单号")
    private String transNo;
}
