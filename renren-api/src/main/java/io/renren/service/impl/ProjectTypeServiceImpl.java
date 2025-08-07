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
 import io.renren.dao.ProjectTypeDao;
 import io.renren.entity.ProjectTypeEntity;
 import io.renren.dto.ProjectTypeDTO;
 import io.renren.service.ProjectTypeService;
 import io.renren.common.utils.ConvertUtils;
 import org.springframework.stereotype.Service;
 
 import java.util.List;
 
 /**
  * 投资项目分类
  *
  * @author Mark sunlightcs@gmail.com
  * @since 1.0.0
  */
 @Service("projectTypeService")
 public class ProjectTypeServiceImpl extends BaseServiceImpl<ProjectTypeDao, ProjectTypeEntity> implements ProjectTypeService {
 
     @Override
     public List<ProjectTypeDTO> queryList() {
         // 构建查询条件
         QueryWrapper<ProjectTypeEntity> queryWrapper = new QueryWrapper<>();
         
         // 只查询启用的分类
         queryWrapper.eq("status", 1);
         
         // 按排序字段和创建时间排序
         queryWrapper.orderByAsc("sort").orderByDesc("create_date");
         
         // 查询数据
         List<ProjectTypeEntity> entityList = baseDao.selectList(queryWrapper);
         
         // 转换为DTO
         return ConvertUtils.sourceToTarget(entityList, ProjectTypeDTO.class);
     }
 }