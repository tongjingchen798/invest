package io.renren.modules.note.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.note.dao.NoteDao;
import io.renren.modules.note.dto.NoteDTO;
import io.renren.modules.note.entity.NoteEntity;
import io.renren.modules.note.service.NoteService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 公告表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Service
public class NoteServiceImpl extends CrudServiceImpl<NoteDao, NoteEntity, NoteDTO> implements NoteService {

    @Override
    public QueryWrapper<NoteEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<NoteEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


}