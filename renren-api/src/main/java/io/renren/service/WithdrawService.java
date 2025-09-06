package io.renren.service;

import io.renren.dto.RewardWithdrawRequestDTO;
import io.renren.dto.RewardWithdrawSumDTO;
import io.renren.dto.WithdrawPageData;
import io.renren.dto.WithdrawQueryDTO;

import java.util.Map;

/**
 * 提现服务接口
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
public interface WithdrawService {

    /**
     * 检查用户是否首次提现
     *
     * @param userId 用户ID
     * @return 包含是否首次提现信息的Map
     */
    Map<String, Object> checkFirstWithdraw(Long userId);

    /**
     * 分页查询用户提现记录
     *
     * @param queryDTO 查询参数
     * @return 提现分页数据
     */
    WithdrawPageData getWithdrawPageData(WithdrawQueryDTO queryDTO);

    /**
     * 分页查询用户佣金提现记录
     *
     * @param queryDTO 查询参数
     * @return 佣金提现分页数据
     */
    WithdrawPageData getRewardWithdrawPageData(WithdrawQueryDTO queryDTO);

    /**
     * 提交佣金提现申请
     *
     * @param userId 用户ID
     * @param requestDTO 提现请求参数
     * @return 提现结果
     */
    Map<String, Object> submitRewardWithdraw(Long userId, RewardWithdrawRequestDTO requestDTO);

    /**
     * 提现申请
     * @param userId
     * @param requestDTO
     * @return
     */
    Map<String, Object> submitWithdraw(Long userId, RewardWithdrawRequestDTO requestDTO) throws Exception;

    /**
     * 获取用户佣金提现统计
     *
     * @param userId 用户ID
     * @return 佣金提现统计信息
     */
    RewardWithdrawSumDTO getRewardWithdrawSum(Long userId);
}
