package io.renren.modules.transfer.service;

import io.renren.common.service.BaseService;
import io.renren.modules.transfer.dto.ManualTransferDTO;
import io.renren.modules.transfer.dto.ManualTransferPageData;
import io.renren.modules.transfer.entity.ManualTransferEntity;

import java.util.List;
import java.util.Map;

/**
 * 人工转帐服务接口
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
public interface ManualTransferService extends BaseService<ManualTransferEntity> {

    /**
     * 获取人工转帐分页数据
     * @param params 查询参数
     * @return 分页数据
     */
    ManualTransferPageData getManualTransferPage(Map<String, Object> params);

    /**
     * 根据订单号查询人工转帐记录
     * @param orderno 平台订单号
     * @return 人工转帐记录
     */
    ManualTransferEntity getManualTransferByOrderno(String orderno);

    /**
     * 根据第三方订单号查询人工转帐记录
     * @param threeorderNo 第三方订单号
     * @return 人工转帐记录
     */
    ManualTransferEntity getManualTransferByThreeOrderNo(String threeorderNo);

    /**
     * 根据状态查询人工转帐记录
     * @param state 状态
     * @return 人工转帐记录列表
     */
    List<ManualTransferEntity> getManualTransfersByState(Integer state);

    /**
     * 根据条件查询人工转帐记录
     * @param params 查询条件
     * @return 人工转帐记录列表
     */
    List<ManualTransferEntity> getManualTransfersByParams(Map<String, Object> params);

    /**
     * 统计用户人工转帐总额
     * @param userId 用户ID
     * @return 人工转帐总额
     */
    Long getTotalManualTransferAmountByUserId(String userId);
}
