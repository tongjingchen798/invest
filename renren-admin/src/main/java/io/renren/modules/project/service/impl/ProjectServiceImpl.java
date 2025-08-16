package io.renren.modules.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.page.PageData;
import io.renren.modules.project.dao.ProjectDao;
import io.renren.modules.project.entity.ProjectEntity;
import io.renren.modules.project.service.ProjectService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 投资项目服务实现类
 * 
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectDao, ProjectEntity> implements ProjectService {

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
        IPage<ProjectEntity> result = this.page(pageParam, queryWrapper);
        
        // 转换为PageData
        return new PageData<>(result.getRecords(), result.getTotal());
    }
    
    /**
     * 构建查询条件
     */
    private QueryWrapper<ProjectEntity> buildQueryWrapper(Map<String, Object> params) {
        QueryWrapper<ProjectEntity> queryWrapper = new QueryWrapper<>();
        
        // 项目名称模糊查询
        String investName = (String) params.get("investName");
        if (StringUtils.isNotBlank(investName)) {
            queryWrapper.like("invest_name", investName);
        }
        
        // 项目状态查询
        String status = (String) params.get("status");
        if (StringUtils.isNotBlank(status)) {
            queryWrapper.eq("status", Integer.parseInt(status));
        }
        
        // 项目类型查询
        String projectType = (String) params.get("projectType");
        if (StringUtils.isNotBlank(projectType)) {
            queryWrapper.eq("project_type", Integer.parseInt(projectType));
        }
        
        // 周期类型查询
        String cycleType = (String) params.get("cycleType");
        if (StringUtils.isNotBlank(cycleType)) {
            queryWrapper.eq("cycle_type", Integer.parseInt(cycleType));
        }
        
        // 排序
        String orderField = (String) params.get("orderField");
        String order = (String) params.get("order");
        if (StringUtils.isNotBlank(orderField)) {
            boolean isAsc = "asc".equalsIgnoreCase(order);
            queryWrapper.orderBy(true, isAsc, orderField);
        } else {
            // 默认按创建时间倒序
            queryWrapper.orderByDesc("create_date");
        }
        
        return queryWrapper;
    }
}
