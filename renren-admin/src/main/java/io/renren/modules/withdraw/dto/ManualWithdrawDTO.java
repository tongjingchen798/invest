package io.renren.modules.withdraw.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户手工提现信息DTO
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@ApiModel(value = "用户手工提现信息")
public class ManualWithdrawDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "代理编号")
    private String agent;

    @ApiModelProperty(value = "代理名称")
    private String agentName;

    @ApiModelProperty(value = "提现到账金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "银行简称")
    private String blankCode;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "银行名称")
    private String blankName;

    @ApiModelProperty(value = "渠道ID")
    private Long channelid;

    @ApiModelProperty(value = "创建日期")
    private String createTime;

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "IFSC代码")
    private String ifsc;

    @ApiModelProperty(value = "商户ID")
    private Long merchantid;

    @ApiModelProperty(value = "商户名称")
    private String merchantname;

    @ApiModelProperty(value = "状态说明")
    private String msg;

    @ApiModelProperty(value = "平台订单号")
    private String orderno;

    @ApiModelProperty(value = "收款姓名")
    private String payName;

    @ApiModelProperty(value = "卡号")
    private String payNo;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "业务员名称")
    private String salesmanName;

    @ApiModelProperty(value = "业务员编号")
    private Long salesmanid;

    @ApiModelProperty(value = "状态 申请中=0 转账中=1 已提现=2 取消=3")
    private Integer state;

    @ApiModelProperty(value = "状态日期")
    private String stateTime;

    @ApiModelProperty(value = "第三方订单号")
    private String threeorderNo;

    @ApiModelProperty(value = "提现日期")
    private String withdrawTime;

    @ApiModelProperty(value = "提现类型 1 余额提现，2 佣金提现")
    private Integer withdrawType;

    // 额外字段，用于汇总
    @ApiModelProperty(value = "渠道金额")
    private BigDecimal channelAmount;

    @ApiModelProperty(value = "实际到账金额")
    private BigDecimal realAmount;

    @ApiModelProperty(value = "手续费")
    private BigDecimal handFee;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "渠道")
    private Long channel;

    @ApiModelProperty(value = "输入金额")
    private BigDecimal inputamount;

    @ApiModelProperty(value = "汇率")
    private Double rate;

    @ApiModelProperty(value = "操作代码")
    private String operCode;

    @ApiModelProperty(value = "部分金额")
    private Integer partMon;

    @ApiModelProperty(value = "来源类型名称")
    private String sourcetypeName;

    @ApiModelProperty(value = "信息IP")
    private String infoIp;

    @ApiModelProperty(value = "标签")
    private String biaoqian;

    @ApiModelProperty(value = "邀请码状态")
    private Integer inviteCodeStatus;

    @ApiModelProperty(value = "是否裂变")
    private Integer liebian;

    @ApiModelProperty(value = "CTC")
    private String ctc;

    @ApiModelProperty(value = "用户代理ID")
    private String userAgent;

    @ApiModelProperty(value = "用户业务员ID")
    private Long userSalesmanid;

    @ApiModelProperty(value = "代理用户名")
    private String agentUserName;

    @ApiModelProperty(value = "业务员用户名")
    private String salesmanUserName;
}
