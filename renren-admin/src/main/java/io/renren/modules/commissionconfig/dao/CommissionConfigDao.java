package io.renren.modules.commissionconfig.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.commissionconfig.entity.CommissionConfigEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 返佣比例配置表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Mapper
public interface CommissionConfigDao extends BaseDao<CommissionConfigEntity> {
	
}