package io.renren.modules.usdtrecord.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.page.PageData;
import io.renren.modules.usdtrecord.dao.UsdtRecordDao;
import io.renren.modules.usdtrecord.dto.UsdtRecordDTO;
import io.renren.modules.usdtrecord.entity.UsdtRecordEntity;
import io.renren.modules.usdtrecord.service.UsdtRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * USDT收款记录服务实现类
 *
 * @author renren
 * @since 2024-01-01
 */
@Service
@Slf4j
public class UsdtRecordServiceImpl extends ServiceImpl<UsdtRecordDao, UsdtRecordEntity> implements UsdtRecordService {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public PageData<UsdtRecordDTO> page(Map<String, Object> params) {
        // 获取分页参数，提供默认值
        long current = 1;
        long size = 10;
        
        if (params.get("page") != null) {
            try {
                current = Long.parseLong(params.get("page").toString());
            } catch (NumberFormatException e) {
                current = 1;
            }
        }
        
        if (params.get("limit") != null) {
            try {
                size = Long.parseLong(params.get("limit").toString());
            } catch (NumberFormatException e) {
                size = 10;
            }
        }
        
        // 构建查询条件
        QueryWrapper<UsdtRecordEntity> wrapper = buildQueryWrapper(params);
        
        // 创建分页对象
        Page<UsdtRecordEntity> page = new Page<>(current, size);
        
        // 执行分页查询
        IPage<UsdtRecordEntity> resultPage = baseMapper.selectPage(page, wrapper);
        
        // 转换为DTO并处理字段映射
        List<UsdtRecordDTO> dtoList = resultPage.getRecords().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        return new PageData<>(dtoList, resultPage.getTotal());
    }

    /**
     * 将实体转换为DTO，处理字段映射
     */
    private UsdtRecordDTO convertToDTO(UsdtRecordEntity entity) {
        UsdtRecordDTO dto = new UsdtRecordDTO();
        dto.setId(entity.getId());
        dto.setTransactionId(entity.getTransactionId());
        dto.setFromAddress(entity.getFromAddress());
        dto.setToAddress(entity.getToAddress());
        dto.setContractAddress(entity.getContractAddress());
        
        // 处理区块时间戳
        if (entity.getBlockTs() != null) {
            dto.setBlockTs(entity.getBlockTs().toString());
        }
        
        // 处理区块号
        if (entity.getBlockNumber() != null) {
            dto.setBlock(entity.getBlockNumber().toString());
        }
        
        // 处理金额字段映射：entity.amount -> dto.quant
        dto.setQuant(entity.getAmount());
        
        dto.setContractType(entity.getContractType());
        dto.setIsRisk(entity.getIsRisk());
        dto.setIsProcess(entity.getIsProcess());
        
        // 处理区块时间
        if (entity.getBlockTime() != null) {
            dto.setBlockTime(DATE_FORMAT.format(entity.getBlockTime()));
        }
        
        // 处理订单号字段映射：entity.orderNo -> dto.orderno
        dto.setOrderno(entity.getOrderNo());
        
        // 处理创建时间
        if (entity.getCreateDate() != null) {
            dto.setCreateDate(DATE_FORMAT.format(entity.getCreateDate()));
        }
        
        // 处理更新时间
        if (entity.getUpdateDate() != null) {
            dto.setUpdateDate(DATE_FORMAT.format(entity.getUpdateDate()));
        }
        
        return dto;
    }

    /**
     * 构建查询条件
     */
    private QueryWrapper<UsdtRecordEntity> buildQueryWrapper(Map<String, Object> params) {
        QueryWrapper<UsdtRecordEntity> wrapper = new QueryWrapper<>();
        
        // 转账地址筛选
        if (params.get("fromaddress") != null && StringUtils.isNotBlank(params.get("fromaddress").toString())) {
            wrapper.like("from_address", params.get("fromaddress").toString());
        }
        
        // 收款地址筛选
        if (params.get("toaddress") != null && StringUtils.isNotBlank(params.get("toaddress").toString())) {
            wrapper.like("to_address", params.get("toaddress").toString());
        }
        
        // 交易Hash筛选
        if (params.get("transactionId") != null && StringUtils.isNotBlank(params.get("transactionId").toString())) {
            wrapper.like("transaction_id", params.get("transactionId").toString());
        }
        
        // 处理状态筛选
        if (params.get("isprocess") != null) {
            try {
                Integer isProcess = Integer.parseInt(params.get("isprocess").toString());
                wrapper.eq("is_process", isProcess);
            } catch (NumberFormatException e) {
                // 忽略无效的状态值
            }
        }
        
        // 创建时间范围筛选
        if (params.get("createstarttime") != null) {
            try {
                Long startTime = Long.parseLong(params.get("createstarttime").toString());
                wrapper.ge("create_date", new Date(startTime));
            } catch (NumberFormatException e) {
                // 忽略无效的时间戳
            }
        }
        
        if (params.get("createendtime") != null) {
            try {
                Long endTime = Long.parseLong(params.get("createendtime").toString());
                wrapper.le("create_date", new Date(endTime));
            } catch (NumberFormatException e) {
                // 忽略无效的时间戳
            }
        }
        
        // 排序处理
        String orderField = (String) params.get("orderField");
        String order = (String) params.get("order");
        
        if (StringUtils.isNotBlank(orderField)) {
            if ("desc".equalsIgnoreCase(order)) {
                wrapper.orderByDesc(orderField);
            } else {
                wrapper.orderByAsc(orderField);
            }
        } else {
            // 默认按创建时间倒序排序
            wrapper.orderByDesc("create_date");
        }
        
        return wrapper;
    }

    @Override
    public boolean matchOrder(Long id, String orderno) {
        try {
            // 查询USDT记录是否存在
            UsdtRecordEntity entity = this.getById(id);
            if (entity == null) {
                return false;
            }
            
            // 更新订单号
            entity.setOrderNo(orderno);
            entity.setUpdateDate(new Date());
            
            // 保存更新
            boolean result = this.updateById(entity);
            
            return result;
        } catch (Exception e) {
            // 记录错误日志
            log.error("匹配订单失败，ID: {}, 订单号: {}, 错误: {}", id, orderno, e.getMessage(), e);
            return false;
        }
    }
}
