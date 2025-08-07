

package io.renren.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.entity.ProjectEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 投资项目
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Mapper
public interface ProjectDao extends BaseMapper<ProjectEntity> {
	
}
