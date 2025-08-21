package io.renren.modules.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 购买记录
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Data
@ApiModel(value = "购买记录")
public class InvestmentRecordDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // 基础字段
    @ApiModelProperty(value = "项目订单ID")
    private Long orderId;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    @ApiModelProperty(value = "项目名称")
    private String investName;

    @ApiModelProperty(value = "投资金额(分)")
    private Long investmentAmount;

    @ApiModelProperty(value = "订单号简称")
    private String orderAbbr;

    @ApiModelProperty(value = "投资日期")
    private String orderDate;

    @ApiModelProperty(value = "收益金额(分)")
    private Long profitAmount;

    @ApiModelProperty(value = "收益日期")
    private String profitDate;

    @ApiModelProperty(value = "收益利息(分)")
    private Long profitInterest;

    @ApiModelProperty(value = "收益本金(分)")
    private Long profitPrincipal;

    @ApiModelProperty(value = "状态 0:未收益 1:已收益")
    private Integer status;

    @ApiModelProperty(value = "项目周期(天)")
    private Integer cycle;

    @ApiModelProperty(value = "周期类型 1:到期收益含本金 2:每日返本金到期收益")
    private Integer cycleType;

    @ApiModelProperty(value = "等待收益(分)")
    private Long ddsy;

    @ApiModelProperty(value = "投资次数")
    private Integer investCount;

    @ApiModelProperty(value = "抢购分钟数")
    private Integer rushMinute;

    @ApiModelProperty(value = "代理")
    private String agent;

    @ApiModelProperty(value = "销售员ID")
    private String salesmanid;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "更新时间")
    private Date updateDate;

    // 关联查询字段（从其他表获取）
    @ApiModelProperty(value = "简称")
    private String abbreviation;

    @ApiModelProperty(value = "用户名称")
    private String mobile;

    @ApiModelProperty(value = "标签")
    private String biaoqian;

    @ApiModelProperty(value = "裂变筛选：是 =1，否 =0")
    private Integer liebian;

    @ApiModelProperty(value = "项目类型（1:固定金额投资,2:进度周期投资）")
    private Integer projectType;

    @ApiModelProperty(value = "到期时间")
    private String expirationTime;

    @ApiModelProperty(value = "描述")
    private String orderDescribe;

    @ApiModelProperty(value = "邀请码状态")
    private Integer inviteCodeStatus;

    @ApiModelProperty(value = "支付热标")
    private Integer payhotflag;

    @ApiModelProperty(value = "收益购买方式")
    private Integer sygmfs;

    @ApiModelProperty(value = "上级手机号")
    private String supmobile;

    @ApiModelProperty(value = "历史购买")
    private String historygm;

    @ApiModelProperty(value = "已返")
    private Integer yifan;

    @ApiModelProperty(value = "类型名称")
    private String typeName;

    @ApiModelProperty(value = "代理名称")
    private String agentName;

    @ApiModelProperty(value = "业务员名称")
    private String salesmanName;

//    @ApiModelProperty(value = "优惠券ID")
//    private Long couponId;
//
//    @ApiModelProperty(value = "优惠券名称")
//    private String couponName;
//
//    @ApiModelProperty(value = "优惠券抵扣金额")
//    private Long couponAmount;
//
//    @ApiModelProperty(value = "是否使用优惠券(1使用 2不使用)")
//    private Integer couponType;
//
//    @ApiModelProperty(value = "用户优惠券ID")
//    private Long userCouponId;

    @ApiModelProperty(value = "下单金额")
    private Long orderAmount;

    @ApiModelProperty(value = "实际金额")
    private Long actualAmount;
}
