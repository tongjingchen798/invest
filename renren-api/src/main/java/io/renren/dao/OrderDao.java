package io.renren.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单表
 *
 * @author renren
 * @since 2024-01-01
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {

}
