package io.renren.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.sys.dao.UAddressConfigDao;
import io.renren.modules.sys.dto.UAddressConfigDTO;
import io.renren.modules.sys.entity.UAddressConfigEntity;
import io.renren.modules.sys.service.UAddressConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * U地址配置表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Service
public class UAddressConfigServiceImpl extends CrudServiceImpl<UAddressConfigDao, UAddressConfigEntity, UAddressConfigDTO> implements UAddressConfigService {

    @Override
    public QueryWrapper<UAddressConfigEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<UAddressConfigEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


}