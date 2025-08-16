

package io.renren.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.service.impl.BaseServiceImpl;
import io.renren.common.utils.ConvertUtils;
import io.renren.modules.security.user.SecurityUser;
import io.renren.modules.security.user.UserDetail;
import io.renren.modules.sys.dao.SysUserDao;
import io.renren.modules.sys.dto.SysUserDTO;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.security.password.PasswordUtils;
import io.renren.modules.sys.enums.SuperAdminEnum;
import io.renren.modules.sys.service.SysDeptService;
import io.renren.modules.sys.service.SysRoleUserService;
import io.renren.modules.sys.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 系统用户
 * 
 * @author Mark sunlightcs@gmail.com
 */
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {
	@Autowired
	private SysRoleUserService sysRoleUserService;
	@Autowired
	private SysDeptService sysDeptService;

	@Override
	public PageData<SysUserDTO> page(Map<String, Object> params) {
		//转换成like
		paramsToLike(params, "username");

		//分页
		IPage<SysUserEntity> page = getPage(params, Constant.CREATE_DATE, false);

		//普通管理员，只能查询所属部门及子部门的数据
		UserDetail user = SecurityUser.getUser();
		if(user.getSuperAdmin() == SuperAdminEnum.NO.value()) {
			params.put("deptIdList", sysDeptService.getSubDeptIdList(user.getDeptId()));
		}

		//查询
		List<SysUserEntity> list = baseDao.getList(params);

		return getPageData(list, page.getTotal(), SysUserDTO.class);
	}
	
	@Override
	public PageData<SysUserDTO> page(Integer page, Integer limit, String username, String gender, String deptId, String order, String orderField) {
		// 创建MyBatis-Plus分页对象
		Page<SysUserEntity> pageParam = new Page<>(page, limit);
		
		// 构建查询条件
		QueryWrapper<SysUserEntity> queryWrapper = new QueryWrapper<>();
		
		// 只查询非超级管理员用户
		queryWrapper.eq("super_admin", 0);
		
		// 用户名模糊查询
		if (StringUtils.isNotBlank(username)) {
			queryWrapper.like("username", username);
		}
		
		// 部门ID筛选
		if (StringUtils.isNotBlank(deptId)) {
			queryWrapper.eq("dept_id", deptId);
		}
		
		// 性别筛选
		if (StringUtils.isNotBlank(gender)) {
			queryWrapper.eq("gender", gender);
		}
		
		// 普通管理员，只能查询所属部门及子部门的数据
		UserDetail user = SecurityUser.getUser();
		if(user.getSuperAdmin() == SuperAdminEnum.NO.value()) {
			List<Long> deptIdList = sysDeptService.getSubDeptIdList(user.getDeptId());
			if (deptIdList != null && !deptIdList.isEmpty()) {
				queryWrapper.in("dept_id", deptIdList);
			}
		}
		
		// 排序处理
		if (StringUtils.isNotBlank(orderField)) {
			if (Constant.DESC.equalsIgnoreCase(order)) {
				queryWrapper.orderByDesc(orderField);
			} else {
				queryWrapper.orderByAsc(orderField);
			}
		} else {
			// 默认按创建时间倒序排序
			queryWrapper.orderByDesc(Constant.CREATE_DATE);
		}
		
		// 执行分页查询
		IPage<SysUserEntity> pageResult = baseDao.selectPage(pageParam, queryWrapper);
		
		// 转换为DTO
		List<SysUserDTO> dtoList = ConvertUtils.sourceToTarget(pageResult.getRecords(), SysUserDTO.class);
		
		// 设置部门名称
		for (SysUserDTO dto : dtoList) {
			if (dto.getDeptId() != null) {
				// 这里可以根据需要查询部门名称
				// dto.setDeptName(deptService.getDeptName(dto.getDeptId()));
			}
		}
		
		return new PageData<>(dtoList, pageResult.getTotal());
	}

	@Override
	public List<SysUserDTO> list(Map<String, Object> params) {
		//普通管理员，只能查询所属部门及子部门的数据
		UserDetail user = SecurityUser.getUser();
		if(user.getSuperAdmin() == SuperAdminEnum.NO.value()) {
			params.put("deptIdList", sysDeptService.getSubDeptIdList(user.getDeptId()));
		}

		List<SysUserEntity> entityList = baseDao.getList(params);

		return ConvertUtils.sourceToTarget(entityList, SysUserDTO.class);
	}

	@Override
	public SysUserDTO get(Long id) {
		SysUserEntity entity = baseDao.getById(id);

		return ConvertUtils.sourceToTarget(entity, SysUserDTO.class);
	}

	@Override
	public SysUserDTO getByUsername(String username) {
		SysUserEntity entity = baseDao.getByUsername(username);
		return ConvertUtils.sourceToTarget(entity, SysUserDTO.class);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(SysUserDTO dto) {
		SysUserEntity entity = ConvertUtils.sourceToTarget(dto, SysUserEntity.class);
		entity.setPassword(PasswordUtils.encode(entity.getPassword()));
		insert(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(SysUserDTO dto) {
		SysUserEntity entity = ConvertUtils.sourceToTarget(dto, SysUserEntity.class);
		updateById(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long[] ids) {
		//删除用户
		deleteBatchIds(Arrays.asList(ids));
		//删除用户角色关系
		sysRoleUserService.deleteByUserIds(ids);
	}

	@Override
	public void updatePassword(Long id, String newPassword) {
		baseDao.updatePassword(id, newPassword);
	}

	@Override
	public int getCountByDeptId(Long deptId) {
		return baseDao.getCountByDeptId(deptId);
	}

	@Override
	public List<Long> getUserIdListByDeptId(List<Long> deptIdList) {
		return baseDao.getUserIdListByDeptId(deptIdList);
	}
}
