package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户提现信息
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@ApiModel(value = "用户提现信息")
public class UserWithdrawInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "记录ID")
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "代理编号")
    private String agent;

    @ApiModelProperty(value = "用户提现到账金额")
    private Long amount;

    @ApiModelProperty(value = "银行简称")
    private String blankCode;

    @ApiModelProperty(value = "银行名称")
    private String blankName;

    @ApiModelProperty(value = "渠道")
    private Long channel;

    @ApiModelProperty(value = "提现通道费用")
    private Long channelAmount;

    @ApiModelProperty(value = "支付通道主键")
    private Long channelid;

    @ApiModelProperty(value = "创建日期")
    private String createTime;

    @ApiModelProperty(value = "用户付的提现手续费")
    private Long handFee;

    @ApiModelProperty(value = "IFSC代码")
    private String ifsc;

    @ApiModelProperty(value = "用户IP")
    private String infoIp;

    @ApiModelProperty(value = "用户输入的金额")
    private Long inputamount;

    @ApiModelProperty(value = "商户主键")
    private Long merchantid;

    @ApiModelProperty(value = "商户名称")
    private String merchantname;

    @ApiModelProperty(value = "用户账户/手机")
    private String mobile;

    @ApiModelProperty(value = "操作人")
    private String operCode;

    @ApiModelProperty(value = "平台订单号")
    private String orderno;

    @ApiModelProperty(value = "月分区")
    private Integer partMon;

    @ApiModelProperty(value = "收款姓名")
    private String payName;

    @ApiModelProperty(value = "卡号")
    private String payNo;

    @ApiModelProperty(value = "费率固定10%")
    private Double rate;

    @ApiModelProperty(value = "真实提现额(分)")
    private Long realAmount;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "业务员编号")
    private Long salesmanid;

    @ApiModelProperty(value = "渠道中文名")
    private String sourcetypeName;

    @ApiModelProperty(value = "状态 申请中=0 转账中=1 已提现=2 取消=3")
    private Integer state;

    @ApiModelProperty(value = "状态日期")
    private String stateTime;

    @ApiModelProperty(value = "第三方订单号")
    private String threeorderNo;

    @ApiModelProperty(value = "提现日期")
    private String withdrawTime;

    @ApiModelProperty(value = "提现类型 1余额提现，2佣金提现")
    private Integer withdrawType;
}
