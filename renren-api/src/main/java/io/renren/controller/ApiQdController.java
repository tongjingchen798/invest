package io.renren.controller;

import io.renren.annotation.Login;
import io.renren.annotation.LoginUser;
import io.renren.common.utils.Result;
import io.renren.dao.*;
import io.renren.dto.PageData;
import io.renren.dto.SignInRecordDTO;
import io.renren.dto.UserQdDTO;
import io.renren.entity.SignRewardConfigEntity;
import io.renren.entity.UserEntity;
import io.renren.entity.UserSignInEntity;
import io.renren.entity.UserSignStatisticsEntity;
import io.renren.entity.UserBalanceDetailEntity;
import io.renren.enums.BusinessTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 签到管理接口
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/api/qd")
@Api(tags = "签到管理接口")
public class ApiQdController {

    @Autowired
    private UserSignInDao userSignInDao;

    @Autowired
    private UserSignStatisticsDao userSignStatisticsDao;

    @Autowired
    private SignRewardConfigDao signRewardConfigDao;

    @Autowired
    private UserBalanceDetailDao userBalanceDetailDao;
    @Autowired
    private UserDao userDao;

    @Login
    @GetMapping("getUserQd")
    @ApiOperation("获取用户签到")
    public Result<UserQdDTO> getUserQd(@LoginUser UserEntity user) {
        try {
            UserQdDTO userQdDTO = new UserQdDTO();
            userQdDTO.setUserId(user.getId());
            
            // 获取当前日期
            LocalDate today = LocalDate.now();
            
            // 检查今日是否已签到
            UserSignInEntity todaySignIn = userSignInDao.selectTodaySignIn(user.getId(), today);
            if (todaySignIn != null) {
                userQdDTO.setToday_qd(1); // 今日已签到
            } else {
                userQdDTO.setToday_qd(0); // 今日未签到
            }
            
            // 获取用户连续签到天数
            UserSignStatisticsEntity statistics = userSignStatisticsDao.selectByUserId(user.getId());
            if (statistics != null && statistics.getContinuousDays() != null) {
                userQdDTO.setDay(statistics.getContinuousDays().longValue());
            } else {
                // 如果没有统计记录，尝试从签到记录表查询连续天数
                Integer continuousDays = userSignInDao.selectContinuousDays(user.getId());
                if (continuousDays != null) {
                    userQdDTO.setDay(continuousDays.longValue());
                } else {
                    userQdDTO.setDay(0L);
                }
            }
            
            return new Result<UserQdDTO>().ok(userQdDTO);
            
        } catch (Exception e) {
            return new Result<UserQdDTO>().error("获取签到信息失败: " + e.getMessage());
        }
    }

