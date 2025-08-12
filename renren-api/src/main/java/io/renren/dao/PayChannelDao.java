package io.renren.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.entity.PayChannelEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付渠道Dao
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Mapper
public interface PayChannelDao extends BaseMapper<PayChannelEntity> {

}