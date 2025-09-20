package io.renren.controller;

import io.renren.common.constant.BusinessTypeEnum;
import io.renren.dao.PayMerchantDao;
import io.renren.dao.UserBalanceDetailDao;
import io.renren.dao.UserDao;
import io.renren.dao.WithdrawOrderDao;
import io.renren.dto.QePayCallbackDTO;
import io.renren.entity.PayMerchantEntity;
import io.renren.entity.UserBalanceDetailEntity;
import io.renren.entity.UserEntity;
import io.renren.entity.WithdrawOrderEntity;
import io.renren.utils.QePaySignatureUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * QePay代付回调控制器
 * 
 * @author renren
 * @date 2024-01-01
 */
@Api(tags = "QePay代付回调接口")
@RestController
@RequestMapping("/api/qepay")
public class QePayCallbackController {
    
    private static final Logger logger = LoggerFactory.getLogger(QePayCallbackController.class);
    
    @Resource
    private PayMerchantDao payMerchantDao;
    
    @Resource
    private WithdrawOrderDao withdrawOrderDao;
    
    @Resource
    private UserDao userDao;

    @Resource
    private UserBalanceDetailDao userBalanceDetailDao;
    
    /**
     * QePay代付回调接口
     * 
     * 重要说明：
     * 1. 代付成功后会发送异步通知，代付结果需根据后台通知为准
     * 2. 异常未收到通知，平台会按照规律重复发送通知，成功之后是连续发8次
     * 3. 异步通知在处理成功之后需要向平台返回"success"，平台收到success后将不会再发送通知
     * 4. 如果返回"fail"，平台会继续重试发送通知
     */
    @PostMapping(value = "/payout/notify", consumes = "application/x-www-form-urlencoded")
    @ApiOperation("QePay代付结果异步通知")
    public String qePayPayoutNotify(@RequestParam Map<String, String> params) {
        String orderNo = "unknown";
        try {
            logger.info("收到QePay代付回调通知 - 参数: {}", params);
            
            // 1. 解析回调数据
            QePayCallbackDTO callbackData = parseQePayCallbackData(params);
            if (callbackData == null) {
                logger.error("解析QePay代付回调数据失败 - 参数: {}", params);
                return "fail";
            }
            
            orderNo = callbackData.getMerTransferId();
            logger.info("开始处理QePay代付回调 - 订单号: {}, 代付状态: {}, 平台订单号: {}", 
                       orderNo, callbackData.getTradeResult(), callbackData.getTradeNo());
            
            // 2. 验证签名
            if (!verifyQePayCallbackSign(callbackData)) {
                logger.error("QePay代付回调签名验证失败 - 订单号: {}", orderNo);
                return "fail";
            }
            
            logger.info("QePay代付回调签名验证成功 - 订单号: {}", orderNo);
            
            // 3. 处理代付结果
            boolean success = processQePayPayoutResult(callbackData);
            
            if (success) {
                logger.info("QePay代付回调处理成功 - 订单号: {}, 返回success给平台", orderNo);
                return "success";
            } else {
                logger.error("QePay代付回调处理失败 - 订单号: {}, 返回fail给平台", orderNo);
                return "fail";
            }
            
        } catch (Exception e) {
            logger.error("处理QePay代付回调异常 - 订单号: {}", orderNo, e);
            return "fail";
        }
    }
    
    /**
     * 解析QePay代付回调数据
     */
    private QePayCallbackDTO parseQePayCallbackData(Map<String, String> params) {
        try {
            QePayCallbackDTO callbackData = new QePayCallbackDTO();
            callbackData.setTradeResult(params.get("tradeResult"));
            callbackData.setMerTransferId(params.get("merTransferId"));
            callbackData.setMerNo(params.get("merNo"));
            callbackData.setTradeNo(params.get("tradeNo"));
            callbackData.setTransferAmount(params.get("transferAmount"));
            callbackData.setApplyDate(params.get("applyDate"));
            callbackData.setVersion(params.get("version"));
            callbackData.setRespCode(params.get("respCode"));
            callbackData.setSign(params.get("sign"));
            callbackData.setSignType(params.get("signType"));
            
            logger.info("解析QePay代付回调数据成功 - 订单号: {}, 代付状态: {}", 
                       callbackData.getMerTransferId(), callbackData.getTradeResult());
            
            return callbackData;
            
        } catch (Exception e) {
            logger.error("解析QePay代付回调数据异常 - 参数: {}", params, e);
            return null;
        }
    }
    
