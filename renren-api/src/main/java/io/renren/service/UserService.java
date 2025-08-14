

package io.renren.service;

import io.renren.common.service.BaseService;
import io.renren.dto.UserDataSummaryDTO;
import io.renren.entity.UserEntity;
import io.renren.dto.LoginDTO;
import io.renren.dto.UserInfoDTO;

import java.util.Map;

/**
 * 用户
 * 
 * @author Mark sunlightcs@gmail.com
 */
public interface UserService extends BaseService<UserEntity> {

	UserEntity getByMobile(String mobile);


	/**
	 * 用户登录
	 * @param dto    登录表单
	 * @return        返回登录信息
	 */
	Map<String, Object> login(LoginDTO dto);

	/**
	 * 获取用户信息（包含上级用户信息）
	 * @param userId 用户ID
	 * @return 用户信息DTO
	 */
	UserInfoDTO getUserInfoWithSuperior(Long userId);

	/**
	 * 根据手机号更新用户密码
	 * @param mobile 手机号
	 * @param newPassword 新密码
	 * @return 是否更新成功
	 */
	boolean updatePasswordByMobile(String mobile, String newPassword);

	/**
	 * 根据用户ID更新用户密码
	 * @param userId 用户ID
	 * @param newPassword 新密码
	 * @return 是否更新成功
	 */
	boolean updatePasswordByUserId(Long userId, String newPassword);

	/**
	 * 获取用户数据汇总信息
	 * @param userId 用户ID
	 * @return 用户数据汇总DTO
	 */
	UserDataSummaryDTO getUserDataSummary(Long userId);
}
