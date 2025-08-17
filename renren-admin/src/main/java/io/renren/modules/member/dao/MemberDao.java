package io.renren.modules.member.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.member.entity.MemberEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员DAO接口
 *
 * @author renren
 * @since 1.0.0
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
    // 使用MyBatis-Plus的BaseMapper提供的基础CRUD方法
}
