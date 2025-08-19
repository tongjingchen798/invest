package io.renren.modules.paychannel.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.paychannel.entity.PayChannelEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付渠道表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Mapper
public interface PayChannelDao extends BaseDao<PayChannelEntity> {
	
}