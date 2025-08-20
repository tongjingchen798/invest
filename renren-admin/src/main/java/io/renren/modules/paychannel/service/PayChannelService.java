package io.renren.modules.paychannel.service;

import io.renren.common.page.PageData;
import io.renren.common.service.CrudService;
import io.renren.modules.paychannel.dto.PayChannelDTO;
import io.renren.modules.paychannel.entity.PayChannelEntity;

import java.util.Map;

/**
 * 支付渠道表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
public interface PayChannelService extends CrudService<PayChannelEntity, PayChannelDTO> {

    /**
     * 分页查询
     */
    PageData<PayChannelDTO> selectPage(Map<String, Object> params);

}