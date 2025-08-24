package io.renren.modules.transfer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 人工转帐记录DTO
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@ApiModel(value = "人工转帐记录")
public class ManualTransferDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "消息/状态说明")
    private String msg;

    @ApiModelProperty(value = "转帐时间")
    private String withdrawTime;

    @ApiModelProperty(value = "转帐金额（分）")
    private BigDecimal amount;

    @ApiModelProperty(value = "银行代码")
    private String blankCode;

    @ApiModelProperty(value = "银行名称")
    private String blankName;

    @ApiModelProperty(value = "收款人姓名")
    private String payName;

    @ApiModelProperty(value = "收款账号")
    private String payNo;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "状态时间")
    private String stateTime;

    @ApiModelProperty(value = "状态：0-待处理，1-处理中，2-已完成，3-失败，4-已取消")
    private Integer state;

    @ApiModelProperty(value = "转帐类型：0-余额转帐，1-佣金转帐，2-其他转帐")
    private Integer withdrawType;

    @ApiModelProperty(value = "订单号")
    private String orderno;

    @ApiModelProperty(value = "第三方订单号")
    private String threeorderNo;

    @ApiModelProperty(value = "IFSC代码")
    private String ifsc;

    @ApiModelProperty(value = "代理名称")
    private String agentName;

    @ApiModelProperty(value = "代理ID")
    private String agent;

    @ApiModelProperty(value = "业务员ID")
    private Long salesmanid;

    @ApiModelProperty(value = "业务员姓名")
    private String salesmanName;

    @ApiModelProperty(value = "渠道ID")
    private String channelid;

    @ApiModelProperty(value = "商户ID")
    private String merchantid;

    @ApiModelProperty(value = "商户名称")
    private String merchantname;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "操作人")
    private String operCode;

    @ApiModelProperty(value = "转帐来源：1-用户申请，2-管理员操作，3-系统自动")
    private Integer transferSource;

    @ApiModelProperty(value = "手续费（分）")
    private BigDecimal handFee;

    @ApiModelProperty(value = "实际到账金额（分）")
    private BigDecimal realAmount;

    @ApiModelProperty(value = "渠道金额（分）")
    private BigDecimal channelAmount;
}
