package io.renren.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.entity.SignRewardConfigEntity;
import io.renren.entity.UserLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.Date;

/**
 * 登录日志
 * 
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface UserLogDao extends BaseMapper<UserLogEntity> {
	
	/**
	 * 更新用户登出时间
	 * @param userId 用户ID
	 * @param logoutTime 登出时间
	 * @return 影响行数
	 */
	@Update("UPDATE tb_user_log SET logout_time = #{logoutTime}, update_time = #{logoutTime} WHERE user_id = #{userId} AND logout_time IS NULL ORDER BY login_time DESC LIMIT 1")
	int updateLogoutTime(@Param("userId") Long userId, @Param("logoutTime") Date logoutTime);
	
}
