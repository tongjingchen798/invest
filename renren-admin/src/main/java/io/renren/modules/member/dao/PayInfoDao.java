package io.renren.modules.member.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.member.entity.PayInfoEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户支付信息表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-20
 */
@Mapper
public interface PayInfoDao extends BaseDao<PayInfoEntity> {
	
}