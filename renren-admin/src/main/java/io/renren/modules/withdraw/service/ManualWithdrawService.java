package io.renren.modules.withdraw.service;

import io.renren.common.service.BaseService;
import io.renren.modules.withdraw.dto.ManualWithdrawDTO;
import io.renren.modules.withdraw.dto.ManualWithdrawPageData;
import io.renren.modules.withdraw.entity.WithdrawOrderEntity;

import java.util.List;
import java.util.Map;

/**
 * 手工提现服务接口
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
public interface ManualWithdrawService extends BaseService<WithdrawOrderEntity> {

    /**
     * 获取手工提现分页数据
     * @param params 查询参数
     * @return 分页数据
     */
    ManualWithdrawPageData getManualWithdrawPage(Map<String, Object> params);

    /**
     * 根据用户ID查询提现订单列表
     * @param userId 用户ID
     * @return 提现订单列表
     */
    List<WithdrawOrderEntity> getWithdrawOrdersByUserId(String userId);

    /**
     * 根据订单号查询提现订单
     * @param orderno 平台订单号
     * @return 提现订单
     */
    WithdrawOrderEntity getWithdrawOrderByOrderno(String orderno);

    /**
     * 根据第三方订单号查询提现订单
     * @param threeorderNo 第三方订单号
     * @return 提现订单
     */
    WithdrawOrderEntity getWithdrawOrderByThreeOrderNo(String threeorderNo);

    /**
     * 根据状态查询提现订单
     * @param state 状态
     * @return 提现订单列表
     */
    List<WithdrawOrderEntity> getWithdrawOrdersByState(Integer state);

    /**
     * 根据条件查询提现订单
     * @param params 查询条件
     * @return 提现订单列表
     */
    List<WithdrawOrderEntity> getWithdrawOrdersByParams(Map<String, Object> params);

    /**
     * 统计用户提现总额
     * @param userId 用户ID
     * @return 提现总额
     */
    Long getTotalWithdrawAmountByUserId(String userId);
}
