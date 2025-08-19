package io.renren.modules.balank.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.balank.entity.BankEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 银行管理表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Mapper
public interface BankDao extends BaseDao<BankEntity> {
	
}