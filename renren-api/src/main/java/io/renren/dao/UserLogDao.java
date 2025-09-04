package io.renren.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.entity.SignRewardConfigEntity;
import io.renren.entity.UserLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 登录日志
 * 
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface UserLogDao extends BaseMapper<UserLogEntity> {
	
}
