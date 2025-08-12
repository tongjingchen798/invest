 

package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 用户信息DTO
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@ApiModel(value = "用户信息")
public class UserInfoDTO {
    @ApiModelProperty(value = "用户ID")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "邀请码")
    private String inviteCode;

    @ApiModelProperty(value = "上级用户ID")
    private Long superiorId;

    @ApiModelProperty(value = "代理信息")
    private String agent;

    @ApiModelProperty(value = "客户渠道号")
    private String channel;

    @ApiModelProperty(value = "登录端口(1:安卓, 2:ios, 3:pc, 4:未知)")
    private Integer equipment;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "资产")
    private Long assets;

    @ApiModelProperty(value = "标签")
    private String biaoqian;

    @ApiModelProperty(value = "已结束项目数")
    private Long endedItems;

    @ApiModelProperty(value = "已结束本金")
    private Long endedPrincipal;

    @ApiModelProperty(value = "已结束收益")
    private Long endedProfit;

    @ApiModelProperty(value = "标志")
    private Long flag;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "邀请码状态 0：禁用 1：正常")
    private Integer inviteCodeStatus;

    @ApiModelProperty(value = "项目数")
    private Long itmes;

    @ApiModelProperty(value = "今日收益")
    private Long jrProfit;

    @ApiModelProperty(value = "登录时间")
    private String loginTime;

    @ApiModelProperty(value = "支付密码")
    private String paymentPwd;

    @ApiModelProperty(value = "注册IP")
    private String registerIp;

    @ApiModelProperty(value = "奖励提现状态 0：禁用 1：正常")
    private Integer rewardWithdrawStatus;

    @ApiModelProperty(value = "业务员ID")
    private String salesmanid;

    @ApiModelProperty(value = "状态 0：禁用 1：正常")
    private Integer status;

    @ApiModelProperty(value = "今日余额10")
    private Long todaybalance10;

    @ApiModelProperty(value = "今日余额20")
    private Long todaybalance20;

    @ApiModelProperty(value = "今日余额5")
    private Long todaybalance5;

    @ApiModelProperty(value = "今日充值100")
    private Long todaycharge100;

    @ApiModelProperty(value = "今日充值20")
    private Long todaycharge20;

    @ApiModelProperty(value = "今日充值50")
    private Long todaycharge50;

    @ApiModelProperty(value = "总本金")
    private Long totalPrincipal;

    @ApiModelProperty(value = "总收益")
    private Long totalProfit;

    @ApiModelProperty(value = "二级密码")
    private String two_pwd;

    @ApiModelProperty(value = "投资提现状态 0：禁用 1：正常")
    private Integer tzWithdrawStatus;

    @ApiModelProperty(value = "有效3个月用户数")
    private Long valid3user;

    @ApiModelProperty(value = "有效6个月用户数")
    private Long valid6user;

    @ApiModelProperty(value = "有效9个月用户数")
    private Long valid9user;

    @ApiModelProperty(value = "VIP等级")
    private Integer vip;

    @ApiModelProperty(value = "VIP1状态")
    private Long vip1state;

    @ApiModelProperty(value = "VIP2状态")
    private Long vip2state;

    @ApiModelProperty(value = "VIP3状态")
    private Long vip3state;

    @ApiModelProperty(value = "VIP4状态")
    private Long vip4state;

    @ApiModelProperty(value = "VIP5状态")
    private Long vip5state;

    @ApiModelProperty(value = "VIP6状态")
    private Long vip6state;

    @ApiModelProperty(value = "VIP利率")
    private Integer viplr;

    @ApiModelProperty(value = "上级用户信息")
    private SuperiorUserInfoDTO superiorInfo;
}
