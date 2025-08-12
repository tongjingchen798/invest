package io.renren.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户银行卡配置表
 *
 * @author renren
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_bank_card")
public class UserBankCardEntity implements Serializable {
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
     * 银行代码
     */
    private String blankCode;

    /**
     * 银行名称
     */
    private String blankName;

    /**
     * 代理ID
     */
    private Long agent;

    /**
     * 渠道
     */
    private String channel;

    /**
     * IFSC代码
     */
    private String ifsc;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 账户持有人姓名
     */
    private String payName;

    /**
     * 银行卡号
     */
    private String payNo;

    /**
     * 业务员姓名
     */
    private String salesmanName;

    /**
     * 业务员ID
     */
    private Long salesmanid;

    /**
     * 操作代码
     */
    private String operCode;

    /**
     * 排序值
     */
    private Integer sortV;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer state;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date operTime;

    /**
     * 状态更新时间
     */
    private Date stateTime;
}
