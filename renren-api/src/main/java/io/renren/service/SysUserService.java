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

    /**
     * 根据用户ID获取用户信息（优先从Redis缓存获取）
     * @param userId 用户ID
     * @return 用户信息
     */
    SysUserEntity getUserById(Long userId);

    /**
     * 根据业务员ID获取业务员名称（优先从Redis缓存获取）
     * @param salesmanId 业务员ID
     * @return 业务员名称
     */
    String getSalesmanNameById(Long salesmanId);

    /**
     * 根据代理ID获取代理名称（优先从Redis缓存获取）
     * @param agentId 代理ID
     * @return 代理名称
     */
    String getAgentNameById(Long agentId);

    /**
     * 刷新用户缓存
     * @param userId 用户ID
     */
    void refreshUserCache(Long userId);

    /**
     * 清除用户缓存
     * @param userId 用户ID
     */
    void clearUserCache(Long userId);
}
