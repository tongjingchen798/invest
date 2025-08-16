package io.renren.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.dao.UserBalanceDetailDao;
import io.renren.dto.TeamPointsDetailDTO;
import io.renren.dto.TeamPointsDetailPageData;
import io.renren.entity.UserBalanceDetailEntity;
import io.renren.service.TeamPointsDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 积分明细服务实现类
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Service
public class TeamPointsDetailServiceImpl implements TeamPointsDetailService {

    @Autowired
    private UserBalanceDetailDao userBalanceDetailDao;

    @Override
    public TeamPointsDetailPageData getTeamPointsDetailPageData(Long userId, Integer page, Integer limit) {
        try {
            TeamPointsDetailPageData pageData = new TeamPointsDetailPageData();
            
            // 使用MyBatis-Plus分页查询
            Page<UserBalanceDetailEntity> pageParam = new Page<>(page, limit);
            
            // 构建查询条件 - 查询积分相关的交易记录
            QueryWrapper<UserBalanceDetailEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId)
                       .in("business_type", Arrays.asList(1, 2, 3, 4, 39)) // 积分相关业务类型
                       .orderByDesc("transaction_date", "create_time");
            
            // 执行分页查询
            Page<UserBalanceDetailEntity> result = userBalanceDetailDao.selectPage(pageParam, queryWrapper);
            
            // 转换为DTO
            List<TeamPointsDetailDTO> detailList = convertToDTOList(result.getRecords());
            
            // 计算汇总信息
            Map<String, Object> sum = calculateSum(detailList);
            
            // 设置分页数据
            pageData.setList(detailList);
            pageData.setSum(sum);
            pageData.setTotal((int) result.getTotal());
            
            return pageData;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取积分明细失败: " + e.getMessage());
        }
    }

    /**
     * 将Entity转换为DTO
     */
    private List<TeamPointsDetailDTO> convertToDTOList(List<UserBalanceDetailEntity> entityList) {
        List<TeamPointsDetailDTO> dtoList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        for (UserBalanceDetailEntity entity : entityList) {
            TeamPointsDetailDTO dto = new TeamPointsDetailDTO();
            
            // 复制基本属性
            BeanUtils.copyProperties(entity, dto);
            
            // 设置积分金额（使用交易金额作为积分金额）
            dto.setPointsAmount(entity.getTransactionAmount() != null ? entity.getTransactionAmount() : 0L);
            
            // 格式化日期字段
            if (entity.getTransactionDate() != null) {
                dto.setPointsDate(sdf.format(entity.getTransactionDate()));
            }
            
            dtoList.add(dto);
        }
        
        return dtoList;
    }

    /**
     * 计算汇总信息
     */
    private Map<String, Object> calculateSum(List<TeamPointsDetailDTO> details) {
        Map<String, Object> sum = new HashMap<>();
        
        long totalPointsAmount = 0;
        int totalCount = 0;
        Map<Integer, Long> busiTypePoints = new HashMap<>();
        Map<Integer, Integer> busiTypeCount = new HashMap<>();
        
        for (TeamPointsDetailDTO detail : details) {
            totalPointsAmount += detail.getPointsAmount() != null ? detail.getPointsAmount() : 0;
            totalCount++;
            
            // 按业务类型统计
            if (detail.getBusiType() != null) {
                busiTypePoints.merge(detail.getBusiType(), 
                    detail.getPointsAmount() != null ? detail.getPointsAmount() : 0L, Long::sum);
                busiTypeCount.merge(detail.getBusiType(), 1, Integer::sum);
            }
        }
        
        sum.put("totalPointsAmount", totalPointsAmount);
        sum.put("totalCount", totalCount);
        sum.put("busiTypePoints", busiTypePoints);
        sum.put("busiTypeCount", busiTypeCount);
        
        return sum;
    }
}
