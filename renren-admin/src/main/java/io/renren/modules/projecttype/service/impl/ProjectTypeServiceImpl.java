package io.renren.modules.projecttype.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.projecttype.dao.ProjectTypeDao;
import io.renren.modules.projecttype.dto.ProjectTypeDTO;
import io.renren.modules.projecttype.entity.ProjectTypeEntity;
import io.renren.modules.projecttype.service.ProjectTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 投资项目分类表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-20
 */
@Service
public class ProjectTypeServiceImpl extends CrudServiceImpl<ProjectTypeDao, ProjectTypeEntity, ProjectTypeDTO> implements ProjectTypeService {

    @Override
    public QueryWrapper<ProjectTypeEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<ProjectTypeEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


}