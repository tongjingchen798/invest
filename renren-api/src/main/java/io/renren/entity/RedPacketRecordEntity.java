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
  * 红包领取记录表
  *
  * @author Mark sunlightcs@gmail.com
  * @since 1.0.0
  */
 @Data
 @TableName("tb_red_packet_record")
 public class RedPacketRecordEntity implements Serializable {
     private static final long serialVersionUID = 1L;
 
     @TableId
     private Long id;
 
     /**
      * 红包ID
      */
     private String redPacketId;
 
     /**
      * 领取用户ID
      */
     private Long userId;
 
     /**
      * 领取用户姓名
      */
     private String userName;
 
     /**
      * 领取金额(分)
      */
     private Long amount;
 
     /**
      * 领取时间
      */
     private Date receiveTime;
 
     /**
      * IP地址
      */
     private String ipAddress;
 
     /**
      * 设备信息
      */
     private String deviceInfo;
 
     /**
      * 状态 1:正常 0:异常
      */
     private Integer status;
 }