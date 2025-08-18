package io.renren.service.impl;

import io.renren.dao.ChargeOrderDao;
import io.renren.dao.UserDao;
import io.renren.entity.ChargeOrderEntity;
import io.renren.entity.UserEntity;
import io.renren.enums.BusinessTypeEnum;
import io.renren.service.ChargeCallbackService;
import io.renren.service.BalanceDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 充值回调服务实现类
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Slf4j
@Service
public class ChargeCallbackServiceImpl implements ChargeCallbackService {

    @Autowired
    private ChargeOrderDao chargeOrderDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private BalanceDetailService balanceDetailService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean processBankCallback(String thirdOrderNo, String orderNo, String amount, 
                                     String status, String payTime, String sign, 
                                     Map<String, String[]> parameterMap) {
        log.info("处理银行卡充值回调，订单号：{}，第三方订单号：{}，状态：{}", orderNo, thirdOrderNo, status);
        
        try {
            // 1. 验证签名
            if (!verifyCallbackSign(parameterMap, sign, getBankSecretKey())) {
                log.warn("银行卡回调签名验证失败，订单号：{}", orderNo);
                return false;
            }

            // 2. 查询订单
            ChargeOrderEntity order = chargeOrderDao.selectByOrderno(orderNo);
            if (order == null) {
                log.warn("银行卡回调订单不存在，订单号：{}", orderNo);
                return false;
            }

            // 3. 检查订单状态
            if (order.getState() != null && order.getState() == 1) {
                log.info("银行卡回调订单已处理，订单号：{}", orderNo);
                return true; // 已处理，返回成功
            }

            // 4. 处理回调
            if ("SUCCESS".equalsIgnoreCase(status) || "1".equals(status)) {
                // 充值成功
                return handleChargeSuccess(orderNo, parseAmount(amount), thirdOrderNo);
            } else {
                // 充值失败
                return handleChargeFail(orderNo, "银行卡支付失败：" + status, thirdOrderNo);
            }

        } catch (Exception e) {
            log.error("处理银行卡充值回调异常，订单号：{}", orderNo, e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean processCryptoCallback(String thirdOrderNo, String orderNo, String uAmount, 
                                       String status, String txHash, String walletAddr, String sign, 
                                       Map<String, String[]> parameterMap) {
        log.info("处理虚拟币充值回调，订单号：{}，第三方订单号：{}，状态：{}，USDT数量：{}", 
                orderNo, thirdOrderNo, status, uAmount);
        
        try {
            // 1. 验证签名
            if (!verifyCallbackSign(parameterMap, sign, getCryptoSecretKey())) {
                log.warn("虚拟币回调签名验证失败，订单号：{}", orderNo);
                return false;
            }

            // 2. 查询订单
            ChargeOrderEntity order = chargeOrderDao.selectByOrderno(orderNo);
            if (order == null) {
                log.warn("虚拟币回调订单不存在，订单号：{}", orderNo);
                return false;
            }

            // 3. 检查订单状态
            if (order.getState() != null && order.getState() == 1) {
                log.info("虚拟币回调订单已处理，订单号：{}", orderNo);
                return true;
            }

            // 4. 处理回调
            if ("SUCCESS".equalsIgnoreCase(status) || "1".equals(status)) {
                // 充值成功，更新USDT相关信息
                Map<String, Object> additionalInfo = new HashMap<>();
                additionalInfo.put("txHash", txHash);
                additionalInfo.put("walletAddr", walletAddr);
                additionalInfo.put("uAmount", uAmount);
                
                return handleChargeSuccess(orderNo, order.getAmount(), thirdOrderNo);
            } else {
                return handleChargeFail(orderNo, "虚拟币支付失败：" + status, thirdOrderNo);
            }

        } catch (Exception e) {
            log.error("处理虚拟币充值回调异常，订单号：{}", orderNo, e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean processUpiCallback(String thirdOrderNo, String orderNo, String amount, 
                                    String status, String upiId, String sign, 
                                    Map<String, String[]> parameterMap) {
        log.info("处理UPI充值回调，订单号：{}，第三方订单号：{}，状态：{}，UPI ID：{}", 
                orderNo, thirdOrderNo, status, upiId);
        
        try {
            // 1. 验证签名
            if (!verifyCallbackSign(parameterMap, sign, getUpiSecretKey())) {
                log.warn("UPI回调签名验证失败，订单号：{}", orderNo);
                return false;
            }

            // 2. 查询订单
            ChargeOrderEntity order = chargeOrderDao.selectByOrderno(orderNo);
            if (order == null) {
                log.warn("UPI回调订单不存在，订单号：{}", orderNo);
                return false;
            }

            // 3. 检查订单状态
            if (order.getState() != null && order.getState() == 1) {
                log.info("UPI回调订单已处理，订单号：{}", orderNo);
                return true;
            }

            // 4. 处理回调
            if ("SUCCESS".equalsIgnoreCase(status) || "1".equals(status)) {
                return handleChargeSuccess(orderNo, parseAmount(amount), thirdOrderNo);
            } else {
                return handleChargeFail(orderNo, "UPI支付失败：" + status, thirdOrderNo);
            }

        } catch (Exception e) {
            log.error("处理UPI充值回调异常，订单号：{}", orderNo, e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean processPaytmCallback(String thirdOrderNo, String orderNo, String amount, 
                                      String status, String paytmOrderId, String sign, 
                                      Map<String, String[]> parameterMap) {
        log.info("处理Paytm充值回调，订单号：{}，第三方订单号：{}，状态：{}，Paytm订单ID：{}", 
                orderNo, thirdOrderNo, status, paytmOrderId);
        
        try {
            // 1. 验证签名
            if (!verifyCallbackSign(parameterMap, sign, getPaytmSecretKey())) {
                log.warn("Paytm回调签名验证失败，订单号：{}", orderNo);
                return false;
            }

            // 2. 查询订单
            ChargeOrderEntity order = chargeOrderDao.selectByOrderno(orderNo);
            if (order == null) {
                log.warn("Paytm回调订单不存在，订单号：{}", orderNo);
                return false;
            }

            // 3. 检查订单状态
            if (order.getState() != null && order.getState() == 1) {
                log.info("Paytm回调订单已处理，订单号：{}", orderNo);
                return true;
            }

            // 4. 处理回调
            if ("SUCCESS".equalsIgnoreCase(status) || "1".equals(status)) {
                return handleChargeSuccess(orderNo, parseAmount(amount), thirdOrderNo);
            } else {
                return handleChargeFail(orderNo, "Paytm支付失败：" + status, thirdOrderNo);
            }

        } catch (Exception e) {
            log.error("处理Paytm充值回调异常，订单号：{}", orderNo, e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean processCommonCallback(String paymentMethod, String thirdOrderNo, String orderNo, 
                                       String amount, String status, String sign, 
                                       Map<String, String[]> parameterMap) {
        log.info("处理通用充值回调，支付方式：{}，订单号：{}，第三方订单号：{}，状态：{}", 
                paymentMethod, orderNo, thirdOrderNo, status);
        
        try {
            // 1. 验证签名
            if (!verifyCallbackSign(parameterMap, sign, getCommonSecretKey())) {
                log.warn("通用回调签名验证失败，订单号：{}", orderNo);
                return false;
            }

            // 2. 查询订单
            ChargeOrderEntity order = chargeOrderDao.selectByOrderno(orderNo);
            if (order == null) {
                log.warn("通用回调订单不存在，订单号：{}", orderNo);
                return false;
            }

            // 3. 检查订单状态
            if (order.getState() != null && order.getState() == 1) {
                log.info("通用回调订单已处理，订单号：{}", orderNo);
                return true;
            }

            // 4. 处理回调
            if ("SUCCESS".equalsIgnoreCase(status) || "1".equals(status)) {
                return handleChargeSuccess(orderNo, parseAmount(amount), thirdOrderNo);
            } else {
                return handleChargeFail(orderNo, "通用支付失败：" + status, thirdOrderNo);
            }

        } catch (Exception e) {
            log.error("处理通用充值回调异常，订单号：{}", orderNo, e);
            return false;
        }
    }

    @Override
    public Map<String, Object> buildOrderStatusResponse(Object orderObj) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (orderObj instanceof ChargeOrderEntity) {
                ChargeOrderEntity order = (ChargeOrderEntity) orderObj;
                
                response.put("orderNo", order.getOrderno());
                response.put("thirdOrderNo", order.getThreeorderNo());
                response.put("userId", order.getUserId());
                response.put("amount", order.getAmount());
                response.put("realAmount", order.getRealAmount());
                response.put("state", order.getState());
                response.put("stateText", getStateText(order.getState()));
                response.put("createTime", order.getCreateTime());
                response.put("updateTime", order.getUpdateTime());
                response.put("channel", order.getChannel());
                response.put("channelType", order.getChannelType());
                
                // 状态说明
                switch (order.getState()) {
                    case 0:
                        response.put("status", "PENDING");
                        response.put("message", "待审核");
                        break;
                    case 1:
                        response.put("status", "SUCCESS");
                        response.put("message", "充值成功");
                        break;
                    case 2:
                        response.put("status", "FAILED");
                        response.put("message", "充值失败");
                        break;
                    default:
                        response.put("status", "UNKNOWN");
                        response.put("message", "未知状态");
                        break;
                }
            }
            
        } catch (Exception e) {
            log.error("构建订单状态响应异常", e);
            response.put("error", "构建响应失败：" + e.getMessage());
        }
        
        return response;
    }

    @Override
    public boolean verifyCallbackSign(Map<String, String[]> parameterMap, String sign, String secretKey) {
        try {
            if (sign == null || secretKey == null) {
                log.warn("签名或密钥为空");
                return false;
            }

            // 构建签名字符串
            String signString = buildSignString(parameterMap, secretKey);
            
            // 计算签名
            String calculatedSign = DigestUtils.md5DigestAsHex(signString.getBytes(StandardCharsets.UTF_8));
            
            log.debug("签名验证：原始签名={}，计算签名={}", sign, calculatedSign);
            
            return sign.equalsIgnoreCase(calculatedSign);
            
        } catch (Exception e) {
            log.error("验证回调签名异常", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateChargeOrderStatus(String orderNo, Integer status, String thirdOrderNo, Map<String, Object> additionalInfo) {
        try {
            log.info("更新充值订单状态，订单号：{}，新状态：{}，第三方订单号：{}", orderNo, status, thirdOrderNo);
            
            // 查询订单
            ChargeOrderEntity order = chargeOrderDao.selectByOrderno(orderNo);
            if (order == null) {
                log.warn("更新状态时订单不存在，订单号：{}", orderNo);
                return false;
            }

            // 更新订单状态
            order.setState(status);
            order.setUpdateTime(new Date());
            
            if (thirdOrderNo != null) {
                order.setThreeorderNo(thirdOrderNo);
            }

            // 更新额外信息
            if (additionalInfo != null) {
                if (additionalInfo.containsKey("txHash")) {
                    // 虚拟币交易哈希
                    // order.setTxHash((String) additionalInfo.get("txHash"));
                }
                if (additionalInfo.containsKey("walletAddr")) {
                    // 钱包地址
                    // order.setWalletAddr((String) additionalInfo.get("walletAddr"));
                }
                if (additionalInfo.containsKey("uAmount")) {
                    // USDT数量
                    // order.setUamout(parseAmount((String) additionalInfo.get("uAmount")));
                }
            }

            // 保存更新
            int result = chargeOrderDao.updateById(order);
            
            if (result > 0) {
                log.info("充值订单状态更新成功，订单号：{}，新状态：{}", orderNo, status);
                return true;
            } else {
                log.warn("充值订单状态更新失败，订单号：{}", orderNo);
                return false;
            }
            
        } catch (Exception e) {
            log.error("更新充值订单状态异常，订单号：{}", orderNo, e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean handleChargeSuccess(String orderNo, Long amount, String thirdOrderNo) {
        try {
            log.info("处理充值成功逻辑，订单号：{}，金额：{}，第三方订单号：{}", orderNo, amount, thirdOrderNo);
            
            // 1. 更新订单状态为成功
            Map<String, Object> additionalInfo = new HashMap<>();
            additionalInfo.put("thirdOrderNo", thirdOrderNo);
            boolean updateSuccess = updateChargeOrderStatus(orderNo, 1, thirdOrderNo, additionalInfo);
            
            if (!updateSuccess) {
                log.error("更新充值订单状态失败，订单号：{}", orderNo);
                return false;
            }

            // 2. 查询订单信息
            ChargeOrderEntity order = chargeOrderDao.selectByOrderno(orderNo);
            if (order == null) {
                log.error("查询充值订单失败，订单号：{}", orderNo);
                return false;
            }

            // 3. 更新用户可用余额
            int balanceResult = userDao.addUserBalance(order.getUserId(), amount);
            if (balanceResult <= 0) {
                log.error("更新用户余额失败，用户ID：{}，金额：{}", order.getUserId(), amount);
                return false;
            }

            // 4. 更新用户充值统计（次数+金额，综合更新）
            int rechargeResult = userDao.updateAllRechargeFields(order.getUserId(), amount);
            if (rechargeResult <= 0) {
                log.error("更新用户充值统计失败，用户ID：{}，金额：{}", order.getUserId(), amount);
                throw new RuntimeException("更新用户充值统计失败");
            }

            // 5. 记录账变明细
            try {
                balanceDetailService.recordChargeSuccess(order.getUserId(), amount, orderNo, 
                                                      order.getChannel(), thirdOrderNo);
            } catch (Exception e) {
                log.error("记录充值成功账变失败，订单号：{}", orderNo, e);
                // 账变记录失败不影响主流程
            }

            log.info("充值成功处理完成，订单号：{}，用户ID：{}，金额：{}，已更新充值次数和金额", orderNo, order.getUserId(), amount);
            return true;
            
        } catch (Exception e) {
            log.error("处理充值成功逻辑异常，订单号：{}", orderNo, e);
            throw e; // 抛出异常，触发事务回滚
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean handleChargeFail(String orderNo, String failReason, String thirdOrderNo) {
        try {
            log.info("处理充值失败逻辑，订单号：{}，失败原因：{}，第三方订单号：{}", orderNo, failReason, thirdOrderNo);
            
            // 1. 更新订单状态为失败
            Map<String, Object> additionalInfo = new HashMap<>();
            additionalInfo.put("thirdOrderNo", thirdOrderNo);
            additionalInfo.put("failReason", failReason);
            boolean updateSuccess = updateChargeOrderStatus(orderNo, 2, thirdOrderNo, additionalInfo);
            
            if (!updateSuccess) {
                log.error("更新充值订单状态失败，订单号：{}", orderNo);
                return false;
            }
            log.info("充值失败处理完成，订单号：{}，失败原因：{}", orderNo, failReason);
            return true;
            
        } catch (Exception e) {
            log.error("处理充值失败逻辑异常，订单号：{}", orderNo, e);
            throw e;
        }
    }

    /**
     * 构建签名字符串
     */
    private String buildSignString(Map<String, String[]> parameterMap, String secretKey) {
        try {
            // 过滤掉sign参数，按key排序
            List<String> keys = parameterMap.entrySet().stream()
                    .filter(entry -> !"sign".equals(entry.getKey()))
                    .map(Map.Entry::getKey)
                    .sorted()
                    .collect(Collectors.toList());

            StringBuilder signString = new StringBuilder();
            for (String key : keys) {
                String[] values = parameterMap.get(key);
                if (values != null && values.length > 0 && values[0] != null) {
                    signString.append(key).append("=").append(values[0]).append("&");
                }
            }
            
            // 添加密钥
            signString.append("key=").append(secretKey);
            
            log.debug("构建签名字符串：{}", signString.toString());
            return signString.toString();
            
        } catch (Exception e) {
            log.error("构建签名字符串异常", e);
            return "";
        }
    }

    /**
     * 解析金额字符串
     */
    private Long parseAmount(String amountStr) {
        try {
            if (amountStr == null || amountStr.trim().isEmpty()) {
                return 0L;
            }
            
            // 移除货币符号和逗号
            String cleanAmount = amountStr.replaceAll("[^\\d.]", "");
            
            // 转换为卢比
            double amount = Double.parseDouble(cleanAmount);
            return (long) (amount * 0.01);
            
        } catch (Exception e) {
            log.warn("解析金额失败：{}", amountStr, e);
            return 0L;
        }
    }

    /**
     * 获取状态文本
     */
    private String getStateText(Integer state) {
        if (state == null) return "未知";
        
        switch (state) {
            case 0: return "待审核";
            case 1: return "审核通过";
            case 2: return "失败";
            default: return "未知状态";
        }
    }

    /**
     * 获取银行卡支付密钥
     */
    private String getBankSecretKey() {
        // 从配置文件或数据库获取
        return "bank_secret_key_123";
    }

    /**
     * 获取虚拟币支付密钥
     */
    private String getCryptoSecretKey() {
        return "crypto_secret_key_456";
    }

    /**
     * 获取UPI支付密钥
     */
    private String getUpiSecretKey() {
        return "upi_secret_key_789";
    }

    /**
     * 获取Paytm支付密钥
     */
    private String getPaytmSecretKey() {
        return "paytm_secret_key_012";
    }

    /**
     * 获取通用支付密钥
     */
    private String getCommonSecretKey() {
        return "common_secret_key_345";
    }
}
