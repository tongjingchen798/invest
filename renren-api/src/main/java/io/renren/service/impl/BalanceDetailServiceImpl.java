package io.renren.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.dao.UserBalanceDetailDao;
import io.renren.dto.BalanceDetailDTO;
import io.renren.dto.BalanceDetailPageData;
import io.renren.entity.UserBalanceDetailEntity;
import io.renren.service.BalanceDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class BalanceDetailServiceImpl implements BalanceDetailService {

    @Autowired
    private UserBalanceDetailDao userBalanceDetailDao;

    @Override
    public BalanceDetailPageData getBalanceDetailPageData(Long userId, Integer page, Integer limit) {
        try {
            BalanceDetailPageData pageData = new BalanceDetailPageData();
            
            // 使用MyBatis-Plus分页查询
            Page<UserBalanceDetailEntity> pageParam = new Page<>(page, limit);
            
            // 构建查询条件
            QueryWrapper<UserBalanceDetailEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId)
                       .orderByDesc("transaction_date", "create_time");
            
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
}
