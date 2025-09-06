package io.renren.service.impl;

import io.renren.dao.ChargeOrderDao;
import io.renren.dao.UserDao;
import io.renren.dao.UserBalanceDetailDao;
import io.renren.dto.WePayCallbackDTO;
import io.renren.entity.ChargeOrderEntity;
import io.renren.entity.UserEntity;
import io.renren.entity.UserBalanceDetailEntity;
import io.renren.service.PaymentCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付回调服务实现类
 * 
 * @author renren
 * @date 2024-01-01
 */
@Service
public class PaymentCallbackServiceImpl implements PaymentCallbackService {
    
    private static final Logger logger = LoggerFactory.getLogger(PaymentCallbackServiceImpl.class);
    
    @Autowired
    private ChargeOrderDao chargeOrderDao;
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private UserBalanceDetailDao userBalanceDetailDao;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean handlePaymentSuccess(WePayCallbackDTO callbackData) {
        try {
            String orderNo = callbackData.getOrderNo();
            String tradeNo = callbackData.getTradeNo();
            BigDecimal amount = callbackData.getAmount();
            BigDecimal charge = callbackData.getCharge();
            
            logger.info("开始处理支付成功逻辑 - 订单号: {}, 系统单号: {}, 支付金额: {}, 手续费: {}", 
                       orderNo, tradeNo, amount, charge);
            
            // 1. 根据订单号查询本地订单
            ChargeOrderEntity chargeOrder = chargeOrderDao.selectByOrderno(orderNo);
            if (chargeOrder == null) {
                logger.error("查询充值订单失败 - 订单号: {}", orderNo);
                return false;
            }
            
            // 检查订单状态，避免重复处理
            if (chargeOrder.getState() != null && chargeOrder.getState() == 1) {
                logger.warn("订单已处理过 - 订单号: {}, 当前状态: {}", orderNo, chargeOrder.getState());
                return true; // 已处理过，返回成功
            }
            
            // 2. 查询用户信息
            UserEntity user = userDao.selectById(chargeOrder.getUserId());
            if (user == null) {
                logger.error("查询用户信息失败 - 用户ID: {}", chargeOrder.getUserId());
                return false;
            }
            
            // 3. 更新订单状态为已支付
            chargeOrder.setState(1);
            chargeOrder.setThreeorderNo(tradeNo); // 第三方订单号
            chargeOrder.setRealAmount(amount.multiply(new BigDecimal("100")).longValue()); // 实际支付金额(分)
            chargeOrder.setUpdateTime(new Date());
            chargeOrder.setRemark("WePay支付成功");
            
            int orderUpdateResult = chargeOrderDao.updateById(chargeOrder);
            if (orderUpdateResult <= 0) {
                logger.error("更新充值订单状态失败 - 订单号: {}", orderNo);
                return false;
            }
            
            // 4. 更新用户余额
            Long amountInCents = amount.multiply(new BigDecimal("100")).longValue(); // 转换为分
            int balanceResult = userDao.addUserBalance(chargeOrder.getUserId(), amountInCents);
            if (balanceResult <= 0) {
                logger.error("更新用户余额失败 - 用户ID: {}, 金额: {}", chargeOrder.getUserId(), amountInCents);
                return false;
            }
            
            // 5. 更新用户充值统计
            int rechargeResult = userDao.updateAllRechargeFields(chargeOrder.getUserId(), amountInCents);
            if (rechargeResult <= 0) {
                logger.error("更新用户充值统计失败 - 用户ID: {}, 金额: {}", chargeOrder.getUserId(), amountInCents);
                return false;
            }
            
            // 6. 记录余额明细
            recordBalanceDetail(chargeOrder, user, amountInCents, tradeNo);
            
            logger.info("支付成功处理完成 - 订单号: {}, 用户ID: {}, 充值金额: {} 分", 
                       orderNo, chargeOrder.getUserId(), amountInCents);
            
            return true;
            
        } catch (Exception e) {
            logger.error("处理支付成功逻辑异常 - 订单号: {}", callbackData.getOrderNo(), e);
            throw e; // 重新抛出异常以触发事务回滚
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean handlePaymentFailure(WePayCallbackDTO callbackData) {
        try {
            String orderNo = callbackData.getOrderNo();
            String remark = callbackData.getRemark();
            
            logger.info("开始处理支付失败逻辑 - 订单号: {}, 失败原因: {}", orderNo, remark);
            
            // 1. 根据订单号查询本地订单
            ChargeOrderEntity chargeOrder = chargeOrderDao.selectByOrderno(orderNo);
            if (chargeOrder == null) {
                logger.error("查询充值订单失败 - 订单号: {}", orderNo);
                return false;
            }
            
            // 检查订单状态，避免重复处理
            if (chargeOrder.getState() != null && chargeOrder.getState() == 2) {
                logger.warn("订单已处理过 - 订单号: {}, 当前状态: {}", orderNo, chargeOrder.getState());
                return true; // 已处理过，返回成功
            }
            
            // 2. 更新订单状态为支付失败
            chargeOrder.setState(2); // 2-失败
            chargeOrder.setUpdateTime(new Date());
            chargeOrder.setRemark("WePay支付失败: " + (remark != null ? remark : "未知原因"));
            
            int orderUpdateResult = chargeOrderDao.updateById(chargeOrder);
            if (orderUpdateResult <= 0) {
                logger.error("更新充值订单状态失败 - 订单号: {}", orderNo);
                return false;
            }
            
            logger.info("支付失败处理完成 - 订单号: {}, 失败原因: {}", orderNo, remark);
            
            return true;
            
        } catch (Exception e) {
            logger.error("处理支付失败逻辑异常 - 订单号: {}", callbackData.getOrderNo(), e);
            throw e; // 重新抛出异常以触发事务回滚
        }
    }
    
    @Override
    public boolean handleOrderCreated(WePayCallbackDTO callbackData) {
        try {
            String orderNo = callbackData.getOrderNo();
            logger.info("处理订单生成回调 - 订单号: {}", orderNo);
            
            // 订单生成状态通常不需要特殊处理，只需要记录日志
            logger.info("订单生成处理完成 - 订单号: {}", orderNo);
            
            return true;
            
        } catch (Exception e) {
            logger.error("处理订单生成逻辑异常 - 订单号: {}", callbackData.getOrderNo(), e);
            return false;
        }
    }
    
    /**
     * 记录余额明细
     */
    private void recordBalanceDetail(ChargeOrderEntity chargeOrder, UserEntity user, Long amountInCents, String tradeNo) {
        try {
            UserBalanceDetailEntity balanceDetail = new UserBalanceDetailEntity();
            balanceDetail.setUserId(chargeOrder.getUserId());
            balanceDetail.setTransactionDate(new Date());
            balanceDetail.setAgentId(user.getAgent());
            balanceDetail.setAgentName(user.getAgentName());
            balanceDetail.setBusiType(11); // 11-线上充值
            balanceDetail.setChannel("1");
            balanceDetail.setOriginalAmount(user.getAssets() != null ? user.getAssets() - amountInCents : 0L);
            balanceDetail.setTransactionAmount(user.getAssets() != null ? user.getAssets() : amountInCents);
            balanceDetail.setUseAmount(amountInCents);
            balanceDetail.setRemarks("WePay充值成功 - 订单号: " + chargeOrder.getOrderno() + ", 系统单号: " + tradeNo);
            balanceDetail.setSalesmanName(user.getSalesmanName());
            balanceDetail.setSalesmanId(user.getSalesmanid());
            balanceDetail.setStatus(1); // 1-正常
            balanceDetail.setStreamId(tradeNo);
            balanceDetail.setCreateDate(new Date());
            balanceDetail.setUpdateDate(new Date());
            
            userBalanceDetailDao.insert(balanceDetail);
            
            logger.info("余额明细记录成功 - 用户ID: {}, 金额: {} 分", chargeOrder.getUserId(), amountInCents);
            
        } catch (Exception e) {
            logger.error("记录余额明细失败 - 用户ID: {}, 金额: {} 分", chargeOrder.getUserId(), amountInCents, e);
            throw e;
        }
    }
}
