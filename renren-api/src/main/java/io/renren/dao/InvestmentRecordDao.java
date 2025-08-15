package io.renren.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.entity.InvestmentRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户投资记录
 * 
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Mapper
public interface InvestmentRecordDao extends BaseMapper<InvestmentRecordEntity> {
	
	/**
	 * 根据用户ID查询投资记录
	 * @param userId 用户ID
	 * @return 投资记录列表
	 */
	List<InvestmentRecordEntity> selectByUserId(@Param("userId") Long userId);
	
	/**
	 * 根据用户ID和状态查询投资记录
	 * @param userId 用户ID
	 * @param status 状态
	 * @return 投资记录列表
	 */
	List<InvestmentRecordEntity> selectByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Integer status);
	
	/**
	 * 根据项目ID查询投资记录
	 * @param projectId 项目ID
	 * @return 投资记录列表
	 */
	List<InvestmentRecordEntity> selectByProjectId(@Param("projectId") Long projectId);
	
	/**
	 * 查询用户总投资金额
	 * @param userId 用户ID
	 * @return 总投资金额
	 */
	Long selectTotalInvestmentByUserId(@Param("userId") Long userId);
	
	/**
	 * 查询用户总收益金额
	 * @param userId 用户ID
	 * @return 总收益金额
	 */
	Long selectTotalProfitByUserId(@Param("userId") Long userId);
	
	/**
	 * 分页查询用户投资记录
	 * @param params 查询参数
	 * @return 投资记录列表
	 */
	List<InvestmentRecordEntity> selectPageByUserId(Map<String, Object> params);
	
	/**
	 * 统计用户投资记录总数
	 * @param params 查询参数
	 * @return 记录总数
	 */
	int selectCountByUserId(Map<String, Object> params);

	/**
	 * 查询用户待收利息金额
	 * @param userId 用户ID
	 * @return 待收利息金额
	 */
	Long selectPendingInterestByUserId(@Param("userId") Long userId);
	
	/**
	 * 查询所有有投资记录的用户ID（去重）
	 * @return 用户ID列表
	 */
	List<Long> selectDistinctUserIdsWithInvestment();

	/**
	 * 查询用户待收本金金额
	 * @param userId 用户ID
	 * @return 待收本金金额
	 */
	Long selectPendingPrincipalByUserId(@Param("userId") Long userId);

	/**
	 * 查询用户已收利息金额
	 * @param userId 用户ID
	 * @return 已收利息金额
	 */
	Long selectReceivedInterestByUserId(@Param("userId") Long userId);

	/**
	 * 查询用户已收本金金额
	 * @param userId 用户ID
	 * @return 已收本金金额
	 */
	Long selectReceivedPrincipalByUserId(@Param("userId") Long userId);

	/**
	 * 更新投资记录状态和收益信息
	 * @param investmentId 投资记录ID
	 * @param profitAmount 收益金额
	 * @param profitDate 收益日期
	 * @return 影响行数
	 */
	@Update("UPDATE tb_investment_record SET " +
			"status = 1, " +
			"profit_amount = #{profitAmount}, " +
			"profit_date = #{profitDate}, " +
			"update_date = #{profitDate} " +
			"WHERE id = #{investmentId}")
	int updateStatusAndProfit(@Param("investmentId") Long investmentId, 
							 @Param("profitAmount") BigDecimal profitAmount, 
							 @Param("profitDate") Date profitDate);

	/**
	 * 批量更新投资记录状态和收益信息
	 * @param investmentIds 投资记录ID列表
	 * @param profitAmount 收益金额
	 * @param profitDate 收益日期
	 * @return 影响行数
	 */
	int batchUpdateStatusAndProfit(@Param("investmentIds") List<Long> investmentIds, 
								  @Param("profitAmount") BigDecimal profitAmount, 
								  @Param("profitDate") Date profitDate);

	/**
	 * 根据状态查询投资记录数量
	 * @param status 状态
	 * @return 记录数量
	 */
	int selectCountByStatus(@Param("status") Integer status);

	/**
	 * 查询指定日期范围内的投资记录
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @return 投资记录列表
	 */
	List<InvestmentRecordEntity> selectByDateRange(@Param("startDate") Date startDate, 
												   @Param("endDate") Date endDate);

	/**
	 * 查询用户指定状态的投资记录数量
	 * @param userId 用户ID
	 * @param status 状态
	 * @return 记录数量
	 */
	int selectCountByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Integer status);

	/**
	 * 查询投资记录统计信息
	 * @param userId 用户ID
	 * @return 统计信息Map
	 */
	Map<String, Object> selectInvestmentStatistics(@Param("userId") Long userId);
}
