

package io.renren.service.impl;

import io.renren.common.exception.ErrorCode;
import io.renren.common.exception.RenException;
import io.renren.common.service.impl.BaseServiceImpl;
import io.renren.common.validator.AssertUtils;
import io.renren.dao.UserDao;
import io.renren.entity.TokenEntity;
import io.renren.entity.UserEntity;
import io.renren.dto.LoginDTO;
import io.renren.dto.UserInfoDTO;
import io.renren.dto.SuperiorUserInfoDTO;
import io.renren.dto.UserDataSummaryDTO;
import io.renren.service.TokenService;
import io.renren.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl extends BaseServiceImpl<UserDao, UserEntity> implements UserService {
	@Autowired
	private TokenService tokenService;

	@Override
	public UserEntity getByMobile(String mobile) {
		return baseDao.getUserByMobile(mobile);
	}


	@Override
	public Map<String, Object> login(LoginDTO dto) {
		UserEntity user = getByMobile(dto.getMobile());
		AssertUtils.isNull(user, ErrorCode.ACCOUNT_PASSWORD_ERROR);

		//密码错误
		if(!user.getPassword().equals(DigestUtils.sha256Hex(dto.getPassword()))){
			throw new RenException(ErrorCode.ACCOUNT_PASSWORD_ERROR);
		}

		//获取登录token
		TokenEntity tokenEntity = tokenService.createToken(user.getId());

		Map<String, Object> map = new HashMap<>(2);
		map.put("token", tokenEntity.getToken());
		map.put("expire", tokenEntity.getExpireDate().getTime() - System.currentTimeMillis());

		return map;
	}

	@Override
	public UserInfoDTO getUserInfoWithSuperior(Long userId) {
		// 获取用户基本信息
		UserEntity user = baseDao.getUserByUserId(userId);
		if (user == null) {
			return null;
		}

		// 转换为DTO
		UserInfoDTO userInfoDTO = new UserInfoDTO();
		BeanUtils.copyProperties(user, userInfoDTO);

		// 如果用户有上级，获取上级用户信息
		if (StringUtils.isNotBlank(user.getSuperiorCode())) {
			UserEntity superiorUser = baseDao.getUserByInviteCode(user.getSuperiorCode());
			if (superiorUser != null) {
				// 创建上级用户信息DTO
				SuperiorUserInfoDTO superiorInfo = new SuperiorUserInfoDTO();
				superiorInfo.setSuperiorId(superiorUser.getId());
				superiorInfo.setSuperiorUsername(superiorUser.getUsername());
				superiorInfo.setSuperiorInviteCode(superiorUser.getInviteCode());
				superiorInfo.setSuperiorAgent(superiorUser.getAgent());
				superiorInfo.setSuperiorAgentName(superiorUser.getAgentName());
				
				// 设置上级用户的U级账户余额
				superiorInfo.setSuperiorUa(superiorUser.getUacnt());
				superiorInfo.setSuperiorUb(superiorUser.getUbcnt());
				superiorInfo.setSuperiorUc(superiorUser.getUccnt());
				
				userInfoDTO.setSuperiorInfo(superiorInfo);
				
				// 设置上级相关字段到主DTO
				userInfoDTO.setUpinviteCode(superiorUser.getInviteCode());
				userInfoDTO.setSuperiorUa(superiorUser.getUacnt());
				userInfoDTO.setSuperiorUb(superiorUser.getUbcnt());
				userInfoDTO.setSuperiorUc(superiorUser.getUccnt());
			}
		}

		// 设置计算字段
		if (userInfoDTO.getAssets() == null) {
			userInfoDTO.setAssets(user.getBalance());
		}
		if (userInfoDTO.getJrProfit() == null) {
			userInfoDTO.setJrProfit(user.getTodayProfit());
		}
		if (userInfoDTO.getTotalPrincipal() == null) {
			userInfoDTO.setTotalPrincipal(user.getHistoryInvestment());
		}
		if (userInfoDTO.getTotalProfit() == null) {
			userInfoDTO.setTotalProfit(user.getHistoryProfit());
		}
		
		// 设置其他字段的默认值
		if (userInfoDTO.getEndedItems() == null) {
			userInfoDTO.setEndedItems(0L);
		}
		if (userInfoDTO.getEndedPrincipal() == null) {
			userInfoDTO.setEndedPrincipal(0L);
		}
		if (userInfoDTO.getEndedProfit() == null) {
			userInfoDTO.setEndedProfit(0L);
		}
		if (userInfoDTO.getItmes() == null) {
			userInfoDTO.setItmes(0L);
		}
		if (userInfoDTO.getTodaybalance10() == null) {
			userInfoDTO.setTodaybalance10(0L);
		}
		if (userInfoDTO.getTodaybalance20() == null) {
			userInfoDTO.setTodaybalance20(0L);
		}
		if (userInfoDTO.getTodaybalance5() == null) {
			userInfoDTO.setTodaybalance5(0L);
		}
		if (userInfoDTO.getTodaycharge100() == null) {
			userInfoDTO.setTodaycharge100(0L);
		}
		if (userInfoDTO.getTodaycharge20() == null) {
			userInfoDTO.setTodaycharge20(0L);
		}
		if (userInfoDTO.getTodaycharge50() == null) {
			userInfoDTO.setTodaycharge50(0L);
		}
		if (userInfoDTO.getValid3user() == null) {
			userInfoDTO.setValid3user(0L);
		}
		if (userInfoDTO.getValid6user() == null) {
			userInfoDTO.setValid6user(0L);
		}
		if (userInfoDTO.getValid9user() == null) {
			userInfoDTO.setValid9user(0L);
		}
		if (userInfoDTO.getVip1state() == null) {
			userInfoDTO.setVip1state(0L);
		}
		if (userInfoDTO.getVip2state() == null) {
			userInfoDTO.setVip2state(0L);
		}
		if (userInfoDTO.getVip3state() == null) {
			userInfoDTO.setVip3state(0L);
		}
		if (userInfoDTO.getVip4state() == null) {
			userInfoDTO.setVip4state(0L);
		}
		if (userInfoDTO.getVip5state() == null) {
			userInfoDTO.setVip5state(0L);
		}
		if (userInfoDTO.getVip6state() == null) {
			userInfoDTO.setVip6state(0L);
		}

		return userInfoDTO;
	}

	@Override
	public boolean updatePasswordByMobile(String mobile, String newPassword) {
		try {
			// 根据手机号查找用户
			UserEntity user = getByMobile(mobile);
			if (user == null) {
				return false;
			}
			
			// 对新密码进行SHA256加密
			String encryptedPassword = DigestUtils.sha256Hex(newPassword);
			
			// 更新用户密码
			user.setPassword(encryptedPassword);
			
			// 保存到数据库
			baseDao.updateById(user);
			
			return true;
		} catch (Exception e) {
			// 记录日志
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updatePasswordByUserId(Long userId, String newPassword) {
		try {
			// 根据用户ID查找用户
			UserEntity user = getUserByUserId(userId);
			if (user == null) {
				return false;
			}
			
			// 对新密码进行SHA256加密
			String encryptedPassword = DigestUtils.sha256Hex(newPassword);
			
			// 更新用户密码
			user.setPassword(encryptedPassword);
			
			// 保存到数据库
			baseDao.updateById(user);
			
			return true;
		} catch (Exception e) {
			// 记录日志
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public UserDataSummaryDTO getUserDataSummary(Long userId) {
		try {
			// 根据用户ID查找用户
			UserEntity user = getUserByUserId(userId);
			if (user == null) {
				return null;
			}
			
			// 创建用户数据汇总DTO
			UserDataSummaryDTO summaryDTO = new UserDataSummaryDTO();
			
			// 设置充值相关统计
			summaryDTO.setChargeMoney(user.getChargeSum() != null ? user.getChargeSum() : 0L);
			//TODO 充值记录里面拿
//			summaryDTO.setChargeNum(user.getc() != null ? user.getRechargeCount() : 0L);
			summaryDTO.setChargeNum(0L);
			// 设置投资收益统计
			summaryDTO.setInvestmentIncome(user.getHistoryProfit() != null ? user.getHistoryProfit() : 0L);
			
			// 设置邀请相关统计 TODO 邀请人总收益
//			summaryDTO.setInviteIncome(user.getInviteProfit() != null ? user.getInviteProfit() : 0L);
			summaryDTO.setInviteIncome( 0L);
			summaryDTO.setInviteNum(user.getUacnt() != null ? user.getUacnt().intValue() : 0);
			
			// 设置等级
			summaryDTO.setVip(user.getVip() != null ? user.getVip() : 0);
			
			// 设置提现统计
			summaryDTO.setWithdrawMoney(user.getWithdrawSum() != null ? user.getWithdrawSum() : 0L);
			
			return summaryDTO;
			
		} catch (Exception e) {
			// 记录日志
			e.printStackTrace();
			return null;
		}
	}
}