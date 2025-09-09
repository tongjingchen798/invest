package io.renren.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import io.renren.dao.PayMerchantDao;
import io.renren.dao.UserBalanceDetailDao;
import io.renren.dao.UserDao;
import io.renren.dao.WithdrawOrderDao;
import io.renren.entity.PayMerchantEntity;
import io.renren.entity.UserBalanceDetailEntity;
import io.renren.entity.UserEntity;
import io.renren.entity.WithdrawOrderEntity;
import io.renren.utils.WePaySignatureUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * WePay代付回调控制器
 * 
 * @author renren
 * @date 2024-01-01
 */
@RestController
@RequestMapping("/api/payout")
public class PayoutCallbackController {
    
    private static final Logger logger = LoggerFactory.getLogger(PayoutCallbackController.class);
    
    @Autowired
    private PayMerchantDao payMerchantDao;
    
    @Autowired
    private WithdrawOrderDao withdrawOrderDao;
    
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserBalanceDetailDao userBalanceDetailDao;
    
    /**
     * WePay代付回调接口
     */
    @PostMapping(value = "/notify", consumes = "application/json")
    public String payoutNotify(@RequestBody String requestBody) {
        try {
            logger.info("收到WePay代付回调通知代付回调JSON数据: {}", requestBody);
            
            // 1. 解析JSON回调数据
            JSONObject jsonData = parseWePayCallbackJson(requestBody);
            if (jsonData == null) {
                logger.error("解析代付回调JSON数据失败");
                return "fail";
            }
            
            // 2. 验证签名
            if (!verifyWePayCallbackSign(jsonData)) {
                logger.error("代付回调签名验证失败 - 订单号: {}", jsonData.getString("orderNo"));
                return "fail";
            }
            
            // 3. 处理代付结果
            boolean success = processPayoutResult(jsonData);
            
            return success ? "success" : "fail";
            
        } catch (Exception e) {
            logger.error("处理代付回调异常", e);
            return "fail";
        }
    }
    
    /**
     * 解析WePay代付回调JSON数据
     */
    private JSONObject parseWePayCallbackJson(String requestBody) {
        try {
            JSONObject jsonObject = JSON.parseObject(requestBody);
            
            logger.info("解析代付回调数据成功 - 订单号: {}, 支付状态: {}", 
                       jsonObject.getString("orderNo"), jsonObject.getInteger("payStatus"));
            
            return jsonObject;
            
        } catch (Exception e) {
            logger.error("解析代付回调JSON数据异常 - 请求体: {}", requestBody, e);
            return null;
        }
    }
    
    /**
     * 验证WePay代付回调签名
     */
    private boolean verifyWePayCallbackSign(JSONObject jsonData) {
        try {
            String orderNo = jsonData.getString("orderNo");
            
            // 根据订单号查询提现订单
            WithdrawOrderEntity withdrawOrder = withdrawOrderDao.selectByOrderno(orderNo);
            if (withdrawOrder == null) {
                logger.error("查询提现订单失败 - 订单号: {}", orderNo);
                return false;
            }
            
            // 查询支付商户信息
            PayMerchantEntity payMerchant = payMerchantDao.selectById(withdrawOrder.getMerchantid());
            if (payMerchant == null) {
                logger.error("查询支付商户信息失败 - 商户ID: {}", withdrawOrder.getMerchantid());
                return false;
            }
            
            // 构建签名数据
            Map<String, Object> signData = new HashMap<>();
            signData.put("tradeNo", jsonData.getString("tradeNo"));
            signData.put("orderNo", jsonData.getString("orderNo"));
            signData.put("orderAmount", jsonData.getBigDecimal("orderAmount"));
            signData.put("amount", jsonData.getBigDecimal("amount"));
            signData.put("payStatus", jsonData.getInteger("payStatus"));
            signData.put("payTime", jsonData.getString("payTime"));
            signData.put("charge", jsonData.getBigDecimal("charge"));
            signData.put("otherData", jsonData.getString("otherData"));
            signData.put("reverse", jsonData.getBoolean("reverse"));
            signData.put("remark", jsonData.getString("remark"));
            
            // 生成签名
            String expectedSign = WePaySignatureUtils.generateSign(signData, payMerchant.getChannelkey());
            
            // 验证签名
            boolean isValid = expectedSign.equals(jsonData.getString("sign"));
            logger.info("代付回调签名验证结果: {} - 订单号: {}", isValid, orderNo);
            
            return isValid;
            
        } catch (Exception e) {
            logger.error("验证代付回调签名异常 - 订单号: {}", jsonData.getString("orderNo"), e);
            return false;
        }
    }
    
