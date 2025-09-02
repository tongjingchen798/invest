package io.renren.modules.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.member.dao.UserLogDao;
import io.renren.modules.member.dto.UserLogDTO;
import io.renren.modules.member.entity.UserLogEntity;
import io.renren.modules.member.service.UserLogService;
import io.renren.modules.security.user.SecurityUser;
import io.renren.modules.security.user.UserDetail;
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

    @Override
    public PageData<UserLogDTO> customPage(Map<String, Object> params) {
        // 创建MyBatis-Plus分页对象
        long curPage = 1;
        long limit = 10;
        
        if (params.get(Constant.PAGE) != null) {
            curPage = Long.parseLong((String) params.get(Constant.PAGE));
        }
        if (params.get(Constant.LIMIT) != null) {
            limit = Long.parseLong((String) params.get(Constant.LIMIT));
        }
        
        // 添加权限控制参数
        UserDetail user = SecurityUser.getUser();
        if (user != null) {
            params.put("currentUserId", user.getId());
            params.put("currentUserType", user.getType());
        }
        
        // 创建分页对象，注意这里使用UserLogDTO作为泛型
        Page<UserLogDTO> page = new Page<>(curPage, limit);
        
        // 调用自定义的XML查询方法
        IPage<UserLogDTO> pageResult = baseDao.selectUserLogPage(page, params);
        
        // 转换为PageData格式
        return new PageData<>(pageResult.getRecords(), pageResult.getTotal());
    }

}