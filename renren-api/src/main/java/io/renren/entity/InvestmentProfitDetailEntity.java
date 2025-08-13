package io.renren.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 投资收益明细
 * 
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@TableName("tb_investment_profit_detail")
public class InvestmentProfitDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId
	private Long id;
	/**
	 * 投资记录ID
	 */
	private Long investmentId;
	/**
	 * 用户ID
	 */
	private Long userId;
	/**
	 * 项目ID
	 */
	private Long projectId;
	/**
	 * 收益类型 1:利息 2:本金 3:其他
	 */
	private Integer profitType;
	/**
	 * 收益金额(分)
	 */
	private Long profitAmount;
	/**
	 * 收益日期
	 */
	private Date profitDate;
	/**
	 * 状态 0:未到账 1:已到账
	 */
	private Integer status;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 更新时间
	 */
	private Date updateDate;
}
