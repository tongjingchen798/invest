package io.renren.modules.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.sms.dao.SmsMerchantConfigDao;
import io.renren.modules.sms.dto.SmsMerchantConfigDTO;
import io.renren.modules.sms.entity.SmsMerchantConfigEntity;
import io.renren.modules.sms.service.SmsMerchantConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 短信商户配置表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Service
public class SmsMerchantConfigServiceImpl extends CrudServiceImpl<SmsMerchantConfigDao, SmsMerchantConfigEntity, SmsMerchantConfigDTO> implements SmsMerchantConfigService {

    @Override
    public QueryWrapper<SmsMerchantConfigEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");
        String captchaName = (String)params.get("captchaName");

        QueryWrapper<SmsMerchantConfigEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.like(StringUtils.isNotBlank(captchaName), "captcha_name", captchaName);

        return wrapper;
    }


}