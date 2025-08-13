package io.renren.service;

import io.renren.dto.BalanceDetailPageData;

/**
 * 资金明细服务接口
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
public interface BalanceDetailService {

    /**
     * 获取用户资金明细分页数据
     * @param userId 用户ID
     * @param page 页码
     * @param limit 每页大小
     * @return 分页数据
     */
    BalanceDetailPageData getBalanceDetailPageData(Long userId, Integer page, Integer limit);
}
