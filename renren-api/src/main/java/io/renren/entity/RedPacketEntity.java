/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 红包主表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Data
@TableName("tb_red_packet")
public class RedPacketEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private String id;

    /**
     * 红包名称
     */
    private String title;

    /**
     * 总金额(分)
     */
    private Long totalAmount;

    /**
     * 红包个数
     */
    private Integer number;

    /**
     * 单个红包金额(分)
     */
    private Long amount;

    /**
     * 口令码
     */
    private String password;

    /**
     * 已领取红包数
     */
    private Integer collated;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 锁定状态 0:未锁定 1:已锁定
     */
    private Integer locking;

    /**
     * 状态 0:正常 1:已过期 2:已领完
     */
    private Integer status;

    /**
     * 有效期(分钟)
     */
    private Integer validityPeriod;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 创建者ID
     */
    private Long creatorId;

    /**
     * 创建者姓名
     */
    private String creatorName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 更新时间
     */
    private Date updateTime;
}
