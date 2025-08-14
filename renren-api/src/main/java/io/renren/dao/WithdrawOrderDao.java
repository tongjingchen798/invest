package io.renren.dao;

import io.renren.common.dao.BaseDao;
import io.renren.entity.WithdrawOrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 提现订单DAO接口
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Mapper
public interface WithdrawOrderDao extends BaseDao<WithdrawOrderEntity> {

    /**
     * 统计用户提现次数
     *
     * @param userId 用户ID
     * @return 提现次数
     */
    Long selectCountByUserId(@Param("userId") Long userId);

    /**
     * 查询用户正在提现金额
     *
     * @param userId 用户ID
     * @return 正在提现金额
     */
    Long selectPendingWithdrawAmountByUserId(@Param("userId") Long userId);
}
