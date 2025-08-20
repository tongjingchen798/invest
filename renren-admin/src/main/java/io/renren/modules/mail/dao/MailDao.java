package io.renren.modules.mail.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.mail.entity.MailEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 站内信
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Mapper
public interface MailDao extends BaseDao<MailEntity> {
	
}