    /**
     * 验证QePay代付回调签名
     */
    private boolean verifyQePayCallbackSign(QePayCallbackDTO callbackData) {
        try {
            String orderNo = callbackData.getMerTransferId();
            
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
            signData.put("tradeResult", callbackData.getTradeResult());
            signData.put("merTransferId", callbackData.getMerTransferId());
            signData.put("merNo", callbackData.getMerNo());
            signData.put("tradeNo", callbackData.getTradeNo());
            signData.put("transferAmount", callbackData.getTransferAmount());
            signData.put("applyDate", callbackData.getApplyDate());
            signData.put("version", callbackData.getVersion());
            signData.put("respCode", callbackData.getRespCode());
            
            // 生成签名
            String expectedSign = QePaySignatureUtils.generateSign(signData, payMerchant.getChannelkey());
            
            // 验证签名
            boolean isValid = expectedSign.equals(callbackData.getSign());
            logger.info("QePay代付回调签名验证结果: {} - 订单号: {}", isValid, orderNo);
            
            return isValid;
            
        } catch (Exception e) {
            logger.error("验证QePay代付回调签名异常 - 订单号: {}", callbackData.getMerTransferId(), e);
            return false;
        }
    }
    
    /**
     * 处理QePay代付结果
     */
    private boolean processQePayPayoutResult(QePayCallbackDTO callbackData) {
        try {
            String orderNo = callbackData.getMerTransferId();
            String tradeResult = callbackData.getTradeResult();
            
            logger.info("开始处理QePay代付结果 - 订单号: {}, 代付状态: {}", orderNo, tradeResult);
            
            // 根据代付状态处理
            switch (tradeResult) {
                case "1": // 代付成功
                    logger.info("QePay代付成功 - 订单号: {}", orderNo);
                    return handleQePayPayoutSuccess(callbackData);
                    
                case "2": // 代付失败
                    logger.info("QePay代付失败 - 订单号: {}", orderNo);
                    return handleQePayPayoutFailure(callbackData);
                    
                default:
                    logger.warn("未知的QePay代付状态: {} - 订单号: {}", tradeResult, orderNo);
                    return false;
            }
            
        } catch (Exception e) {
            logger.error("处理QePay代付结果异常 - 订单号: {}", callbackData.getMerTransferId(), e);
            return false;
        }
    }
    
