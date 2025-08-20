package io.renren.modules.mail.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.mail.entity.MailEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 站内信
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Mapper
public interface MailDao extends BaseMapper<MailEntity> {
	
}
