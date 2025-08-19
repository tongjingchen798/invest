package io.renren.modules.advertisement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.CrudService;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.advertisement.dao.AdvertisementDao;
import io.renren.modules.advertisement.dto.AdvertisementDTO;
import io.renren.modules.advertisement.entity.AdvertisementEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 广告素材表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Service
public class AdvertisementServiceImpl extends CrudServiceImpl<AdvertisementDao, AdvertisementEntity, AdvertisementDTO> implements CrudService<AdvertisementEntity, AdvertisementDTO> {

    @Override
    public QueryWrapper<AdvertisementEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<AdvertisementEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


}