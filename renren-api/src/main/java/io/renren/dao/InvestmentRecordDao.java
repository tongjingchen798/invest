package io.renren.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.entity.InvestmentRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
}
