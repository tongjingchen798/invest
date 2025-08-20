package io.renren.modules.home.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 首页统计数据DTO
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@ApiModel(value = "首页统计数据")
public class MainStatsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 充值金额
     */
    @ApiModelProperty(value = "充值金额")
    private Long chargeOrderAmount;

    /**
     * 充值订单数
     */
    @ApiModelProperty(value = "充值订单数")
    private Long charge_order_cnt;

    /**
     * 在线项目数
     */
    @ApiModelProperty(value = "在线项目数")
    private Long online_privilege_cnt;

    /**
     * 购买项目数
     */
    @ApiModelProperty(value = "购买项目数")
    private Long privilege_cnt;

    /**
     * 提现金额
     */
    @ApiModelProperty(value = "提现金额")
    private Long withdrawOrderAmount;

    /**
     * 提现订单数
     */
    @ApiModelProperty(value = "提现订单数")
    private Long withdraw_order_cnt;

    /**
     * 销售总额
     */
    @ApiModelProperty(value = "销售总额")
    private Long xs_amount;

    /**
     * 注册会员数
     */
    @ApiModelProperty(value = "注册会员数")
    private Long zc_cnt;
}
