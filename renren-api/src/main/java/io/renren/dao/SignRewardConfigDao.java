package io.renren.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.entity.SignRewardConfigEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 签到奖励配置
 * 
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface SignRewardConfigDao extends BaseMapper<SignRewardConfigEntity> {
	
}
