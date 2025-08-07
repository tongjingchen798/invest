/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.entity.RedPacketRecordEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 红包领取记录表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Mapper
public interface RedPacketRecordDao extends BaseMapper<RedPacketRecordEntity> {

}