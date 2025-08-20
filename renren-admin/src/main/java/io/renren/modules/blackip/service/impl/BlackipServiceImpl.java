package io.renren.modules.blackip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.blackip.dao.BlackipDao;
import io.renren.modules.blackip.dto.BlackipDTO;
import io.renren.modules.blackip.entity.BlackipEntity;
import io.renren.modules.blackip.service.BlackipService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-20
 */
@Service
public class BlackipServiceImpl extends CrudServiceImpl<BlackipDao, BlackipEntity, BlackipDTO> implements BlackipService {

    @Override
    public QueryWrapper<BlackipEntity> getWrapper(Map<String, Object> params){
        String ip = (String)params.get("ip");

        QueryWrapper<BlackipEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(ip), "ip", ip);

        return wrapper;
    }


}