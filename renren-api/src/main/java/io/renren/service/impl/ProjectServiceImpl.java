

package io.renren.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.common.page.PageData;
import io.renren.common.service.impl.BaseServiceImpl;
import io.renren.dao.ProjectDao;
import io.renren.entity.ProjectEntity;
import io.renren.dto.ProjectDTO;
import io.renren.service.ProjectService;
import io.renren.common.utils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import io.renren.dto.ProjectDetailDTO;

/**
 * 投资项目
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Service("projectService")
public class ProjectServiceImpl extends BaseServiceImpl<ProjectDao, ProjectEntity> implements ProjectService {

    @Override
    public List<ProjectDTO> queryListGroup() {
        // 构建查询条件
        QueryWrapper<ProjectEntity> queryWrapper = new QueryWrapper<>();
        
        // 只查询上架的项目
        queryWrapper.eq("status", 1);
        
        // 按排序字段和创建时间排序
        queryWrapper.orderByAsc("sort").orderByDesc("create_date");
        
        // 查询数据
        List<ProjectEntity> entityList = baseDao.selectList(queryWrapper);
        
        // 转换为DTO
        		return ConvertUtils.sourceToTarget(entityList, ProjectDTO.class);
	}

	@Override
	public PageData<ProjectEntity> getProjectPage(Map<String, Object> params) {
		// 获取分页参数
		long page = Long.parseLong(params.getOrDefault("page", "1").toString());
		long limit = Long.parseLong(params.getOrDefault("limit", "10").toString());
		
		// 创建分页对象
		IPage<ProjectEntity> pageParam = new Page<>(page, limit);
		
		// 构建查询条件
		QueryWrapper<ProjectEntity> queryWrapper = buildQueryWrapper(params);
		
		// 执行分页查询
		IPage<ProjectEntity> result = baseDao.selectPage(pageParam, queryWrapper);
		
		// 转换为PageData
		return new PageData<>(result.getRecords(), result.getTotal());
	}
	
	@Override
	public ProjectDetailDTO getProjectDetail(Long projectId) {
		// 根据ID查询项目
		ProjectEntity project = baseDao.selectById(projectId);
		
		if (project == null) {
			return null;
		}
		
		// 转换为DTO
		ProjectDetailDTO detailDTO = new ProjectDetailDTO();
		detailDTO.setInvestId(project.getInvestId() != null ? project.getInvestId().toString() : null);
		detailDTO.setInvestName(project.getInvestName());
		detailDTO.setAbbreviation(project.getAbbreviation());
		detailDTO.setStatus(project.getStatus());
		detailDTO.setInvestRepeat(project.getInvestRepeat());
		detailDTO.setProjectType(project.getProjectType());
		detailDTO.setCycleType(project.getCycleType());
		detailDTO.setScaleAmount(project.getScaleAmount() != null ? project.getScaleAmount().toString() : null);
		detailDTO.setCycle(project.getCycle());
		detailDTO.setConversion(project.getConversion());
		detailDTO.setPrincipalProfit(project.getPrincipalProfit() != null ? project.getPrincipalProfit().toString() : null);
		detailDTO.setTotalProfit(project.getTotalProfit() != null ? project.getTotalProfit().toString() : null);
		detailDTO.setTotalCost(project.getTotalCost() != null ? project.getTotalCost().toString() : null);
		detailDTO.setTypeId(project.getTypeId() != null ? project.getTypeId().toString() : null);
		detailDTO.setImg(project.getImg());
		detailDTO.setProjectDescribe(project.getProjectDescribe());
		detailDTO.setSort(project.getSort());
		detailDTO.setVip(project.getVip());
		
		return detailDTO;
	}
	
	/**
	 * 构建查询条件
	 */
	private QueryWrapper<ProjectEntity> buildQueryWrapper(Map<String, Object> params) {
		QueryWrapper<ProjectEntity> queryWrapper = new QueryWrapper<>();
		
		// 项目名称模糊查询
		Object investNameObj = params.get("investName");
		if (investNameObj != null && StringUtils.isNotBlank(investNameObj.toString())) {
			queryWrapper.like("invest_name", investNameObj.toString());
		}
		
		// 项目状态查询
		Object statusObj = params.get("status");
		if (statusObj != null) {
			if (statusObj instanceof Integer) {
				queryWrapper.eq("status", statusObj);
			} else if (statusObj instanceof String && StringUtils.isNotBlank((String) statusObj)) {
				try {
					queryWrapper.eq("status", Integer.parseInt((String) statusObj));
				} catch (NumberFormatException e) {
					// 忽略无效的状态值
				}
			}
		}
		
		// 项目类型查询
		Object projectTypeObj = params.get("projectType");
		if (projectTypeObj != null) {
			if (projectTypeObj instanceof Integer) {
				queryWrapper.eq("project_type", projectTypeObj);
			} else if (projectTypeObj instanceof String && StringUtils.isNotBlank((String) projectTypeObj)) {
				try {
					queryWrapper.eq("project_type", Integer.parseInt((String) projectTypeObj));
				} catch (NumberFormatException e) {
					// 忽略无效的项目类型值
				}
			}
		}
		
		// 周期类型查询
		Object cycleTypeObj = params.get("cycleType");
		if (cycleTypeObj != null) {
			if (cycleTypeObj instanceof Integer) {
				queryWrapper.eq("cycle_type", cycleTypeObj);
			} else if (cycleTypeObj instanceof String && StringUtils.isNotBlank((String) cycleTypeObj)) {
				try {
					queryWrapper.eq("cycle_type", Integer.parseInt((String) cycleTypeObj));
				} catch (NumberFormatException e) {
					// 忽略无效的周期类型值
				}
			}
		}
		
		// 排序
		Object orderFieldObj = params.get("orderField");
		Object orderObj = params.get("order");
		if (orderFieldObj != null && StringUtils.isNotBlank(orderFieldObj.toString()) && 
			orderObj != null && StringUtils.isNotBlank(orderObj.toString())) {
			boolean isAsc = "asc".equalsIgnoreCase(orderObj.toString());
			queryWrapper.orderBy(true, isAsc, orderFieldObj.toString());
		} else {
			// 默认按创建时间倒序
			queryWrapper.orderByDesc("create_date");
		}
		
		return queryWrapper;
	}




}
