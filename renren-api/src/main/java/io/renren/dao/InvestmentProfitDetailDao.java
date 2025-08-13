package io.renren.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.entity.InvestmentProfitDetailEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 投资收益明细
 * 
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Mapper
public interface InvestmentProfitDetailDao extends BaseMapper<InvestmentProfitDetailEntity> {
	
	/**
	 * 根据投资记录ID查询收益明细
	 * @param investmentId 投资记录ID
	 * @return 收益明细列表
	 */
	List<InvestmentProfitDetailEntity> selectByInvestmentId(@Param("investmentId") Long investmentId);
	
	/**
	 * 根据用户ID查询收益明细
	 * @param userId 用户ID
	 * @return 收益明细列表
	 */
	List<InvestmentProfitDetailEntity> selectByUserId(@Param("userId") Long userId);
	
	/**
	 * 根据项目ID查询收益明细
	 * @param projectId 项目ID
	 * @return 收益明细列表
	 */
	List<InvestmentProfitDetailEntity> selectByProjectId(@Param("projectId") Long projectId);
	
	/**
	 * 根据收益类型查询收益明细
	 * @param profitType 收益类型
	 * @return 收益明细列表
	 */
	List<InvestmentProfitDetailEntity> selectByProfitType(@Param("profitType") Integer profitType);
	
	/**
	 * 查询用户总收益金额
	 * @param userId 用户ID
	 * @return 总收益金额
	 */
	Long selectTotalProfitByUserId(@Param("userId") Long userId);
	
	/**
	 * 查询用户指定类型的收益金额
	 * @param userId 用户ID
	 * @param profitType 收益类型
	 * @return 收益金额
	 */
	Long selectProfitByUserIdAndType(@Param("userId") Long userId, @Param("profitType") Integer profitType);
	
	/**
	 * 分页查询用户收益明细
	 * @param params 查询参数
	 * @return 收益明细列表
	 */
	List<InvestmentProfitDetailEntity> selectPageByUserId(Map<String, Object> params);
	
	/**
	 * 统计用户收益明细总数
	 * @param params 查询参数
	 * @return 记录总数
	 */
	int selectCountByUserId(Map<String, Object> params);
}
