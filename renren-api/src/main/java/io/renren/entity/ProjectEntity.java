package io.renren.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 投资项目实体类
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_project")
public class ProjectEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 项目ID
     */
    @TableId
    private Long investId;

    /**
     * 项目名称
     */
    private String investName;

    /**
     * 项目简称
     */
    private String abbreviation;

    /**
     * 项目状态（0:下架,1:上架,2:删除）
     */
    private Integer status;

    /**
     * 可买台数
     */
    private Integer investRepeat;

    /**
     * 项目类型（0:默认类型,1:固定金额投资,2:众筹）
     */
    private Integer projectType;

        /**
     * 回款方式
     * 1: 到期返还 (Return upon maturity)
     * 2: 每日返利 (Daily rebate) 
     * 3: 不返本金 (No principal return)
     * 4: 复利产品 (Compound interest product)
     * 5: 阶梯日益 (Stepped daily increase)
     * 6: 拼团 (Group buy/Team purchase)
     */
    private Integer cycleType;

    /**
     * 项目规模金额
     */
    private Long scaleAmount;

    /**
     * 投资周期(天)
     */
    private Integer cycle;

    /**
     * 日收益率(%)
     */
    private String conversion;

    /**
     * 每日收益
     */
    private Long principalProfit;

    /**
     * 总收益
     */
    private Long totalProfit;

    /**
     * 总成本(本金+收益)
     */
    private Long totalCost;

    /**
     * 分类ID
     */
    private Long typeId;

    /**
     * 项目图片地址
     */
    private String img;

    /**
     * 优惠券ID
     */
    private Long couponId;

    /**
     * 优惠券名称
     */
    private String couponName;

    /**
     * 项目描述
     */
    private String projectDescribe;

    /**
     * 排序权重
     */
    private Integer sort;

    /**
     * VIP等级要求
     */
    private Integer vip;

    /**
     * 限时抢购小时数
     */
    private Integer hour;

    /**
     * 抢购结束时间
     */
    private String expire;

    /**
     * 指定上架日期
     */
    private String sjDate;

    /**
     * 指定下架日期
     */
    private String xjDate;

    /**
     * 会员注册时间要求
     */
    private Integer memberRegistrationTime;

    /**
     * 注册开始时间
     */
    private String registerStartDate;

    /**
     * 注册结束时间
     */
    private String registerEndDate;

    /**
     * 是否立返本金（-1:否,0:否,1:是）
     */
    private Integer returnPrincipal;

    /**
     * 返还到谁
     */
    private Integer returnTo;

    /**
     * 返还比例
     */
    private BigDecimal returnRatio;

    /**
     * 是否立返项目
     */
    private Integer returnProject;

    /**
     * 返还给上级
     */
    private Integer returnToSup;

    /**
     * 上级返还比例
     */
    private BigDecimal returnRatioSup;

    /**
     * 项目返还给谁
     */
    private Integer returnProjectTo;

    /**
     * 项目返还给上级
     */
    private Integer returnProjectToSup;

    /**
     * 返还投资项目ID
     */
    private Long returnInvest;

    /**
     * 返还投资项目名称
     */
    private String returnInvestName;

    /**
     * 其他返还设置
     */
    private Integer ret;

    /**
     * 二级市场开放时间
     */
    private String marketDate;

    /**
     * 一级市场开放周几
     */
    private Integer rushWeek;

    /**
     * 一级市场开放时间(小时)
     */
    private Integer rushHour;

    /**
     * 一级市场可抢购时间(分钟)
     */
    private Integer rushMinute;

    /**
     * 折扣标志
     */
    private Integer disFlag;

    /**
     * 折扣信息
     */
    private String discount;

    /**
     * 折扣开始时间
     */
    private String discountStart;

    /**
     * 折扣结束时间
     */
    private String discountEnd;

    /**
     * 折扣数量
     */
    private Integer discountNum;

    /**
     * 折扣VIP等级
     */
    private Integer discountVip;

    /**
     * 参与人数
     */
    private Integer joinNum;

    /**
     * 团长返利
     */
    private Integer groupLeaderRebate;

    /**
     * 团员返利
     */
    private Integer groupMemberRebate;

    /**
     * 团购时间
     */
    private Integer groupTime;

    /**
     * 是否限时抢购（0:否,1:是）
     */
    private Integer rushbuyFalg;

    /**
     * 限时抢购开始时间
     */
    private String rushbuyStart;

    /**
     * 限时抢购结束时间
     */
    private String rushbuyEnd;

    /**
     * 限时可抢份数
     */
    private Integer rushbuyNum;

    /**
     * 项目说明
     */
    private String projectExplain;

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