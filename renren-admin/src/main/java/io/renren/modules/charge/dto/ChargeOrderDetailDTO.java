package io.renren.modules.charge.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户充值信息DTO
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@ApiModel(value = "用户充值信息")
public class ChargeOrderDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "代理编号")
    private String agent;

    @ApiModelProperty(value = "代理名称")
    private String agentName;

    @ApiModelProperty(value = "充值金币(分)")
    private BigDecimal amount;

    @ApiModelProperty(value = "标签")
    private String biaoqian;

    @ApiModelProperty(value = "渠道")
    private String channel;

    @ApiModelProperty(value = "通道类型")
    private String channelType;

    @ApiModelProperty(value = "渠道ID")
    private Long channelid;

    @ApiModelProperty(value = "充值ID")
    private Long chargeId;

    @ApiModelProperty(value = "充值日期")
    private String chargeTime;

    @ApiModelProperty(value = "创建日期")
    private String createTime;

    @ApiModelProperty(value = "用户IP")
    private String infoIp;

    @ApiModelProperty(value = "邀请码状态")
    private Integer inviteCodeStatus;

    @ApiModelProperty(value = "裂变状态")
    private Integer liebian;

    @ApiModelProperty(value = "商户ID")
    private Long merchantid;

    @ApiModelProperty(value = "商户名称")
    private String merchantname;

    @ApiModelProperty(value = "用户账号")
    private String mobile;

    @ApiModelProperty(value = "操作人")
    private String operCode;

    @ApiModelProperty(value = "平台订单号")
    private String orderno;

    @ApiModelProperty(value = "渠道编码")
    private String platform;

    @ApiModelProperty(value = "真实充值额(分)")
    private BigDecimal realAmount;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "业务员名称")
    private String salesmanName;

    @ApiModelProperty(value = "业务员编号")
    private String salesmanid;

    @ApiModelProperty(value = "充值渠道名称")
    private String sourcetypeName;

    @ApiModelProperty(value = "状态 0 待审核  1 审核通过  2 失败")
    private Integer state;

    @ApiModelProperty(value = "成功次数")
    private Integer successcnt;

    @ApiModelProperty(value = "成功充值")
    private Integer successcz;

    @ApiModelProperty(value = "第三方订单号")
    private String threeorderNo;

    @ApiModelProperty(value = "U实际支付金额")
    private BigDecimal uRealAmout;

    @ApiModelProperty(value = "U金额")
    private BigDecimal uamout;

    @ApiModelProperty(value = "U价格")
    private BigDecimal uprice;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "用户创建日期")
    private String usercreateTime;

    @ApiModelProperty(value = "钱包地址")
    private String walletAddr;

    @ApiModelProperty(value = "钱包ID")
    private Long walletId;
}
