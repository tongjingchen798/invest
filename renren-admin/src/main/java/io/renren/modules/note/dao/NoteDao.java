package io.renren.modules.note.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.note.entity.NoteEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 公告表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Mapper
public interface NoteDao extends BaseDao<NoteEntity> {
	
}