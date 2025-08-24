package io.renren.service;

import io.renren.dto.ChannelAllocationResult;
import io.renren.entity.SysUserEntity;

/**
 * 系统用户服务接口
 *
 * @author renren
 * @since 2024-01-01
 */
public interface SysUserService {

    /**
     * 根据渠道查询业务员信息（一个渠道对应一个业务员）
     * @param channel 渠道ID
     * @return 业务员信息
     */
    SysUserEntity getSalesmanByChannel(Long channel);

    /**
     * 根据渠道查询代理信息（一个渠道对应一个代理）
     * @param channel 渠道ID
     * @return 代理信息
     */
    SysUserEntity getAgentByChannel(Long channel);

    /**
     * 根据渠道获取业务员和代理信息
     * @param channel 渠道ID
     * @return 分配结果，包含业务员ID和代理ID
     */
    ChannelAllocationResult getChannelResources(Long channel);
}
