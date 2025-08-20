package io.renren.modules.coupon.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.coupon.entity.CouponEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠卷
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-20
 */
@Mapper
public interface CouponDao extends BaseDao<CouponEntity> {
	
}