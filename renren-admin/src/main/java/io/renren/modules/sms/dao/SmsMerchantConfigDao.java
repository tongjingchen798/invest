package io.renren.modules.sms.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.sms.entity.SmsMerchantConfigEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 短信商户配置表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Mapper
public interface SmsMerchantConfigDao extends BaseDao<SmsMerchantConfigEntity> {
	
}