    /**
     * 处理代付结果
     */
    private boolean processPayoutResult(JSONObject jsonData) {
        try {
            String orderNo = jsonData.getString("orderNo");
            Integer payStatus = jsonData.getInteger("payStatus");
            
            logger.info("开始处理代付结果 - 订单号: {}, 支付状态: {}", orderNo, payStatus);
            
            // 根据支付状态处理
            switch (payStatus) {
                case 0: // 代付中
                    logger.info("代付中 - 订单号: {}", orderNo);
                    return true;
                    
                case 1: // 代付成功
                    logger.info("代付成功 - 订单号: {}", orderNo);
                    return handlePayoutSuccess(jsonData);
                    
                case 2: // 代付失败
                    logger.info("代付失败 - 订单号: {}", orderNo);
                    return handlePayoutFailure(jsonData);
                    
                default:
                    logger.warn("未知的代付状态: {} - 订单号: {}", payStatus, orderNo);
                    return false;
            }
            
        } catch (Exception e) {
            logger.error("处理代付结果异常 - 订单号: {}", jsonData.getString("orderNo"), e);
            return false;
        }
    }
    
    /**
     * 处理代付成功 - 直接使用JSONObject
     */
    private boolean handlePayoutSuccess(JSONObject jsonData) {
        try {
            String orderNo = jsonData.getString("orderNo");
            String tradeNo = jsonData.getString("tradeNo");
            
            // 查询提现订单
            WithdrawOrderEntity withdrawOrder = withdrawOrderDao.selectByOrderno(orderNo);
            if (withdrawOrder == null) {
                logger.error("查询提现订单失败 - 订单号: {}", orderNo);
                return false;
            }
            
            // 检查订单状态，避免重复处理
            if (withdrawOrder.getState() != null && withdrawOrder.getState() == 2) {
                logger.warn("代付订单已处理过 - 订单号: {}, 当前状态: {}", orderNo, withdrawOrder.getState());
                return true;
            }
            
            // 更新订单状态为代付成功
            withdrawOrder.setState(2);
            withdrawOrder.setThreeorderNo(tradeNo); // 第三方订单号
            withdrawOrder.setStateTime(new Date());
            withdrawOrder.setRemark("WePay代付成功");
            
            int updateResult = withdrawOrderDao.updateById(withdrawOrder);
            if (updateResult <= 0) {
                logger.error("更新代付订单状态失败 - 订单号: {}", orderNo);
                return false;
            }
            UserEntity user=userDao.selectById(withdrawOrder.getUserId());
            //从冻结余额中真正扣减
            user.setFreezeBalance(user.getFreezeBalance() - withdrawOrder.getAmount());
            //2 余额提现 33佣金提现
            if (withdrawOrder.getWithdrawType() == 2) {
                //佣金提现总额
                user.setWithdrawQuota(user.getWithdrawQuota()+withdrawOrder.getAmount());
                user.setWithdrawCount(user.getWithdrawCount()+1);
                user.setHistorywithdrawcnt(user.getHistorywithdrawcnt()+1);
            }
            //历史总提现
            user.setWithdrawSum(user.getWithdrawSum()+withdrawOrder.getAmount());
            user.setTodayWithdraw(user.getTodayWithdraw()+withdrawOrder.getAmount());
            user.setHistorywithdrawcnt(user.getHistorywithdrawcnt()+1);
            userDao.updateById(user);

            //记录账变明细
            recordBalanceDetail(withdrawOrder, user, withdrawOrder.getAmount());

            logger.info("代付成功处理完成 - 订单号: {}, 系统单号: {}", orderNo, tradeNo);
            return true;
            
        } catch (Exception e) {
            logger.error("处理代付成功异常 - 订单号: {}", jsonData.getString("orderNo"), e);
            return false;
        }
    }
    
    /**
     * 处理代付失败
     */
    private boolean handlePayoutFailure(JSONObject jsonData) {
        try {
            String orderNo = jsonData.getString("orderNo");
            String remark = jsonData.getString("remark");
            
            // 查询提现订单
            WithdrawOrderEntity withdrawOrder = withdrawOrderDao.selectByOrderno(orderNo);
            if (withdrawOrder == null) {
                logger.error("查询提现订单失败 - 订单号: {}", orderNo);
                return false;
            }
            
            // 检查订单状态，避免重复处理
            if (withdrawOrder.getState() != null && withdrawOrder.getState() == 2) {
                logger.warn("代付订单已处理过 - 订单号: {}, 当前状态: {}", orderNo, withdrawOrder.getState());
                return true;
            }
            
            // 查询用户信息
            UserEntity user = userDao.selectById(Long.valueOf(withdrawOrder.getUserId()));
            if (user == null) {
                logger.error("查询用户信息失败 - 用户ID: {}", withdrawOrder.getUserId());
                return false;
            }
            
            // 根据提现类型更新对应的钱包余额
            boolean walletUpdated = updateWalletOnPayoutFailure(withdrawOrder, user);
            if (!walletUpdated) {
                logger.error("更新钱包余额失败 - 订单号: {}", orderNo);
                return false;
            }
            
            // 更新订单状态为代付失败
            withdrawOrder.setState(4);
            withdrawOrder.setStateTime(new Date());
            withdrawOrder.setRemark("WePay代付失败: " + (remark != null ? remark : "未知原因"));
            
            int updateResult = withdrawOrderDao.updateById(withdrawOrder);
            if (updateResult <= 0) {
                logger.error("更新代付订单状态失败 - 订单号: {}", orderNo);
                return false;
            }
            
            logger.info("代付失败处理完成 - 订单号: {}, 失败原因: {}, 提现类型: {}", 
                       orderNo, remark, withdrawOrder.getWithdrawType());
            return true;
            
        } catch (Exception e) {
            logger.error("处理代付失败异常 - 订单号: {}", jsonData.getString("orderNo"), e);
            return false;
        }
    }
    
