package io.renren.dao;

import io.renren.common.dao.BaseDao;
import io.renren.entity.WithdrawOrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

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

    /**
     * 查询用户指定日期的提现总额
     *
     * @param userId 用户ID
     * @param date 指定日期
     * @return 提现总额
     */
    Long selectWithdrawAmountByUserIdAndDate(@Param("userId") Long userId, @Param("date") Date date);

    @Select("select * from tb_withdraw_order where  orderno=#{orderno}")
    WithdrawOrderEntity selectByOrderno(@Param("orderno") String orderno);
}
