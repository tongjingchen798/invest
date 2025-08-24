package io.renren.service.impl;

import io.renren.dao.SysUserDao;
import io.renren.dto.ChannelAllocationResult;
import io.renren.entity.SysUserEntity;
import io.renren.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统用户服务实现类
 *
 * @author renren
 * @since 2024-01-01
 */
@Slf4j
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserDao sysUserDao;

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

            // 查询该渠道下的代理
            SysUserEntity agent = getAgentByChannel(channel);
            if (agent == null) {
                log.warn("渠道 {} 下没有可用的代理", channel);
                return ChannelAllocationResult.failure("该渠道下没有可用的代理");
            }

            log.info("渠道 {} 获取成功 - 业务员: {} (ID: {}), 代理: {} (ID: {})", 
                    channel, salesman.getRealName(), salesman.getId(),
                    agent.getRealName(), agent.getId());

            return ChannelAllocationResult.success(
                    salesman.getId(),
                    salesman.getRealName(),
                    agent.getId(),
                    agent.getRealName(),
                    channel
            );

        } catch (Exception e) {
            log.error("获取渠道 {} 的业务员和代理信息时发生异常", channel, e);
            return ChannelAllocationResult.failure("获取失败：" + e.getMessage());
        }
    }
}
