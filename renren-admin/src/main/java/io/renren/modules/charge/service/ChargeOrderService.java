package io.renren.modules.charge.service;

import io.renren.common.service.BaseService;
import io.renren.modules.charge.dto.ChargeOrderDetailDTO;
import io.renren.modules.charge.dto.ChargePageData;
import io.renren.modules.charge.entity.ChargeOrderEntity;

import java.util.List;
import java.util.Map;

/**
 * 充值订单服务接口
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
public interface ChargeOrderService extends BaseService<ChargeOrderEntity> {

    /**
     * 获取管理员充值列表分页数据
     * @param params 查询参数
     * @return 分页数据
     */
    ChargePageData getAdminChargePage(Map<String, Object> params);

    /**
     * 根据用户ID查询充值订单列表
     * @param userId 用户ID
     * @return 充值订单列表
     */
    List<ChargeOrderEntity> getChargeOrdersByUserId(Long userId);

    /**
     * 根据订单号查询充值订单
     * @param orderno 平台订单号
     * @return 充值订单
     */
    ChargeOrderEntity getChargeOrderByOrderno(String orderno);

    /**
     * 根据第三方订单号查询充值订单
     * @param threeorderNo 第三方订单号
     * @return 充值订单
     */
    ChargeOrderEntity getChargeOrderByThreeOrderNo(String threeorderNo);

    /**
     * 根据状态查询充值订单
     * @param state 状态
     * @return 充值订单列表
     */
    List<ChargeOrderEntity> getChargeOrdersByState(Integer state);

    /**
     * 根据条件查询充值订单
     * @param params 查询条件
     * @return 充值订单列表
     */
    List<ChargeOrderEntity> getChargeOrdersByParams(Map<String, Object> params);

    /**
     * 统计用户充值总额
     * @param userId 用户ID
     * @return 充值总额
     */
    Long getTotalChargeAmountByUserId(Long userId);
}
