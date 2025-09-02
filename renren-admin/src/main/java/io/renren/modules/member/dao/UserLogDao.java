package io.renren.modules.member.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.common.dao.BaseDao;
import io.renren.modules.member.dto.UserLogDTO;
import io.renren.modules.member.entity.UserLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 用户登录日志表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-20
 */
@Mapper
public interface UserLogDao extends BaseDao<UserLogEntity> {
	
	/**
	 * 自定义分页查询，支持多表关联查询和权限筛选
	 * @param page 分页参数
	 * @param params 查询参数
	 * @return 分页结果
	 */
	IPage<UserLogDTO> selectUserLogPage(Page<UserLogDTO> page, @Param("params") Map<String, Object> params);
}