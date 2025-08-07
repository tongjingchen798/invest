/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

 package io.renren.dao;

 import com.baomidou.mybatisplus.core.mapper.BaseMapper;
 import io.renren.entity.RedPacketEntity;
 import org.apache.ibatis.annotations.Mapper;
 import org.apache.ibatis.annotations.Param;
 import org.apache.ibatis.annotations.Update;
 
 /**
  * 红包主表
  *
  * @author Mark sunlightcs@gmail.com
  * @since 1.0.0
  */
 @Mapper
 public interface RedPacketDao extends BaseMapper<RedPacketEntity> {
 
     /**
      * 更新已领取红包数
      */
     @Update("UPDATE tb_red_packet SET collated = collated + 1 WHERE id = #{id} AND collated < number")
     int updateCollated(@Param("id") String id);
 }