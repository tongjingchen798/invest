package io.renren.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.renren.common.exception.ErrorCode;
import io.renren.common.exception.RenException;
import io.renren.dao.ChargeOrderDao;
import io.renren.dao.UserBalanceDetailDao;
import io.renren.dao.UserDao;
import io.renren.dao.UsdtRecordDao;
import io.renren.entity.ChargeOrderEntity;
import io.renren.entity.UserBalanceDetailEntity;
import io.renren.entity.UserEntity;
import io.renren.entity.UsdtRecordEntity;
import io.renren.enums.BusinessTypeEnum;
import io.renren.service.USDTTransactionMonitorService;
import io.renren.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * USDT转账监控服务实现类
 *
 * @author renren
 */
@Slf4j
@Service
public class USDTTransactionMonitorServiceImpl implements USDTTransactionMonitorService {
    

    @Autowired
    private UserDao userDao;
    
    @Autowired
    private ChargeOrderDao chargeOrderDao;
    
    @Autowired
    private UserBalanceDetailDao userBalanceDetailDao;
    
    @Autowired
    private UsdtRecordDao usdtRecordDao;
    
    // TRON API配置
    private static final String TRON_API_BASE = "https://api.trongrid.io";
    private static final String USDT_CONTRACT_ADDRESS = "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t"; // USDT-TRC20合约地址
    
    @Override
    public void fetchAndStoreUSDTTransactions(String usdtAddress) {
        try {
            // 1. 获取地址的USDT交易记录
            Map<String, Object> transactions = getUSDTTransactions(usdtAddress);
            
            if (transactions == null || !transactions.containsKey("data")) {
                return;
            }
            
            JSONArray transactionList = (JSONArray) transactions.get("data");
            
            // 2. 遍历所有交易记录，存储到U收款记录表
            for (Object obj : transactionList) {
                JSONObject transaction = (JSONObject) obj;
                String txHash = transaction.getString("transaction_id");
                
                // 检查是否已存在记录
                UsdtRecordEntity existingRecord = findUsdtRecordByTxHash(txHash);
                if (existingRecord != null) {
                    continue;
                }
                
                // 检查是否为USDT转账
                if (isUSDTTransaction(transaction)) {
                    // 创建U收款记录
                    createUsdtRecord(transaction, usdtAddress, null, null);
                }
            }
            
        } catch (Exception e) {
            // 记录错误日志，但不抛出异常，避免影响定时任务
            log.error("拉取USDT交易记录失败: {}", e.getMessage(), e);
        }
    }
    
    @Override
    public Map<String, Object> matchUSDTRecords(String usdtAddress, Long amount, Long userId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 1. 查找未匹配的U收款记录
            List<UsdtRecordEntity> unmatchedRecords = findUnmatchedUSDTRecords(usdtAddress, amount);
            
            if (unmatchedRecords.isEmpty()) {
                result.put("success", false);
                result.put("message", "未找到符合条件的未匹配U收款记录");
                return result;
            }
            
            // 2. 查找待处理的USDT充值订单
            ChargeOrderEntity pendingOrder = findPendingUSDTOrder(userId, usdtAddress, amount);
            if (pendingOrder == null) {
                result.put("success", false);
                result.put("message", "未找到对应的待处理充值订单");
                return result;
            }
            
            // 3. 匹配第一个符合条件的记录
            UsdtRecordEntity matchedRecord = unmatchedRecords.get(0);
            
            // 4. 更新U收款记录
            updateUsdtRecordAsMatched(matchedRecord, pendingOrder.getOrderno());
            
            // 5. 更新订单状态为成功
            updateChargeOrderStatus(pendingOrder, matchedRecord.getTransactionId());
            
            // 6. 更新用户余额
            userDao.addUserBalance(userId, amount);
            
            // 7. 记录账变明细
            recordUSDTRechargeBalanceDetail(userId, amount, matchedRecord.getTransactionId(), usdtAddress);
            
            result.put("success", true);
            result.put("message", "U收款记录匹配成功");
            result.put("txHash", matchedRecord.getTransactionId());
            result.put("orderNo", pendingOrder.getOrderno());
            result.put("recordId", matchedRecord.getId());
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "匹配U收款记录失败: " + e.getMessage());
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
    
//    @Override
//    public Map<String, Object> verifyUSDTTransaction(String txHash, String usdtAddress, Long amount) {
//        Map<String, Object> result = new HashMap<>();
//
//        try {
//            // 获取交易详情
//            String url = TRON_API_BASE + "/v1/transactions/" + txHash;
//            String response = HttpUtils.get(url);
//
//            JSONObject transaction = JSON.parseObject(response);
//
//            if (transaction.containsKey("error")) {
//                result.put("success", false);
//                result.put("message", "交易不存在或无效");
//                return result;
//            }
//
//            // 验证交易
//            if (isValidUSDTTransaction(transaction, usdtAddress, amount)) {
//                result.put("success", true);
//                result.put("message", "交易验证成功");
//                result.put("txHash", txHash);
//            } else {
//                result.put("success", false);
//                result.put("message", "交易验证失败");
//            }
//
//        } catch (Exception e) {
//            result.put("success", false);
//            result.put("message", "验证交易失败: " + e.getMessage());
//        }
//
//        return result;
//    }
    
