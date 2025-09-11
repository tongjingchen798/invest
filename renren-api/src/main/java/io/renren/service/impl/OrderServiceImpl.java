package io.renren.service.impl;

import io.renren.common.exception.RenException;
import io.renren.common.exception.ErrorCode;
import io.renren.common.utils.MessageUtils;
import io.renren.config.CommissionConfig;
import io.renren.dao.InvestmentRecordDao;
import io.renren.dao.ProjectDao;
import io.renren.dao.UserDao;
import io.renren.dao.UserBalanceDetailDao;
import io.renren.dto.PlaceOrderDTO;
import io.renren.entity.InvestmentRecordEntity;
import io.renren.entity.ProjectEntity;
import io.renren.entity.UserBalanceDetailEntity;
import io.renren.entity.UserEntity;
import io.renren.enums.BusinessTypeEnum;
import io.renren.service.OrderService;
import io.renren.utils.InvestmentProfitCalculator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
@Slf4j
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

	@Autowired
	private CommissionConfig commissionConfig;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map<String, String> placeOrder(PlaceOrderDTO dto, Long userId) {
		Map<String, String> result = new HashMap<>();
			//验证投资金额
			if (dto.getAmount() <= 0) {
				throw new RenException(ErrorCode.INVESTMENT_AMOUNT_INVALID);
			}

			//验证购买份数
			if (dto.getCount() <= 0) {
				throw new RenException(ErrorCode.INVESTMENT_COUNT_INVALID);
			}

			// 2. 获取项目信息
			ProjectEntity project = projectDao.selectProjectById(dto.getInvestId());
			if (project == null) {
				throw new RenException(10031);
			}
			if (project.getStatus() != 1) {
				throw new RenException(ErrorCode.PROJECT_NOT_AVAILABLE);
			}

			//获取用户信息
			UserEntity user = userDao.selectById(userId);
			//验证并扣减用户余额
			Long currentAssets = user.getAssets();
			if (currentAssets < dto.getAmount()) {
				throw new RenException(ErrorCode.INSUFFICIENT_BALANCE);
			}
			//执行余额扣款（原子操作，包含余额验证）
			int updateRows = userDao.updateBalanceForInvestment(user.getId(), dto.getAmount());
			if (updateRows == 0) {
				throw new RenException(ErrorCode.INSUFFICIENT_BALANCE);
			}
			
			//生成订单号
			String orderNumber = generateOrderNumber();
			Date transactionDate=new Date();
			// 5. 创建投资记录
			InvestmentRecordEntity investmentRecord = new InvestmentRecordEntity();
			investmentRecord.setUserId(userId);
			investmentRecord.setProjectId(dto.getInvestId());
			investmentRecord.setInvestName(project.getInvestName());
			investmentRecord.setOrderAbbr(orderNumber.substring(orderNumber.length() - 8));
			investmentRecord.setOrderDate(transactionDate);
			investmentRecord.setStatus(0); // 0:未收益
			// 计算收益结束时间和总收益金额
			Date orderDate = investmentRecord.getOrderDate();
			Integer cycle = project.getCycle();
			Integer cycleType = project.getCycleType();
			String conversion = project.getConversion();
			// 计算收益结束时间
			Date profitEndDate = InvestmentProfitCalculator.calculateProfitEndDate(orderDate, cycle);
			investmentRecord.setProfitDate(profitEndDate);
			
			BigDecimal rate = new BigDecimal(conversion);
			rate = rate.divide(new BigDecimal("100"), 4, BigDecimal.ROUND_DOWN);

			BigDecimal investmentAmountTotal = new BigDecimal(dto.getAmount());
			// 计算每日收益金额
			BigDecimal ddsy=investmentAmountTotal.multiply(rate).multiply(new BigDecimal(dto.getCount()));
			investmentRecord.setInvestmentAmount(investmentAmountTotal.longValue());
			Long totalProfit = ddsy.multiply(new BigDecimal(cycle)).longValue();
			investmentRecord.setProfitAmount(totalProfit);
			investmentRecord.setCycle(cycle);
			investmentRecord.setCycleType(cycleType);
			investmentRecord.setDdsy(ddsy.longValue()); // 等待每日收益金额
			investmentRecord.setInvestCount(dto.getCount());
			investmentRecord.setRushMinute(project.getRushMinute());
			UserEntity userEntity=userDao.selectById(userId);
			investmentRecord.setAgent(userEntity.getAgent()); // 代理信息
			investmentRecord.setSalesmanid(userEntity.getSalesmanid()); // 销售员ID
			investmentRecord.setCreateDate(new Date());
			investmentRecord.setUpdateDate(new Date());
			
			// 6. 保存投资记录
			investmentRecordDao.insert(investmentRecord);
			
			// 7. 记录账变明细
			recordBalanceDetail(userId, dto.getAmount(), orderNumber, project.getInvestName(), currentAssets);
			
			// 8. 更新项目参与人数
			projectDao.updateInvestmentAmount(dto.getInvestId(), dto.getAmount());

			// 9. 更新用户表中的投资相关字段（项目数、总本金等）
			userDao.updateAllInvestmentFields(userId, dto.getAmount());

			// 10. 处理本金返还逻辑
			handlePrincipalReturn(project, user, investmentAmountTotal, investmentRecord.getOrderId(), transactionDate);

			// 获取1级推荐人
			String firstLevelInviteCode = user.getUpinviteCode();
			if(StringUtils.isNotBlank(firstLevelInviteCode)){
				// 查询1级推荐人
				UserEntity firstLevelReferrer = userDao.selectByInviteCode(firstLevelInviteCode);
				if (firstLevelReferrer != null) {
					// 计算1级佣金
					Long firstLevelCommission = calculateCommission(investmentAmountTotal, commissionConfig.getFirstLevelRateDecimal());
					//更新上级佣金余额 累加相关字段 记录账变
					userDao.updateCommissionFields(firstLevelReferrer.getId(), firstLevelCommission);

					log.debug("用户佣金更新成功，用户ID: {}, 一级佣金金额: {}", userId, firstLevelCommission);
					//记录1级佣金流水
					UserBalanceDetailEntity detail = new UserBalanceDetailEntity();
					detail.setBusiType(BusinessTypeEnum.COMMISSION_A.getCode());
					detail.setUserId(firstLevelReferrer.getId());
					detail.setOriginalAmount(firstLevelReferrer.getAssets());
					detail.setUseAmount(firstLevelCommission);
					detail.setTransactionAmount(firstLevelReferrer.getAssets()+firstLevelCommission);
					detail.setStatus(1);
					detail.setFormUserId(userId);
					detail.setTransactionDate(transactionDate);
					detail.setCreateDate(transactionDate);
					detail.setUpdateDate(transactionDate);
					detail.setRemarks("一级返佣");
					detail.setStreamId(investmentRecord.getOrderId().toString());
					userBalanceDetailDao.insert(detail);
					// 查询2级推荐人
					UserEntity twoLevelReferrer = userDao.selectByInviteCode(firstLevelReferrer.getUpinviteCode());
					if (twoLevelReferrer != null) {
						// 计算2级佣金
						Long twoLevelCommission = calculateCommission(investmentAmountTotal, commissionConfig.getSecondLevelRateDecimal());
						//更新上级余额 累加相关字段 记录账变
						userDao.updateCommissionFields(twoLevelReferrer.getId(), twoLevelCommission);
						log.debug("用户佣金更新成功，用户ID: {}, 二级佣金金额: {}", userId, twoLevelCommission);
						//记录2级佣金流水
						UserBalanceDetailEntity detail1 = new UserBalanceDetailEntity();
						detail1.setBusiType(BusinessTypeEnum.COMMISSION_B.getCode());
						detail1.setUserId(twoLevelReferrer.getId());
						detail1.setOriginalAmount(twoLevelReferrer.getAssets());
						detail1.setUseAmount(twoLevelCommission);
						detail1.setTransactionAmount(twoLevelReferrer.getAssets()+twoLevelCommission);
						detail1.setStatus(1);
						detail1.setFormUserId(userId);
						detail1.setTransactionDate(transactionDate);
						detail1.setCreateDate(transactionDate);
						detail1.setUpdateDate(transactionDate);
						detail1.setRemarks("二级返佣");
						detail1.setStreamId(investmentRecord.getOrderId().toString());
						userBalanceDetailDao.insert(detail1);
					}
				}
			}
			result.put("status", "success");
			result.put("message", "下单成功");
			result.put("orderNumber", orderNumber);
			result.put("investmentId", investmentRecord.getOrderId().toString());
			result.put("investName", investmentRecord.getInvestName());
			result.put("count", investmentRecord.getInvestCount().toString());

		return result;
	}
	
	/**
	 * 验证并扣减用户余额
	 */
	private Map<String, String> validateAndDeductBalance(UserEntity user, Long amount) {
		Map<String, String> result = new HashMap<>();
		Long currentAssets = user.getAssets() != null ? user.getAssets() : 0L;
		if (currentAssets < amount) {
			throw new RenException(ErrorCode.INSUFFICIENT_BALANCE);
		}
		// 3. 执行余额扣款（原子操作，包含余额验证）
		int updateRows = userDao.updateBalanceForInvestment(user.getId(), amount);
		if (updateRows == 0) {
			throw new RenException(ErrorCode.INSUFFICIENT_BALANCE);
		}

		// 4. 记录原始余额，用于账变记录
		result.put("originalBalance", currentAssets.toString());
		result.put("status", "success");
		result.put("message", "余额扣款成功");

		return result;
	}
	
	/**
	 * 记录账变明细
	 */
	private void recordBalanceDetail(Long userId, Long amount, String orderNumber, String projectName, Long originalBalance) {
		try {
			UserBalanceDetailEntity balanceDetail = new UserBalanceDetailEntity();
			balanceDetail.setUserId(userId);
			
			Date now = new Date();
			balanceDetail.setTransactionDate(now);
			balanceDetail.setBusiType(BusinessTypeEnum.PURCHASE_FLOW.getCode()); // 1:购买流水
			balanceDetail.setChannel("1"); //先全部设置为默认
			balanceDetail.setStreamId(orderNumber);
			balanceDetail.setUseAmount(amount);
			balanceDetail.setOriginalAmount(originalBalance);
			balanceDetail.setTransactionAmount(originalBalance - amount);
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

	/**
	 * 计算佣金金额
	 * 
	 * @param investmentAmount 投资金额（分）
	 * @param rate 佣金比例
	 * @return 佣金金额（分）
	 */
	private Long calculateCommission(BigDecimal investmentAmount, BigDecimal rate) {
		BigDecimal commission = investmentAmount.multiply(rate).setScale(0, RoundingMode.HALF_DOWN);
		return commission.longValue();
	}

	/**
	 * 处理本金返还逻辑
	 * 
	 * @param project 项目信息
	 * @param user 用户信息
	 * @param investmentAmount 投资金额
	 * @param orderId 订单ID
	 * @param transactionDate 交易时间
	 */
	private void handlePrincipalReturn(ProjectEntity project, UserEntity user, BigDecimal investmentAmount, Long orderId, Date transactionDate) {
		try {
			// 检查是否需要返还本金
			if (project.getReturnPrincipal() == null || project.getReturnPrincipal() != 1) {
				log.debug("项目 {} 不需要返还本金", project.getInvestId());
				return;
			}

			// 获取返还金额
			BigDecimal returnRatio = project.getReturnRatio();
			if (returnRatio == null || returnRatio.compareTo(BigDecimal.ZERO) <= 0) {
				log.warn("项目 {} 返还金额无效: {}", project.getInvestId(), returnRatio);
				return;
			}

			// 返还金额转换为分
			Long returnAmountInCents = returnRatio.multiply(new BigDecimal(100)).longValue();

			if (returnAmountInCents <= 0) {
				log.debug("项目 {} 返还金额为0，跳过处理", project.getInvestId());
				return;
			}

			// 根据returnTo字段决定返还给谁
			Integer returnTo = project.getReturnTo();
			Long targetUserId = null;
			String remarks = "";

			if (returnTo == null || returnTo == 0) {
				// 返还给自己
				targetUserId = user.getId();
				remarks = "项目本金返还";
				log.info("项目 {} 本金返还给用户自己，金额: {} 分", project.getInvestId(), returnAmountInCents);
			} else if (returnTo == 1) {
				// 返还给上级
				String upinviteCode = user.getUpinviteCode();
				if (StringUtils.isNotBlank(upinviteCode)) {
					UserEntity referrer = userDao.selectByInviteCode(upinviteCode);
					if (referrer != null) {
						targetUserId = referrer.getId();
						remarks = "项目本金返还给上级";
						log.info("项目 {} 本金返还给上级用户 {}，金额: {} 分", project.getInvestId(), targetUserId, returnAmountInCents);
					} else {
						log.warn("项目 {} 上级推荐人不存在，返还给用户自己", project.getInvestId());
						targetUserId = user.getId();
						remarks = "项目本金返还（上级不存在）";
					}
				} else {
					log.warn("项目 {} 用户没有上级推荐人，返还给用户自己", project.getInvestId());
					targetUserId = user.getId();
					remarks = "项目本金返还（无上级）";
				}
			} else {
				log.warn("项目 {} 未知的返还对象类型: {}，返还给用户自己", project.getInvestId(), returnTo);
				targetUserId = user.getId();
				remarks = "项目本金返还（未知类型）";
			}

			if (targetUserId != null) {
				// 更新目标用户余额
				userDao.updateCommissionFields(targetUserId, returnAmountInCents);

				// 记录账变明细
				UserBalanceDetailEntity detail = new UserBalanceDetailEntity();
				if(returnTo == 1){
					detail.setBusiType(BusinessTypeEnum.PROJECT_COMMISSION_UP.getCode()); //项目返上级
				}else {
					detail.setBusiType(BusinessTypeEnum.PROJECT_COMMISSION_SELF.getCode()); //项目返自己
				}
				detail.setUserId(targetUserId);
				detail.setOriginalAmount(userDao.selectById(targetUserId).getAssets() - returnAmountInCents);
				detail.setUseAmount(returnAmountInCents);
				detail.setTransactionAmount(userDao.selectById(targetUserId).getAssets());
				detail.setStatus(1);
				detail.setFormUserId(user.getId());
				detail.setTransactionDate(transactionDate);
				detail.setCreateDate(transactionDate);
				detail.setUpdateDate(transactionDate);
				detail.setRemarks(remarks);
				detail.setStreamId(orderId.toString());
				userBalanceDetailDao.insert(detail);

				log.info("项目 {} 本金返还处理完成，目标用户: {}，金额: {} 分", project.getInvestId(), targetUserId, returnAmountInCents);
			}

		} catch (Exception e) {
			log.error("处理项目 {} 本金返还失败", project.getInvestId(), e);
			// 本金返还失败不影响主流程，只记录错误日志
		}
	}
}
