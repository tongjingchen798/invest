package io.renren.modules.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.modules.order.dto.InvestmentRecordDTO;
import io.renren.modules.order.entity.InvestmentRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 购买记录
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Mapper
public interface InvestmentRecordDao extends BaseMapper<InvestmentRecordEntity> {
    
    /**
     * 联表分页查询购买记录（关联tb_user表）
     */
    Page<InvestmentRecordDTO> selectPageWithUser(Page<InvestmentRecordDTO> page, @Param("params") Map<String, Object> params);
    
}
