package io.renren.modules.usdtrecord.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.usdtrecord.entity.UsdtRecordEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * USDT收款记录DAO
 *
 * @author renren
 * @since 2024-01-01
 */
@Mapper
public interface UsdtRecordDao extends BaseMapper<UsdtRecordEntity> {

}
