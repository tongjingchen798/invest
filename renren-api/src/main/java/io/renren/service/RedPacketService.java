/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

 package io.renren.service;

 import com.baomidou.mybatisplus.extension.service.IService;
 import io.renren.common.service.BaseService;
 import io.renren.entity.RedPacketEntity;
 import io.renren.entity.RedPacketRecordEntity;
 
 import java.util.Map;
 
 /**
  * 红包主表
  *
  * @author Mark sunlightcs@gmail.com
  * @since 1.0.0
  */
 public interface RedPacketService extends BaseService<RedPacketEntity> {
 
     /**
      * 领取红包
      */
     Map<String, Object> receiveRedPacket(String password, Long userId, String userName, String ipAddress, String deviceInfo);
 }