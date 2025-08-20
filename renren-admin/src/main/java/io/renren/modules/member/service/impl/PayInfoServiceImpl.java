package io.renren.modules.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.member.dao.PayInfoDao;
import io.renren.modules.member.dto.PayInfoDTO;
import io.renren.modules.member.entity.PayInfoEntity;
import io.renren.modules.member.service.PayInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 用户支付信息表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-20
 */
@Service
public class PayInfoServiceImpl extends CrudServiceImpl<PayInfoDao, PayInfoEntity, PayInfoDTO> implements PayInfoService {

    @Override
    public QueryWrapper<PayInfoEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<PayInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


}