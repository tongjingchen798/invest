package io.renren.modules.signconfig.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.signconfig.entity.SignRewardConfigEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 签到奖励配置表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Mapper
public interface SignRewardConfigDao extends BaseDao<SignRewardConfigEntity> {
	
}