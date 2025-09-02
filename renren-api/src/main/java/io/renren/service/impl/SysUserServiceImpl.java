package io.renren.service.impl;

import io.renren.dao.SysUserDao;
import io.renren.dto.ChannelAllocationResult;
import io.renren.entity.SysUserEntity;
import io.renren.service.SysUserService;
import io.renren.utils.RedisCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 系统用户服务实现类
 *
 * @author renren
 * @since 2024-01-01
 */
@Slf4j
@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserDao sysUserDao;

    @Resource
    private RedisCacheUtil redisCacheUtil;


    @Override
    public SysUserEntity getSalesmanByChannel(Long channel) {
        log.debug("根据渠道查询业务员信息，渠道ID: {}", channel);
        try {
            return sysUserDao.selectSalesmanByChannel(channel);
        } catch (Exception e) {
            log.error("根据渠道查询业务员信息失败，渠道ID: {}", channel, e);
            return null;
        }
    }

    @Override
    public SysUserEntity getAgentByChannel(Long channel) {
        log.debug("根据渠道查询代理信息，渠道ID: {}", channel);
        try {
            return sysUserDao.selectAgentByChannel(channel);
        } catch (Exception e) {
            log.error("根据渠道查询代理信息失败，渠道ID: {}", channel, e);
            return null;
        }
    }

    @Override
    public ChannelAllocationResult getChannelResources(Long channel) {
        log.info("开始获取渠道 {} 的业务员和代理信息", channel);
        
        try {
            // 查询该渠道下的业务员
            SysUserEntity salesman = getSalesmanByChannel(channel);
            if (salesman == null) {
                log.warn("渠道 {} 下没有可用的业务员", channel);
                return ChannelAllocationResult.failure("该渠道下没有可用的业务员");
            }
            SysUserEntity agentInfo = sysUserDao.selectById(salesman.getAgent());
            return ChannelAllocationResult.success(
                    salesman.getId(),
                    salesman.getRealName(),
                    agentInfo.getId(),
                    agentInfo.getRealName(),
                    channel
            );

        } catch (Exception e) {
            log.error("获取渠道 {} 的业务员和代理信息时发生异常", channel, e);
            return ChannelAllocationResult.failure("获取失败：" + e.getMessage());
        }
    }

    @Override
    public SysUserEntity getUserById(Long userId) {
        if (userId == null) {
            return null;
        }

        log.debug("根据用户ID获取用户信息，用户ID: {}", userId);

        // 优先从Redis缓存获取
        SysUserEntity cachedUser = redisCacheUtil.getUserCache(userId, SysUserEntity.class);
        if (cachedUser != null) {
            log.debug("从缓存获取用户信息成功，用户ID: {}", userId);
            return cachedUser;
        }

        // 缓存中没有，从数据库查询
        try {
            SysUserEntity user = sysUserDao.selectById(userId);
            if (user != null) {
                // 将查询结果放入缓存
                redisCacheUtil.setUserCache(userId, user);
                log.debug("从数据库查询用户信息成功并放入缓存，用户ID: {}", userId);
            }
            return user;
        } catch (Exception e) {
            log.error("根据用户ID查询用户信息失败，用户ID: {}", userId, e);
            return null;
        }
    }

    @Override
    public String getSalesmanNameById(Long salesmanId) {
        if (salesmanId == null) {
            return null;
        }

        SysUserEntity user = getUserById(salesmanId);
        if (user != null && user.getType() != null && user.getType() == 2) {
            return user.getRealName();
        }
        return null;
    }

    @Override
    public String getAgentNameById(Long agentId) {
        if (agentId == null) {
            return null;
        }

        SysUserEntity user = getUserById(agentId);
        if (user != null && user.getType() != null && user.getType() == 1) {
            return user.getRealName();
        }
        return null;
    }

    @Override
    public void refreshUserCache(Long userId) {
        if (userId == null) {
            return;
        }

        try {
            // 删除旧缓存
            redisCacheUtil.deleteUserCache(userId);
            
            // 重新查询并设置缓存
            SysUserEntity user = sysUserDao.selectById(userId);
            if (user != null) {
                redisCacheUtil.setUserCache(userId, user);
                log.info("刷新用户缓存成功，用户ID: {}", userId);
            }
        } catch (Exception e) {
            log.error("刷新用户缓存失败，用户ID: {}", userId, e);
        }
    }

    @Override
    public void clearUserCache(Long userId) {
        if (userId == null) {
            return;
        }

        redisCacheUtil.deleteUserCache(userId);
        log.info("清除用户缓存成功，用户ID: {}", userId);
    }

    @Override
    public SysUserEntity selectByAgentInviteCode(String agentInviteCode) {
        return sysUserDao.selectByAgentInviteCode(agentInviteCode);
    }
}
