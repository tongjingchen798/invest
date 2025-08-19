package io.renren.modules.issues.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.issues.dao.IssuesDao;
import io.renren.modules.issues.entity.IssuesEntity;
import io.renren.modules.issues.dto.IssuesDTO;
import io.renren.modules.issues.service.IssuesService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 广告/图片管理表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Service
public class IssuesServiceImpl extends CrudServiceImpl<IssuesDao, IssuesEntity, IssuesDTO> implements IssuesService {

    @Override
    public QueryWrapper<IssuesEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<IssuesEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


}