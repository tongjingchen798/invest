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
  * 付息还本表
  *
  * @author Mark sunlightcs@gmail.com
  * @since 1.0.0
  */
 @Data
 @TableName("tb_profit")
 public class ProfitEntity implements Serializable {
     private static final long serialVersionUID = 1L;
 
     @TableId
     private Long id;
 
     /**
      * 用户ID
      */
     private Long userId;
 
     /**
      * 投资项目ID
      */
     private Long investId;
 
     /**
      * 项目名称
      */
     private String investName;
 
     /**
      * 投资金额
      */
     private Long investmentAmount;
 
     /**
      * 收益金额
      */
     private Long profitAmount;
 
     /**
      * 收益利息
      */
     private Long profitInterest;
 
     /**
      * 收益本金
      */
     private Long profitPrincipal;
 
     /**
      * 等待收益
      */
     private Long ddsy;
 
     /**
      * 项目周期
      */
     private Integer cycle;
 
     /**
      * 周期类型（1:到期收益含本金,2:每日返本金到期收益）
      */
     private Integer cycleType;
 
     /**
      * 投资日期
      */
     private Date orderDate;
 
     /**
      * 收益日期
      */
     private Date profitDate;
 
     /**
      * 下单ID
      */
     private Long orderId;
 
     /**
      * 订单号简称
      */
     private String orderAbbr;
 
     /**
      * 投资总数
      */
     private Integer investCount;
 
     /**
      * 图片
      */
     private String img;
 
     /**
      * 状态 0：未收益 1：已收益
      */
     private Integer status;
 
     /**
      * 代理ID
      */
     private Long agent;
 
     /**
      * 业务员ID
      */
     private Long salesmanid;
 
     /**
      * 手机号
      */
     private String mobile;
 
     /**
      * 抢购分钟
      */
     private Integer rushMinute;
 
     /**
      * 创建时间
      */
     private Date createTime;
 
     /**
      * 更新时间
      */
     private Date updateTime;
 }