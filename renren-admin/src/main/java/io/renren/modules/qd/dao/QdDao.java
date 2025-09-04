package io.renren.modules.qd.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.modules.qd.dto.QdRecordDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 签到记录DAO
 *
 * @author renren
 * @since 2024-01-01
 */
@Mapper
public interface QdDao {

    /**
     * 分页查询签到记录（关联用户表）
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<QdRecordDTO> selectQdRecordPage(@Param("page") Page<QdRecordDTO> page, 
                                          @Param("params") Map<String, Object> params);

    /**
     * 统计签到记录汇总数据
     * @param params 查询参数
     * @return 汇总数据
     */
    Map<String, Object> selectQdRecordSummary(@Param("params") Map<String, Object> params);
}
