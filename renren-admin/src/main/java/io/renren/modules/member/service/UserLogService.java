package io.renren.modules.member.service;

import io.renren.common.service.CrudService;
import io.renren.modules.member.dto.UserLogDTO;
import io.renren.modules.member.entity.UserLogEntity;

/**
 * 用户登录日志表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-20
 */
public interface UserLogService extends CrudService<UserLogEntity, UserLogDTO> {

}