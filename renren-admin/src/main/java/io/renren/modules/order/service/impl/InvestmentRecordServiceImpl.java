package io.renren.modules.order.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.page.PageData;
import io.renren.modules.order.dao.InvestmentRecordDao;
import io.renren.modules.order.dto.InvestmentRecordDTO;
import io.renren.modules.order.entity.InvestmentRecordEntity;
import io.renren.modules.order.service.InvestmentRecordService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        
        // 创建分页对象
        Page<InvestmentRecordDTO> page = new Page<>(current, size);
        
        // 使用XML联表查询
        Page<InvestmentRecordDTO> resultPage = baseMapper.selectPageWithUser(page, params);
        
        // 计算统计数据
        Map<String, Object> sum = calculateSum(resultPage.getRecords());
        
        // 转换为PageData，包含list和sum
        return new PageData<>(resultPage.getRecords(), resultPage.getTotal(), sum);
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
