package io.renren.modules.withdraw.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.modules.withdraw.dto.ManualWithdrawDTO;
import io.renren.modules.withdraw.entity.WithdrawOrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * 提现订单Dao
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Mapper
public interface WithdrawOrderDao extends BaseMapper<WithdrawOrderEntity> {

    /**
     * 自定义分页查询手工提现记录（关联用户表）
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<ManualWithdrawDTO> selectManualWithdrawPage(@Param("page") Page<ManualWithdrawDTO> page, 
                                                      @Param("params") Map<String, Object> params);

    /**
     * 统计手工提现汇总数据
     * @param params 查询参数
     * @return 汇总数据
     */
    Map<String, Object> selectManualWithdrawSummary(@Param("params") Map<String, Object> params);

    @Select("select * from tb_withdraw_order where  orderno=#{orderno}")
    WithdrawOrderEntity selectByOrderno(@Param("orderno") String orderno);

    /**
     * 查询用户正在提现状态订单
     *
     * @param userId 用户ID
     * @return 正在提现
     */
    Long selectPendingWithdrawCountByUserId(@Param("userId") Long userId);
}
