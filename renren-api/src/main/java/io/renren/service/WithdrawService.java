package io.renren.service;

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
}
