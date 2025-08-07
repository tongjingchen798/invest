

 package io.renren.dto;

 import io.swagger.annotations.ApiModel;
 import io.swagger.annotations.ApiModelProperty;
 import lombok.Data;
 
 import java.io.Serializable;
 import java.math.BigDecimal;
 
 /**
  * 投资项目DTO
  *
  * @author Mark sunlightcs@gmail.com
  * @since 1.0.0
  */
 @Data
 @ApiModel(value = "投资项目表")
 public class ProjectDTO implements Serializable {
     private static final long serialVersionUID = 1L;
 
     @ApiModelProperty(value = "项目简称")
     private String abbreviation;
 
     @ApiModelProperty(value = "日收益率(%)")
     private String conversion;
 
     @ApiModelProperty(value = "赠送优惠券id")
     private Long couponId;
 
     @ApiModelProperty(value = "优惠券名称")
     private String couponName;
 
     @ApiModelProperty(value = "周期(天)")
     private Integer cycle;
 
     @ApiModelProperty(value = "回款方式（1:到期返还,2:每日返利）")
     private Integer cycleType;
 
     @ApiModelProperty(value = "disFlag")
     private Integer disFlag;
 
     @ApiModelProperty(value = "discount")
     private String discount;
 
     @ApiModelProperty(value = "discountEnd")
     private String discountEnd;
 
     @ApiModelProperty(value = "discountNum")
     private Integer discountNum;
 
     @ApiModelProperty(value = "discountStart")
     private String discountStart;
 
     @ApiModelProperty(value = "discountVip")
     private Integer discountVip;
 
     @ApiModelProperty(value = "抢购结束时间，从上架那一刻算，上架的时间加上抢购时间")
     private String expire;
 
     @ApiModelProperty(value = "groupLeaderRebate")
     private Integer groupLeaderRebate;
 
     @ApiModelProperty(value = "groupMemberRebate")
     private Integer groupMemberRebate;
 
     @ApiModelProperty(value = "groupTime")
     private Integer groupTime;
 
     @ApiModelProperty(value = "限时抢购几个小时")
     private Integer hour;
 
     @ApiModelProperty(value = "设备图片")
     private String img;
 
     @ApiModelProperty(value = "项目id")
     private Long investId;
 
     @ApiModelProperty(value = "项目名称")
     private String investName;
 
     @ApiModelProperty(value = "可买台数")
     private Integer investRepeat;
 
     @ApiModelProperty(value = "joinNum")
     private Integer joinNum;
 
     @ApiModelProperty(value = "二级市场开放时间")
     private String marketDate;
 
     @ApiModelProperty(value = "会员注册时间")
     private Integer memberRegistrationTime;
 
     @ApiModelProperty(value = "每日收益，（每日返还才有）")
     private Long principalProfit;
 
     @ApiModelProperty(value = "项目描述")
     private String projectDescribe;
 
     @ApiModelProperty(value = "项目类型（1:固定金额投资,2:众筹）")
     private Integer projectType;
 
     @ApiModelProperty(value = "注册结束时间")
     private String registerEndDate;
 
     @ApiModelProperty(value = "注册起始时间")
     private String registerStartDate;
 
     @ApiModelProperty(value = "retTo")
     private Integer retTo;
 
     @ApiModelProperty(value = "returnInvest")
     private Long returnInvest;
 
     @ApiModelProperty(value = "是否立返本金")
     private Integer returnPrincipal;
 
     @ApiModelProperty(value = "是否立返项目")
     private Integer returnProject;
 
     @ApiModelProperty(value = "returnProjectTo")
     private Integer returnProjectTo;
 
     @ApiModelProperty(value = "returnProjectToSup")
     private Integer returnProjectToSup;
 
     @ApiModelProperty(value = "返还比例")
     private BigDecimal returnRatio;
 
     @ApiModelProperty(value = "returnRatioSup")
     private BigDecimal returnRatioSup;
 
     @ApiModelProperty(value = "返还到谁")
     private Integer returnTo;
 
     @ApiModelProperty(value = "returnToSup")
     private Integer returnToSup;
 
     @ApiModelProperty(value = "是否返还转盘次数")
     private Integer returnWheel;
 
     @ApiModelProperty(value = "返还转盘次数给本人")
     private Integer returnWheelTo;
 
     @ApiModelProperty(value = "返还转盘次数给上级")
     private Integer returnWheelToSup;
 
     @ApiModelProperty(value = "一级市场开放时间")
     private Integer rushHour;
 
     @ApiModelProperty(value = "一级市场可抢购时间")
     private Integer rushMinute;
 
     @ApiModelProperty(value = "一级市场开放周几")
     private Integer rushWeek;
 
     @ApiModelProperty(value = "限时抢购结束时间")
     private String rushbuyEnd;
 
     @ApiModelProperty(value = "rushbuyEndTime")
     private Long rushbuyEndTime;
 
     @ApiModelProperty(value = "是否限时抢购 1是，0不是")
     private Integer rushbuyFalg;
 
     @ApiModelProperty(value = "限时可抢份数")
     private Integer rushbuyNum;
 
     @ApiModelProperty(value = "限时可抢剩余份数")
     private Integer rushbuyRemainNum;
 
     @ApiModelProperty(value = "限时抢购开始时间")
     private String rushbuyStart;
 
     @ApiModelProperty(value = "rushbuyStartTime")
     private Long rushbuyStartTime;
 
     @ApiModelProperty(value = "项目规模金额(个人购买)")
     private Long scaleAmount;
 
     @ApiModelProperty(value = "指定上架日期")
     private String sjDate;
 
     @ApiModelProperty(value = "排序")
     private Integer sort;
 
     @ApiModelProperty(value = "上下架 0下架  1上架   2 删除")
     private Integer status;
 
     @ApiModelProperty(value = "taskLimitTime")
     private String taskLimitTime;
 
     @ApiModelProperty(value = "本金+收益 = 投资金额 + 每日收益 * 周期")
     private Long totalCost;
 
     @ApiModelProperty(value = "总收益 =每日收益 * 周期")
     private Long totalProfit;
 
     @ApiModelProperty(value = "分类id")
     private Long typeId;
 
     @ApiModelProperty(value = "vip")
     private Integer vip;
 
     @ApiModelProperty(value = "指定下架日期")
     private String xjDate;
 }