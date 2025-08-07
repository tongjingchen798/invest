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
 * 银行管理
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Data
@TableName("tb_bank")
public class BankEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;

    /**
     * 银行简称
     */
    private String blankCode;

    /**
     * 银行名称
     */
    private String blankName;

    /**
     * 渠道
     */
    private String channel;

    /**
     * 货币
     */
    private String currency;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态 0：停用 1：正常
     */
    private Integer state;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 状态时间
     */
    private Date stateTime;
}
