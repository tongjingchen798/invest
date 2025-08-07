/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.service.impl.BaseServiceImpl;
import io.renren.dao.IssuesDao;
import io.renren.dao.ProjectDao;
import io.renren.entity.IssuesEntity;
import io.renren.entity.ProjectEntity;
import io.renren.dto.ProjectDTO;
import io.renren.service.ProjectService;
import io.renren.common.utils.ConvertUtils;
import org.springframework.stereotype.Service;

import java.util.List;

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
public ProjectDTO getById(Long id) {
    ProjectEntity entity = baseDao.selectById(id);
    if (entity == null) {
        return null;
    }
    return ConvertUtils.sourceToTarget(entity, ProjectDTO.class);
}
}
