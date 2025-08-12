package io.renren.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.entity.RebateRatioConfigEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 返佣比例配置表
 *
 * @author renren
 * @since 2024-01-01
 */
@Mapper
public interface RebateRatioConfigDao extends BaseMapper<RebateRatioConfigEntity> {

}
