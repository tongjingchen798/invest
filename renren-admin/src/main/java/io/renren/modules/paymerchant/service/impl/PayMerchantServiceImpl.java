package io.renren.modules.paymerchant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.paymerchant.dao.PayMerchantDao;
import io.renren.modules.paymerchant.dto.PayMerchantDTO;
import io.renren.modules.paymerchant.entity.PayMerchantEntity;
import io.renren.modules.paymerchant.service.PayMerchantService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 支付商户配置表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Service
public class PayMerchantServiceImpl extends CrudServiceImpl<PayMerchantDao, PayMerchantEntity, PayMerchantDTO> implements PayMerchantService {

    @Override
    public QueryWrapper<PayMerchantEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<PayMerchantEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


}