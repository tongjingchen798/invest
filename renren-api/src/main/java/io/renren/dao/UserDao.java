

package io.renren.dao;

import io.renren.common.dao.BaseDao;
import io.renren.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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

    /**
     * 更新用户佣金余额
     * @param userId 用户ID
     * @param amount 佣金金额（分）
     * @return 影响行数
     */
    @Update("UPDATE tb_user SET commission_balance = commission_balance + #{amount} WHERE id = #{userId}")
    int addCommissionBalance(@Param("userId") Long userId, @Param("amount") Long amount);

    /**
     * 更新用户历史佣金余额
     * @param userId 用户ID
     * @param amount 佣金金额（分）
     * @return 影响行数
     */
    @Update("UPDATE tb_user SET history_commission_balance = history_commission_balance + #{amount} WHERE id = #{userId}")
    int addHistoryCommissionBalance(@Param("userId") Long userId, @Param("amount") Long amount);

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
     * 重置用户今日收益（每日定时任务调用）
     * @return 影响行数
     */
    @Update("UPDATE tb_user SET today_profit = 0, today_investment = 0")
    int resetTodayFields();

    /**
     * 获取用户当前余额信息
     * @param userId 用户ID
     * @return 用户余额信息
     */
    @Select("SELECT id, assets, balance, commission_balance, today_profit, history_profit, total_profit FROM tb_user WHERE id = #{userId}")
    UserEntity getUserBalanceInfo(@Param("userId") Long userId);
}
