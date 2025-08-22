package io.renren.modules.projecttype.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.projecttype.dao.ProjectTypeDao;
import io.renren.modules.projecttype.dto.ProjectTypeDTO;
import io.renren.modules.projecttype.entity.ProjectTypeEntity;
import io.renren.modules.projecttype.service.ProjectTypeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 投资项目分类表
 *
 * @author renren
 * @email renren@gmail.com
 * @since 1.0.0 2025-08-20
 */
@Slf4j
@Service
public class ProjectTypeServiceImpl extends CrudServiceImpl<ProjectTypeDao, ProjectTypeEntity, ProjectTypeDTO> implements ProjectTypeService {

    @Override
    public QueryWrapper<ProjectTypeEntity> getWrapper(Map<String, Object> params){
        QueryWrapper<ProjectTypeEntity> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("sort").orderByDesc("create_date");


        // 项目分类ID查询
//        String typeId = (String)params.get("typeId");
//        if (StringUtils.isNotBlank(typeId)) {
//            wrapper.eq("type_id", typeId);
//            log.debug("添加typeId查询条件: {}", typeId);
//        }
//
//        // 分类名称模糊查询
//        String typeName = (String)params.get("typeName");
//        if (StringUtils.isNotBlank(typeName)) {
//            wrapper.like("type_name", "%" + typeName + "%");
//            log.debug("添加typeName模糊查询条件: {}", typeName);
//        }
//
//        // 状态查询
//        String status = (String)params.get("status");
//        if (StringUtils.isNotBlank(status)) {
//            wrapper.eq("status", status);
//            log.debug("添加status查询条件: {}", status);
//        }
//
//        // 排序字段
//        String orderField = (String)params.get("orderField");
//        String order = (String)params.get("order");
//
//        // 前端字段排序
//        if (StringUtils.isNotBlank(orderField) && StringUtils.isNotBlank(order)) {
//            if ("asc".equalsIgnoreCase(order)) {
//                wrapper.orderByAsc(orderField);
//                log.debug("添加升序排序: {}", orderField);
//            } else {
//                wrapper.orderByDesc(orderField);
//                log.debug("添加降序排序: {}", orderField);
//            }
//        } else {
//            // 默认按排序字段和创建时间排序
//            log.debug("使用默认排序: sort ASC, create_date DESC");
//        }
        
        log.info("构建查询条件完成，参数: {}", params);
        return wrapper;
    }


}