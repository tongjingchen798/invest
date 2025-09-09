package io.renren.modules.charge.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.modules.charge.dto.ChargeOrderDetailDTO;
import io.renren.modules.charge.entity.ChargeOrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * 充值订单Dao
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Mapper
public interface ChargeOrderDao extends BaseMapper<ChargeOrderEntity> {

    /**
     * 自定义分页查询充值订单详情
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<ChargeOrderDetailDTO> selectAdminChargePage(@Param("page") Page<ChargeOrderDetailDTO> page, 
                                                      @Param("params") Map<String, Object> params);

    /**
     * 统计充值订单汇总数据
     * @param params 查询参数
     * @return 汇总数据
     */
    Map<String, Object> selectAdminChargeSummary(@Param("params") Map<String, Object> params);

    @Select("SELECT * FROM tb_charge_order where orderno=#{orderno}")
    ChargeOrderEntity selectByOrderNo(@Param("orderno")  String orderno);
}
