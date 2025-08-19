package io.renren.modules.issues.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.issues.entity.IssuesEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 广告/图片管理表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Mapper
public interface IssuesDao extends BaseDao<IssuesEntity> {
	
}