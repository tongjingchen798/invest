package io.renren.modules.withdraw.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.withdraw.entity.WithdrawOrderEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 提现订单Dao
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Mapper
public interface WithdrawOrderDao extends BaseMapper<WithdrawOrderEntity> {

}
