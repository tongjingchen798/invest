package io.renren.modules.order.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.page.PageData;
import io.renren.modules.order.dao.InvestmentRecordDao;
import io.renren.modules.order.dto.InvestmentRecordDTO;
import io.renren.modules.order.entity.InvestmentRecordEntity;
import io.renren.modules.order.service.InvestmentRecordService;
import io.renren.modules.security.user.SecurityUser;
import io.renren.modules.security.user.UserDetail;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 购买记录
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Service
public class InvestmentRecordServiceImpl extends ServiceImpl<InvestmentRecordDao, InvestmentRecordEntity> implements InvestmentRecordService {

    /**
     * 分页查询购买记录
     */
    @Override
    public PageData<InvestmentRecordDTO> selectPage(Map<String, Object> params) {
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
        
        // 添加权限控制参数
        UserDetail user = SecurityUser.getUser();
        if (user != null) {
            params.put("currentUserId", user.getId());
            params.put("currentUserType", user.getType());
        }
        
        // 创建分页对象
        Page<InvestmentRecordDTO> page = new Page<>(current, size);
        
        // 使用XML联表查询
        Page<InvestmentRecordDTO> resultPage = baseMapper.selectPageWithUser(page, params);
        
        // 获取所有记录
        List<InvestmentRecordDTO> records = resultPage.getRecords();
        
        // 为每条记录设置historygm字段
        setHistorygmForRecords(records);
        
        // 计算统计数据
        Map<String, Object> sum = calculateSum(records);
        
        // 转换为PageData，包含list和sum
        return new PageData<>(records, resultPage.getTotal(), sum);
    }
    
    /**
     * 为记录设置historygm字段
     */
    private void setHistorygmForRecords(List<InvestmentRecordDTO> records) {
        if (records == null || records.isEmpty()) {
            return;
        }
        
        // 按用户ID分组，获取每个用户的所有投资记录
        Map<Long, List<InvestmentRecordDTO>> userRecordsMap = records.stream()
                .collect(Collectors.groupingBy(record -> {
                    try {
                        return Long.parseLong(record.getUserId().toString());
                    } catch (Exception e) {
                        return 0L;
                    }
                }));
        
        // 为每条记录设置historygm
        for (InvestmentRecordDTO record : records) {
            try {
                Long userId = Long.parseLong(record.getUserId().toString());
                List<InvestmentRecordDTO> userRecords = userRecordsMap.get(userId);
                
                if (userRecords != null && userRecords.size() > 1) {
                    // 构建historygm JSON字符串
                    String historygm = buildHistorygmJson(userRecords, record.getOrderId());
                    record.setHistorygm(historygm);
                } else {
                    // 如果没有其他记录，设置为空数组
                    record.setHistorygm("[]");
                }
            } catch (Exception e) {
                record.setHistorygm("[]");
            }
        }
    }
    
    /**
     * 构建historygm JSON字符串
     */
    private String buildHistorygmJson(List<InvestmentRecordDTO> userRecords, Long currentOrderId) {
        StringBuilder jsonBuilder = new StringBuilder("[");
        
        boolean first = true;
        for (InvestmentRecordDTO userRecord : userRecords) {
            // 跳过当前记录
            if (userRecord.getOrderId() != null && userRecord.getOrderId().equals(currentOrderId)) {
                continue;
            }
            
            if (!first) {
                jsonBuilder.append(",");
            }
            
            jsonBuilder.append("{");
            jsonBuilder.append("\"abbreviation\":\"").append(userRecord.getAbbreviation() != null ? userRecord.getAbbreviation() : "").append("\",");
            jsonBuilder.append("\"expiration_time\":\"").append(getExpirationTimeStatus(userRecord.getExpirationTime())).append("\",");
            jsonBuilder.append("\"invest_count\":").append(userRecord.getInvestCount() != null ? userRecord.getInvestCount() : 1).append(",");
            jsonBuilder.append("\"sygmfs\":").append(userRecord.getInvestCount() != null ? userRecord.getInvestCount() : 1);
            jsonBuilder.append("}");
            
            first = false;
        }
        
        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }
    
    /**
     * 获取到期时间状态
     */
    private String getExpirationTimeStatus(String expirationTime) {
        if (expirationTime == null || expirationTime.trim().isEmpty()) {
            return "未到期";
        }
        
        try {
            // 解析到期时间格式：2025-08-18 23:24:01
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            java.time.LocalDateTime expirationDateTime = java.time.LocalDateTime.parse(expirationTime.trim(), formatter);
            
            // 获取当前时间
            java.time.LocalDateTime currentDateTime = java.time.LocalDateTime.now();
            
            // 比较时间
            if (expirationDateTime.isAfter(currentDateTime)) {
                return "未到期";
            } else {
                return "已到期";
            }
        } catch (Exception e) {
            // 如果解析失败，返回"未到期"
            return "未到期";
        }
    }
    
    /**
     * 计算统计数据
     */
    private Map<String, Object> calculateSum(List<InvestmentRecordDTO> records) {
        Map<String, Object> sum = new HashMap<>();
        
        long totalActualAmount = 0;
        int totalInvestCount = 0;
        
        for (InvestmentRecordDTO record : records) {
            if (record.getActualAmount() != null) {
                totalActualAmount += record.getActualAmount();
            }
            if (record.getInvestCount() != null) {
                totalInvestCount += record.getInvestCount();
            }
        }
        
        sum.put("actualAmount", totalActualAmount);
        sum.put("investCount", totalInvestCount);
        
        return sum;
    }
}
