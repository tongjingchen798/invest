package io.renren.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.dto.MyInvestmentDTO;
import io.renren.entity.InvestmentRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 我的投资DAO接口
 *
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface MyInvestmentDao extends BaseMapper<InvestmentRecordEntity> {

    /**
     * 分页查询投资列表
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<MyInvestmentDTO> selectPage(IPage<MyInvestmentDTO> page, @Param("params") Map<String, Object> params);
}