package io.renren.modules.transfer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.common.constant.Constant;
import io.renren.common.service.impl.BaseServiceImpl;
import io.renren.modules.transfer.dao.ManualTransferDao;
import io.renren.modules.transfer.dto.ManualTransferDTO;
import io.renren.modules.transfer.dto.ManualTransferPageData;
import io.renren.modules.transfer.entity.ManualTransferEntity;
import io.renren.modules.transfer.service.ManualTransferService;
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
 * 人工转帐服务实现类
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Slf4j
@Service("manualTransferService")
public class ManualTransferServiceImpl extends BaseServiceImpl<ManualTransferDao, ManualTransferEntity> implements ManualTransferService {

    @Autowired
    private ManualTransferDao manualTransferDao;

    @Override
    public ManualTransferPageData getManualTransferPage(Map<String, Object> params) {
        try {
            // 获取分页参数
            Integer page = Integer.parseInt(params.get(Constant.PAGE).toString());
            Integer limit = Integer.parseInt(params.get(Constant.LIMIT).toString());
            
            // 创建分页对象
            Page<ManualTransferEntity> pageParam = new Page<>(page, limit);
            
            // 构建查询条件
            QueryWrapper<ManualTransferEntity> queryWrapper = new QueryWrapper<>();
            
            // 时间范围筛选
            if (params.get("startTime") != null) {
                Long startTime = Long.parseLong(params.get("startTime").toString());
                queryWrapper.ge("create_time", new Date(startTime));
            }
            
            if (params.get("endTime") != null) {
                Long endTime = Long.parseLong(params.get("endTime").toString());
                queryWrapper.le("create_time", new Date(endTime));
            }
            
            // 平台订单号筛选
            if (params.get("orderno") != null && StringUtils.isNotBlank(params.get("orderno").toString())) {
                queryWrapper.like("orderno", params.get("orderno"));
            }
            
            // 卡号筛选
            if (params.get("pay_no") != null && StringUtils.isNotBlank(params.get("pay_no").toString())) {
                queryWrapper.like("pay_no", params.get("pay_no"));
            }
            
            // 状态筛选
            if (params.get("state") != null) {
                Integer state = Integer.parseInt(params.get("state").toString());
                queryWrapper.eq("state", state);
            }
            
            // 三方订单号筛选
            if (params.get("threeorder_no") != null && StringUtils.isNotBlank(params.get("threeorder_no").toString())) {
                queryWrapper.like("threeorder_no", params.get("threeorder_no"));
            }
            
            // 提现类型筛选
            if (params.get("withdraw_type") != null && StringUtils.isNotBlank(params.get("withdraw_type").toString())) {
                Integer withdrawType = Integer.parseInt(params.get("withdraw_type").toString());
                queryWrapper.eq("withdraw_type", withdrawType);
            }
            
            // 排序处理
            if (params.get(Constant.ORDER_FIELD) != null && params.get(Constant.ORDER) != null) {
                String orderField = params.get(Constant.ORDER_FIELD).toString();
                String order = params.get(Constant.ORDER).toString();
                if ("desc".equalsIgnoreCase(order)) {
                    queryWrapper.orderByDesc(orderField);
                } else {
                    queryWrapper.orderByAsc(orderField);
                }
            } else {
                // 默认按创建时间倒序
                queryWrapper.orderByDesc("create_time");
            }
            
            // 执行分页查询
            Page<ManualTransferEntity> result = manualTransferDao.selectPage(pageParam, queryWrapper);
            
            // 转换为DTO
            List<ManualTransferDTO> dtoList = result.getRecords().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            
            // 计算汇总数据
            Map<String, Object> sum = calculateSum(result.getRecords());
            
            // 构建分页数据
            ManualTransferPageData pageData = new ManualTransferPageData();
            pageData.setList(dtoList);
            pageData.setTotal((int) result.getTotal());
            pageData.setSum(sum);
            
            return pageData;
            
        } catch (Exception e) {
            log.error("查询人工转帐列表失败", e);
            throw new RuntimeException("查询人工转帐列表失败: " + e.getMessage());
        }
    }

