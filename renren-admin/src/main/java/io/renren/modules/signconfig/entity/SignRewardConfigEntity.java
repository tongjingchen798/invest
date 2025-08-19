package io.renren.modules.signconfig.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 签到奖励配置表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Data
@TableName("tb_sign_reward_config")
public class SignRewardConfigEntity {

    /**
     * 主键ID
     */
	private Long id;
    /**
     * 第1天奖励金额(分)
     */
	private Long oneDay;
    /**
     * 第2天奖励金额(分)
     */
	private Long twoDay;
    /**
     * 第3天奖励金额(分)
     */
	private Long threeDay;
    /**
     * 第4天奖励金额(分)
     */
	private Long fourDay;
    /**
     * 第5天奖励金额(分)
     */
	private Long fiveDay;
    /**
     * 第6天奖励金额(分)
     */
	private Long sixDay;
    /**
     * 第7天奖励金额(分)
     */
	private Long sevenDay;
    /**
     * 首次7天奖励金额(分)
     */
	private Long firstsevenDay;
    /**
     * 状态 0:禁用 1:启用
     */
	private Integer status;
    /**
     * 第1天签到类型 1:积分 2:余额
     */
	private Integer oneDayqdtype;
    /**
     * 第2天签到类型 1:积分 2:余额
     */
	private Integer twoDayqdtype;
    /**
     * 第3天签到类型 1:积分 2:余额
     */
	private Integer threeDayqdtype;
    /**
     * 第4天签到类型 1:积分 2:余额
     */
	private Integer fourDayqdtype;
    /**
     * 第5天签到类型 1:积分 2:余额
     */
	private Integer fiveDayqdtype;
    /**
     * 第6天签到类型 1:积分 2:余额
     */
	private Integer sixDayqdtype;
    /**
     * 第7天签到类型 1:积分 2:余额
     */
	private Integer sevenDayqdtype;
    /**
     * 首次7天签到类型 1:积分 2:余额
     */
	private Integer firstsevenDayqdtype;
    /**
     * 创建时间
     */
	private Date createTime;
    /**
     * 更新时间
     */
	private Date updateTime;
}