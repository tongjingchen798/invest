package io.renren.modules.home.service.impl;

import io.renren.modules.home.dao.HomeStatsDao;
import io.renren.modules.home.dto.DailyReportDTO;
import io.renren.modules.home.dto.MainStatsDTO;
import io.renren.modules.home.dto.QuantityAnalysisDTO;
import io.renren.modules.home.service.HomeStatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * 首页统计服务实现类
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Service
public class HomeStatsServiceImpl implements HomeStatsService {

    private static final Logger logger = LoggerFactory.getLogger(HomeStatsServiceImpl.class);

    @Autowired
    private HomeStatsDao homeStatsDao;

    @Override
    public MainStatsDTO getMainStats(Long startTime, Long endTime) {
        logger.info("开始获取首页统计数据，时间范围: {} - {}", startTime, endTime);
        
        MainStatsDTO statsDTO = new MainStatsDTO();

        try {
            // 获取充值订单统计
            logger.debug("获取充值订单统计...");
            Map<String, Object> chargeStats = homeStatsDao.getChargeOrderStats(startTime, endTime);
            if (chargeStats != null) {
                statsDTO.setChargeOrderAmount(getLongValue(chargeStats.get("chargeOrderAmount")));
                statsDTO.setCharge_order_cnt(getLongValue(chargeStats.get("charge_order_cnt")));
                logger.debug("充值订单统计: {}", chargeStats);
            }

            // 获取提现订单统计
            logger.debug("获取提现订单统计...");
            Map<String, Object> withdrawStats = homeStatsDao.getWithdrawOrderStats(startTime, endTime);
            if (withdrawStats != null) {
                statsDTO.setWithdrawOrderAmount(getLongValue(withdrawStats.get("withdrawOrderAmount")));
                statsDTO.setWithdraw_order_cnt(getLongValue(withdrawStats.get("withdraw_order_cnt")));
                logger.debug("提现订单统计: {}", withdrawStats);
            }

            // 获取项目统计
            logger.debug("获取项目统计...");
            Map<String, Object> projectStats = homeStatsDao.getProjectStats(startTime, endTime);
            if (projectStats != null) {
                statsDTO.setOnline_privilege_cnt(getLongValue(projectStats.get("online_privilege_cnt")));
                statsDTO.setPrivilege_cnt(getLongValue(projectStats.get("privilege_cnt")));
                logger.debug("项目统计 - 在线项目数: {}, 购买项目总数: {}", 
                    projectStats.get("online_privilege_cnt"), projectStats.get("privilege_cnt"));
            }

            // 获取用户注册统计
            logger.debug("获取用户注册统计...");
            Map<String, Object> userStats = homeStatsDao.getUserRegistrationStats(startTime, endTime);
            if (userStats != null) {
                statsDTO.setZc_cnt(getLongValue(userStats.get("zc_cnt")));
                logger.debug("用户注册统计: {}", userStats);
            }

            // 获取销售总额统计
            logger.debug("获取销售总额统计...");
            Map<String, Object> salesStats = homeStatsDao.getSalesAmountStats(startTime, endTime);
            if (salesStats != null) {
                statsDTO.setXs_amount(getLongValue(salesStats.get("xs_amount")));
                logger.debug("销售总额统计: {}", salesStats);
            }

            logger.info("成功获取所有统计数据: {}", statsDTO);

        } catch (Exception e) {
            logger.error("获取统计数据时发生异常", e);
            // 设置默认值
            setDefaultValues(statsDTO);
        }

        return statsDTO;
    }

    @Override
    public List<DailyReportDTO> getDailyReportStats(Long startTime, Long endTime, String type) {
        logger.info("开始获取日报表统计数据，时间范围: {} - {}, 类型: {}", startTime, endTime, type);
        
        List<DailyReportDTO> reportList = new ArrayList<>();
        
        try {
            List<Map<String, Object>> rawData = homeStatsDao.getDailyReportStats(startTime, endTime, type);
            
            if (rawData != null && !rawData.isEmpty()) {
                for (Map<String, Object> data : rawData) {
                    DailyReportDTO reportDTO = new DailyReportDTO();
                    reportDTO.setDate(getStringValue(data.get("date")));
                    reportDTO.setTotalCharges(getStringValue(data.get("totalCharges")));
                    reportDTO.setTotalWithdraws(getStringValue(data.get("totalWithdraws")));
                    reportDTO.setTotalOrders(getStringValue(data.get("totalOrders")));
                    reportList.add(reportDTO);
                }
                logger.debug("成功转换日报表数据，共 {} 条记录", reportList.size());
            } else {
                logger.warn("未查询到日报表数据");
            }
            
        } catch (Exception e) {
            logger.error("获取日报表统计数据时发生异常", e);
        }
        
        logger.info("日报表统计完成，返回 {} 条记录", reportList.size());
        return reportList;
    }

