package io.renren.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.dao.UserBalanceDetailDao;
import io.renren.dao.UserDao;
import io.renren.dto.BalanceDetailDTO;
import io.renren.dto.BalanceDetailPageData;
import io.renren.entity.UserBalanceDetailEntity;
import io.renren.entity.UserEntity;
import io.renren.enums.BusinessTypeEnum;
import io.renren.service.BalanceDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 资金明细服务实现类
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Service
@Slf4j
public class BalanceDetailServiceImpl implements BalanceDetailService {

    @Autowired
    private UserBalanceDetailDao userBalanceDetailDao;
    @Autowired
    private UserDao userDao;

    @Override
    public BalanceDetailPageData getBalanceDetailPageData(Long userId, Integer page, Integer limit) {
        try {
            BalanceDetailPageData pageData = new BalanceDetailPageData();
            
            // 使用MyBatis-Plus分页查询
            Page<UserBalanceDetailEntity> pageParam = new Page<>(page, limit);
            
            // 构建查询条件
            QueryWrapper<UserBalanceDetailEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId)
                       .orderByDesc("transaction_date");
            
            // 执行分页查询
            Page<UserBalanceDetailEntity> result = userBalanceDetailDao.selectPage(pageParam, queryWrapper);
            
            // 转换为DTO
            List<BalanceDetailDTO> detailList = convertToDTOList(result.getRecords());
            
            // 计算汇总信息
            Map<String, Object> sum = calculateSum(detailList);
            
            // 设置分页数据
            pageData.setList(detailList);
            pageData.setSum(sum);
            pageData.setTotal((int) result.getTotal());
            
            return pageData;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取资金明细失败: " + e.getMessage());
        }
    }
    @Override
    public BalanceDetailPageData getAgentBalanceDetailPageData(Long userId, Integer page, Integer limit) {
        try {
            BalanceDetailPageData pageData = new BalanceDetailPageData();

            // 使用MyBatis-Plus分页查询
            Page<UserBalanceDetailEntity> pageParam = new Page<>(page, limit);

            // 构建查询条件
            QueryWrapper<UserBalanceDetailEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId)
                    .in("busi_type", Arrays.asList( 3, 30,22,14))
                    .orderByDesc("transaction_date");

            // 执行分页查询
            Page<UserBalanceDetailEntity> result = userBalanceDetailDao.selectPage(pageParam, queryWrapper);

            // 转换为DTO
            List<BalanceDetailDTO> detailList = convertToDTOList(result.getRecords());

            // 计算汇总信息
            Map<String, Object> sum = calculateSum(detailList);

            // 设置分页数据
            pageData.setList(detailList);
            pageData.setSum(sum);
            pageData.setTotal((int) result.getTotal());

            return pageData;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取资金明细失败: " + e.getMessage());
        }
    }

    /**
     * 将Entity转换为DTO
     */
    private List<BalanceDetailDTO> convertToDTOList(List<UserBalanceDetailEntity> entityList) {
        List<BalanceDetailDTO> dtoList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        for (UserBalanceDetailEntity entity : entityList) {
            BalanceDetailDTO dto = new BalanceDetailDTO();
            
            // 复制基本属性
            BeanUtils.copyProperties(entity, dto);
            
            // 处理字段名映射
            dto.setFormuserid(entity.getFormUserId());
            
            // 格式化日期字段
            if (entity.getTransactionDate() != null) {
                dto.setTransactionDate(sdf.format(entity.getTransactionDate()));
            }
            
            dtoList.add(dto);
        }
        
        return dtoList;
    }

    /**
     * 计算汇总信息
     */
    private Map<String, Object> calculateSum(List<BalanceDetailDTO> details) {
        Map<String, Object> sum = new HashMap<>();
        
        long totalOriginalAmount = 0;
        long totalUseAmount = 0;
        long totalTransactionAmount = 0;
        long totalYhqAmount = 0;
        int successCount = 0;
        int failedCount = 0;
        
        for (BalanceDetailDTO detail : details) {
            totalOriginalAmount += detail.getOriginalAmount() != null ? detail.getOriginalAmount() : 0;
            totalUseAmount += detail.getUseAmount() != null ? detail.getUseAmount() : 0;
            totalTransactionAmount += detail.getTransactionAmount() != null ? detail.getTransactionAmount() : 0;
            totalYhqAmount += detail.getYhqAmount() != null ? detail.getYhqAmount() : 0;
            
            if (detail.getStatus() != null && detail.getStatus() == 1) {
                successCount++;
            } else {
                failedCount++;
            }
        }
        
        sum.put("totalOriginalAmount", totalOriginalAmount);
        sum.put("totalUseAmount", totalUseAmount);
        sum.put("totalTransactionAmount", totalTransactionAmount);
        sum.put("totalYhqAmount", totalYhqAmount);
        sum.put("successCount", successCount);
        sum.put("failedCount", failedCount);
        sum.put("totalCount", details.size());
        
        return sum;
    }

    @Override
    public boolean recordReferralReward(Long currentAssets,Long assets,Long userId, Long amount, Long newUserId) {
        try {
            UserBalanceDetailEntity detail = new UserBalanceDetailEntity();
            detail.setBusiType(14);
            // 设置基本信息
            detail.setUserId(userId);
            detail.setOriginalAmount(currentAssets);
            detail.setUseAmount(amount);
            detail.setTransactionAmount(assets);
            detail.setStatus(1); // 成功状态
            // 设置交易时间
            detail.setTransactionDate(new Date());
            detail.setCreateDate(new Date());
            detail.setUpdateDate(new Date());
            detail.setRemarks("推荐用户注册成功，获得返利");
            
            // 设置关联用户ID（新注册用户）
            if (newUserId != null) {
                detail.setFormUserId(newUserId);
            }
            
            // 生成流水ID
            String streamId =  userId.toString();
            detail.setStreamId(streamId);
            
            int result = userBalanceDetailDao.insert(detail);
            
            if (result > 0) {
                log.info("记录推荐返利流水成功，用户ID: {}, 返利金额: {} 分，新用户ID: {}, 流水ID: {}", 
                    userId, amount, newUserId, streamId);
                return true;
            } else {
                log.error("记录推荐返利流水失败，用户ID: {}, 返利金额: {} 分", userId, amount);
                return false;
            }
            
        } catch (Exception e) {
            log.error("记录推荐返利流水异常，用户ID: {}, 返利金额: {} 分", userId, amount, e);
            return false;
        }
    }

    @Override
    public boolean recordChargeSuccess(Long userId, Long amount, String orderNo, String channel, String thirdOrderNo) {
        try {
            log.info("记录充值成功流水，用户ID: {}, 金额: {} 分，订单号: {}, 通道: {}, 第三方订单号: {}", 
                    userId, amount, orderNo, channel, thirdOrderNo);
            
            // 获取用户当前余额
            Long currentAssets = getUserCurrentAssets(userId);
            
            UserBalanceDetailEntity detail = new UserBalanceDetailEntity();
            
            // 设置基本信息
            detail.setUserId(userId);
            detail.setOriginalAmount(currentAssets);
            detail.setUseAmount(amount);
            detail.setTransactionAmount(currentAssets + amount);
            detail.setStatus(1); // 成功状态
            
            detail.setBusiType(BusinessTypeEnum.ONLINE_RECHARGE.getCode());
            
            // 设置交易时间
            detail.setTransactionDate(new Date());
            detail.setCreateDate(new Date());
            detail.setUpdateDate(new Date());
            
            // 设置备注信息
            detail.setRemarks(String.format("充值成功【%s】", channel != null ? channel : "未知通道"));
            
            // 设置订单相关信息
            detail.setStreamId(orderNo); // 使用订单号作为流水ID
            
            // 设置通道信息
            detail.setChannel(channel);
            
            // 设置第三方订单号到备注中
            if (thirdOrderNo != null && !thirdOrderNo.isEmpty()) {
                detail.setRemarks(detail.getRemarks() + "，第三方订单：" + thirdOrderNo);
            }
            
            int result = userBalanceDetailDao.insert(detail);
            
            if (result > 0) {
                log.info("记录充值成功流水成功，用户ID: {}, 金额: {} 分，订单号: {}, 流水ID: {}", 
                    userId, amount, orderNo, orderNo);
                return true;
            } else {
                log.error("记录充值成功流水失败，用户ID: {}, 金额: {} 分，订单号: {}", userId, amount, orderNo);
                return false;
            }
            
        } catch (Exception e) {
            log.error("记录充值成功流水异常，用户ID: {}, 金额: {} 分，订单号: {}", userId, amount, orderNo, e);
            return false;
        }
    }


    /**
     * 获取用户当前可用余额
     * 
     * @param userId 用户ID
     * @return 当前余额
     */
    private Long getUserCurrentAssets(Long userId) {
        try {
            // 获取推荐人信息
            UserEntity userEntity = userDao.selectById(userId);
            if (userEntity == null) {
                return userEntity.getAssets();
            }
            return 0L;
        } catch (Exception e) {
            log.warn("获取用户当前余额失败，用户ID: {}", userId, e);
            return 0L;
        }
    }
}