    /**
     * 处理QePay代付成功
     */
    private boolean handleQePayPayoutSuccess(QePayCallbackDTO callbackData) {
        try {
            String orderNo = callbackData.getMerTransferId();
            String tradeNo = callbackData.getTradeNo();
            
            // 查询提现订单
            WithdrawOrderEntity withdrawOrder = withdrawOrderDao.selectByOrderno(orderNo);
            if (withdrawOrder == null) {
                logger.error("查询提现订单失败 - 订单号: {}", orderNo);
                return false;
            }
            
            // 检查订单状态，避免重复处理
            if (withdrawOrder.getState() != null && withdrawOrder.getState() == 2) {
                logger.info("QePay代付订单已处理过 - 订单号: {}", orderNo);
                return true;
            }
            
            // 更新订单状态为代付成功
            withdrawOrder.setState(2);
            withdrawOrder.setThreeorderNo(tradeNo); // 第三方订单号
            withdrawOrder.setStateTime(new Date());
            withdrawOrder.setRemark("QePay代付成功");
            
            int updateResult = withdrawOrderDao.updateById(withdrawOrder);
            if (updateResult <= 0) {
                logger.error("更新QePay代付订单状态失败 - 订单号: {}", orderNo);
                return false;
            }
            
            // 更新用户余额
            UserEntity user = userDao.selectById(withdrawOrder.getUserId());
            if (user != null) {
                Long oldFreezeBalance = user.getFreezeBalance();
                // 从冻结余额中真正扣减
                user.setFreezeBalance(oldFreezeBalance - withdrawOrder.getAmount());
                
                // 2 余额提现 33佣金提现
                if (withdrawOrder.getWithdrawType() == 2) {
                    // 佣金提现总额
                    user.setWithdrawQuota(user.getWithdrawQuota() + withdrawOrder.getAmount());
                    user.setWithdrawCount(user.getWithdrawCount() + 1);
                    user.setHistorywithdrawcnt(user.getHistorywithdrawcnt() + 1);
                }
                
                // 历史总提现
                user.setWithdrawSum(user.getWithdrawSum() + withdrawOrder.getAmount());
                user.setTodayWithdraw(user.getTodayWithdraw() + withdrawOrder.getAmount());
                user.setHistorywithdrawcnt(user.getHistorywithdrawcnt() + 1);
                userDao.updateById(user);
                
                // 记录账变明细
                recordBalanceDetail(withdrawOrder, user, withdrawOrder.getAmount());
            }
            
            logger.info("QePay代付成功处理完成 - 订单号: {}, 平台订单号: {}", orderNo, tradeNo);
            return true;
            
        } catch (Exception e) {
            logger.error("处理QePay代付成功异常 - 订单号: {}", callbackData.getMerTransferId(), e);
            return false;
        }
    }
    
    /**
     * 处理QePay代付失败
     */
    private boolean handleQePayPayoutFailure(QePayCallbackDTO callbackData) {
        try {
            String orderNo = callbackData.getMerTransferId();
            
            // 查询提现订单
            WithdrawOrderEntity withdrawOrder = withdrawOrderDao.selectByOrderno(orderNo);
            if (withdrawOrder == null) {
                logger.error("查询提现订单失败 - 订单号: {}", orderNo);
                return false;
            }
            
            // 检查订单状态，避免重复处理
            if (withdrawOrder.getState() != null && withdrawOrder.getState() == 2) {
                logger.warn("QePay代付订单已处理过 - 订单号: {}, 当前状态: {}", orderNo, withdrawOrder.getState());
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
            withdrawOrder.setRemark("QePay代付失败");
            
            int updateResult = withdrawOrderDao.updateById(withdrawOrder);
            if (updateResult <= 0) {
                logger.error("更新QePay代付订单状态失败 - 订单号: {}", orderNo);
                return false;
            }
            
            logger.info("QePay代付失败处理完成 - 订单号: {}, 提现类型: {}", 
                       orderNo, withdrawOrder.getWithdrawType());
            return true;
            
        } catch (Exception e) {
            logger.error("处理QePay代付失败异常 - 订单号: {}", callbackData.getMerTransferId(), e);
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
            Long oldFreezeBalance = user.getFreezeBalance();
            switch (withdrawType) {
                case 1: // 余额提现
                    // 将冻结的余额提现金额返还到可用余额和可提现余额
                    user.setFreezeBalance(oldFreezeBalance - amount);
                    user.setAssets(user.getAssets() + amount);
                    user.setCashwithdrawable(user.getCashwithdrawable() + amount);
                    logger.info("余额提现失败，返还到可用余额和可提现余额 - 用户ID: {}, 金额: {}", user.getId(), amount);
                    break;
                    
                case 2: // 佣金提现
                    // 将冻结的佣金提现金额返还到佣金可提现余额
                    user.setFreezeBalance(oldFreezeBalance - amount);
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
            
            // 解冻操作不记录资金账变，因为只是状态恢复，不是真正的资金流动

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
                balanceDetail.setBusiType(BusinessTypeEnum.BALANCE_WITHDRAWAL_FLOW.getCode());
                balanceDetail.setRemarks("余额提现");
            } else {
                balanceDetail.setBusiType(BusinessTypeEnum.COMMISSION_WITHDRAWAL_FLOW.getCode());
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