    /**
     * 获取USDT交易记录（只获取转入交易）
     */
    private Map<String, Object> getUSDTTransactions(String usdtAddress) {
        try {
            // 构建API URL，添加参数限制只获取转入交易
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(TRON_API_BASE);
            urlBuilder.append("/v1/accounts/");
            urlBuilder.append(usdtAddress);
            urlBuilder.append("/transactions/trc20");
            
            // 添加查询参数
            urlBuilder.append("?limit=200");                    // 限制返回数量
            urlBuilder.append("&only_confirmed=true");         // 只获取已确认的交易
            urlBuilder.append("&only_to=true");                // 只获取转入交易
            urlBuilder.append("&contract_address=").append(USDT_CONTRACT_ADDRESS); // 指定USDT合约地址
            
            String url = urlBuilder.toString();
            log.debug("调用TRON API获取USDT转入交易: {}", url);
            
            String response = HttpUtils.get(url);
            if (response == null || response.trim().isEmpty()) {
                log.warn("TRON API返回空响应");
                return null;
            }
            
            JSONObject result = JSON.parseObject(response);
            if (result.containsKey("error")) {
                log.error("TRON API返回错误: {}", result.getString("error"));
                return null;
            }
            
            // 记录获取到的交易数量
            if (result.containsKey("data")) {
                JSONArray data = result.getJSONArray("data");
                log.info("成功获取到 {} 条USDT转入交易记录", data != null ? data.size() : 0);
            }
            
            return result;
        } catch (Exception e) {
            log.error("调用TRON API获取USDT交易记录失败: {}", e.getMessage(), e);
            return null;
        }
    }
    
//    /**
//     * 查找符合条件的USDT转账（按时间倒序，优先处理最新的交易）
//     */
//    private JSONObject findMatchingUSDTTransaction(JSONArray transactionList, String usdtAddress, Long amount) {
//        try {
//            // 按时间戳倒序排序，优先处理最新的交易
//            List<JSONObject> sortedTransactions = new ArrayList<>();
//            for (Object obj : transactionList) {
//                JSONObject transaction = (JSONObject) obj;
//                sortedTransactions.add(transaction);
//            }
//
//            // 按block_timestamp倒序排序
//            sortedTransactions.sort((t1, t2) -> {
//                Long ts1 = t1.getLong("block_timestamp");
//                Long ts2 = t2.getLong("block_timestamp");
//                if (ts1 == null) ts1 = 0L;
//                if (ts2 == null) ts2 = 0L;
//                return ts2.compareTo(ts1); // 倒序
//            });
//
//            // 查找符合条件的交易
//            for (JSONObject transaction : sortedTransactions) {
//                if (isValidUSDTTransaction(transaction, usdtAddress, amount)) {
//                    return transaction;
//                }
//            }
//
//            return null;
//        } catch (Exception e) {
//            return null;
//        }
//    }
    
//    /**
//     * 验证USDT交易是否有效
//     */
//    private boolean isValidUSDTTransaction(JSONObject transaction, String usdtAddress, Long amount) {
//        try {
//            // 检查交易状态
//            if (!"SUCCESS".equals(transaction.getString("contractRet"))) {
//                return false;
//            }
//
//            // 检查是否为USDT转账
//            JSONObject rawData = transaction.getJSONObject("raw_data");
//            if (rawData == null) {
//                return false;
//            }
//
//            JSONArray contract = rawData.getJSONArray("contract");
//            if (contract == null) {
//                return false;
//            }
//
//            for (Object obj : contract) {
//                JSONObject contractObj = (JSONObject) obj;
//                if ("TriggerSmartContract".equals(contractObj.getString("type"))) {
//                    JSONObject parameter = contractObj.getJSONObject("parameter");
//                    if (parameter == null) continue;
//
//                    JSONObject value = parameter.getJSONObject("value");
//                    if (value == null) continue;
//
//                    // 检查合约地址是否为USDT
//                    String contractAddress = value.getString("contract_address");
//                    if (!USDT_CONTRACT_ADDRESS.equals(contractAddress)) {
//                        continue;
//                    }
//
//                    // 检查目标地址
//                    String data = value.getString("data");
//                    if (data == null) continue;
//
//                    // 解析data字段获取目标地址和金额
//                    TransactionData parsedData = parseTransactionData(data);
//                    if (parsedData != null &&
//                        usdtAddress.equals(parsedData.getToAddress()) &&
//                        amount.equals(parsedData.getAmount())) {
//                        return true;
//                    }
//
//                    // 如果无法解析data，则使用简化的验证方式
//                    // 检查data是否包含目标地址的hex编码
//                    if (isDataContainsAddress(data, usdtAddress)) {
//                        return true;
//                    }
//                }
//            }
//
//            return false;
//        } catch (Exception e) {
//            return false;
//        }
//    }
    
//    /**
//     * 解析交易数据
//     */
//    private TransactionData parseTransactionData(String data) {
//        try {
//            // 简化实现：直接返回null，让调用方使用其他方式验证
//            // 在实际应用中，这里应该解析TRON的hex数据来获取目标地址和金额
//            // 由于TRON的hex数据解析比较复杂，暂时返回null
//            return null;
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    /**
//     * 检查data是否包含目标地址
//     */
//    private boolean isDataContainsAddress(String data, String usdtAddress) {
//        try {
//            // 将地址转换为hex格式进行匹配
//            // 这是一个简化的实现，实际应该根据TRON协议进行精确匹配
//            String addressHex = addressToHex(usdtAddress);
//            return data != null && data.contains(addressHex);
//        } catch (Exception e) {
//            return false;
//        }
//    }
    
//    /**
//     * 将TRON地址转换为hex格式
//     */
//    private String addressToHex(String address) {
//        try {
//            // 简化实现：直接返回地址的hash值
//            // 实际应该使用TRON的地址转换算法
//            return address.replace("T", "").toLowerCase();
//        } catch (Exception e) {
//            return address;
//        }
//    }
    
