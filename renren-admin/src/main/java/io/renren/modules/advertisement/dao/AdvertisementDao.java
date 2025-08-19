package io.renren.modules.advertisement.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.advertisement.entity.AdvertisementEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 广告素材表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Mapper
public interface AdvertisementDao extends BaseDao<AdvertisementEntity> {
	
}