package io.renren.modules.usdtrecord.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.modules.usdtrecord.entity.UsdtRecordEntity;
import io.renren.modules.usdtrecord.dto.UsdtRecordDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * USDT收款记录DAO
 *
 * @author renren
 * @since 2024-01-01
 */
@Mapper
public interface UsdtRecordDao extends BaseMapper<UsdtRecordEntity> {

    /**
     * 自定义分页查询USDT收款记录
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<UsdtRecordDTO> selectUsdtRecordPage(@Param("page") Page<UsdtRecordDTO> page, 
                                             @Param("params") Map<String, Object> params);

}
