package io.renren.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.common.exception.ErrorCode;
import io.renren.common.exception.RenException;
import io.renren.common.utils.Result;
import io.renren.dao.UserDao;
import io.renren.dao.WithdrawOrderDao;
import io.renren.dto.RewardWithdrawRequestDTO;
import io.renren.dto.RewardWithdrawSumDTO;
import io.renren.dto.UserWithdrawInfoDTO;
import io.renren.dto.WithdrawPageData;
import io.renren.dto.WithdrawQueryDTO;
import io.renren.entity.UserEntity;
import io.renren.entity.WithdrawOrderEntity;
import io.renren.service.WithdrawService;
import io.renren.utils.WithdrawRuleValidator;
import io.renren.utils.WithdrawRuleValidator.ValidationResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 提现服务实现类
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Service
public class WithdrawServiceImpl implements WithdrawService {

    @Autowired
    private WithdrawOrderDao withdrawOrderDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private WithdrawRuleValidator withdrawRuleValidator;

    // 订单号生成器
    private static final AtomicLong orderNoGenerator = new AtomicLong(System.currentTimeMillis());

    @Override
    public Map<String, Object> checkFirstWithdraw(Long userId) {
        try {
            Map<String, Object> result = new HashMap<>();
            
            // 查询用户提现记录数量
            Long withdrawCount = withdrawOrderDao.selectCountByUserId(userId);
            
            // 判断是否首次提现
            boolean isFirstWithdraw = withdrawCount == 0;
            
            // 构建返回结果
            result.put("isFirstWithdraw", isFirstWithdraw);
            result.put("withdrawCount", withdrawCount);
            result.put("userId", userId);
            
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("检查首次提现状态失败: " + e.getMessage());
        }
    }

    @Override
    public WithdrawPageData getWithdrawPageData(WithdrawQueryDTO queryDTO) {
        try {
            WithdrawPageData pageData = new WithdrawPageData();
            
            // 使用MyBatis-Plus分页查询
            Page<WithdrawOrderEntity> pageParam = new Page<>(queryDTO.getPage(), queryDTO.getLimit());
            
            // 构建查询条件
            QueryWrapper<WithdrawOrderEntity> queryWrapper = buildQueryWrapper(queryDTO);
            
            // 执行分页查询
            Page<WithdrawOrderEntity> result = withdrawOrderDao.selectPage(pageParam, queryWrapper);
            
            // 转换为DTO列表
            List<UserWithdrawInfoDTO> dtoList = convertToDTOList(result.getRecords());
            
            // 计算汇总信息
            Map<String, Object> sum = calculateSum(result.getRecords());
            
            // 设置分页数据
            pageData.setList(dtoList);
            pageData.setSum(sum);
            pageData.setTotal((int) result.getTotal());
            
            return pageData;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取提现分页数据失败: " + e.getMessage());
        }
    }

