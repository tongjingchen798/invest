package io.renren.modules.paymerchant.service;

import io.renren.common.service.CrudService;
import io.renren.modules.paymerchant.dto.PayMerchantDTO;
import io.renren.modules.paymerchant.entity.PayMerchantEntity;

/**
 * 支付商户配置表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
public interface PayMerchantService extends CrudService<PayMerchantEntity, PayMerchantDTO> {

    /**
     * 更新商户优先级
     * @param dto 商户信息
     * @return 是否成功
     */
    boolean updateDegreeHeat(PayMerchantDTO dto);
}