    @Login
    @PostMapping("qd")
    @ApiOperation("立即签到")
    @Transactional
    public Result signIn(@LoginUser UserEntity user) {
        try {
            // 获取当前日期
            LocalDate today = LocalDate.now();
            
            // 检查今日是否已签到
            UserSignInEntity todaySignIn = userSignInDao.selectTodaySignIn(user.getId(), today);
            if (todaySignIn != null) {
                return new Result<Map<String, Object>>().error("今日已签到，请明天再来");
            }
            
            // 获取签到奖励配置
            SignRewardConfigEntity rewardConfig = signRewardConfigDao.selectById(1L);
            if (rewardConfig == null || rewardConfig.getStatus() != 1) {
                return new Result<Map<String, Object>>().error("签到功能暂未开放");
            }
            
            // 获取用户当前连续签到天数
            UserSignStatisticsEntity statistics = userSignStatisticsDao.selectByUserId(user.getId());
            int currentContinuousDays = 0;
            if (statistics != null && statistics.getContinuousDays() != null) {
                currentContinuousDays = statistics.getContinuousDays();
            }
            
            // 计算新的连续签到天数
            int newContinuousDays = currentContinuousDays + 1;
            if (newContinuousDays > 7) {
                newContinuousDays = 1; // 超过7天重新开始
            }
            
            // 获取对应天数的奖励
            Long rewardAmount = getRewardAmount(rewardConfig, newContinuousDays);
            Integer rewardType = getRewardType(rewardConfig, newContinuousDays);
            
            // 创建签到记录
            UserSignInEntity signInRecord = new UserSignInEntity();
            signInRecord.setUserId(user.getId());
            signInRecord.setSignDate(today);
            signInRecord.setSignTime(new Date());
            signInRecord.setContinuousDays(newContinuousDays);
            signInRecord.setRewardAmount(rewardAmount);
            signInRecord.setRewardType(rewardType);
            signInRecord.setStatus(1);
            
            // 保存签到记录
            userSignInDao.insert(signInRecord);
            
            // 记录账变明细
            UserBalanceDetailEntity userBalanceDetail = new UserBalanceDetailEntity();
            userBalanceDetail.setUserId(user.getId());
            
            // 设置交易时间
            Date now = new Date();
            userBalanceDetail.setTransactionDate(now);
            
            // 设置业务类型：13签到奖励
            userBalanceDetail.setBusiType(BusinessTypeEnum.SIGN_IN_REWARD.getCode());
            
            // 设置渠道
            userBalanceDetail.setChannel("1");
            
            // 设置交易流水ID（使用签到记录ID）
            userBalanceDetail.setStreamId(signInRecord.getId().toString());
            
            // 设置使用金额（签到奖励金额）
            userBalanceDetail.setUseAmount(rewardAmount);
            
            // 设置原始金额（签到前的余额）
            userBalanceDetail.setOriginalAmount(user.getAssets() != null ? user.getAssets() : 0L);
            
            // 设置交易后金额（签到后的余额）
            userBalanceDetail.setTransactionAmount(user.getAssets() != null ? user.getAssets() + rewardAmount : rewardAmount);
            
            // 设置备注：连续签到X天，送钱【金额】
            String remarks = String.format("连续签到%d天，送钱【%d】", 
                newContinuousDays, // 使用前面已经计算好的连续天数
                rewardAmount / 100); // 转换为元显示
            userBalanceDetail.setRemarks(remarks);
            
            // 设置状态：1正常
            userBalanceDetail.setStatus(1);
            
            // 设置业务员ID和代理ID（如果有的话）
//            if (user.getSalesmanId() != null) {
//                userBalanceDetail.setSalesmanId(user.getSalesmanId());
//            }
//            if (user.getAgentId() != null) {
//                userBalanceDetail.setAgentId(user.getAgentId());
//            }
            
            // 设置创建和更新时间
            userBalanceDetail.setCreateDate(now);
            userBalanceDetail.setUpdateDate(now);
            
            // 插入账变记录
            userBalanceDetailDao.insert(userBalanceDetail);
            //更新当日收益、历史收益、
            userDao.updateAllProfitFields(user.getId(), rewardAmount);


            // 更新或创建用户签到统计
            if (statistics == null) {
                // 创建新的统计记录
                statistics = new UserSignStatisticsEntity();
                statistics.setUserId(user.getId());
                statistics.setTotalSignDays(1);
                statistics.setContinuousDays(newContinuousDays);
                statistics.setMaxContinuousDays(newContinuousDays);
                statistics.setTotalRewardAmount(rewardAmount);
                statistics.setLastSignDate(today);
                statistics.setLastSignTime(new Date());
                userSignStatisticsDao.insert(statistics);
            } else {
                // 更新现有统计记录
                statistics.setTotalSignDays(statistics.getTotalSignDays() + 1);
                statistics.setContinuousDays(newContinuousDays);
                if (newContinuousDays > statistics.getMaxContinuousDays()) {
                    statistics.setMaxContinuousDays(newContinuousDays);
                }
                statistics.setTotalRewardAmount(statistics.getTotalRewardAmount() + rewardAmount);
                statistics.setLastSignDate(today);
                statistics.setLastSignTime(new Date());
                userSignStatisticsDao.updateById(statistics);
            }
            
            // 构建返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("qdamount", String.valueOf(rewardAmount)); // 签到奖励金额
            data.put("qdtype", rewardType); // 签到奖励类型
            data.put("qdamountjc", rewardAmount);
            data.put("vip", 0); // VIP等级（固定为0）
            data.put("day", String.valueOf(newContinuousDays)); // 连续签到天数

            return new Result<Map<String, Object>>().ok(data);
            
        } catch (Exception e) {
            return new Result().error("签到失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据连续签到天数获取奖励金额
     */
    private Long getRewardAmount(SignRewardConfigEntity config, int continuousDays) {
        switch (continuousDays) {
            case 1: return config.getOneDay();
            case 2: return config.getTwoDay();
            case 3: return config.getThreeDay();
            case 4: return config.getFourDay();
            case 5: return config.getFiveDay();
            case 6: return config.getSixDay();
            case 7: return config.getSevenDay();
            default: return config.getOneDay();
        }
    }
    
    /**
     * 根据连续签到天数获取奖励类型
     */
    private Integer getRewardType(SignRewardConfigEntity config, int continuousDays) {
        switch (continuousDays) {
            case 1: return config.getOneDayqdtype();
            case 2: return config.getTwoDayqdtype();
            case 3: return config.getThreeDayqdtype();
            case 4: return config.getFourDayqdtype();
            case 5: return config.getFiveDayqdtype();
            case 6: return config.getSixDayqdtype();
            case 7: return config.getSevenDayqdtype();
            default: return config.getOneDayqdtype();
        }
    }

    @Login
    @GetMapping("qddetail")
    @ApiOperation("个人中心日历签到记录")
    public Result<PageData<SignInRecordDTO>> getSignInRecords(
            @LoginUser UserEntity user,
            @RequestParam(value = "limit", required = true) Integer limit,
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "orderField", required = false) String orderField) {
        
        try {
            // 参数验证
            if (limit == null || limit <= 0) {
                limit = 10; // 默认每页10条
            }
            if (page == null || page <= 0) {
                page = 1; // 默认第1页
            }
            
            // 计算偏移量
            int offset = (page - 1) * limit;
            
            // 设置默认排序
            if (order == null || order.isEmpty()) {
                order = "desc";
            }
            if (orderField == null || orderField.isEmpty()) {
                orderField = "sign_date";
            }
            
            // 查询签到记录
            List<UserSignInEntity> signInEntities = userSignInDao.selectSignInRecordsByPage(
                user.getId(), offset, limit, orderField, order);
            
            // 查询总记录数
            Long total = userSignInDao.selectSignInRecordsCount(user.getId());
            
            // 转换为DTO
            List<SignInRecordDTO> records = signInEntities.stream()
                .map(this::convertToSignInRecordDTO)
                .collect(Collectors.toList());
            
            // 构建分页数据
            PageData<SignInRecordDTO> pageData = new PageData<>();
            pageData.setList(records);
            pageData.setTotal(total);
            pageData.setSum(new HashMap<>()); // 暂时为空，可根据需要添加汇总信息
            
            return new Result<PageData<SignInRecordDTO>>().ok(pageData);
            
        } catch (Exception e) {
            return new Result<PageData<SignInRecordDTO>>().error("获取签到记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 将签到实体转换为DTO
     */
    private SignInRecordDTO convertToSignInRecordDTO(UserSignInEntity entity) {
        SignInRecordDTO dto = new SignInRecordDTO();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setDay(entity.getContinuousDays());
        dto.setAmount(entity.getRewardAmount());
        dto.setQdtype(entity.getRewardType());
        dto.setCreateDate(formatDate(entity.getSignTime()));
        dto.setCreator(entity.getUserId());
        
        // 设置默认值
        dto.setMobile("");
        dto.setAgent("");
        dto.setSalesmanid("");
        dto.setPartDay(0);
        dto.setSumday(0);
        dto.setCouponid(0L);
        dto.setCouponName("");
        dto.setPrivilegeid(0L);
        dto.setPrivilegeName("");
        
        return dto;
    }
    
    /**
     * 格式化日期
     */
    private String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return date.toInstant().atZone(java.time.ZoneId.systemDefault())
            .toLocalDateTime().format(formatter);
    }
}
