package io.renren.utils;

import io.renren.entity.ProjectEntity;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;

/**
 * 投资收益计算工具类
 *
 * @author renren
 * @since 1.0.0
 */
@Slf4j
public class InvestmentProfitCalculator {

    /**
     * 计算收益结束时间
     * 
     * @param orderDate 投资日期
     * @param cycle 投资周期（天）
     * @return 收益结束时间
     */
    public static Date calculateProfitEndDate(Date orderDate, Integer cycle) {
        if (orderDate == null || cycle == null || cycle <= 0) {
            log.warn("投资日期或周期无效，orderDate: {}, cycle: {}", orderDate, cycle);
            return orderDate;
        }

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(orderDate);
            calendar.add(Calendar.DAY_OF_MONTH, cycle);
            
            Date endDate = calendar.getTime();
            log.debug("计算收益结束时间：投资日期={}, 周期={}天, 结束日期={}", 
                     orderDate, cycle, endDate);
            
            return endDate;
        } catch (Exception e) {
            log.error("计算收益结束时间失败，orderDate: {}, cycle: {}", orderDate, cycle, e);
            return orderDate;
        }
    }

    /**
     * 计算总收益金额
     * 
     * @param investmentAmount 投资金额（分）
     * @param cycle 投资周期（天）
     * @param cycleType 周期类型
     * @param conversion 项目收益率配置
     * @return 总收益金额（分）
     */
    public static Long calculateTotalProfit(Long investmentAmount, Integer cycle, 
                                          Integer cycleType, String conversion) {
        if (investmentAmount == null || investmentAmount <= 0 || 
            cycle == null || cycle <= 0 || cycleType == null) {
            log.warn("计算参数无效，investmentAmount: {}, cycle: {}, cycleType: {}", 
                     investmentAmount, cycle, cycleType);
            return 0L;
        }

        try {
            BigDecimal profitAmount = BigDecimal.ZERO;
            
            switch (cycleType) {
                case 1: // 到期收益含本金
                    profitAmount = calculateMaturityProfit(investmentAmount, cycle, conversion);
                    break;
                case 2: // 每日返本金到期收益
                    profitAmount = calculateDailyReturnProfit(investmentAmount, cycle, conversion);
                    break;
                case 3: // 不返本金
                    profitAmount = calculateNoPrincipalReturnProfit(investmentAmount, cycle, conversion);
                    break;
                case 4: // 复利产品
                    profitAmount = calculateCompoundInterestProfit(investmentAmount, cycle, conversion);
                    break;
                case 5: // 阶梯日益
                    profitAmount = calculateSteppedDailyProfit(investmentAmount, cycle, conversion);
                    break;
                case 6: // 拼团
                    profitAmount = calculateGroupBuyProfit(investmentAmount, cycle, conversion);
                    break;
                default:
                    log.warn("未知的周期类型: {}，使用默认计算方式", cycleType);
                    profitAmount = calculateMaturityProfit(investmentAmount, cycle, conversion);
                    break;
            }
            
            Long totalProfit = profitAmount.longValue();
            
            log.debug("计算总收益金额：投资金额={}分, 周期={}天, 类型={}, 收益率={}, 总收益={}分", 
                     investmentAmount, cycle, cycleType, conversion, totalProfit);
            
            return totalProfit;
            
        } catch (Exception e) {
            log.error("计算总收益金额失败，investmentAmount: {}, cycle: {}, cycleType: {}, conversion: {}", 
                     investmentAmount, cycle, cycleType, conversion, e);
            return 0L;
        }
    }

    /**
     * 计算到期收益含本金的收益
     * 
     * @param investmentAmount 投资金额（分）
     * @param cycle 投资周期（天）
     * @param conversion 项目收益率配置
     * @return 收益金额（元）
     */
    private static BigDecimal calculateMaturityProfit(Long investmentAmount, Integer cycle, String conversion) {
        BigDecimal annualRate = parseProjectRate(conversion);
        BigDecimal investmentAmountYuan = new BigDecimal(investmentAmount).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        
        // 计算实际收益率：年化收益率 * 投资天数 / 365
        BigDecimal actualRate = annualRate.multiply(new BigDecimal(cycle))
                                         .divide(new BigDecimal(365), 6, RoundingMode.HALF_UP);
        
        return investmentAmountYuan.multiply(actualRate);
    }

    /**
     * 计算每日返本金到期收益的收益
     * 
     * @param investmentAmount 投资金额（分）
     * @param cycle 投资周期（天）
     * @param conversion 项目收益率配置
     * @return 收益金额（元）
     */
    private static BigDecimal calculateDailyReturnProfit(Long investmentAmount, Integer cycle, String conversion) {
        BigDecimal annualRate = parseProjectRate(conversion);
        BigDecimal investmentAmountYuan = new BigDecimal(investmentAmount);
        return investmentAmountYuan.multiply(annualRate).multiply(new BigDecimal(cycle));
    }

    /**
     * 计算不返本金的收益
     * 
     * @param investmentAmount 投资金额（分）
     * @param cycle 投资周期（天）
     * @param conversion 项目收益率配置
     * @return 收益金额（元）
     */
    private static BigDecimal calculateNoPrincipalReturnProfit(Long investmentAmount, Integer cycle, String conversion) {
        BigDecimal annualRate = parseProjectRate(conversion);
        BigDecimal investmentAmountYuan = new BigDecimal(investmentAmount).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        
        // 计算实际收益率：年化收益率 * 投资天数 / 365
        BigDecimal actualRate = annualRate.multiply(new BigDecimal(cycle))
                                         .divide(new BigDecimal(365), 6, RoundingMode.HALF_UP);
        
        return investmentAmountYuan.multiply(actualRate);
    }

    /**
     * 计算复利产品的收益
     * 
     * @param investmentAmount 投资金额（分）
     * @param cycle 投资周期（天）
     * @param conversion 项目收益率配置
     * @return 收益金额（元）
     */
    private static BigDecimal calculateCompoundInterestProfit(Long investmentAmount, Integer cycle, String conversion) {
        BigDecimal dailyRate = parseProjectDailyRate(conversion);
        BigDecimal investmentAmountYuan = new BigDecimal(investmentAmount).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        
        // 复利计算：P * (1 + r)^n - P
        BigDecimal compoundFactor = BigDecimal.ONE.add(dailyRate).pow(cycle);
        
        return investmentAmountYuan.multiply(compoundFactor).subtract(investmentAmountYuan);
    }

    /**
     * 计算阶梯日益的收益
     * 
     * @param investmentAmount 投资金额（分）
     * @param cycle 投资周期（天）
     * @param conversion 项目收益率配置
     * @return 收益金额（元）
     */
    private static BigDecimal calculateSteppedDailyProfit(Long investmentAmount, Integer cycle, String conversion) {
        BigDecimal investmentAmountYuan = new BigDecimal(investmentAmount).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal totalProfit = BigDecimal.ZERO;
        
        // 解析阶梯收益率配置
        BigDecimal[] dailyRates = parseSteppedDailyRates(conversion);
        
        for (int day = 1; day <= cycle; day++) {
            BigDecimal dailyRate;
            if (day <= 7) {
                dailyRate = dailyRates[0]; // 第一周
            } else if (day <= 15) {
                dailyRate = dailyRates[1]; // 第二周
            } else {
                dailyRate = dailyRates[2]; // 后续
            }
            
            totalProfit = totalProfit.add(investmentAmountYuan.multiply(dailyRate));
        }
        
        return totalProfit;
    }

    /**
     * 计算拼团的收益
     * 
     * @param investmentAmount 投资金额（分）
     * @param cycle 投资周期（天）
     * @param conversion 项目收益率配置
     * @return 收益金额（元）
     */
    private static BigDecimal calculateGroupBuyProfit(Long investmentAmount, Integer cycle, String conversion) {
        BigDecimal annualRate = parseProjectRate(conversion);
        BigDecimal investmentAmountYuan = new BigDecimal(investmentAmount).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        
        // 拼团收益：基础收益 + 拼团奖励
        BigDecimal baseProfit = annualRate.multiply(new BigDecimal(cycle))
                                         .divide(new BigDecimal(365), 6, RoundingMode.HALF_UP);
        
        // 拼团奖励（假设为年化收益率的20%）
        BigDecimal groupBonus = annualRate.multiply(new BigDecimal("0.2"));
        
        return investmentAmountYuan.multiply(baseProfit).add(investmentAmountYuan.multiply(groupBonus));
    }

    /**
     * 解析项目配置的年化收益率
     * 
     * @param conversion 项目收益率配置字符串
     * @return 年化收益率
     */
    private static BigDecimal parseProjectRate(String conversion) {
        try {
            BigDecimal rate = new BigDecimal(conversion);
            rate = rate.divide(new BigDecimal("100"), 4, RoundingMode.HALF_DOWN);
            return rate;
        } catch (Exception e) {
            log.warn("解析项目收益率失败: {}, 使用默认配置", conversion, e);
            return new BigDecimal("0.03"); // 默认8%年化收益率
        }
    }

    /**
     * 解析项目配置的日收益率
     * 
     * @param conversion 项目收益率配置字符串
     * @return 日收益率
     */
    private static BigDecimal parseProjectDailyRate(String conversion) {
        try {
            if (conversion == null || conversion.trim().isEmpty()) {
                return new BigDecimal("0.0002"); // 默认0.02%日收益率
            }
            
            BigDecimal rate = new BigDecimal(conversion);
            rate = rate.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
            return rate;
        } catch (Exception e) {
            log.warn("解析项目日收益率失败: {}, 使用默认配置", conversion, e);
            return new BigDecimal("0.0002"); // 默认0.02%日收益率
        }
    }

    /**
     * 解析阶梯日收益率配置
     * 
     * @param conversion 项目收益率配置字符串
     * @return 阶梯日收益率数组 [第一周, 第二周, 后续]
     */
    private static BigDecimal[] parseSteppedDailyRates(String conversion) {
        try {
            if (conversion == null || conversion.trim().isEmpty()) {
                // 默认阶梯收益率：第一周0.03%，第二周0.025%，后续0.02%
                return new BigDecimal[]{
                    new BigDecimal("0.0003"),
                    new BigDecimal("0.00025"),
                    new BigDecimal("0.0002")
                };
            }
            
            // 这里可以根据具体的配置格式进行解析
            // 例如：conversion = "0.03%,0.025%,0.02%" 或 "3%,2.5%,2%"
            String[] rates = conversion.split(",");
            if (rates.length >= 3) {
                BigDecimal[] dailyRates = new BigDecimal[3];
                for (int i = 0; i < 3; i++) {
                    dailyRates[i] = parseProjectDailyRate(rates[i]);
                }
                return dailyRates;
            }
            
            // 如果解析失败，使用默认值
            return new BigDecimal[]{
                new BigDecimal("0.0003"),
                new BigDecimal("0.00025"),
                new BigDecimal("0.0002")
            };
            
        } catch (Exception e) {
            log.warn("解析阶梯日收益率失败: {}, 使用默认配置", conversion, e);
            return new BigDecimal[]{
                new BigDecimal("0.0003"),
                new BigDecimal("0.00025"),
                new BigDecimal("0.0002")
            };
        }
    }

    /**
     * 计算投资天数（从投资日期到当前日期的天数）
     * 
     * @param orderDate 投资日期
     * @return 投资天数
     */
    public static int calculateInvestmentDays(Date orderDate) {
        if (orderDate == null) {
            return 0;
        }

        try {
            Date currentDate = new Date();
            long diffInMillies = currentDate.getTime() - orderDate.getTime();
            long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);
            return Math.max(0, (int) diffInDays);
        } catch (Exception e) {
            log.error("计算投资天数失败，orderDate: {}", orderDate, e);
            return 0;
        }
    }

    /**
     * 检查投资是否到期
     * 
     * @param orderDate 投资日期
     * @param cycle 投资周期（天）
     * @return 是否到期
     */
    public static boolean isInvestmentMatured(Date orderDate, Integer cycle) {
        if (orderDate == null || cycle == null || cycle <= 0) {
            return false;
        }

        try {
            Date endDate = calculateProfitEndDate(orderDate, cycle);
            Date currentDate = new Date();
            
            return currentDate.after(endDate) || currentDate.equals(endDate);
        } catch (Exception e) {
            log.error("检查投资是否到期失败，orderDate: {}, cycle: {}", orderDate, cycle, e);
            return false;
        }
    }

    /**
     * 获取剩余投资天数
     * 
     * @param orderDate 投资日期
     * @param cycle 投资周期（天）
     * @return 剩余天数
     */
    public static int getRemainingDays(Date orderDate, Integer cycle) {
        if (orderDate == null || cycle == null || cycle <= 0) {
            return 0;
        }

        try {
            Date endDate = calculateProfitEndDate(orderDate, cycle);
            Date currentDate = new Date();
            
            if (currentDate.after(endDate)) {
                return 0; // 已到期
            }
            
            long diffInMillies = endDate.getTime() - currentDate.getTime();
            long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);
            return Math.max(0, (int) diffInDays);
        } catch (Exception e) {
            log.error("获取剩余投资天数失败，orderDate: {}, cycle: {}", orderDate, cycle, e);
            return 0;
        }
    }
}
