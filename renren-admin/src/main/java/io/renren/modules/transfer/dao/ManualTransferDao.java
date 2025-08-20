package io.renren.modules.transfer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.transfer.entity.ManualTransferEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 人工转帐Dao
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Mapper
public interface ManualTransferDao extends BaseMapper<ManualTransferEntity> {

}
