package io.renren.service;

import io.renren.dto.ProfitEndedDTO;

/**
 * 付息还本服务接口
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
public interface ProfitEndedService {

    /**
     * 获取用户付息还本记录
     *
     * @param userId 用户ID
     * @return 付息还本记录
     */
    ProfitEndedDTO getProfitEndedRecord(Long userId);

    /**
     * 获取用户投资中项目统计
     *
     * @param userId 用户ID
     * @return 投资中项目统计
     */
    ProfitEndedDTO getProfitInvestingRecord(Long userId);
}