    @Override
    public List<QuantityAnalysisDTO> getQuantityAnalysisData(Long startTime, Long endTime, String type) {
        logger.info("开始获取数量统计分析图数据，时间范围: {} - {}, 类型: {}", startTime, endTime, type);
        
        List<QuantityAnalysisDTO> analysisList = new ArrayList<>();
        
        try {
            // 参数验证
            if (startTime == null || endTime == null) {
                logger.warn("时间参数为空，使用默认时间范围");
                startTime = LocalDate.now().atStartOfDay(ZoneOffset.of("+8")).toInstant().toEpochMilli();
                endTime = LocalDate.now().atTime(23, 59, 59).atZone(ZoneOffset.of("+8")).toInstant().toEpochMilli();
            }
            
            if (startTime > endTime) {
                logger.warn("开始时间大于结束时间，交换时间范围");
                Long temp = startTime;
                startTime = endTime;
                endTime = temp;
            }
            
            // 将字符串type转换为数值，避免SQL中的字符串比较问题
            Integer typeNum = convertTypeToNumber(type);
            
            // 直接获取DTO对象列表
            analysisList = homeStatsDao.getQuantityAnalysisData(startTime, endTime, typeNum);
            
            if (analysisList != null && !analysisList.isEmpty()) {
                // 设置type字段
                analysisList.forEach(dto -> dto.setType(type));
                logger.debug("成功获取数量统计分析数据，共 {} 条记录", analysisList.size());
            } else {
                logger.warn("未查询到数量统计分析数据");
            }
            
        } catch (Exception e) {
            logger.error("获取数量统计分析图数据时发生异常", e);
            throw new RuntimeException("获取统计数据失败", e);
        }
        
        logger.info("数量统计分析完成，返回 {} 条记录", analysisList.size());
        return analysisList;
    }

    /**
     * 将字符串类型的统计类型转换为数值
     * @param type 字符串类型 (d: 日, w: 周, m: 月)
     * @return 数值类型 (1: 日, 2: 周, 3: 月)
     */
    private Integer convertTypeToNumber(String type) {
        if (type == null) {
            return 1; // 默认日统计
        }
        
        switch (type.toLowerCase()) {
            case "d":
                return 1;
            case "w":
                return 2;
            case "m":
                return 3;
            default:
                return 1; // 默认日统计
        }
    }

    /**
     * 安全获取Long值
     */
    private Long getLongValue(Object value) {
        if (value == null) {
            return 0L;
        }
        
        try {
            if (value instanceof Number) {
                return ((Number) value).longValue();
            } else if (value instanceof String) {
                String strValue = ((String) value).trim();
                return strValue.isEmpty() ? 0L : Long.parseLong(strValue);
            }
            return 0L;
        } catch (NumberFormatException e) {
            logger.warn("无法将值转换为Long类型: {}", value);
            return 0L;
        }
    }

    /**
     * 安全获取String值
     */
    private String getStringValue(Object value) {
        if (value == null) {
            return "";
        }
        return value.toString().trim();
    }

    /**
     * 设置默认值
     */
    private void setDefaultValues(MainStatsDTO statsDTO) {
        logger.warn("设置统计数据默认值");
        statsDTO.setChargeOrderAmount(0L);
        statsDTO.setCharge_order_cnt(0L);
        statsDTO.setOnline_privilege_cnt(0L);
        statsDTO.setPrivilege_cnt(0L);
        statsDTO.setWithdrawOrderAmount(0L);
        statsDTO.setWithdraw_order_cnt(0L);
        statsDTO.setXs_amount(0L);
        statsDTO.setZc_cnt(0L);
    }
}
