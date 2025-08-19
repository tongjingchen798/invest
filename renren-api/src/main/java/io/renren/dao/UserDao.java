

package io.renren.dao;

import io.renren.common.dao.BaseDao;
import io.renren.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 用户
 * 
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface UserDao extends BaseDao<UserEntity> {
    UserEntity getUserByMobile(String mobile);

    UserEntity getUserByUserId(Long userId);

    @Select("select * from tb_user where invite_code = #{inviteCode}")
    UserEntity getUserByInviteCode(@Param("inviteCode") String inviteCode);

    /**
     * 根据上级邀请码查询用户列表
     * @param upinviteCode 上级邀请码
     * @return 用户列表
     */
    List<UserEntity> selectBySuperiorId(@Param("upinviteCode") String upinviteCode);

    /**
     * 更新用户余额（扣款）
     * @param userId 用户ID
     * @param amount 扣款金额（分）
     * @return 影响行数
     */
    @Update("UPDATE tb_user SET assets = assets - #{amount}, history_investment = history_investment + #{amount}, today_investment = today_investment + #{amount} WHERE id = #{userId} AND assets >= #{amount}")
    int updateBalanceForInvestment(@Param("userId") Long userId, @Param("amount") Long amount);

    /**
     * 更新用户累计投资金额
     * @param userId 用户ID
     * @param amount 投资金额（分）
     * @return 影响行数
     */
    @Update("UPDATE tb_user SET history_investment = history_investment + #{amount}, today_investment = today_investment + #{amount} WHERE id = #{userId}")
    int updateInvestmentAmount(@Param("userId") Long userId, @Param("amount") Long amount);

    /**
     * 更新用户投资统计信息（项目数、总本金等）
     * @param userId 用户ID
     * @param amount 投资金额（分）
     * @return 影响行数
     */
    @Update("UPDATE tb_user SET itmes = itmes + 1, total_principal = total_principal + #{amount} WHERE id = #{userId}")
    int updateInvestmentStatistics(@Param("userId") Long userId, @Param("amount") Long amount);

    /**
     * 更新用户投资相关字段（综合更新）
     * @param userId 用户ID
     * @param amount 投资金额（分）
     * @return 影响行数
     */
    @Update("UPDATE tb_user SET " +
            "history_investment = history_investment + #{amount}, " +
            "today_investment = today_investment + #{amount}, " +
            "itmes = itmes + 1, " +
            "total_principal = total_principal + #{amount} " +
            "WHERE id = #{userId}")
    int updateAllInvestmentFields(@Param("userId") Long userId, @Param("amount") Long amount);

    /**
     * 更新用户可用余额（增加收益）
     * @param userId 用户ID
     * @param amount 收益金额（分）
     * @return 影响行数
     */
    @Update("UPDATE tb_user SET assets = assets + #{amount} WHERE id = #{userId}")
    int addUserBalance(@Param("userId") Long userId, @Param("amount") Long amount);


    /**
     * 减少用户可用余额
     * @param userId 用户ID
     * @param amount 收益金额（分）
     * @return 影响行数
     */
    @Update("UPDATE tb_user SET assets = assets - #{amount} WHERE id = #{userId}")
    int reduceUserBalance(@Param("userId") Long userId, @Param("amount") Long amount);
    /**
     * 更新用户今日收益
     * @param userId 用户ID
     * @param amount 收益金额（分）
     * @return 影响行数
     */
    @Update("UPDATE tb_user SET today_profit = today_profit + #{amount} WHERE id = #{userId}")
    int addTodayProfit(@Param("userId") Long userId, @Param("amount") Long amount);

    /**
     * 更新用户历史收益
     * @param userId 用户ID
     * @param amount 收益金额（分）
     * @return 影响行数
     */
    @Update("UPDATE tb_user SET history_profit = history_profit + #{amount} WHERE id = #{userId}")
    int addHistoryProfit(@Param("userId") Long userId, @Param("amount") Long amount);

    /**
     * 更新用户总收益
     * @param userId 用户ID
     * @param amount 收益金额（分）
     * @return 影响行数
     */
    @Update("UPDATE tb_user SET total_profit = total_profit + #{amount} WHERE id = #{userId}")
    int addTotalProfit(@Param("userId") Long userId, @Param("amount") Long amount);

    /**
     * 更新用户收益相关字段（综合更新）
     * @param userId 用户ID
     * @param amount 收益金额（分）
     * @return 影响行数
     */
    @Update("UPDATE tb_user SET " +
            "assets = assets + #{amount}, " +
            "today_profit = today_profit + #{amount}, " +
            "history_profit = history_profit + #{amount}, " +
            "total_profit = total_profit + #{amount} " +
            "WHERE id = #{userId}")
    int updateAllProfitFields(@Param("userId") Long userId, @Param("amount") Long amount);

//    /**
//     * 更新用户佣金余额
//     * @param userId 用户ID
//     * @param amount 佣金金额（分）
//     * @return 影响行数
//     */
//    @Update("UPDATE tb_user SET commission_balance = commission_balance + #{amount} WHERE id = #{userId}")
//    int addCommissionBalance(@Param("userId") Long userId, @Param("amount") Long amount);
//
//    /**
//     * 更新用户历史佣金余额
//     * @param userId 用户ID
//     * @param amount 佣金金额（分）
//     * @return 影响行数
//     */
//    @Update("UPDATE tb_user SET history_commission_balance = history_commission_balance + #{amount} WHERE id = #{userId}")
//    int addHistoryCommissionBalance(@Param("userId") Long userId, @Param("amount") Long amount);

    /**
     * 更新用户佣金相关字段（综合更新）
     * @param userId 用户ID
     * @param amount 佣金金额（分）
     * @return 影响行数
     */
    @Update("UPDATE tb_user SET " +
            "commission_balance = commission_balance + #{amount}, " +
            "history_commission_balance = history_commission_balance + #{amount} " +
            "WHERE id = #{userId}")
    int updateAllCommissionFields(@Param("userId") Long userId, @Param("amount") Long amount);

    /**
     * 重置用户今日收益和充值字段（每日定时任务调用）
     * @return 影响行数
     */
    @Update("UPDATE tb_user SET today_profit = 0, today_investment = 0, today_recharge = 0, today_recharge_cnt = 0")
    int resetTodayFields();

    /**
     * 获取用户当前余额信息
     * @param userId 用户ID
     * @return 用户余额信息
     */
    @Select("SELECT id, assets, balance, commission_balance, today_profit, history_profit, total_profit FROM tb_user WHERE id = #{userId}")
    UserEntity getUserBalanceInfo(@Param("userId") Long userId);

//    /**
//     * 更新用户今日充值次数
//     * @param userId 用户ID
//     * @return 影响行数
//     */
//    @Update("UPDATE tb_user SET today_recharge_cnt = today_recharge_cnt + 1 WHERE id = #{userId}")
//    int addTodayRechargeCount(@Param("userId") Long userId);
//
//    /**
//     * 更新用户历史充值次数
//     * @param userId 用户ID
//     * @return 影响行数
//     */
//    @Update("UPDATE tb_user SET historychargecnt = historychargecnt + 1 WHERE id = #{userId}")
//    int addHistoryRechargeCount(@Param("userId") Long userId);

    /**
     * 更新用户充值次数相关字段（综合更新）
     * @param userId 用户ID
     * @return 影响行数
     */
    @Update("UPDATE tb_user SET " +
            "today_recharge_cnt = today_recharge_cnt + 1, " +
            "historychargecnt = historychargecnt + 1 " +
            "WHERE id = #{userId}")
    int updateAllRechargeCountFields(@Param("userId") Long userId);

//    /**
//     * 更新用户今日充值金额
//     * @param userId 用户ID
//     * @param amount 充值金额（分）
//     * @return 影响行数
//     */
//    @Update("UPDATE tb_user SET today_recharge = today_recharge + #{amount} WHERE id = #{userId}")
//    int addTodayRechargeAmount(@Param("userId") Long userId, @Param("amount") Long amount);
//
//    /**
//     * 更新用户累计充值金额
//     * @param userId 用户ID
//     * @param amount 充值金额（分）
//     * @return 影响行数
//     */
//    @Update("UPDATE tb_user SET charge_sum = charge_sum + #{amount} WHERE id = #{userId}")
//    int addChargeSum(@Param("userId") Long userId, @Param("amount") Long amount);

    /**
     * 更新用户充值金额相关字段（综合更新）
     * @param userId 用户ID
     * @param amount 充值金额（分）
     * @return 影响行数
     */
    @Update("UPDATE tb_user SET " +
            "today_recharge = today_recharge + #{amount}, " +
            "charge_sum = charge_sum + #{amount} " +
            "WHERE id = #{userId}")
    int updateAllRechargeAmountFields(@Param("userId") Long userId, @Param("amount") Long amount);

    /**
     * 更新用户充值相关字段（次数+金额，综合更新）
     * @param userId 用户ID
     * @param amount 充值金额（分）
     * @return 影响行数
     */
    @Update("UPDATE tb_user SET " +
            "today_recharge_cnt = today_recharge_cnt + 1, " +
            "historychargecnt = historychargecnt + 1, " +
            "today_recharge = today_recharge + #{amount}, " +
            "charge_sum = charge_sum + #{amount} " +
            "WHERE id = #{userId}")
    int updateAllRechargeFields(@Param("userId") Long userId, @Param("amount") Long amount);

    /**
     * 检查手机号是否已存在
     * @param mobile 手机号
     * @return 存在返回1，不存在返回0
     */
    @Select("SELECT COUNT(1) FROM tb_user WHERE mobile = #{mobile}")
    int checkMobileExists(@Param("mobile") String mobile);

    /**
     * 根据邀请码查询用户
     * @param inviteCode 邀请码
     * @return 用户信息
     */
    @Select("SELECT * FROM tb_user WHERE invite_code = #{inviteCode}")
    UserEntity selectByInviteCode(@Param("inviteCode") String inviteCode);

    /**
     * 更新用户佣金相关字段
     * @param userId 用户ID
     * @param commissionAmount 佣金金额（分）
     */
    @Update("UPDATE tb_user SET " +
            "commission_balance = commission_balance + #{commissionAmount}, " +
            "history_commission = history_commission + #{commissionAmount}, " +
            "today_commission = today_commission + #{commissionAmount} " +
            "WHERE id = #{userId}")
    int updateCommissionFields(@Param("userId") Long userId, @Param("commissionAmount") long commissionAmount);
}
