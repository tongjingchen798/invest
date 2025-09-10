package io.renren.dao;

import io.renren.common.dao.BaseDao;
import io.renren.entity.SmsMerchantConfigEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 短信商户配置表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Mapper
public interface SmsMerchantConfigDao extends BaseDao<SmsMerchantConfigEntity> {
	

	/**
	 * 根据商户名称查询配置
	 * 
	 * @return 短信商户配置
	 */
	@Select("select * from tb_sms_merchant_config where status=1 LIMIT 1")
	SmsMerchantConfigEntity selectByCaptchaName();
}