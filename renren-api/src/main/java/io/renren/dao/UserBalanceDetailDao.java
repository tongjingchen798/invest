package io.renren.dao;

import io.renren.common.dao.BaseDao;
import io.renren.entity.UserBalanceDetailEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户余额明细
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Mapper
public interface UserBalanceDetailDao extends BaseDao<UserBalanceDetailEntity> {
    // 使用MyBatis-Plus的BaseMapper提供的基础CRUD方法
    // 分页查询通过selectPage方法实现
}
