
package io.renren.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@TableName("tb_user")
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@TableId
	private Long id;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 手机号
	 */
	private String mobile;

	/**
	 * 密码
	 */
	@JsonIgnore
	private String password;

	/**
	 * 二级密码 (目前没有使用)
	 */
	@JsonIgnore
	@TableField("two_pwd")
	private String twoPwd;

	/**
	 * 邀请码
	 */
	@TableField("invite_code")
	private String inviteCode;

	/**
	 * 上级邀请码
	 */
	@TableField("upinvite_code")
	private String upinviteCode;

	/**
	 * 代理信息
	 */
	private Long agent;

	/**
	 * 代理名称
	 */
	@TableField("agent_name")
	private String agentName;

	/**
	 * 业务员ID
	 */
	private Long salesmanid;

	/**
	 * 业务员名称
	 */
	@TableField("salesman_name")
	private String salesmanName;

	/**
	 * 上级名称
	 */
	@TableField("superior_name")
	private String superiorName;

	/**
	 * 上级邀请码
	 */
	@TableField("superior_code")
	private String superiorCode;

	/**
	 * 客户渠道号
	 */
	private Long channel;

	/**
	 * 登录端口(1:安卓, 2:ios, 3:pc, 4:未知)
	 */
	private Integer equipment;

	/**
	 * 注册IP
	 */
	@TableField("register_ip")
	private String registerIp;

	/**
	 * 最后登录IP
	 */
	@TableField("last_ip")
	private String lastIp;

	/**
	 * 最后登录时间
	 */
	@TableField("last_date")
	private Date lastDate;

	/**
	 * 创建时间
	 */
	@TableField("create_date")
	private Date createDate;

	/**
	 * 推广名称
	 */
	@TableField("extension_name")
	private String extensionName;

	/**
	 * 今日投资(分)
	 */
	@TableField("today_investment")
	private Long todayInvestment;

	/**
	 * 历史投资(分)
	 */
	@TableField("history_investment")
	private Long historyInvestment;

	/**
	 * 今日收益(分)
	 */
	@TableField("today_profit")
	private Long todayProfit;

	/**
	 * 历史收益(分)
	 */
	@TableField("history_profit")
	private Long historyProfit;

	/**
	 * 今日充值(分)
	 */
	@TableField("today_recharge")
	private Long todayRecharge;

	/**
	 * 今日提现(分)
	 */
	@TableField("today_withdraw")
	private Long todayWithdraw;

	/**
	 * 今日充值次数
	 */
	@TableField("today_recharge_cnt")
	private Long todayRechargeCnt;

	/**
	 * 今日提现次数
	 */
	@TableField("today_withdraw_cnt")
	private Long todayWithdrawCnt;

	/**
	 * 累计充值(分)
	 */
	@TableField("charge_sum")
	private Long chargeSum;

	/**
	 * 累计提现(分)
	 */
	@TableField("withdraw_sum")
	private Long withdrawSum;

	/**
	 * 历史充值次数
	 */
	private Long historychargecnt;

	/**
	 * 历史提现次数
	 */
	private Long historywithdrawcnt;

	/**
	 * 历史优惠券余额(分) (目前没有使用)
	 */
	@TableField("history_coupon_balance")
	private Long historyCouponBalance;

	/**
	 * 优惠券数量 (目前没有使用)
	 */
	@TableField("coupon_cnt")
	private Long couponCnt;

	/**
	 * 优惠券余额(分) (目前没有使用)
	 */
	@TableField("coupon_balance")
	private Long couponBalance;

	/**
	 * 历史特权券数量 (目前没有使用)
	 */
	@TableField("history_privilege_cnt")
	private Long historyPrivilegeCnt;

	/**
	 * 特权券数量 (目前没有使用)
	 */
	@TableField("privilege_cnt")
	private Long privilegeCnt;

	/**
	 * 我的资产=余额+可提现+等待收益
	 */
	@TableField(exist = false)
	private Long balance;

	/**
	 * 可提现余额(分)
	 */
	private Long cashwithdrawable;

	/**
	 * 冻结余额(分)
	 */
	@TableField("freeze_balance")
	private Long freezeBalance;

	/**
	 * 1级会员数
	 */
	private Long uacnt;

	/**
	 * 2级会员数
	 */
	private Long ubcnt;

	/**
	 * 3级会员数
	 */
	private Long uccnt;

	/**
	 * 1级今日收益(分)
	 */
	@TableField("ua_profit")
	private Long uaProfit;

	/**
	 * 2级今日收益(分)
	 */
	@TableField("ub_profit")
	private Long ubProfit;

	/**
	 * 3级今日收益(分)
	 */
	@TableField("uc_profit")
	private Long ucProfit;

	/**
	 * 历史1级账户收益(分)
	 */
	@TableField("history_ua_profit")
	private Long historyUaProfit;

	/**
	 * 历史2级账户收益(分)
	 */
	@TableField("history_ub_profit")
	private Long historyUbProfit;

	/**
	 * 历史3级账户收益(分)
	 */
	@TableField("history_uc_profit")
	private Long historyUcProfit;

	/**
	 * 今日提现次数
	 */
	@TableField("to_daywithdraw_count")
	private Long toDaywithdrawCount;

	/**
	 * 今日佣金提现总额(分)
	 */
	@TableField("to_daywithdraw_quota")
	private Long toDaywithdrawQuota;

	/**
	 * 佣金提现总次数
	 */
	@TableField("withdraw_count")
	private Long withdrawCount;

	/**
	 * 佣金提现总额(分)
	 */
	@TableField("withdraw_quota")
	private Long withdrawQuota;

	/**
	 * 佣金余额(分)
	 */
	@TableField("commission_balance")
	private Long commissionBalance;

	/**
	 * 历史佣金总计
	 */
	@TableField("history_commission")
	private Long historyCommission;

	/**
	 * 佣金提现总额(分)
	 */
	@ApiModelProperty(value = "佣金提现总额(分)")
	@TableField("commission_withdraw_sum")
	private Long commissionWithdrawSum;

	/**
	 * 今日佣金
	 */
	@TableField("today_commission")
	private Long todayCommission;

	/**
	 * 推广人数
	 */
	private Long tgrs;

	/**
	 * 收益总额(分)
	 */
	@TableField("sy_sum")
	private Long sySum;

	/**
	 * 今日CTC(分) (这个不清楚是什么, 没有用过)
	 */
	@TableField("to_dayctc")
	private Long toDayctc;

	/**
	 * 历史CTC(分) (这个不清楚是什么, 没有用过)
	 */
	private Long historyctc;

	/**
	 * 状态 0：禁用 1：正常
	 */
	private Integer status;

	/**
	 * 邀请码状态 0：禁用 1：正常
	 */
	@TableField("invite_code_status")
	private Integer inviteCodeStatus;

	/**
	 * 投资提现状态 0：禁用 1：正常
	 */
	@TableField("tz_withdraw_status")
	private Integer tzWithdrawStatus;

	/**
	 * 奖励提现状态 0：禁用 1：正常
	 */
	@TableField("reward_withdraw_status")
	private Integer rewardWithdrawStatus;

	/**
	 * 标签
	 */
	private String biaoqian;

	/**
	 * VIP等级
	 */
	private Integer vip;

	/**
	 * VIP利率(分)
	 */
	private Long viplr;

	/**
	 * 可用余额(分为单位)
	 */
	private Long assets;

	/**
	 * 已结束项目数
	 */
	private Long endedItems;

	/**
	 * 已结束本金
	 */
	private Long endedPrincipal;

	/**
	 * 已结束收益
	 */
	private Long endedProfit;

	/**
	 * 标志
	 */
	private Long flag;


	/**
	 * 项目数
	 */
	private Long itmes;

	/**
	 * 今日收益
	 */
	private Long jrProfit;

	/**
	 * 登录时间
	 */
	private String loginTime;

