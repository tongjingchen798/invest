package io.renren.modules.balank.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.balank.dao.BankDao;
import io.renren.modules.balank.dto.BankDTO;
import io.renren.modules.balank.entity.BankEntity;
import io.renren.modules.balank.service.BankService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 银行管理表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Service
public class BankServiceImpl extends CrudServiceImpl<BankDao, BankEntity, BankDTO> implements BankService {

    @Override
    public QueryWrapper<BankEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");
        Integer state = Integer.parseInt(params.get("state").toString());

        QueryWrapper<BankEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(state != null, "state", state);

        return wrapper;
    }


}