package io.renren.modules.projecttype.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.projecttype.entity.ProjectTypeEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 投资项目分类表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-20
 */
@Mapper
public interface ProjectTypeDao extends BaseDao<ProjectTypeEntity> {
	
}