package io.renren.service.impl;

import io.renren.dao.InvestmentRecordDao;
import io.renren.dao.ProjectDao;
import io.renren.dao.UserDao;
import io.renren.dao.UserBalanceDetailDao;
import io.renren.dto.PlaceOrderDTO;
import io.renren.entity.InvestmentRecordEntity;
import io.renren.entity.ProjectEntity;
import io.renren.entity.UserBalanceDetailEntity;
import io.renren.entity.UserEntity;
import io.renren.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 下单服务实现类
 * 
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private ProjectDao projectDao;
	
	@Autowired
	private InvestmentRecordDao investmentRecordDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserBalanceDetailDao userBalanceDetailDao;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map<String, String> placeOrder(PlaceOrderDTO dto, Long userId) {
		Map<String, String> result = new HashMap<>();
		
		try {
			// 1. 验证投资条件
			Map<String, String> validation = validateInvestment(dto, userId);
			if (!"success".equals(validation.get("status"))) {
				result.put("status", "error");
				result.put("message", validation.get("message"));
				return result;
			}
			
			// 2. 获取项目信息
			ProjectEntity project = projectDao.selectProjectById(dto.getInvestId());
			if (project == null) {
				result.put("status", "error");
				result.put("message", "投资项目不存在");
				return result;
			}
			
			// 3. 验证并扣减用户余额
			Map<String, String> balanceResult = validateAndDeductBalance(userId, dto.getAmount());
			if (!"success".equals(balanceResult.get("status"))) {
				result.put("status", "error");
				result.put("message", balanceResult.get("message"));
				return result;
			}
			
			// 4. 生成订单号
			String orderNumber = generateOrderNumber();
			
			// 5. 创建投资记录
			InvestmentRecordEntity investmentRecord = new InvestmentRecordEntity();
			investmentRecord.setUserId(userId);
			investmentRecord.setProjectId(dto.getInvestId());
			investmentRecord.setInvestmentAmount(dto.getAmount());
			investmentRecord.setOrderId(Long.parseLong(orderNumber));
			investmentRecord.setOrderAbbr(orderNumber.substring(orderNumber.length() - 8));
			investmentRecord.setOrderDate(new Date());
			investmentRecord.setStatus(0); // 0:未收益
			investmentRecord.setCycle(project.getCycle());
			investmentRecord.setCycleType(project.getCycleType());
			investmentRecord.setDdsy(0L); // 等待收益
			investmentRecord.setInvestCount(dto.getCount());
			investmentRecord.setRushMinute(project.getRushMinute());
			investmentRecord.setAgent(""); // 代理信息
			investmentRecord.setSalesmanid(""); // 销售员ID
			investmentRecord.setCreateDate(new Date());
			investmentRecord.setUpdateDate(new Date());
			
			// 6. 保存投资记录
			investmentRecordDao.insert(investmentRecord);
			
			// 7. 记录账变明细
			recordBalanceDetail(userId, dto.getAmount(), orderNumber, project.getInvestName(), balanceResult.get("originalBalance"));
			
			// 8. 更新项目投资金额和参与人数
			int projectUpdateRows = projectDao.updateInvestmentAmount(dto.getInvestId(), dto.getAmount());
			if (projectUpdateRows == 0) {
				throw new RuntimeException("更新项目投资金额失败");
			}
			
			// 9. 更新用户表中的投资相关字段（项目数、总本金等）
			int userUpdateRows = userDao.updateAllInvestmentFields(userId, dto.getAmount());
			if (userUpdateRows == 0) {
				throw new RuntimeException("更新用户投资统计失败");
			}
			
			
			result.put("status", "success");
			result.put("message", "下单成功");
			result.put("orderNumber", orderNumber);
			result.put("investmentId", investmentRecord.getId().toString());
			
		} catch (Exception e) {
			result.put("status", "error");
			result.put("message", "下单失败: " + e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 验证并扣减用户余额
	 */
	private Map<String, String> validateAndDeductBalance(Long userId, Long amount) {
		Map<String, String> result = new HashMap<>();
		
		try {
			// 1. 获取用户信息
			UserEntity user = userDao.getUserByUserId(userId);
			if (user == null) {
				result.put("status", "error");
				result.put("message", "用户不存在");
				return result;
			}
			
			// 2. 验证用户可用余额是否充足
			Long currentAssets = user.getAssets() != null ? user.getAssets() : 0L;
			if (currentAssets < amount) {
				result.put("status", "error");
				result.put("message", "可用余额不足，当前可用余额: " + (currentAssets / 100.0) + "元，需要: " + (amount / 100.0) + "元");
				return result;
			}
			
			// 3. 执行余额扣款（原子操作，包含余额验证）
			int updateRows = userDao.updateBalanceForInvestment(userId, amount);
			if (updateRows == 0) {
				result.put("status", "error");
				result.put("message", "余额扣款失败，余额不足");
				return result;
			}
			
			// 4. 记录原始余额，用于账变记录
			result.put("originalBalance", currentAssets.toString());
			result.put("status", "success");
			result.put("message", "余额扣款成功");
			
		} catch (Exception e) {
			result.put("status", "error");
			result.put("message", "余额验证失败: " + e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 记录账变明细
	 */
	private void recordBalanceDetail(Long userId, Long amount, String orderNumber, String projectName, String originalBalance) {
		try {
			UserBalanceDetailEntity balanceDetail = new UserBalanceDetailEntity();
			balanceDetail.setUserId(userId);
			
			Date now = new Date();
			balanceDetail.setTransactionDate(now);
			balanceDetail.setBusinessType(1); // 1:购买流水
			balanceDetail.setChannel("1");
			balanceDetail.setStreamId(orderNumber);
			balanceDetail.setUseAmount(amount);
			balanceDetail.setOriginalAmount(Long.parseLong(originalBalance));
			balanceDetail.setTransactionAmount(Long.parseLong(originalBalance) - amount);
			balanceDetail.setRemarks("购买投资项目【" + projectName + "】");
			balanceDetail.setStatus(1);
			balanceDetail.setSalesmanId(1748403763980L);
			balanceDetail.setAgentId(1748403763980L);
			balanceDetail.setCreateDate(now);
			balanceDetail.setUpdateDate(now);
			
			userBalanceDetailDao.insert(balanceDetail);
			
		} catch (Exception e) {
			// 记录账变失败不影响主流程，只记录日志
			e.printStackTrace();
		}
	}
	
	@Override
	public Map<String, String> validateInvestment(PlaceOrderDTO dto, Long userId) {
		Map<String, String> result = new HashMap<>();
		
		try {
			// 1. 验证项目是否存在
			ProjectEntity project = projectDao.selectProjectById(dto.getInvestId());
			if (project == null) {
				result.put("status", "error");
				result.put("message", "投资项目不存在");
				return result;
			}
			
			// 2. 验证项目状态
			if (project.getStatus() != 1) {
				result.put("status", "error");
				result.put("message", "项目已下架或不可投资");
				return result;
			}
			
			// 3. 验证投资金额
			if (dto.getAmount() <= 0) {
				result.put("status", "error");
				result.put("message", "投资金额必须大于0");
				return result;
			}
			
			// 4. 验证购买份数
			if (dto.getCount() <= 0) {
				result.put("status", "error");
				result.put("message", "购买份数必须大于0");
				return result;
			}
			
			// 5. 验证项目投资限额
			if (project.getScaleAmount() != null && dto.getAmount() > project.getScaleAmount()) {
				result.put("status", "error");
				result.put("message", "投资金额超过项目限额");
				return result;
			}
			
			// 6. 验证可买台数
			if (project.getInvestRepeat() != null && dto.getCount() > project.getInvestRepeat()) {
				result.put("status", "error");
				result.put("message", "购买份数超过项目可买台数");
				return result;
			}
			
//			// 7. 验证项目投资限额（参与人数和剩余份数）
//			boolean canInvest = projectDao.checkInvestmentLimit(dto.getInvestId(), dto.getAmount());
//			if (!canInvest) {
//				result.put("status", "error");
//				result.put("message", "项目投资限额已满或剩余份数不足");
//				return result;
//			}
			
			// 8. 验证用户可用余额是否充足
			UserEntity user = userDao.getUserByUserId(userId);
			if (user == null) {
				result.put("status", "error");
				result.put("message", "用户不存在");
				return result;
			}
			
			Long currentAssets = user.getAssets() != null ? user.getAssets() : 0L;
			if (currentAssets < dto.getAmount()) {
				result.put("status", "error");
				result.put("message", "可用余额不足，当前可用余额: " + (currentAssets / 100.0) + "元，需要: " + (dto.getAmount() / 100.0) + "元");
				return result;
			}
			
			result.put("status", "success");
			result.put("message", "验证通过");
			
		} catch (Exception e) {
			result.put("status", "error");
			result.put("message", "验证失败: " + e.getMessage());
		}
		
		return result;
	}
	
	@Override
	public Map<String, Object> calculateAmount(PlaceOrderDTO dto) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			// 获取项目信息
			ProjectEntity project = projectDao.selectProjectById(dto.getInvestId());
			if (project == null) {
				result.put("status", "error");
				result.put("message", "投资项目不存在");
				return result;
			}
			
			// 计算基础金额
			Long baseAmount = dto.getAmount();
			Long finalAmount = baseAmount;
			
			// 处理优惠券折扣（如果有）
			if (dto.getCouponType() != null && dto.getCouponType() == 1 && dto.getUserCouponId() != null && !dto.getUserCouponId().isEmpty()) {
				// TODO: 实现优惠券折扣计算逻辑
				// finalAmount = baseAmount * discountRate;
			}
			
			// 处理赠送金额
			if (dto.getGift() != null && dto.getGift() > 0) {
				finalAmount += dto.getGift();
			}
			
			result.put("status", "success");
			result.put("baseAmount", baseAmount);
			result.put("finalAmount", finalAmount);
			result.put("discountAmount", baseAmount - finalAmount);
			result.put("giftAmount", dto.getGift() != null ? dto.getGift() : 0);
			
		} catch (Exception e) {
			result.put("status", "error");
			result.put("message", "计算失败: " + e.getMessage());
		}
		
		return result;
	}
	
	@Override
	public String generateOrderNumber() {
		// 生成订单号：时间戳 + 随机数
		String timestamp = String.valueOf(System.currentTimeMillis());
		String random = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
		return timestamp + random;
	}
}
