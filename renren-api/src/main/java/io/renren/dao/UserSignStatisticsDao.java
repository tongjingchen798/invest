package io.renren.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.entity.UserSignStatisticsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户签到统计
 * 
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface UserSignStatisticsDao extends BaseMapper<UserSignStatisticsEntity> {
    
    /**
     * 根据用户ID查询签到统计
     * 
     * @param userId 用户ID
     * @return 签到统计信息
     */
    UserSignStatisticsEntity selectByUserId(@Param("userId") Long userId);
    
    /**
     * 更新用户连续签到天数
     * 
     * @param userId 用户ID
     * @param continuousDays 连续签到天数
     * @return 更新行数
     */
    int updateContinuousDays(@Param("userId") Long userId, @Param("continuousDays") Integer continuousDays);
    
    /**
     * 更新用户最大连续签到天数
     * 
     * @param userId 用户ID
     * @param maxContinuousDays 最大连续签到天数
     * @return 更新行数
     */
    int updateMaxContinuousDays(@Param("userId") Long userId, @Param("maxContinuousDays") Integer maxContinuousDays);
}
