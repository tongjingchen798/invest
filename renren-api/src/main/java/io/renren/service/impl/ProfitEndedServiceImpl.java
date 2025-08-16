package io.renren.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.dao.InvestmentRecordDao;
import io.renren.dao.ProjectDao;
import io.renren.dao.UserBalanceDetailDao;
import io.renren.dto.ProfitEndedDTO;
import io.renren.entity.InvestmentRecordEntity;
import io.renren.entity.ProjectEntity;
import io.renren.entity.UserBalanceDetailEntity;
import io.renren.enums.BusinessTypeEnum;
import io.renren.service.ProfitEndedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    private ProjectDao projectDao;

    @Autowired
    private UserBalanceDetailDao userBalanceDetailDao;

    @Override
    public ProfitEndedDTO getProfitEndedRecord(Long userId) {
        try {
            ProfitEndedDTO profitEndedDTO = new ProfitEndedDTO();
            
            // 使用分页查询优化大数据量场景
            Page<InvestmentRecordEntity> page = new Page<>(1, 1000); // 设置较大的页面大小
            QueryWrapper<InvestmentRecordEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId)
                       .orderByDesc("create_time");
            
            Page<InvestmentRecordEntity> result = investmentRecordDao.selectPage(page, queryWrapper);
            List<InvestmentRecordEntity> investmentRecords = result.getRecords();
            
            // 初始化统计数据
            long totalPrincipal = 0;        // 总本金
            long totalProfit = 0;           // 总收益
            long items = 0;                 // 项目数
            
            for (InvestmentRecordEntity record : investmentRecords) {
                if (record.getInvestmentAmount() != null) {
                    totalPrincipal += record.getInvestmentAmount();
                }
                
                // 根据项目状态分类统计
                if (record.getProjectId() != null) {
                    ProjectEntity project = projectDao.selectById(record.getProjectId());
                    if (project != null) {
                        items++; // 项目数统计
                        
                        // 根据项目状态和收益计算
                        if (record.getProfitAmount() != null) {
                            totalProfit += record.getProfitAmount();
                        }
                        
                        // 项目状态：0-进行中，1-已结束
                        if (project.getStatus() != null) {
                                if (record.getInvestmentAmount() != null) {
                                    totalPrincipal += record.getInvestmentAmount();
                                }
                                if (record.getProfitAmount() != null) {
                                    totalProfit += record.getProfitAmount();
                                }
                        }
                    }
                }
            }
            
            // 设置统计数据
            profitEndedDTO.setItems(items);
            profitEndedDTO.setTotalPrincipal(totalPrincipal);
            profitEndedDTO.setTotalProfit(totalProfit);
            return profitEndedDTO;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取付息还本记录失败: " + e.getMessage());
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
            
            // 使用分页查询优化投资中项目统计
            Page<InvestmentRecordEntity> page = new Page<>(1, 1000); // 设置较大的页面大小
            QueryWrapper<InvestmentRecordEntity> investmentQuery = new QueryWrapper<>();
            investmentQuery.eq("user_id", userId)
                          .orderByDesc("create_date");
            
            Page<InvestmentRecordEntity> result = investmentRecordDao.selectPage(page, investmentQuery);
            List<InvestmentRecordEntity> investmentRecords = result.getRecords();
            
            // 初始化统计数据
            long totalPrincipal = 0;        // 总本金
            long totalProfit = 0;           // 总收益
            long items = 0;                 // 项目数
            
            for (InvestmentRecordEntity record : investmentRecords) {
                // 根据项目状态分类统计
                if (record.getProjectId() != null) {
                    ProjectEntity project = projectDao.selectById(record.getProjectId());
                    if (project != null) {
                        // 只统计投资中的项目（状态为0）
                        if (project.getStatus() != null && project.getStatus() == 0) {
                            items++; // 项目数统计
                            
                            if (record.getInvestmentAmount() != null) {
                                totalPrincipal += record.getInvestmentAmount();
                            }
                            
                            if (record.getProfitAmount() != null) {
                                totalProfit += record.getProfitAmount();
                            }
                        }
                    }
                }
            }
            
            // 从账变记录查询今日收益
            long jrAmount = getTodayProfitFromBalanceDetail(userId, todayStart, todayEnd);
            long jrProfit = jrAmount;
            
            // 设置统计数据
            profitEndedDTO.setItems(items);
            profitEndedDTO.setJrProfit(jrProfit);
            profitEndedDTO.setTotalPrincipal(totalPrincipal);
            profitEndedDTO.setTotalProfit(totalProfit);
            return profitEndedDTO;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取投资中项目统计失败: " + e.getMessage());
        }
    }

    /**
     * 从账变记录查询今日收益
     */
    private long getTodayProfitFromBalanceDetail(Long userId, Date todayStart, Date todayEnd) {
        try {
            // 使用分页查询优化账变记录查询
            Page<UserBalanceDetailEntity> page = new Page<>(1, 1000); // 设置较大的页面大小
            QueryWrapper<UserBalanceDetailEntity> profitQuery = new QueryWrapper<>();
            profitQuery.eq("user_id", userId)
                      .in("business_type", Arrays.asList(
                          BusinessTypeEnum.INCOME.getCode()
                      ))
                      .between("transaction_date", todayStart, todayEnd)
                      .orderByDesc("transaction_date");
            
            Page<UserBalanceDetailEntity> result = userBalanceDetailDao.selectPage(page, profitQuery);
            List<UserBalanceDetailEntity> todayProfits = result.getRecords();
            
            // 计算今日收益总额
            return todayProfits.stream()
                .mapToLong(record -> record.getTransactionAmount() != null ? record.getTransactionAmount() : 0)
                .sum();
                
        } catch (Exception e) {
            e.printStackTrace();
            return 0L; // 查询失败时返回0
        }
    }
}
