package io.renren.modules.member.service;

import io.renren.common.page.PageData;
import io.renren.common.service.CrudService;
import io.renren.modules.member.dto.UserLogDTO;
import io.renren.modules.member.entity.UserLogEntity;

import java.util.Map;

/**
 * 用户登录日志表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-20
 */
public interface UserLogService extends CrudService<UserLogEntity, UserLogDTO> {

    /**
     * 自定义分页查询，支持多表关联查询和权限筛选
     * @param params 查询参数
     * @return 分页数据
     */
    PageData<UserLogDTO> customPage(Map<String, Object> params);
}