    /**
     * 代付失败时更新钱包余额
     */
    private boolean updateWalletOnPayoutFailure(WithdrawOrderEntity withdrawOrder, UserEntity user) {
        try {
            Long amount = withdrawOrder.getAmount();
            Integer withdrawType = withdrawOrder.getWithdrawType();
            
            logger.info("开始更新钱包余额 - 用户ID: {}, 金额: {}, 提现类型: {}", 
                       user.getId(), amount, withdrawType);
            
            // 根据提现类型更新对应的钱包
            switch (withdrawType) {
                case 1: // 余额提现
                    // 将冻结的余额提现金额返还到可提现余额
                    user.setFreezeBalance(user.getFreezeBalance() - amount);
                    user.setCashwithdrawable(user.getCashwithdrawable() + amount);
                    logger.info("余额提现失败，返还到可提现余额 - 用户ID: {}, 金额: {}", user.getId(), amount);
                    break;
                    
                case 2: // 佣金提现
                    // 将冻结的佣金提现金额返还到佣金可提现余额
                    user.setFreezeBalance(user.getFreezeBalance() - amount);
                    user.setCommissionBalance(user.getCommissionBalance() + amount);
                    logger.info("佣金提现失败，返还到佣金可提现余额 - 用户ID: {}, 金额: {}", user.getId(), amount);
                    break;
                    
                default:
                    logger.warn("未知的提现类型: {} - 订单号: {}", withdrawType, withdrawOrder.getOrderno());
                    return false;
            }
            
            // 更新用户信息
            int updateResult = userDao.updateById(user);
            if (updateResult <= 0) {
                logger.error("更新用户钱包余额失败 - 用户ID: {}", user.getId());
                return false;
            }
            
//            // 记录账变明细
//            recordBalanceDetailOnPayoutFailure(withdrawOrder, user, amount, withdrawType);
            
            logger.info("钱包余额更新成功 - 用户ID: {}, 提现类型: {}, 金额: {}", 
                       user.getId(), withdrawType, amount);
            return true;
            
        } catch (Exception e) {
            logger.error("更新钱包余额异常 - 订单号: {}, 用户ID: {}", 
                        withdrawOrder.getOrderno(), user.getId(), e);
            return false;
        }
    }


    /**
     * 记录余额明细
     */
    private void recordBalanceDetail(WithdrawOrderEntity withdrawOrder, UserEntity user, Long amountInCents) {
        try {
            UserBalanceDetailEntity balanceDetail = new UserBalanceDetailEntity();
            balanceDetail.setUserId(Long.valueOf(withdrawOrder.getUserId()));
            balanceDetail.setTransactionDate(new Date());
            balanceDetail.setAgentId(user.getAgent());
            balanceDetail.setAgentName(user.getAgentName());
            //2 余额提现 33佣金提现
            if (withdrawOrder.getWithdrawType() == 1) {
                balanceDetail.setBusiType(2);
                balanceDetail.setRemarks("余额提现");
            } else {
                balanceDetail.setBusiType(33);
                balanceDetail.setRemarks("佣金提现");
            }

            balanceDetail.setChannel("1");
            balanceDetail.setOriginalAmount(user.getAssets() != null ? user.getAssets() - amountInCents : 0L);
            balanceDetail.setTransactionAmount(user.getAssets() != null ? user.getAssets() : amountInCents);
            balanceDetail.setUseAmount(amountInCents);
            balanceDetail.setSalesmanName(user.getSalesmanName());
            balanceDetail.setSalesmanId(user.getSalesmanid());
            balanceDetail.setStatus(1); // 1-正常
            balanceDetail.setStreamId(withdrawOrder.getThreeorderNo());
            balanceDetail.setCreateDate(new Date());
            balanceDetail.setUpdateDate(new Date());

            userBalanceDetailDao.insert(balanceDetail);

        } catch (Exception e) {
            logger.error("记录余额明细失败 - 用户ID: {}, 金额: {} 分", withdrawOrder.getUserId(), amountInCents, e);
            throw e;
        }
    }
}
