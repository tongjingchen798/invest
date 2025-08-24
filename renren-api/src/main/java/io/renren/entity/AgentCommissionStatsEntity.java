package io.renren.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 代理兑付收益统计表实体类
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@TableName("tb_agent_commission_stats")
public class AgentCommissionStatsEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 代理ID
     */
    private Long agentId;

    /**
     * 代理名称
     */
    private String agentName;

    /**
     * 业务员ID
     */
    private Long salesmanId;

    /**
     * 业务员名称
     */
    private String salesmanName;

    /**
     * 统计日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date statisticsDate;

    /**
     * 总充值金额(分)
     */
    private Long totalChargeAmount;

    /**
     * 总提现金额(分)
     */
    private Long totalWithdrawAmount;

    /**
     * 净额(充值-提现)(分)
     */
    private Long netAmount;

    /**
     * 兑付金额(分)
     */
    private Long commissionAmount;

    /**
     * 兑付比例(默认5%)
     */
    private BigDecimal commissionRate;

    /**
     * 名下用户数量
     */
    private Integer userCount;

    /**
     * 状态：1-正常，0-禁用
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
