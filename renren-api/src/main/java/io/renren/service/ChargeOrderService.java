package io.renren.service;

import io.renren.common.service.BaseService;
import io.renren.dto.ChargeOrderDetailDTO;
import io.renren.dto.ChargePageData;
import io.renren.entity.ChargeOrderEntity;

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
     * 获取用户充值订单详情
     * @param userId 用户ID
     * @return 充值订单详情
     */
    ChargeOrderDetailDTO getChargeOrderDetail(Long userId);

    /**
     * 根据订单号获取充值订单详情
     * @param orderNo 订单号
     * @return 充值订单详情
     */
    ChargeOrderDetailDTO getChargeOrderDetailByOrderNo(String orderNo);

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


    /**
     * 创建充值订单
     * @param userId 用户ID
     * @param amount 充值金额
     * @param chargeType 充值类型
     * @param channelid 支付通道主键
     * @return 充值订单号
     */
    String createChargeOrder(Long userId, Long amount, Integer chargeType, Long channelid);

    /**
     * 分页查询用户充值记录
     * @param userId 用户ID
     * @param page 页码
     * @param limit 每页大小
     * @return 分页数据
     */
    ChargePageData getChargePageData(Long userId, Integer page, Integer limit);
}