    @Override
    public ManualTransferEntity getManualTransferByOrderno(String orderno) {
        QueryWrapper<ManualTransferEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("orderno", orderno);
        return manualTransferDao.selectOne(queryWrapper);
    }

    @Override
    public ManualTransferEntity getManualTransferByThreeOrderNo(String threeorderNo) {
        QueryWrapper<ManualTransferEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("threeorder_no", threeorderNo);
        return manualTransferDao.selectOne(queryWrapper);
    }

    @Override
    public List<ManualTransferEntity> getManualTransfersByState(Integer state) {
        QueryWrapper<ManualTransferEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state", state);
        return manualTransferDao.selectList(queryWrapper);
    }

    @Override
    public List<ManualTransferEntity> getManualTransfersByParams(Map<String, Object> params) {
        QueryWrapper<ManualTransferEntity> queryWrapper = new QueryWrapper<>();
        // 根据参数构建查询条件
        if (params.get("state") != null) {
            queryWrapper.eq("state", params.get("state"));
        }
        if (params.get("withdraw_type") != null) {
            queryWrapper.eq("withdraw_type", params.get("withdraw_type"));
        }
        return manualTransferDao.selectList(queryWrapper);
    }

    @Override
    public Long getTotalManualTransferAmountByUserId(String userId) {
        QueryWrapper<ManualTransferEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state", 2); // 只统计已完成的
        List<ManualTransferEntity> transfers = manualTransferDao.selectList(queryWrapper);
        return transfers.stream()
                .mapToLong(transfer -> transfer.getAmount() != null ? transfer.getAmount() : 0L)
                .sum();
    }

    /**
     * 转换为DTO
     */
    private ManualTransferDTO convertToDTO(ManualTransferEntity entity) {
        ManualTransferDTO dto = new ManualTransferDTO();
        
        dto.setId(entity.getId());
        dto.setMsg(entity.getMsg());
        dto.setWithdrawTime(formatDate(entity.getWithdrawTime()));
        dto.setAmount(entity.getAmount() != null ? new java.math.BigDecimal(entity.getAmount()) : null);
        dto.setBlankCode(entity.getBlankCode());
        dto.setBlankName(entity.getBlankName());
        dto.setPayName(entity.getPayName());
        dto.setPayNo(entity.getPayNo());
        dto.setRemark(entity.getRemark());
        dto.setCreateTime(formatDate(entity.getCreateTime()));
        dto.setStateTime(formatDate(entity.getStateTime()));
        dto.setState(entity.getState());
        dto.setWithdrawType(entity.getWithdrawType());
        dto.setOrderno(entity.getOrderno());
        dto.setThreeorderNo(entity.getThreeorderNo());
        dto.setIfsc(entity.getIfsc());
        dto.setAgentName(entity.getAgentName());
        dto.setAgent(entity.getAgent());
        dto.setSalesmanid(entity.getSalesmanid());
        dto.setSalesmanName(entity.getSalesmanName());
        dto.setChannelid(entity.getChannelid());
        dto.setMerchantid(entity.getMerchantid());
        dto.setMerchantname(entity.getMerchantname());
        dto.setUpdateTime(formatDate(entity.getUpdateTime()));
        dto.setOperCode(entity.getOperCode());
        dto.setTransferSource(entity.getTransferSource());
        dto.setHandFee(entity.getHandFee() != null ? new java.math.BigDecimal(entity.getHandFee()) : null);
        dto.setRealAmount(entity.getRealAmount() != null ? new java.math.BigDecimal(entity.getRealAmount()) : null);
        dto.setChannelAmount(entity.getChannelAmount() != null ? new java.math.BigDecimal(entity.getChannelAmount()) : null);
        
        return dto;
    }

    /**
     * 计算汇总数据
     */
    private Map<String, Object> calculateSum(List<ManualTransferEntity> records) {
        Map<String, Object> sum = new HashMap<>();
        
        long totalAmount = 0;
        long totalRealAmount = 0;
        long totalChannelAmount = 0;
        long totalHandFee = 0;
        
        for (ManualTransferEntity entity : records) {
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
