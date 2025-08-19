package io.renren.dao;

import io.renren.common.dao.BaseDao;
import io.renren.entity.UserBalanceDetailEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * 用户余额明细
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Mapper
public interface UserBalanceDetailDao extends BaseDao<UserBalanceDetailEntity> {
    /**
     * 查询用户今日收益金额
     * @param userId 用户ID
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 今日收益金额（分）
     */
    Long getTodayProfitAmount(@Param("userId") Long userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
