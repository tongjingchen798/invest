

package io.renren.modules.project.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.project.entity.ProjectEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 投资项目DAO接口
 * 
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Mapper
public interface ProjectDao extends BaseMapper<ProjectEntity> {
	
	/**
	 * 根据ID查询项目详情
	 * @param projectId 项目ID
	 * @return 项目实体
	 */
	ProjectEntity selectProjectById(@Param("projectId") Long projectId);
	
	/**
	 * 查询上架的项目列表
	 * @return 项目列表
	 */
	List<ProjectEntity> selectActiveProjects();
	
	/**
	 * 根据类型查询项目
	 * @param projectType 项目类型
	 * @return 项目列表
	 */
	List<ProjectEntity> selectProjectsByType(@Param("projectType") Integer projectType);
	
	/**
	 * 根据周期类型查询项目
	 * @param cycleType 周期类型
	 * @return 项目列表
	 */
	List<ProjectEntity> selectProjectsByCycleType(@Param("cycleType") Integer cycleType);
	
	/**
	 * 分页查询项目
	 * @param params 查询参数
	 * @return 项目列表
	 */
	List<ProjectEntity> selectProjectsByPage(Map<String, Object> params);
	
	/**
	 * 统计项目总数
	 * @param params 查询参数
	 * @return 项目总数
	 */
	int selectProjectCount(Map<String, Object> params);
	
	/**
	 * 更新项目投资金额
	 * @param projectId 项目ID
	 * @param amount 投资金额
	 * @return 影响行数
	 */
	int updateInvestmentAmount(@Param("projectId") Long projectId, @Param("amount") Long amount);
	
//	/**
//	 * 检查项目投资限额
//	 * @param projectId 项目ID
//	 * @param amount 投资金额
//	 * @return 是否可投资
//	 */
//	boolean checkInvestmentLimit(@Param("projectId") Long projectId, @Param("amount") Long amount);
}
