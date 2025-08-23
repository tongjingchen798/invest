package io.renren.modules.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 会员信息列表DTO
 *
 * @author renren
 * @since 1.0.0
 */
@Data
@ApiModel(value = "会员信息列表")
public class MemberInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "代理编号")
    private String agent;

    @ApiModelProperty(value = "代理名称")
    private String agentName;

    @ApiModelProperty(value = "用户余额分为单位")
    private Long balance;

    @ApiModelProperty(value = "标签")
    private String biaoqian;

    @ApiModelProperty(value = "用户可提余额分为单位")
    private Long cashwithdrawable;

    @ApiModelProperty(value = "历史总充值")
    private Long chargeSum;

    @ApiModelProperty(value = "佣金余额")
    private Long commissionBalance;

    @ApiModelProperty(value = "待使用优惠券金额")
    private Long couponBalance;

    @ApiModelProperty(value = "待使用优惠券数量")
    private Long couponCnt;

    @ApiModelProperty(value = "创建时间")
    private String createDate;

    @ApiModelProperty(value = "登录端口(1安卓手机端，2ios手机端，3pc端，4未知)")
    private Integer equipment;

    @ApiModelProperty(value = "渠道名称")
    private String extensionName;

    @ApiModelProperty(value = "冻结余额分为单位")
    private Long freezeBalance;

    @ApiModelProperty(value = "历史总优惠券金额")
    private Long historyCouponBalance;

    @ApiModelProperty(value = "历史投资数")
    private Long historyInvestment;

    @ApiModelProperty(value = "历史总特权券数量")
    private Long historyPrivilegeCnt;

    @ApiModelProperty(value = "历史收益")
    private Long historyProfit;

    @ApiModelProperty(value = "1级历史收益")
    private Long historyUaProfit;

    @ApiModelProperty(value = "2级历史收益")
    private Long historyUbProfit;

    @ApiModelProperty(value = "3级历史收益")
    private Long historyUcProfit;

    @ApiModelProperty(value = "历史总充值次数")
    private Long historychargecnt;

    @ApiModelProperty(value = "历史充提差（历史充值-历史提现）")
    private Long historyctc;

    @ApiModelProperty(value = "历史总提现次数")
    private Long historywithdrawcnt;

    @ApiModelProperty(value = "邀请码")
    private String inviteCode;

    @ApiModelProperty(value = "邀请状态 0 异常 1 正常 异常的名字标红")
    private Integer inviteCodeStatus;

    @ApiModelProperty(value = "最后登录时间")
    private String lastDate;

    @ApiModelProperty(value = "最后登录ip")
    private String lastIp;

    @ApiModelProperty(value = "账号")
    private String mobile;

    @ApiModelProperty(value = "待使用特权券数量")
    private Long privilegeCnt;

    @ApiModelProperty(value = "注册ip")
    private String registerIp;

    @ApiModelProperty(value = "佣金账户的提现 0 禁用 1 正常")
    private Integer rewardWithdrawStatus;

    @ApiModelProperty(value = "业务员名称")
    private String salesmanName;

    @ApiModelProperty(value = "业务员编号")
    private String salesmanid;

    @ApiModelProperty(value = "状态 0：停用 1：正常")
    private Integer status;

//    @ApiModelProperty(value = "上级邀请码")
//    private String superiorCode;

//    @ApiModelProperty(value = "上级会员名称")
//    private String superiorName;

    @ApiModelProperty(value = "历史总收益")
    private Long sySum;

    @ApiModelProperty(value = "推广人数")
    private Long tgrs;

    @ApiModelProperty(value = "今日充提差（今日充值-今日提现）")
    private Long toDayctc;

    @ApiModelProperty(value = "今日佣金提现总数")
    private Long toDaywithdrawCount;

    @ApiModelProperty(value = "今日佣金提现总额")
    private Long toDaywithdrawQuota;

    @ApiModelProperty(value = "今日投资数")
    private Long todayInvestment;

    @ApiModelProperty(value = "今日投收益")
    private Long todayProfit;

    @ApiModelProperty(value = "今日总充值")
    private Long todayRecharge;

    @ApiModelProperty(value = "今日总充值次数")
    private Long todayRechargeCnt;

    @ApiModelProperty(value = "今日总提现")
    private Long todayWithdraw;

    @ApiModelProperty(value = "今日总提现次数")
    private Long todayWithdrawCnt;

    @ApiModelProperty(value = "投资账户的提现 0 禁用 1 正常")
    private Integer tzWithdrawStatus;

    @ApiModelProperty(value = "1级今日收益")
    private Long uaProfit;

    @ApiModelProperty(value = "1级会员数")
    private Long uacnt;

    @ApiModelProperty(value = "2级今日收益")
    private Long ubProfit;

    @ApiModelProperty(value = "2级会员数")
    private Long ubcnt;

    @ApiModelProperty(value = "3级今日收益")
    private Long ucProfit;

    @ApiModelProperty(value = "3级会员数")
    private Long uccnt;

    @ApiModelProperty(value = "注册填的上级邀请码")
    private String upinviteCode;

    @ApiModelProperty(value = "姓名")
    private String username;

    @ApiModelProperty(value = "VIP等级")
    private Integer vip;

    @ApiModelProperty(value = "VIP等级范围")
    private Integer viplr;

    @ApiModelProperty(value = "历史佣金提现总数")
    private Long withdrawCount;

    @ApiModelProperty(value = "历史佣金提现总额")
    private Long withdrawQuota;

    @ApiModelProperty(value = "历史总提现")
    private Long withdrawSum;

    @ApiModelProperty(value = "是否裂变(0否1是)")
    private Integer liebian;
}
