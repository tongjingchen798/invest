package io.renren.modules.member.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.member.entity.BlacklistEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 黑白名单DAO
 *
 * @author renren
 * @since 1.0.0
 */
@Mapper
public interface BlacklistDao extends BaseMapper<BlacklistEntity> {

}
