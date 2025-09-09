package io.renren.service.impl;

import io.renren.common.exception.ErrorCode;
import io.renren.common.exception.RenException;
import io.renren.dao.InvestmentRecordDao;
import io.renren.dao.UserBalanceDetailDao;
import io.renren.dto.ProfitEndedDTO;
import io.renren.service.ProfitEndedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * 付息还本服务实现类
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Service
public class ProfitEndedServiceImpl implements ProfitEndedService {

    @Autowired
    private InvestmentRecordDao investmentRecordDao;


    @Autowired
    private UserBalanceDetailDao userBalanceDetailDao;

    @Override
    public ProfitEndedDTO getProfitEndedRecord(Long userId) {
        try {
            ProfitEndedDTO profitEndedDTO = new ProfitEndedDTO();
            
            // 直接使用SQL统计查询，避免分页和循环查询
            Map<String, Object> statistics = investmentRecordDao.getProfitEndedStatistics(userId);
            
            // 设置统计数据
            profitEndedDTO.setItems(((Number) statistics.get("items")).longValue());
            profitEndedDTO.setTotalPrincipal(((Number) statistics.get("totalPrincipal")).longValue());
            profitEndedDTO.setTotalProfit(((Number) statistics.get("totalProfit")).longValue());
            
            return profitEndedDTO;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RenException(ErrorCode.GET_PROFIT_ENDED_RECORD_FAILED);
        }
    }

    @Override
    public ProfitEndedDTO getProfitInvestingRecord(Long userId) {
        try {
            ProfitEndedDTO profitEndedDTO = new ProfitEndedDTO();
            
            // 获取今日日期
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date todayStart = calendar.getTime();
            
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            Date todayEnd = calendar.getTime();
            
            // 直接使用SQL统计查询投资中项目，避免分页和循环查询
            Map<String, Object> statistics = investmentRecordDao.getProfitInvestingStatistics(userId);
            
            // 从账变记录查询今日收益
            long jrAmount = getTodayProfitFromBalanceDetail(userId, todayStart, todayEnd);
            long jrProfit = jrAmount;
            
            // 设置统计数据
            profitEndedDTO.setItems(((Number) statistics.get("items")).longValue());
            profitEndedDTO.setJrProfit(jrProfit);
            profitEndedDTO.setTotalPrincipal(((Number) statistics.get("totalPrincipal")).longValue());
            profitEndedDTO.setTotalProfit(((Number) statistics.get("totalProfit")).longValue());
            
            return profitEndedDTO;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RenException(ErrorCode.GET_INVESTMENT_PROJECT_STATS_FAILED);
        }
    }

    /**
     * 从账变记录查询今日收益
     */
    private long getTodayProfitFromBalanceDetail(Long userId, Date todayStart, Date todayEnd) {
        try {
            // 直接使用SQL统计查询今日收益，避免分页查询
            return userBalanceDetailDao.getTodayProfitAmount(userId, todayStart, todayEnd);
            
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }
}
