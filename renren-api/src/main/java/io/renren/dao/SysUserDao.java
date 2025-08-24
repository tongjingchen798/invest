package io.renren.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统用户Dao
 *
 * @author renren
 * @since 2024-01-01
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUserEntity> {

    /**
     * 根据渠道查询业务员信息
     * @param channel 渠道ID
     * @return 业务员信息列表
     */
    List<SysUserEntity> selectByChannel(@Param("channel") Long channel);

    /**
     * 根据渠道查询代理信息
     * @param channel 渠道ID
     * @return 代理信息列表
     */
    List<SysUserEntity> selectAgentsByChannel(@Param("channel") Long channel);

    /**
     * 根据渠道查询单个业务员信息（用于注册时分配）
     * @param channel 渠道ID
     * @return 业务员信息
     */
    SysUserEntity selectSalesmanByChannel(@Param("channel") Long channel);

    /**
     * 根据渠道查询单个代理信息（用于注册时分配）
     * @param channel 渠道ID
     * @return 代理信息
     */
    SysUserEntity selectAgentByChannel(@Param("channel") Long channel);
}
