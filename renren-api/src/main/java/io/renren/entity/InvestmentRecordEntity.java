package io.renren.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户投资记录
 * 
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@TableName("tb_investment_record")
public class InvestmentRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId
	private Long id;
	/**
	 * 用户ID
	 */
	private Long userId;
	/**
	 * 项目ID
	 */
	private Long projectId;

	/**
	 * 项目名称
	 */
	private String investName;
	/**
	 * 投资金额(分)
	 */
	private Long investmentAmount;
	/**
	 * 订单ID
	 */
	private Long orderId;
	/**
	 * 订单号简称
	 */
	private String orderAbbr;
	/**
	 * 投资日期
	 */
	private Date orderDate;
	/**
	 * 收益金额(分)
	 */
	private Long profitAmount;
	/**
	 * 收益日期
	 */
	private Date profitDate;
	/**
	 * 收益利息(分)
	 */
	private Long profitInterest;
	/**
	 * 收益本金(分)
	 */
	private Long profitPrincipal;
	/**
	 * 状态 0:未收益 1:已收益
	 */
	private Integer status;
	/**
	 * 项目周期(天)
	 */
	private Integer cycle;
	/**
	 * 周期类型 1:到期收益含本金 2:每日返本金到期收益
	 */
	private Integer cycleType;
	/**
	 * 等待收益(分)
	 */
	private Long ddsy;
	/**
	 * 投资次数
	 */
	private Integer investCount;
	/**
	 * 抢购分钟数
	 */
	private Integer rushMinute;
	/**
	 * 代理
	 */
	private String agent;
	/**
	 * 销售员ID
	 */
	private String salesmanid;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 更新时间
	 */
	private Date updateDate;
}
