package io.renren.modules.paymerchant.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.paymerchant.entity.PayMerchantEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付商户配置表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Mapper
public interface PayMerchantDao extends BaseDao<PayMerchantEntity> {
	
}