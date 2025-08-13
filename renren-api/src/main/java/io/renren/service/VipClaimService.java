package io.renren.service;

import io.renren.dto.VipClaimStatusDTO;

/**
 * VIP领取状态服务接口
 * 
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
public interface VipClaimService {
	
	/**
	 * 获取用户VIP领取状态
	 * @param userId 用户ID
	 * @return VIP领取状态
	 */
	VipClaimStatusDTO getUserVipClaimStatus(Long userId);
	
	/**
	 * 检查用户是否可以领取指定VIP等级
	 * @param userId 用户ID
	 * @param vipLevel VIP等级
	 * @return 是否可以领取
	 */
	boolean canClaimVipLevel(Long userId, Integer vipLevel);
	
	/**
	 * 领取VIP等级
	 * @param userId 用户ID
	 * @param vipLevel VIP等级
	 * @return 是否领取成功
	 */
	boolean claimVipLevel(Long userId, Integer vipLevel);
	
	/**
	 * 获取系统VIP统计信息
	 * @return VIP统计信息
	 */
	VipClaimStatusDTO getSystemVipStats();
	
	/**
	 * 计算用户VIP等级
	 * @param userId 用户ID
	 * @return 计算后的VIP等级
	 */
	Integer calculateUserVipLevel(Long userId);
}
