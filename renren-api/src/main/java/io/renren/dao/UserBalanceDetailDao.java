package io.renren.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.entity.UserBalanceDetailEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 账变详情表
 *
 * @author renren
 * @since 2024-01-01
 */
@Mapper
public interface UserBalanceDetailDao extends BaseMapper<UserBalanceDetailEntity> {

}
