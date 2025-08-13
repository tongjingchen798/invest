 

package io.renren.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.entity.ProjectEntity;

import java.util.List;
import java.util.Map;

/**
 * 投资项目服务接口
 * 
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
public interface ProjectService extends IService<ProjectEntity> {
	
	/**
	 * 根据ID查询项目详情
	 * @param projectId 项目ID
	 * @return 项目实体
	 */
	ProjectEntity getProjectById(Long projectId);
	
	/**
	 * 查询上架的项目列表
	 * @return 项目列表
	 */
	List<ProjectEntity> getActiveProjects();
	
	/**
	 * 根据类型查询项目
	 * @param projectType 项目类型
	 * @return 项目列表
	 */
	List<ProjectEntity> getProjectsByType(Integer projectType);
	
	/**
	 * 根据周期类型查询项目
	 * @param cycleType 周期类型
	 * @return 项目列表
	 */
	List<ProjectEntity> getProjectsByCycleType(Integer cycleType);
	
	/**
	 * 分页查询项目
	 * @param params 查询参数
	 * @return 项目列表
	 */
	List<ProjectEntity> getProjectsByPage(Map<String, Object> params);
	
	/**
	 * 统计项目总数
	 * @param params 查询参数
	 * @return 项目总数
	 */
	int getProjectCount(Map<String, Object> params);
	
	/**
	 * 更新项目投资金额
	 * @param projectId 项目ID
	 * @param amount 投资金额
	 * @return 是否成功
	 */
	boolean updateProjectInvestmentAmount(Long projectId, Long amount);
	
	/**
	 * 检查项目是否可投资
	 * @param projectId 项目ID
	 * @param amount 投资金额
	 * @return 是否可投资
	 */
	boolean canInvest(Long projectId, Long amount);
}
