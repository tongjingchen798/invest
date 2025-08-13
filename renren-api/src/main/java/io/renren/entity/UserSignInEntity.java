package io.renren.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * 用户签到记录
 * 
 * @author Mark sunlightcs@gmail.com
 */
@Data
@TableName("tb_user_sign_in")
public class UserSignInEntity implements Serializable {
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
     * 签到日期
     */
    private LocalDate signDate;

    /**
     * 签到时间
     */
    private Date signTime;

    /**
     * 连续签到天数
     */
    private Integer continuousDays;

    /**
     * 奖励金额(分)
     */
    private Long rewardAmount;

    /**
     * 奖励类型 1:积分 2:余额
     */
    private Integer rewardType;

    /**
     * 状态 0:无效 1:有效
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
