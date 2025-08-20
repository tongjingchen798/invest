package io.renren.modules.member.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.modules.member.entity.MemberEntity;
import io.renren.modules.member.dto.SettlementReportDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 会员查询管理
 *
 * @author renren
 * @since 1.0.0
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {

    /**
     * 查询结算报表分页数据
     *
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页结果
     */
    Page<SettlementReportDTO> getSettlementReport(Page<SettlementReportDTO> page, @Param("params") Map<String, Object> params);

}
