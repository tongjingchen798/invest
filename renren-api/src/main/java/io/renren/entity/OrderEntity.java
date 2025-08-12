package io.renren.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单表
 *
 * @author renren
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("`order`")
public class OrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 投资ID
     */
    private Long investId;

    /**
     * 订单金额
     */
    private BigDecimal orderAmount;

    /**
     * 优惠券类型
     */
    private String couponType;

    /**
     * 优惠券ID
     */
    private Long couponId;

    /**
     * 优惠券名称
     */
    private String couponName;

    /**
     * 订单描述
     */
    private String orderDescribe;

    /**
     * 优惠券金额
     */
    private BigDecimal couponAmount;

    /**
     * 用户优惠券ID
     */
    private Long userCouponId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 项目类型
     */
    private Integer projectType;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 业务员ID
     */
    private Long salesmanId;

    /**
     * 业务员姓名
     */
    private String salesmanName;

    /**
     * 投资名称
     */
    private String investName;

    /**
     * 订单时间
     */
    private Date orderDate;

    /**
     * 投资数量
     */
    private Integer investCount;

    /**
     * 实际金额
     */
    private BigDecimal actualAmount;

    /**
     * 代理ID
     */
    private Long agentId;

    /**
     * 代理名称
     */
    private String agentName;

    /**
     * 标签
     */
    private String label;

    /**
     * 邀请码状态
     */
    private Integer inviteCodeStatus;

    /**
     * 裂变状态：0-否，1-是
     */
    private Integer fissionStatus;

    /**
     * 过期时间
     */
    private Date expirationTime;

    /**
     * 历史购买记录(JSON格式)
     */
    private String historyGm;

    /**
     * 项目简称
     */
    private String abbreviation;

    /**
     * 剩余购买份数
     */
    private Integer remainingPurchaseCount;

    /**
     * 支付热门标志
     */
    private Integer payHotFlag;

    /**
     * 已返标志
     */
    private Integer yifan;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 上级手机号
     */
    private String superiorMobile;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createDate;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;
}