//	/**
//	 * 支付密码 (没有使用)
//	 */
//	private String paymentPwd;

//	/**
//	 * 今日余额10
//	 */
//	private Long todaybalance10;
//
//	/**
//	 * 今日余额20
//	 */
//	private Long todaybalance20;
//
//	/**
//	 * 今日余额5
//	 */
//	private Long todaybalance5;
//
//	/**
//	 * 今日充值100
//	 */
//	private Long todaycharge100;
//
//	/**
//	 * 今日充值20
//	 */
//	private Long todaycharge20;
//
//	/**
//	 * 今日充值50
//	 */
//	private Long todaycharge50;

	/**
	 * 总本金
	 */
	private Long totalPrincipal;

	/**
	 * 总收益
	 */
	private Long totalProfit;

	/**
	 * 有效3个月用户数
	 */
	private Long valid3user;

	/**
	 * 有效6个月用户数
	 */
	private Long valid6user;

	/**
	 * 有效9个月用户数
	 */
	private Long valid9user;

	/**
	 * VIP1状态
	 */
	private Long vip1state;

	/**
	 * VIP2状态
	 */
	private Long vip2state;

	/**
	 * VIP3状态
	 */
	private Long vip3state;

	/**
	 * VIP4状态
	 */
	private Long vip4state;

	/**
	 * VIP5状态
	 */
	private Long vip5state;

	/**
	 * VIP6状态
	 */
	private Long vip6state;

//	@ApiModelProperty(value = "总邀请收益(分)")
//	private Long inviteIncome;

	private Integer liebian;
}