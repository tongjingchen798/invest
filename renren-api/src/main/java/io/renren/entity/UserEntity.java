 

package io.renren.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
	 * 二级密码
	 */
	@JsonIgnore
	private String twoPwd;
	
	/**
	 * 邀请码
	 */
	private String inviteCode;
	
	/**
	 * 上级用户ID（外键关联）
	 */
	private Long superiorId;
	
	/**
	 * 代理信息
	 */
	private String agent;
	
	/**
	 * 代理名称
	 */
	private String agentName;
	
	/**
	 * 业务员ID
	 */
	private String salesmanid;
	
	/**
	 * 业务员名称
	 */
	private String salesmanName;
	
	/**
	 * 客户渠道号
	 */
	private String channel;
	
	/**
	 * 登录端口(1:安卓, 2:ios, 3:pc, 4:未知)
	 */
	private Integer equipment;
	
	/**
	 * 注册IP
	 */
	private String registerIp;
	
	/**
	 * 最后登录IP
	 */
	private String lastIp;
	
	/**
	 * 最后登录时间
	 */
	private Date lastDate;
	
	/**
	 * 创建时间
	 */
	private Date createDate;
	
	/**
	 * 推广名称
	 */
	private String extensionName;
	
	/**
	 * 今日投资
	 */
	private Long todayInvestment;
	
	/**
	 * 历史投资
	 */
	private Long historyInvestment;
	
	/**
	 * 今日收益
	 */
	private Long todayProfit;
	
	/**
	 * 历史收益
	 */
	private Long historyProfit;
	
	/**
	 * 今日充值
	 */
	private Long todayRecharge;
	
	/**
	 * 今日提现
	 */
	private Long todayWithdraw;
	
	/**
	 * 今日充值次数
	 */
	private Long todayRechargeCnt;
	
	/**
	 * 今日提现次数
	 */
	private Long todayWithdrawCnt;
	
	/**
	 * 累计充值
	 */
	private Long chargeSum;
	
	/**
	 * 累计提现
	 */
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
	 * 历史优惠券余额
	 */
	private Long historyCouponBalance;
	
	/**
	 * 优惠券数量
	 */
	private Long couponCnt;
	
	/**
	 * 优惠券余额
	 */
	private Long couponBalance;
	
	/**
	 * 历史特权券数量
	 */
	private Long historyPrivilege_cnt;
	
	/**
	 * 特权券数量
	 */
	private Long privilege_cnt;
	
	/**
	 * 余额
	 */
	private Long balance;
	
	/**
	 * 可提现余额
	 */
	private Long cashwithdrawable;
	
	/**
	 * 冻结余额
	 */
	private Long freeze_balance;
	
	/**
	 * U级账户余额
	 */
	private Long uacnt;
	
	/**
	 * U级账户余额
	 */
	private Long ubcnt;
	
	/**
	 * U级账户余额
	 */
	private Long uccnt;
	
	/**
	 * U级账户收益
	 */
	private Long uaProfit;
	
	/**
	 * U级账户收益
	 */
	private Long ubProfit;
	
	/**
	 * U级账户收益
	 */
	private Long ucProfit;
	
	/**
	 * 历史U级账户收益
	 */
	private Long historyUaProfit;
	
	/**
	 * 历史U级账户收益
	 */
	private Long historyUbProfit;
	
	/**
	 * 历史U级账户收益
	 */
	private Long historyUcProfit;
	
	/**
	 * 今日提现次数
	 */
	private Long toDaywithdrawCount;
	
	/**
	 * 今日提现额度
	 */
	private Long toDaywithdrawQuota;
	
	/**
	 * 提现次数
	 */
	private Long withdrawCount;
	
	/**
	 * 提现额度
	 */
	private Long withdrawQuota;
	
	/**
	 * 佣金余额
	 */
	private Long commissionBalance;
	
	/**
	 * 推广人数
	 */
	private Long tgrs;
	
	/**
	 * 收益总额
	 */
	private Long sySum;
	
	/**
	 * 今日CTC
	 */
	private Long toDayctc;
	
	/**
	 * 历史CTC
	 */
	private Long historyctc;
	
	/**
	 * 状态 0：禁用 1：正常
	 */
	private Integer status;
	
	/**
	 * 邀请码状态 0：禁用 1：正常
	 */
	private Integer inviteCodeStatus;
	
	/**
	 * 投资提现状态 0：禁用 1：正常
	 */
	private Integer tzWithdrawStatus;
	
	/**
	 * 奖励提现状态 0：禁用 1：正常
	 */
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
	 * VIP利率
	 */
	private Long viplr;
	
	// 新增字段 - 根据接口文档
	/**
	 * 资产
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
	 * 身份证号
	 */
	private String idCard;
	
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
	
	/**
	 * 支付密码
	 */
	private String paymentPwd;
	
	/**
	 * 今日余额10
	 */
	private Long todaybalance10;
	
	/**
	 * 今日余额20
	 */
	private Long todaybalance20;
	
	/**
	 * 今日余额5
	 */
	private Long todaybalance5;
	
	/**
	 * 今日充值100
	 */
	private Long todaycharge100;
	
	/**
	 * 今日充值20
	 */
	private Long todaycharge20;
	
	/**
	 * 今日充值50
	 */
	private Long todaycharge50;
	
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
}