package io.renren.modules.withdraw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.common.constant.Constant;
import io.renren.common.service.impl.BaseServiceImpl;
import io.renren.modules.withdraw.dao.WithdrawOrderDao;
import io.renren.modules.withdraw.dto.ManualWithdrawDTO;
import io.renren.modules.withdraw.dto.ManualWithdrawPageData;
import io.renren.modules.withdraw.entity.WithdrawOrderEntity;
import io.renren.modules.withdraw.service.ManualWithdrawService;
import io.renren.modules.security.user.SecurityUser;
import io.renren.modules.security.user.UserDetail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 手工提现服务实现类
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Slf4j
@Service("manualWithdrawService")
public class ManualWithdrawServiceImpl extends BaseServiceImpl<WithdrawOrderDao, WithdrawOrderEntity> implements ManualWithdrawService {

    @Autowired
    private WithdrawOrderDao withdrawOrderDao;

    @Override
    public ManualWithdrawPageData getManualWithdrawPage(Map<String, Object> params) {
        try {
            // 获取分页参数
            Integer page = Integer.parseInt(params.get(Constant.PAGE).toString());
            Integer limit = Integer.parseInt(params.get(Constant.LIMIT).toString());
            
            // 添加权限控制参数
            UserDetail user = SecurityUser.getUser();
            if (user != null) {
                params.put("currentUserId", user.getId());
                params.put("currentUserType", user.getType());
            }
            
            // 创建分页对象
            Page<ManualWithdrawDTO> pageParam = new Page<>(page, limit);
            
            // 使用自定义分页查询
            IPage<ManualWithdrawDTO> result = withdrawOrderDao.selectManualWithdrawPage(pageParam, params);
            
            // 统计汇总数据
            Map<String, Object> summaryData = withdrawOrderDao.selectManualWithdrawSummary(params);
            
            // 构建分页数据
            ManualWithdrawPageData pageData = new ManualWithdrawPageData();
            pageData.setList(result.getRecords());
            pageData.setTotal((int) result.getTotal());
            pageData.setSum(summaryData);
            
            return pageData;
            
        } catch (Exception e) {
            log.error("查询手工提现列表失败", e);
            throw new RuntimeException("查询手工提现列表失败: " + e.getMessage());
        }
    }

    @Override
    public List<WithdrawOrderEntity> getWithdrawOrdersByUserId(String userId) {
        QueryWrapper<WithdrawOrderEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return withdrawOrderDao.selectList(queryWrapper);
    }

    @Override
    public WithdrawOrderEntity getWithdrawOrderByOrderno(String orderno) {
        QueryWrapper<WithdrawOrderEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("orderno", orderno);
        return withdrawOrderDao.selectOne(queryWrapper);
    }

    @Override
    public WithdrawOrderEntity getWithdrawOrderByThreeOrderNo(String threeorderNo) {
        QueryWrapper<WithdrawOrderEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("threeorder_no", threeorderNo);
        return withdrawOrderDao.selectOne(queryWrapper);
    }

    @Override
    public List<WithdrawOrderEntity> getWithdrawOrdersByState(Integer state) {
        QueryWrapper<WithdrawOrderEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state", state);
        return withdrawOrderDao.selectList(queryWrapper);
    }

    @Override
    public List<WithdrawOrderEntity> getWithdrawOrdersByParams(Map<String, Object> params) {
        QueryWrapper<WithdrawOrderEntity> queryWrapper = new QueryWrapper<>();
        // 根据参数构建查询条件
        if (params.get("userId") != null) {
            queryWrapper.eq("user_id", params.get("userId"));
        }
        if (params.get("state") != null) {
            queryWrapper.eq("state", params.get("state"));
        }
        return withdrawOrderDao.selectList(queryWrapper);
    }

    @Override
    public Long getTotalWithdrawAmountByUserId(String userId) {
        QueryWrapper<WithdrawOrderEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("state", 2); // 只统计已提现的
        List<WithdrawOrderEntity> orders = withdrawOrderDao.selectList(queryWrapper);
        return orders.stream()
                .mapToLong(order -> order.getAmount() != null ? order.getAmount() : 0L)
                .sum();
    }

    /**
     * 转换为DTO
     */
    private ManualWithdrawDTO convertToDTO(WithdrawOrderEntity entity) {
        ManualWithdrawDTO dto = new ManualWithdrawDTO();
        
        dto.setAgent(entity.getAgent());
        dto.setAgentName(entity.getAgentName());
        dto.setAmount(entity.getAmount() != null ? new java.math.BigDecimal(entity.getAmount()) : null);
        dto.setBlankCode(entity.getBlankCode());
        dto.setBlankName(entity.getBlankName());
        dto.setChannelid(entity.getChannelid() != null ? Long.parseLong(entity.getChannelid()) : null);
        dto.setCreateTime(formatDate(entity.getCreateTime()));
        dto.setId(Long.parseLong(entity.getId()));
        dto.setIfsc(entity.getIfsc());
        dto.setMerchantid(entity.getMerchantid() != null ? Long.parseLong(entity.getMerchantid()) : null);
        dto.setMerchantname(entity.getMerchantname());
        dto.setMsg(entity.getMsg());
        dto.setOrderno(entity.getOrderno());
        dto.setPayName(entity.getPayName());
        dto.setPayNo(entity.getPayNo());
        dto.setRemark(entity.getRemark());
        dto.setSalesmanName(entity.getSalesmanName());
        dto.setSalesmanid(entity.getSalesmanid());
        dto.setState(entity.getState());
        dto.setStateTime(formatDate(entity.getStateTime()));
        dto.setThreeorderNo(entity.getThreeorderNo());
        dto.setWithdrawTime(formatDate(entity.getWithdrawTime()));
        dto.setWithdrawType(entity.getWithdrawType());
        
        // 设置汇总相关字段
        dto.setChannelAmount(entity.getChannelAmount() != null ? new java.math.BigDecimal(entity.getChannelAmount()) : null);
        dto.setRealAmount(entity.getRealAmount() != null ? new java.math.BigDecimal(entity.getRealAmount()) : null);
        dto.setHandFee(entity.getHandFee() != null ? new java.math.BigDecimal(entity.getHandFee()) : null);
        
        return dto;
    }

    /**
     * 计算汇总数据
     */
    private Map<String, Object> calculateSum(List<WithdrawOrderEntity> records) {
        Map<String, Object> sum = new HashMap<>();
        
        long totalAmount = 0;
        long totalRealAmount = 0;
        long totalChannelAmount = 0;
        long totalHandFee = 0;
        
        for (WithdrawOrderEntity entity : records) {
            if (entity.getAmount() != null) {
                totalAmount += entity.getAmount();
            }
            if (entity.getRealAmount() != null) {
                totalRealAmount += entity.getRealAmount();
            }
            if (entity.getChannelAmount() != null) {
                totalChannelAmount += entity.getChannelAmount();
            }
            if (entity.getHandFee() != null) {
                totalHandFee += entity.getHandFee();
            }
        }
        
        sum.put("amount", totalAmount);
        sum.put("realAmount", totalRealAmount);
        sum.put("channelAmount", totalChannelAmount);
        sum.put("handFee", totalHandFee);
        
        return sum;
    }

    /**
     * 格式化日期
     */
    private String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
