package io.renren.modules.signconfig.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.signconfig.dao.SignRewardConfigDao;
import io.renren.modules.signconfig.dto.SignRewardConfigDTO;
import io.renren.modules.signconfig.entity.SignRewardConfigEntity;
import io.renren.modules.signconfig.service.SignRewardConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 签到奖励配置表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Service
public class SignRewardConfigServiceImpl extends CrudServiceImpl<SignRewardConfigDao, SignRewardConfigEntity, SignRewardConfigDTO> implements SignRewardConfigService {

    @Override
    public QueryWrapper<SignRewardConfigEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SignRewardConfigEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


}