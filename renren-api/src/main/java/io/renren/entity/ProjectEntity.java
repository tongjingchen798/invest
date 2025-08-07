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
 import java.math.BigDecimal;
 import java.util.Date;
 
 /**
  * 投资项目
  *
  * @author Mark sunlightcs@gmail.com
  * @since 1.0.0
  */
 @Data
 @TableName("tb_project")
 public class ProjectEntity implements Serializable {
     private static final long serialVersionUID = 1L;
 
     /**
      * 项目id
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
      * 项目描述
      */
     private String projectDescribe;
     
     /**
      * 项目类型（1:固定金额投资,2:众筹）
      */
     private Integer projectType;
     
     /**
      * 分类id
      */
     private Long typeId;
     
     /**
      * 周期(天)
      */
     private Integer cycle;
     
     /**
      * 回款方式（1:到期返还,2:每日返利）
      */
     private Integer cycleType;
     
     /**
      * 日收益率(%)
      */
     private String conversion;
     
     /**
      * 项目规模金额(个人购买)
      */
     private Long scaleAmount;
     
     /**
      * 可买台数
      */
     private Integer investRepeat;
     
     /**
      * 每日收益，（每日返还才有）
      */
     private Long principalProfit;
     
     /**
      * 总收益 =每日收益 * 周期
      */
     private Long totalProfit;
     
     /**
      * 本金+收益 = 投资金额 + 每日收益 * 周期
      */
     private Long totalCost;
     
     /**
      * 设备图片
      */
     private String img;
     
     /**
      * 排序
      */
     private Integer sort;
     
     /**
      * 上下架 0下架  1上架   2 删除
      */
     private Integer status;
     
     /**
      * 是否限时抢购 1是，0不是
      */
     private Integer rushbuyFalg;
     
     /**
      * 限时抢购几个小时
      */
     private Integer hour;
     
     /**
      * 限时抢购开始时间
      */
     private String rushbuyStart;
     
     /**
      * 限时抢购结束时间
      */
     private String rushbuyEnd;
     
     /**
      * 限时抢购开始时间戳
      */
     private Long rushbuyStartTime;
     
     /**
      * 限时抢购结束时间戳
      */
     private Long rushbuyEndTime;
     
     /**
      * 限时可抢份数
      */
     private Integer rushbuyNum;
     
     /**
      * 限时可抢剩余份数
      */
     private Integer rushbuyRemainNum;
     
     /**
      * 抢购结束时间，从上架那一刻算，上架的时间加上抢购时间
      */
     private String expire;
     
     /**
      * 一级市场开放周几
      */
     private Integer rushWeek;
     
     /**
      * 一级市场开放时间
      */
     private Integer rushHour;
     
     /**
      * 一级市场可抢购时间
      */
     private Integer rushMinute;
     
     /**
      * 二级市场开放时间
      */
     private String marketDate;
     
     /**
      * 注册起始时间
      */
     private String registerStartDate;
     
     /**
      * 注册结束时间
      */
     private String registerEndDate;
     
     /**
      * 指定上架日期
      */
     private String sjDate;
     
     /**
      * 指定下架日期
      */
     private String xjDate;
     
     /**
      * 是否立返本金
      */
     private Integer returnPrincipal;
     
     /**
      * 是否立返项目
      */
     private Integer returnProject;
     
     /**
      * 返还比例
      */
     private BigDecimal returnRatio;
     
     /**
      * 返还到谁
      */
     private Integer returnTo;
     
     /**
      * 是否返还转盘次数
      */
     private Integer returnWheel;
     
     /**
      * 返还转盘次数给本人
      */
     private Integer returnWheelTo;
     
     /**
      * 返还转盘次数给上级
      */
     private Integer returnWheelToSup;
     
     /**
      * 赠送优惠券id
      */
     private Long couponId;
     
     /**
      * 优惠券名称
      */
     private String couponName;
     
     /**
      * 折扣
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
      * 折扣VIP
      */
     private Integer discountVip;
     
     /**
      * 团购时间
      */
     private Integer groupTime;
     
     /**
      * 团长返利
      */
     private Integer groupLeaderRebate;
     
     /**
      * 团员返利
      */
     private Integer groupMemberRebate;
     
     /**
      * 参与人数
      */
     private Integer joinNum;
     
     /**
      * 会员注册时间
      */
     private Integer memberRegistrationTime;
     
     /**
      * VIP等级
      */
     private Integer vip;
     
     /**
      * 任务限制时间
      */
     private String taskLimitTime;
     
     /**
      * 其他字段
      */
     private Integer disFlag;
     private Long returnInvest;
     private Integer returnProjectTo;
     private Integer returnProjectToSup;
     private BigDecimal returnRatioSup;
     private Integer returnToSup;
     private Integer retTo;
     
     /**
      * 创建时间
      */
     private Date createDate;
     
     /**
      * 更新时间
      */
     private Date updateDate;
 }