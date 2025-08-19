package io.renren.dao;

import io.renren.common.dao.BaseDao;
import io.renren.entity.ChargeOrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 充值订单DAO接口
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Mapper
public interface ChargeOrderDao extends BaseDao<ChargeOrderEntity> {

    /**
     * 根据用户ID查询充值订单
     * @param userId 用户ID
     * @return 充值订单列表
     */
    List<ChargeOrderEntity> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据订单号查询充值订单
     * @param orderno 平台订单号
     * @return 充值订单
     */
    ChargeOrderEntity selectByOrderno(@Param("orderno") String orderno);

    /**
     * 根据第三方订单号查询充值订单
     * @param threeorderNo 第三方订单号
     * @return 充值订单
     */
    ChargeOrderEntity selectByThreeOrderNo(@Param("threeorderNo") String threeorderNo);

    /**
     * 根据状态查询充值订单
     * @param state 状态
     * @return 充值订单列表
     */
    List<ChargeOrderEntity> selectByState(@Param("state") Integer state);

    /**
     * 根据条件查询充值订单
     * @param params 查询条件
     * @return 充值订单列表
     */
    List<ChargeOrderEntity> selectByParams(Map<String, Object> params);

    /**
     * 统计用户充值总额
     * @param userId 用户ID
     * @return 充值总额
     */
    Long selectTotalAmountByUserId(@Param("userId") Long userId);

}
