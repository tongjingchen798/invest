package io.renren.modules.paychannel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.paychannel.dao.PayChannelDao;
import io.renren.modules.paychannel.dto.PayChannelDTO;
import io.renren.modules.paychannel.entity.PayChannelEntity;
import io.renren.modules.paychannel.service.PayChannelService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 支付渠道表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Service
public class PayChannelServiceImpl extends CrudServiceImpl<PayChannelDao, PayChannelEntity, PayChannelDTO> implements PayChannelService {

    @Override
    public QueryWrapper<PayChannelEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<PayChannelEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


}