    /**
     * 检查是否为有效的USDT转入交易
     */
    private boolean isUSDTTransaction(JSONObject transaction) {
        try {
            // 1. 检查交易类型
            String type = transaction.getString("type");
            if (!"Transfer".equals(type)) {
//                log.debug("交易类型不是Transfer: {}", type);
                return false;
            }
            
            // 2. 检查token_info中的合约地址
            JSONObject tokenInfo = transaction.getJSONObject("token_info");
            if (tokenInfo == null) {
                log.debug("交易缺少token_info信息");
                return false;
            }
            
            String contractAddress = tokenInfo.getString("address");
            if (!USDT_CONTRACT_ADDRESS.equals(contractAddress)) {
                log.debug("合约地址不匹配: 期望={}, 实际={}", USDT_CONTRACT_ADDRESS, contractAddress);
                return false;
            }
            
            // 3. 检查是否有有效的value
            String value = transaction.getString("value");
            if (value == null || value.equals("0")) {
                log.debug("交易金额无效: {}", value);
                return false;
            }
            
            // 4. 验证交易方向（确保是转入交易）
            String toAddress = transaction.getString("to");
            if (toAddress == null || toAddress.trim().isEmpty()) {
                log.debug("交易缺少收款地址");
                return false;
            }
            
            // 注意：由于API已经通过only_to参数过滤，这里主要是双重验证
            // 如果API参数不生效，这里可以作为备用验证
            
            // 5. 检查交易状态（确保是成功状态）
            if (transaction.containsKey("result") && !"SUCCESS".equals(transaction.getString("result"))) {
                log.debug("交易状态不是成功: {}", transaction.getString("result"));
                return false;
            }
            
            log.debug("发现有效的USDT转入交易: txHash={}, to={}, value={}", 
                     transaction.getString("transaction_id"), toAddress, value);
            
            return true;
        } catch (Exception e) {
            log.error("验证USDT交易时发生异常: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 查找未匹配的U收款记录
     */
    private List<UsdtRecordEntity> findUnmatchedUSDTRecords(String usdtAddress, Long amount) {
        try {
            // 将金额转换为USDT单位进行比较
            BigDecimal usdtAmount = new BigDecimal(amount).divide(new BigDecimal("1000000"));
            
            // 查询未匹配的记录，按时间倒序
            return usdtRecordDao.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<UsdtRecordEntity>()
                    .eq("to_address", usdtAddress)
                    .eq("is_process", 0) // 未处理
                    .eq("amount", usdtAmount) // 精确匹配金额
                    .orderByDesc("create_date")
            );
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    /**
     * 更新U收款记录为已匹配
     */
    private void updateUsdtRecordAsMatched(UsdtRecordEntity record, String orderNo) {
        try {
            record.setIsProcess(1); // 1-已处理
            record.setOrderNo(orderNo);
            record.setUpdateDate(new Date());
            
            usdtRecordDao.updateById(record);
        } catch (Exception e) {
            throw new RenException(ErrorCode.UPDATE_USDT_RECORD_FAILED);
        }
    }

    
    /**
     * 查找待处理的USDT充值订单
     */
    private ChargeOrderEntity findPendingUSDTOrder(Long userId, String usdtAddress, Long amount) {
        try {
            // 查询用户待处理的USDT充值订单
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("state", 0); // 0-待处理
            params.put("channelType", "USDT");
            params.put("walletAddr", usdtAddress);
            params.put("amount", amount);
            
            // 按创建时间倒序，取最新的订单
            params.put("orderBy", "create_time DESC");
            params.put("limit", 1);
            
            return chargeOrderDao.selectByParams(params).stream()
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 更新充值订单状态
     */
    private void updateChargeOrderStatus(ChargeOrderEntity order, String txHash) {
        try {
            order.setState(1); // 1-成功
            order.setThreeorderNo(txHash); // 设置第三方订单号（交易哈希）
            order.setChargeTime(new Date()); // 设置充值时间
            order.setUpdateTime(new Date()); // 设置更新时间
            order.setRemark("USDT转账监控自动确认");
            
            chargeOrderDao.updateById(order);
        } catch (Exception e) {
            throw new RenException(ErrorCode.UPDATE_CHARGE_ORDER_STATUS_FAILED);
        }
    }
    
    /**
     * 记录USDT充值账变明细
     */
    private void recordUSDTRechargeBalanceDetail(Long userId, Long amount, String txHash, String usdtAddress) {
        try {
            // 获取用户信息
            UserEntity user = userDao.selectById(userId);
            if (user == null) {
                throw new RenException(ErrorCode.USER_NOT_EXISTS);
            }
            
            // 创建账变记录
            UserBalanceDetailEntity balanceDetail = new UserBalanceDetailEntity();
            balanceDetail.setUserId(userId);
            balanceDetail.setTransactionDate(new Date());
            balanceDetail.setAgentId(user.getAgent());
            balanceDetail.setAgentName(user.getAgentName());
            balanceDetail.setBusiType(BusinessTypeEnum.ONLINE_RECHARGE.getCode()); // 11-线上充值
            balanceDetail.setChannel("1");
            balanceDetail.setStreamId(txHash); // 使用交易哈希作为流水ID
            balanceDetail.setUseAmount(amount); // 充值金额
            balanceDetail.setOriginalAmount(user.getAssets() != null ? user.getAssets() - amount : 0L); // 充值前余额
            balanceDetail.setTransactionAmount(user.getAssets() != null ? user.getAssets() : amount); // 充值后余额
            balanceDetail.setRemarks("USDT充值成功 - 地址: " + usdtAddress + ", 交易哈希: " + txHash);
            balanceDetail.setSalesmanName(user.getSalesmanName());
            balanceDetail.setSalesmanId(user.getSalesmanid());
            balanceDetail.setStatus(1); // 1-正常
            balanceDetail.setCreateDate(new Date());
            balanceDetail.setUpdateDate(new Date());
            
            // 插入账变记录
            userBalanceDetailDao.insert(balanceDetail);
        } catch (Exception e) {
            throw new RenException(ErrorCode.RECORD_USDT_RECHARGE_BALANCE_DETAIL_FAILED);
        }
    }
    
    /**
     * 根据交易哈希查找U收款记录
     */
    private UsdtRecordEntity findUsdtRecordByTxHash(String txHash) {
        try {
            // 使用MyBatis-Plus的查询方法
            return usdtRecordDao.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<UsdtRecordEntity>()
                    .eq("transaction_id", txHash)
            );
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 创建U收款记录（只处理转入交易）
     */
    private void createUsdtRecord(JSONObject transaction, String usdtAddress, Long amount, String orderNo) {
        try {
            // 验证交易方向，确保是转入交易
            String toAddress = transaction.getString("to");
            if (toAddress == null || !usdtAddress.equals(toAddress)) {
                return;
            }
            
            UsdtRecordEntity usdtRecord = new UsdtRecordEntity();
            
            // 设置基本信息
            usdtRecord.setTransactionId(transaction.getString("transaction_id"));
            usdtRecord.setToAddress(toAddress); // 确保是目标地址
            usdtRecord.setContractAddress(USDT_CONTRACT_ADDRESS);
            usdtRecord.setContractType("USDT-TRC20");
            
            // 设置金额（如果amount为null，则从交易数据中解析）
            if (amount != null) {
                usdtRecord.setAmount(new BigDecimal(amount).divide(new BigDecimal("1000000"))); // 转换为USDT单位
            } else {
                // 从交易数据中解析金额
                String valueStr = transaction.getString("value");
                if (valueStr != null) {
                    // USDT有6位小数，所以需要除以1000000
                    BigDecimal value = new BigDecimal(valueStr);
                    usdtRecord.setAmount(value.divide(new BigDecimal("1000000")));
                } else {
                    usdtRecord.setAmount(BigDecimal.ZERO);
                }
            }
            
            usdtRecord.setOrderNo(orderNo);
            
            // 设置区块信息
            if (transaction.containsKey("block_timestamp")) {
                usdtRecord.setBlockTs(transaction.getLong("block_timestamp"));
                usdtRecord.setBlockTime(new Date(transaction.getLong("block_timestamp")));
            }
            
            // 设置转账地址
            usdtRecord.setFromAddress(transaction.getString("from"));
            
            // 设置状态
            usdtRecord.setIsRisk(0); // 0-否
            usdtRecord.setIsProcess(orderNo != null ? 1 : 0); // 有订单号表示已处理
            
            // 设置时间
            usdtRecord.setCreateDate(new Date());
            usdtRecord.setUpdateDate(new Date());
            
            // 插入记录
            usdtRecordDao.insert(usdtRecord);
            
            log.info("成功创建USDT转入记录: txHash={}, from={}, to={}, amount={}", 
                    usdtRecord.getTransactionId(), usdtRecord.getFromAddress(), 
                    usdtRecord.getToAddress(), usdtRecord.getAmount());
                    
        } catch (Exception e) {
            log.error("创建USDT记录失败: txHash={}, error={}", 
                     transaction.getString("transaction_id"), e.getMessage(), e);
            throw new RenException(ErrorCode.CREATE_USDT_RECORD_FAILED);
        }
    }
    

}
