package io.renren.modules.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.member.dao.UserLogDao;
import io.renren.modules.member.dto.UserLogDTO;
import io.renren.modules.member.entity.UserLogEntity;
import io.renren.modules.member.service.UserLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 用户登录日志表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-20
 */
@Service
public class UserLogServiceImpl extends CrudServiceImpl<UserLogDao, UserLogEntity, UserLogDTO> implements UserLogService {

    @Override
    public QueryWrapper<UserLogEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<UserLogEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


}