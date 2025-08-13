package io.renren.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * 用户签到统计
 * 
 * @author Mark sunlightcs@gmail.com
 */
@Data
@TableName("tb_user_sign_statistics")
public class UserSignStatisticsEntity implements Serializable {
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
     * 总签到天数
     */
    private Integer totalSignDays;

    /**
     * 当前连续签到天数
     */
    private Integer continuousDays;

    /**
     * 历史最大连续签到天数
     */
    private Integer maxContinuousDays;

    /**
     * 累计获得奖励金额(分)
     */
    private Long totalRewardAmount;

    /**
     * 最后签到日期
     */
    private LocalDate lastSignDate;

    /**
     * 最后签到时间
     */
    private Date lastSignTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
