package io.renren.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.entity.UserSignInEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 用户签到记录
 * 
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface UserSignInDao extends BaseMapper<UserSignInEntity> {
    
    /**
     * 查询用户今日是否已签到
     * 
     * @param userId 用户ID
     * @param signDate 签到日期
     * @return 签到记录
     */
    UserSignInEntity selectTodaySignIn(@Param("userId") Long userId, @Param("signDate") LocalDate signDate);
    
    /**
     * 查询用户连续签到天数
     * 
     * @param userId 用户ID
     * @return 连续签到天数
     */
    Integer selectContinuousDays(@Param("userId") Long userId);
    
    /**
     * 查询用户最近7天的签到记录
     * 
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 签到记录列表
     */
    List<UserSignInEntity> selectRecentSignIns(@Param("userId") Long userId, 
                                             @Param("startDate") LocalDate startDate, 
                                             @Param("endDate") LocalDate endDate);
    
    /**
     * 分页查询用户签到记录
     * 
     * @param userId 用户ID
     * @param offset 偏移量
     * @param limit 每页记录数
     * @param orderField 排序字段
     * @param order 排序方式
     * @return 签到记录列表
     */
    List<UserSignInEntity> selectSignInRecordsByPage(@Param("userId") Long userId,
                                                    @Param("offset") Integer offset,
                                                    @Param("limit") Integer limit,
                                                    @Param("orderField") String orderField,
                                                    @Param("order") String order);
    
    /**
     * 查询用户签到记录总数
     * 
     * @param userId 用户ID
     * @return 总记录数
     */
    Long selectSignInRecordsCount(@Param("userId") Long userId);
}
