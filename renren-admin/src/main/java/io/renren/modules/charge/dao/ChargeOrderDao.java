package io.renren.modules.charge.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.charge.entity.ChargeOrderEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 充值订单Dao
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Mapper
public interface ChargeOrderDao extends BaseMapper<ChargeOrderEntity> {

}
