package io.renren.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统用户Dao
 *
 * @author renren
 * @since 2024-01-01
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUserEntity> {

}
