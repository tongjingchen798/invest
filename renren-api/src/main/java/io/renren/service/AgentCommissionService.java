package io.renren.service;

import io.renren.dto.AgentCenterDTO;
import io.renren.dto.MyAgentDTO;

/**
 * 代理佣金服务接口
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
public interface AgentCommissionService {

    /**
     * 获取我的佣金信息
     * @param userId 用户ID
     * @return 佣金信息
     */
    MyAgentDTO getMyAgentCommission(Long userId);

    /**
     * 获取代理中心数据
     * @param userId 用户ID
     * @return 代理中心数据
     */
    AgentCenterDTO getAgentCenterData(Long userId);
}