    @Override
    public WithdrawPageData getRewardWithdrawPageData(WithdrawQueryDTO queryDTO) {
        try {
            WithdrawPageData pageData = new WithdrawPageData();
            
            // 使用MyBatis-Plus分页查询
            Page<WithdrawOrderEntity> pageParam = new Page<>(queryDTO.getPage(), queryDTO.getLimit());
            
            // 构建佣金提现查询条件
            QueryWrapper<WithdrawOrderEntity> queryWrapper = buildRewardWithdrawQueryWrapper(queryDTO);
            
            // 执行分页查询
            Page<WithdrawOrderEntity> result = withdrawOrderDao.selectPage(pageParam, queryWrapper);
            
            // 转换为DTO列表
            List<UserWithdrawInfoDTO> dtoList = convertToDTOList(result.getRecords());
            
            // 计算汇总信息
            Map<String, Object> sum = calculateSum(result.getRecords());
            
            // 设置分页数据
            pageData.setList(dtoList);
            pageData.setSum(sum);
            pageData.setTotal((int) result.getTotal());
            
            return pageData;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取佣金提现分页数据失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> submitRewardWithdraw(Long userId, RewardWithdrawRequestDTO requestDTO) {
        try {

            // 查询用户信息
            UserEntity user = userDao.selectById(userId);
            if (user == null) {
                throw new RenException(30001);
            }

//            // 验证支付密码
//            if (!validatePayPassword(user, requestDTO.getPayPassword())) {
//                throw new RuntimeException("支付密码错误");
//            }

            // 检查佣金余额
            if (user.getCommissionBalance() == null || user.getCommissionBalance() < requestDTO.getAmount()) {
                throw new RuntimeException("佣金余额不足");
            }
            
            // 验证提现规则
            ValidationResult ruleResult = withdrawRuleValidator.validateAllRules(userId, new BigDecimal(requestDTO.getAmount()));
            if (!ruleResult.isValid()) {
                throw new RuntimeException(ruleResult.getErrorMessage());
            }
            
            // 生成订单号
            String orderNo = generateOrderNo();
            
            // 计算手续费和实际到账金额（5%手续费）
            BigDecimal amount = new BigDecimal(requestDTO.getAmount());
            BigDecimal handFee = withdrawRuleValidator.calculateFee(amount);
            BigDecimal realAmount = withdrawRuleValidator.calculateRealAmount(amount);
            
            // 创建提现订单
            WithdrawOrderEntity withdrawOrder = new WithdrawOrderEntity();
            withdrawOrder.setId(String.valueOf(System.currentTimeMillis()));
            withdrawOrder.setUserId(userId.toString());
            withdrawOrder.setMobile(user.getMobile());
            withdrawOrder.setUsername(user.getUsername());
            withdrawOrder.setAmount(requestDTO.getAmount());
            withdrawOrder.setInputamount(requestDTO.getAmount());
            withdrawOrder.setHandFee(handFee.longValue());
            withdrawOrder.setRealAmount(realAmount.longValue());
            withdrawOrder.setPayNo(requestDTO.getPayNo());
            withdrawOrder.setPayName(user.getUsername());
            withdrawOrder.setWithdrawType(2); // 佣金提现
            withdrawOrder.setState(0); // 待审核
            withdrawOrder.setOrderno(orderNo);
            withdrawOrder.setCreateTime(new Date());
            withdrawOrder.setWithdrawTime(new Date());
            withdrawOrder.setRemark("佣金提现申请");
            
            // 保存提现订单
            withdrawOrderDao.insert(withdrawOrder);
            
            // 冻结用户佣金余额
            user.setCommissionBalance(user.getCommissionBalance() - requestDTO.getAmount());
            user.setFreezeBalance(user.getFreezeBalance() + requestDTO.getAmount());
            userDao.updateById(user);
            
            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("orderNo", orderNo);
            result.put("amount", requestDTO.getAmount());
            result.put("handFee", handFee.longValue());
            result.put("realAmount", realAmount.longValue());
            result.put("status", "待审核");
            result.put("message", "佣金提现申请提交成功");
            
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("提交佣金提现申请失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> submitWithdraw(Long userId, RewardWithdrawRequestDTO requestDTO) {
        try {
            UserEntity user = userDao.selectById(userId);
            if (user == null) {
                throw new RenException(30001);
            }

//            // 验证支付密码
//            if (!validatePayPassword(user, requestDTO.getPayPassword())) {
//                throw new RuntimeException("支付密码错误");
//            }

            // 检查佣金余额
            if (user.getAssets() == null || user.getAssets() < requestDTO.getAmount() || user.getCashwithdrawable() < requestDTO.getAmount() ) {
                throw new RuntimeException("余额不足");
            }

            // 验证提现规则
            ValidationResult ruleResult = withdrawRuleValidator.validateAllRules(userId, new BigDecimal(requestDTO.getAmount()));
            if (!ruleResult.isValid()) {
                throw new RuntimeException(ruleResult.getErrorMessage());
            }

            // 生成订单号
            String orderNo = generateOrderNo();

            // 计算手续费和实际到账金额（5%手续费）
            BigDecimal amount = new BigDecimal(requestDTO.getAmount());
            BigDecimal handFee = withdrawRuleValidator.calculateFee(amount);
            BigDecimal realAmount = withdrawRuleValidator.calculateRealAmount(amount);

            // 创建提现订单
            WithdrawOrderEntity withdrawOrder = new WithdrawOrderEntity();
            withdrawOrder.setId(String.valueOf(System.currentTimeMillis()));
            withdrawOrder.setUserId(userId.toString());
            withdrawOrder.setMobile(user.getMobile());
            withdrawOrder.setUsername(user.getUsername());
            withdrawOrder.setAmount(requestDTO.getAmount());
            withdrawOrder.setInputamount(requestDTO.getAmount());
            withdrawOrder.setHandFee(handFee.longValue());
            withdrawOrder.setRealAmount(realAmount.longValue());
            withdrawOrder.setPayNo(requestDTO.getPayNo());
            withdrawOrder.setPayName(user.getUsername());
            withdrawOrder.setWithdrawType(1);
            withdrawOrder.setState(0);
            withdrawOrder.setOrderno(orderNo);
            withdrawOrder.setCreateTime(new Date());
            withdrawOrder.setWithdrawTime(new Date());
            withdrawOrder.setRemark("余额提现申请");

            // 保存提现订单
            withdrawOrderDao.insert(withdrawOrder);

            // 扣除用户可提现余额
            userDao.reduceUserBalance(userId,amount.longValue());

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("orderNo", orderNo);
            result.put("amount", requestDTO.getAmount());
            result.put("handFee", handFee.longValue());
            result.put("realAmount", realAmount.longValue());
            result.put("status", "待审核");
            result.put("message", "提现申请提交成功");

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("提交佣金提现申请失败: " + e.getMessage());
        }
    }

    @Override
    public RewardWithdrawSumDTO getRewardWithdrawSum(Long userId) {
        try {
            RewardWithdrawSumDTO sumDTO = new RewardWithdrawSumDTO();
            
            // 构建查询条件：佣金提现类型
            QueryWrapper<WithdrawOrderEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId.toString())
                       .eq("withdraw_type", 2); // 佣金提现
            
            // 查询所有佣金提现记录
            List<WithdrawOrderEntity> withdrawList = withdrawOrderDao.selectList(queryWrapper);
            
            long historyAmount = 0; // 累计提现
            long zztxAmount = 0;    // 提现中
            
            for (WithdrawOrderEntity withdraw : withdrawList) {
                if (withdraw.getAmount() != null) {
                    // 累计提现：包括所有状态的提现记录
                    historyAmount += withdraw.getAmount();
                    
                    // 提现中：状态为0（待审核）和1（审核通过）的记录
                    if (withdraw.getState() != null && (withdraw.getState() == 0 || withdraw.getState() == 1)) {
                        zztxAmount += withdraw.getAmount();
                    }
                }
            }
            
            sumDTO.setHistoryAmount(historyAmount);
            sumDTO.setZztxAmount(zztxAmount);
            
            return sumDTO;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取佣金提现统计失败: " + e.getMessage());
        }
    }

//    /**
//     * 验证支付密码
//     */
//    private boolean validatePayPassword(UserEntity user, String payPassword) {
//        // 这里应该根据实际的密码验证逻辑来实现
//        // 暂时使用简单的字符串比较，实际项目中应该使用加密验证
//        return StringUtils.hasText(payPassword) && payPassword.equals(user.getPaymentPwd());
//    }

    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        long timestamp = System.currentTimeMillis();
        long sequence = orderNoGenerator.incrementAndGet();
        return "RW" + timestamp + String.format("%04d", sequence % 10000);
    }

    /**
     * 构建查询条件
     */
    private QueryWrapper<WithdrawOrderEntity> buildQueryWrapper(WithdrawQueryDTO queryDTO) {
        QueryWrapper<WithdrawOrderEntity> queryWrapper = new QueryWrapper<>();
        
        // 用户ID条件
        queryWrapper.eq("user_id", queryDTO.getUserId().toString());
        
        // 第三方订单号条件
        if (StringUtils.hasText(queryDTO.getOrderno())) {
            queryWrapper.eq("threeorder_no", queryDTO.getOrderno());
        }
        
        // 卡号条件
        if (StringUtils.hasText(queryDTO.getPayNo())) {
            queryWrapper.eq("pay_no", queryDTO.getPayNo());
        }
        
        // 状态条件
        if (queryDTO.getState() != null) {
            queryWrapper.eq("state", queryDTO.getState());
        }
        
        // 我方订单号条件
        if (StringUtils.hasText(queryDTO.getTransNo())) {
            queryWrapper.eq("orderno", queryDTO.getTransNo());
        }
        
        // 排序
        if (StringUtils.hasText(queryDTO.getOrderField())) {
            String order = "desc".equalsIgnoreCase(queryDTO.getOrder()) ? "desc" : "asc";
            queryWrapper.orderBy(true, "desc".equals(order), queryDTO.getOrderField());
        } else {
            // 默认按创建时间倒序
            queryWrapper.orderByDesc("create_time");
        }
        
        return queryWrapper;
    }

    /**
     * 构建佣金提现查询条件
     */
    private QueryWrapper<WithdrawOrderEntity> buildRewardWithdrawQueryWrapper(WithdrawQueryDTO queryDTO) {
        QueryWrapper<WithdrawOrderEntity> queryWrapper = new QueryWrapper<>();
        
        // 用户ID条件
        queryWrapper.eq("user_id", queryDTO.getUserId().toString());
        
        // 佣金提现类型条件（withdraw_type = 2）
        queryWrapper.eq("withdraw_type", 2);
        
        // 第三方订单号条件
        if (StringUtils.hasText(queryDTO.getOrderno())) {
            queryWrapper.eq("threeorder_no", queryDTO.getOrderno());
        }
        
        // 卡号条件
        if (StringUtils.hasText(queryDTO.getPayNo())) {
            queryWrapper.eq("pay_no", queryDTO.getPayNo());
        }
        
        // 状态条件
        if (queryDTO.getState() != null) {
            queryWrapper.eq("state", queryDTO.getState());
        }
        
        // 我方订单号条件
        if (StringUtils.hasText(queryDTO.getTransNo())) {
            queryWrapper.eq("orderno", queryDTO.getTransNo());
        }
        
        // 排序
        if (StringUtils.hasText(queryDTO.getOrderField())) {
            String order = "desc".equalsIgnoreCase(queryDTO.getOrder()) ? "desc" : "asc";
            queryWrapper.orderBy(true, "desc".equals(order), queryDTO.getOrderField());
        } else {
            // 默认按创建时间倒序
            queryWrapper.orderByDesc("create_time");
        }
        
        return queryWrapper;
    }

    /**
     * 将Entity转换为DTO
     */
    private List<UserWithdrawInfoDTO> convertToDTOList(List<WithdrawOrderEntity> entityList) {
        List<UserWithdrawInfoDTO> dtoList = new java.util.ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        for (WithdrawOrderEntity entity : entityList) {
            UserWithdrawInfoDTO dto = new UserWithdrawInfoDTO();
            
            // 复制基本属性
            BeanUtils.copyProperties(entity, dto);
            
            // 处理字段类型转换
            dto.setId(Long.parseLong(entity.getId()));
            dto.setUserId(Long.parseLong(entity.getUserId()));
            dto.setChannelid(entity.getChannelid() != null ? Long.parseLong(entity.getChannelid()) : null);
            dto.setMerchantid(entity.getMerchantid() != null ? Long.parseLong(entity.getMerchantid()) : null);
            
            // 格式化日期字段
            if (entity.getCreateTime() != null) {
                dto.setCreateTime(sdf.format(entity.getCreateTime()));
            }
            if (entity.getStateTime() != null) {
                dto.setStateTime(sdf.format(entity.getStateTime()));
            }
            if (entity.getWithdrawTime() != null) {
                dto.setWithdrawTime(sdf.format(entity.getWithdrawTime()));
            }
            
            dtoList.add(dto);
        }
        
        return dtoList;
    }

    /**
     * 计算汇总信息
     */
    private Map<String, Object> calculateSum(List<WithdrawOrderEntity> records) {
        Map<String, Object> sum = new HashMap<>();
        
        long totalAmount = 0;
        long totalInputAmount = 0;
        long totalRealAmount = 0;
        long totalHandFee = 0;
        long totalChannelAmount = 0;
        int totalCount = 0;
        
        for (WithdrawOrderEntity record : records) {
            totalAmount += record.getAmount() != null ? record.getAmount() : 0;
            totalInputAmount += record.getInputamount() != null ? record.getInputamount() : 0;
            totalRealAmount += record.getRealAmount() != null ? record.getRealAmount() : 0;
            totalHandFee += record.getHandFee() != null ? record.getHandFee() : 0;
            totalChannelAmount += record.getChannelAmount() != null ? record.getChannelAmount() : 0;
            totalCount++;
        }
        
        sum.put("totalAmount", totalAmount);
        sum.put("totalInputAmount", totalInputAmount);
        sum.put("totalRealAmount", totalRealAmount);
        sum.put("totalHandFee", totalHandFee);
        sum.put("totalChannelAmount", totalChannelAmount);
        sum.put("totalCount", totalCount);
        
        return sum;
    }
}
