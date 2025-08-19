package io.renren.service.impl;

import io.renren.common.exception.RenException;
import io.renren.dao.UserDao;
import io.renren.dto.VipClaimStatusDTO;
import io.renren.entity.UserEntity;
import io.renren.service.VipClaimService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * VIP领取状态服务实现类
 * 
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Service
public class VipClaimServiceImpl implements VipClaimService {
	
	// 使用内存数据作为示例，实际项目中应该使用数据库
	// 系统统计信息
	private static final AtomicInteger systemPayCount = new AtomicInteger(0);
	private static final AtomicLong systemPaySumAmount = new AtomicLong(0);
	private static final AtomicInteger systemVipCont = new AtomicInteger(0);

	@Resource
	private UserDao userDao;
	
	@Override
	public VipClaimStatusDTO getUserVipClaimStatus(Long userId) {
		VipClaimStatusDTO status = new VipClaimStatusDTO();
		
		try {
			UserEntity user = userDao.selectById(userId);
			if (user == null) {
				throw new RenException(30001);
			}
			
			// 设置系统统计信息
			status.setPaycount(systemPayCount.get());
			status.setPaysumamount(systemPaySumAmount.get());
			status.setVipcont(systemVipCont.get());
			status.setVip(user.getVip());
//			status.setViplr();
			// 0=不可领取，1=可领取，2=已领取
			setVipLevelStates(status, user.getVip());
			
		} catch (Exception e) {
			e.printStackTrace();
			// 设置默认值
			setDefaultValues(status);
		}
		
		return status;
	}
	
	@Override
	public boolean canClaimVipLevel(Long userId, Integer vipLevel) {
		try {
			int currentVip = calculateUserVipLevel(userId);
			// 只能领取比当前等级高的VIP
			return vipLevel > currentVip;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean claimVipLevel(Long userId, Integer vipLevel) {
		try {
			// 检查是否可以领取
			if (!canClaimVipLevel(userId, vipLevel)) {
				return false;
			}
			// 更新系统统计
			if (vipLevel >= 3) {
				systemVipCont.incrementAndGet();
			}
			
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public VipClaimStatusDTO getSystemVipStats() {
		VipClaimStatusDTO stats = new VipClaimStatusDTO();
		
		// 设置系统统计信息
		stats.setPaycount(systemPayCount.get());
		stats.setPaysumamount(systemPaySumAmount.get());
		stats.setVipcont(systemVipCont.get());
		
		// 其他字段设置为0
		stats.setSvip4state(0L);
		stats.setSvip5state(0L);
		stats.setSvip6state(0L);
		stats.setVip(0);
		stats.setVip1state(0L);
		stats.setVip2state(0L);
		stats.setVip3state(0L);
		stats.setVip4state(0L);
		stats.setVip5state(0L);
		stats.setVip6state(0L);
		stats.setViplr(0);
		
		return stats;
	}
	
	@Override
	public Integer calculateUserVipLevel(Long userId) {
		try {
			// TODO: 实际项目中应该根据用户的投资金额、投资次数等计算VIP等级
			// 这里使用简单的模拟逻辑
			
			// 模拟：根据用户ID的哈希值计算VIP等级
			int hash = userId.hashCode();
			int vipLevel = Math.abs(hash % 7); // 0-6级
			
			return vipLevel;
			
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 设置VIP等级状态
	 */
	private void setVipLevelStates(VipClaimStatusDTO status, int userVipLevel) {
		// 根据用户当前VIP等级设置各等级的状态
		for (int i = 1; i <= 6; i++) {
			long state;
			if (i <= userVipLevel) {
				state = 2L; // 已领取
			} else if (i == userVipLevel + 1) {
				state = 1L; // 可领取
			} else {
				state = 0L; // 不可领取
			}
			
			switch (i) {
				case 1:
					status.setVip1state(state);
					break;
				case 2:
					status.setVip2state(state);
					break;
				case 3:
					status.setVip3state(state);
					break;
				case 4:
					status.setVip4state(state);
					break;
				case 5:
					status.setVip5state(state);
					break;
				case 6:
					status.setVip6state(state);
					break;
			}
		}
		
		// 设置SVIP状态（类似逻辑）
		int userSvipLevel = Math.max(0, userVipLevel - 3);
		for (int i = 4; i <= 6; i++) {
			long state;
			if (i <= userSvipLevel) {
				state = 2L; // 已领取
			} else if (i == userSvipLevel + 1) {
				state = 1L; // 可领取
			} else {
				state = 0L; // 不可领取
			}
			
			switch (i) {
				case 4:
					status.setSvip4state(state);
					break;
				case 5:
					status.setSvip5state(state);
					break;
				case 6:
					status.setSvip6state(state);
					break;
			}
		}
	}
	
	/**
	 * 设置默认值
	 */
	private void setDefaultValues(VipClaimStatusDTO status) {
		status.setPaycount(0);
		status.setPaysumamount(0L);
		status.setSvip4state(0L);
		status.setSvip5state(0L);
		status.setSvip6state(0L);
		status.setVip(0);
		status.setVip1state(0L);
		status.setVip2state(0L);
		status.setVip3state(0L);
		status.setVip4state(0L);
		status.setVip5state(0L);
		status.setVip6state(0L);
		status.setVipcont(0);
		status.setViplr(0);
	}
	
	/**
	 * 更新系统统计信息（可以配置定时任务调用）
	 */
	public void updateSystemStats() {
		// TODO: 实际项目中应该从数据库统计
		// 这里只是示例
		systemPayCount.set(0);
		systemPaySumAmount.set(0L);
		systemVipCont.set(0);
	}
}
