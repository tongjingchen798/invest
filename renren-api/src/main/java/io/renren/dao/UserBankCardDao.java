package io.renren.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.entity.UserBankCardEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户银行卡配置表
 *
 * @author renren
 * @since 2024-01-01
 */
@Mapper
public interface UserBankCardDao extends BaseMapper<UserBankCardEntity> {

}
