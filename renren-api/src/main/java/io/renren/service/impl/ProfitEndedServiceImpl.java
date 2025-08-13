package io.renren.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
            
            // 查询用户的所有投资记录
            QueryWrapper<InvestmentRecordEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId);
            List<InvestmentRecordEntity> investmentRecords = investmentRecordDao.selectList(queryWrapper);
            
            // 初始化统计数据
            long totalPrincipal = 0;        // 总本金
            long totalProfit = 0;           // 总收益
            long dsAmount = 0;              // 待收金额
            long dsbjAmount = 0;            // 待收本金
            long dslxAmount = 0;            // 待收利息
            long ysAmount = 0;              // 已收利息
            long ysbjAmount = 0;            // 已收本金
            long jrAmount = 0;              // 今日收益
            long jrProfit = 0;              // 今日收益（重复字段）
            long items = 0;                 // 项目数
            
            // 获取今日日期
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date todayStart = calendar.getTime();
            
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            Date todayEnd = calendar.getTime();
            
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
                            if (project.getStatus() == 0) {
                                // 进行中的项目：待收
                                if (record.getInvestmentAmount() != null) {
                                    dsbjAmount += record.getInvestmentAmount();
                                }
                                if (record.getProfitAmount() != null) {
                                    dslxAmount += record.getProfitAmount();
                                    dsAmount += record.getProfitAmount();
                                }
                            } else if (project.getStatus() == 1) {
                                // 已结束的项目：已收
                                if (record.getInvestmentAmount() != null) {
                                    ysbjAmount += record.getInvestmentAmount();
                                }
                                if (record.getProfitAmount() != null) {
                                    ysAmount += record.getProfitAmount();
                                }
                            }
                        }
                    }
                }
            }
            
            // 从账变记录查询今日收益
            jrAmount = getTodayProfitFromBalanceDetail(userId, todayStart, todayEnd);
            jrProfit = jrAmount;
            
            // 设置统计数据
            profitEndedDTO.setDsAmount(dsAmount);
            profitEndedDTO.setDsbjAmount(dsbjAmount);
            profitEndedDTO.setDslxAmount(dslxAmount);
            profitEndedDTO.setItems(items);
            profitEndedDTO.setJrAmount(jrAmount);
            profitEndedDTO.setJrProfit(jrProfit);
            profitEndedDTO.setTotalPrincipal(totalPrincipal);
            profitEndedDTO.setTotalProfit(totalProfit);
            profitEndedDTO.setYsAmount(ysAmount);
            profitEndedDTO.setYsbjAmount(ysbjAmount);
            
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
            
            // 查询投资中项目统计
            QueryWrapper<InvestmentRecordEntity> investmentQuery = new QueryWrapper<>();
            investmentQuery.eq("user_id", userId);
            List<InvestmentRecordEntity> investmentRecords = investmentRecordDao.selectList(investmentQuery);
            
            // 初始化统计数据
            long totalPrincipal = 0;        // 总本金
            long totalProfit = 0;           // 总收益
            long dsAmount = 0;              // 待收金额
            long dsbjAmount = 0;            // 待收本金
            long dslxAmount = 0;            // 待收利息
            long ysAmount = 0;              // 已收利息（投资中项目无已收）
            long ysbjAmount = 0;            // 已收本金（投资中项目无已收）
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
                                dsbjAmount += record.getInvestmentAmount(); // 待收本金
                            }
                            
                            if (record.getProfitAmount() != null) {
                                totalProfit += record.getProfitAmount();
                                dslxAmount += record.getProfitAmount(); // 待收利息
                                dsAmount += record.getProfitAmount();   // 待收金额
                            }
                        }
                    }
                }
            }
            
            // 从账变记录查询今日收益
            long jrAmount = getTodayProfitFromBalanceDetail(userId, todayStart, todayEnd);
            long jrProfit = jrAmount;
            
            // 设置统计数据
            profitEndedDTO.setDsAmount(dsAmount);
            profitEndedDTO.setDsbjAmount(dsbjAmount);
            profitEndedDTO.setDslxAmount(dslxAmount);
            profitEndedDTO.setItems(items);
            profitEndedDTO.setJrAmount(jrAmount);
            profitEndedDTO.setJrProfit(jrProfit);
            profitEndedDTO.setTotalPrincipal(totalPrincipal);
            profitEndedDTO.setTotalProfit(totalProfit);
            profitEndedDTO.setYsAmount(ysAmount);
            profitEndedDTO.setYsbjAmount(ysbjAmount);
            
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
            // 查询今日收益相关的账变记录
            QueryWrapper<UserBalanceDetailEntity> profitQuery = new QueryWrapper<>();
            profitQuery.eq("user_id", userId)
                      .in("busi_type", Arrays.asList(
                          BusinessTypeEnum.INCOME.getCode(),           // 收益
                          BusinessTypeEnum.COMMISSION_A.getCode(),     // 返佣A
                          BusinessTypeEnum.COMMISSION_B.getCode(),     // 返佣B
                          BusinessTypeEnum.PROJECT_COMMISSION_SELF.getCode(), // 项目返自己
                          BusinessTypeEnum.TASK_REWARD.getCode(),      // 任务奖励
                          BusinessTypeEnum.SIGN_IN_REWARD.getCode()    // 签到奖励
                      ))
                      .between("transaction_date", todayStart, todayEnd);
            
            List<UserBalanceDetailEntity> todayProfits = userBalanceDetailDao.selectList(profitQuery);
            
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
