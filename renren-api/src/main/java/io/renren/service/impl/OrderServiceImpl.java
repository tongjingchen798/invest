package io.renren.service.impl;

import io.renren.dao.InvestmentRecordDao;
import io.renren.dao.ProjectDao;
import io.renren.dto.PlaceOrderDTO;
import io.renren.entity.InvestmentRecordEntity;
import io.renren.entity.ProjectEntity;
import io.renren.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
			
			// 3. 生成订单号
			String orderNumber = generateOrderNumber();
			
			// 4. 创建投资记录
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
			
			// 5. 保存投资记录
			investmentRecordDao.insert(investmentRecord);
			
			// 6. 更新项目投资金额（如果需要）
			// projectDao.updateInvestmentAmount(dto.getInvestId(), dto.getAmount());
			
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
