

package io.renren.modules.sys.service;

import io.renren.common.page.PageData;
import io.renren.common.service.BaseService;
import io.renren.modules.sys.dto.SysUserDTO;
import io.renren.modules.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;


/**
 * 系统用户
 * 
 * @author Mark sunlightcs@gmail.com
 */
public interface SysUserService extends BaseService<SysUserEntity> {

	/**
	 * 分页查询用户（使用Map参数）
	 */
	PageData<SysUserDTO> page(Map<String, Object> params);
	
	/**
	 * 分页查询用户（直接参数）
	 * @param page 页码
	 * @param limit 每页大小
	 * @param username 用户名（模糊查询）
	 * @param gender 性别
	 * @param deptId 部门ID
	 * @param order 排序方式
	 * @param orderField 排序字段
	 * @return 分页数据
	 */
	PageData<SysUserDTO> page(Integer page, Integer limit, String username, String gender, String deptId, String order, String orderField);

	List<SysUserDTO> list(Map<String, Object> params);

	SysUserDTO get(Long id);

	SysUserDTO getByUsername(String username);

	void save(SysUserDTO dto);

	void update(SysUserDTO dto);

	void delete(Long[] ids);

	/**
	 * 修改密码
	 * @param id           用户ID
	 * @param newPassword  新密码
	 */
	void updatePassword(Long id, String newPassword);

	/**
	 * 根据部门ID，查询用户数
	 */
	int getCountByDeptId(Long deptId);

	/**
	 * 根据部门ID,查询用户Id列表
	 */
	List<Long> getUserIdListByDeptId(List<Long> deptIdList);
}
