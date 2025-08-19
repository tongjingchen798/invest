package io.renren.modules.commissionconfig.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.commissionconfig.dao.CommissionConfigDao;
import io.renren.modules.commissionconfig.dto.CommissionConfigDTO;
import io.renren.modules.commissionconfig.entity.CommissionConfigEntity;
import io.renren.modules.commissionconfig.service.CommissionConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 返佣比例配置表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Service
public class CommissionConfigServiceImpl extends CrudServiceImpl<CommissionConfigDao, CommissionConfigEntity, CommissionConfigDTO> implements CommissionConfigService {

    @Override
    public QueryWrapper<CommissionConfigEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<CommissionConfigEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


}