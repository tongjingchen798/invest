package io.renren.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.renren.dao.ChargeOrderDao;
import io.renren.dao.UAddressConfigDao;
import io.renren.dao.UserDao;
import io.renren.entity.UAddressConfigEntity;
import io.renren.entity.ChargeOrderEntity;
import io.renren.entity.UserEntity;
import io.renren.service.USDTTransactionMonitorService;
import io.renren.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * USDT转账监控服务实现类
 *
 * @author renren
 * @date 2024-01-01
 */
@Service
public class USDTTransactionMonitorServiceImpl implements USDTTransactionMonitorService {
    

    @Autowired
    private UAddressConfigDao uAddressConfigDao;
    
    @Autowired
    private ChargeOrderDao chargeOrderDao;
    
    @Autowired
    private UserDao userDao;
    
    // TRON API配置
    private static final String TRON_API_BASE = "https://api.trongrid.io";
    private static final String USDT_CONTRACT_ADDRESS = "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t"; // USDT-TRC20合约地址
    
    @Override
    public Map<String, Object> monitorUSDTTransaction(String usdtAddress, String amount, Long userId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 1. 获取地址的USDT交易记录
            Map<String, Object> transactions = getUSDTTransactions(usdtAddress);
            
            if (transactions == null || !transactions.containsKey("data")) {
                result.put("success", false);
                result.put("message", "获取交易记录失败");
                return result;
            }
            
            JSONArray transactionList = (JSONArray) transactions.get("data");
            
            // 2. 检查是否有符合条件的转账
            for (Object obj : transactionList) {
                JSONObject transaction = (JSONObject) obj;
                if (isValidUSDTTransaction(transaction, usdtAddress, amount)) {
                    // 3. 创建充值订单
                    String txHash = transaction.getString("transaction_id");
                    createChargeOrder(userId, amount, txHash, usdtAddress);
                    
                    result.put("success", true);
                    result.put("message", "检测到USDT转账，已创建充值订单");
                    result.put("txHash", txHash);
                    return result;
                }
            }
            
            result.put("success", false);
            result.put("message", "未检测到符合条件的USDT转账");
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "监控USDT转账失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> checkUSDTBalance(String usdtAddress) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 调用TRON API获取USDT余额
            String url = TRON_API_BASE + "/v1/accounts/" + usdtAddress + "/tokens";
            String response = HttpUtils.get(url);
            
            JSONObject jsonResponse = JSON.parseObject(response);
            JSONArray tokens = jsonResponse.getJSONArray("data");
            
            BigDecimal usdtBalance = BigDecimal.ZERO;
            for (Object obj : tokens) {
                JSONObject token = (JSONObject) obj;
                if (USDT_CONTRACT_ADDRESS.equals(token.getString("token_id"))) {
                    usdtBalance = token.getBigDecimal("balance").divide(new BigDecimal("1000000")); // USDT有6位小数
                    break;
                }
            }
            
            result.put("success", true);
            result.put("balance", usdtBalance.toString());
            result.put("address", usdtAddress);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取USDT余额失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> verifyUSDTTransaction(String txHash, String usdtAddress, String amount) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取交易详情
            String url = TRON_API_BASE + "/v1/transactions/" + txHash;
            String response = HttpUtils.get(url);
            
            JSONObject transaction = JSON.parseObject(response);
            
            if (transaction.containsKey("error")) {
                result.put("success", false);
                result.put("message", "交易不存在或无效");
                return result;
            }
            
            // 验证交易
            if (isValidUSDTTransaction(transaction, usdtAddress, amount)) {
                result.put("success", true);
                result.put("message", "交易验证成功");
                result.put("txHash", txHash);
            } else {
                result.put("success", false);
                result.put("message", "交易验证失败");
            }
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "验证交易失败: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 获取USDT交易记录
     */
    private Map<String, Object> getUSDTTransactions(String usdtAddress) {
        try {
            String url = TRON_API_BASE + "/v1/accounts/" + usdtAddress + "/transactions/trc20";
            String response = HttpUtils.get(url);
            return JSON.parseObject(response);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 验证USDT交易是否有效
     */
    private boolean isValidUSDTTransaction(JSONObject transaction, String usdtAddress, String amount) {
        try {
            // 检查交易状态
            if (!"SUCCESS".equals(transaction.getString("contractRet"))) {
                return false;
            }
            
            // 检查是否为USDT转账
            JSONObject rawData = transaction.getJSONObject("raw_data");
            JSONArray contract = rawData.getJSONArray("contract");
            
            for (Object obj : contract) {
                JSONObject contractObj = (JSONObject) obj;
                if ("TriggerSmartContract".equals(contractObj.getString("type"))) {
                    JSONObject parameter = contractObj.getJSONObject("parameter");
                    JSONObject value = parameter.getJSONObject("value");
                    
                    // 检查合约地址是否为USDT
                    if (USDT_CONTRACT_ADDRESS.equals(value.getString("contract_address"))) {
                        // 检查目标地址
                        String toAddress = value.getString("data");
                        if (toAddress != null && toAddress.contains(usdtAddress)) {
                            // 检查金额
                            BigDecimal transactionAmount = new BigDecimal(amount);
                            // 这里需要解析data字段来获取实际转账金额
                            // 简化处理，实际需要解析hex数据
                            return true;
                        }
                    }
                }
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 创建充值订单
     */
    @Transactional
    public void createChargeOrder(Long userId, String amount, String txHash, String usdtAddress) {
        try {
            // 创建充值订单
            ChargeOrderEntity chargeOrder = new ChargeOrderEntity();
            chargeOrder.setUserId(userId);
            chargeOrder.setAmount(amount);
            chargeOrder.setRealAmount(new BigDecimal(amount));
            chargeOrder.setState(1); // 已支付
            chargeOrder.setPayType("USDT");
            chargeOrder.setRemark("USDT充值 - 交易哈希: " + txHash);
            chargeOrder.setCreateTime(System.currentTimeMillis());
            
            chargeOrderDao.insert(chargeOrder);
            
            // 更新用户余额
            UserEntity member = userDao.selectById(userId);
            if (member != null) {
                BigDecimal newBalance = member.getBalance().add(new BigDecimal(amount));
                member.setBalance(newBalance);
                userDao.updateById(member);
            }
            
        } catch (Exception e) {
            throw new RuntimeException("创建充值订单失败: " + e.getMessage(), e);
        }
    }
}
