package io.renren.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.entity.PayChannelEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 支付通道
 * 
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface PayChannelDao extends BaseMapper<PayChannelEntity> {
    
    /**
     * 查询所有上架的支付通道
     * 
     * @return 支付通道列表
     */
    List<PayChannelEntity> selectAllActiveChannels();
    
    /**
     * 根据充提类型查询支付通道
     * 
     * @param chargeorwithdraw 充提类型 1:充值 2:提现
     * @return 支付通道列表
     */
    List<PayChannelEntity> selectChannelsByType(@Param("chargeorwithdraw") String chargeorwithdraw);
    
    /**
     * 根据通道类型查询支付通道
     * 
     * @param channelType 通道类型 UPI/SWIPE/USDT
     * @return 支付通道列表
     */
    List<PayChannelEntity> selectChannelsByChannelType(@Param("channelType") String channelType);
}