

package io.renren.modules.sys.controller;

import io.renren.common.annotation.LogOperation;
import io.renren.common.constant.Constant;
import io.renren.common.exception.ErrorCode;
import io.renren.common.page.PageData;
import io.renren.common.utils.ConvertUtils;
import io.renren.common.utils.ExcelUtils;
import io.renren.common.utils.Result;
import io.renren.common.validator.AssertUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.common.validator.group.AddGroup;
import io.renren.common.validator.group.DefaultGroup;
import io.renren.common.validator.group.UpdateGroup;
import io.renren.modules.security.user.SecurityUser;
import io.renren.modules.security.user.UserDetail;
import io.renren.modules.sys.dto.PasswordDTO;
import io.renren.modules.sys.dto.SysUserDTO;
import io.renren.modules.sys.dto.CustomerServiceSettingDTO;
import io.renren.modules.sys.excel.SysUserExcel;
import io.renren.modules.security.password.PasswordUtils;
import io.renren.modules.sys.service.SysRoleUserService;
import io.renren.modules.sys.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import io.swagger.annotations.ApiParam;


import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;

/**
 * 系统用户管理
 * 
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/user")
@Api(tags="系统用户管理")
public class SysUserController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysRoleUserService sysRoleUserService;

	@GetMapping("page")
	@ApiOperation("分页")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "username", value = "用户名", paramType = "query", dataType="String")
	})
	@RequiresPermissions("sys:user:page")
	public Result<PageData<SysUserDTO>> page(@ApiParam(value = "用户名", required = false) @RequestParam(required = false) String username) {

		// 构建查询参数
		Map<String, Object> params = new HashMap<>();
		if (StringUtils.isNotBlank(username)) {
			params.put("username", username);
		}
		PageData<SysUserDTO> pageData = sysUserService.page(params);

		return new Result<PageData<SysUserDTO>>().ok(pageData);
	}

	@GetMapping("{id}")
	@ApiOperation("信息")
	@RequiresPermissions("sys:user:info")
	public Result<SysUserDTO> get(@PathVariable("id") Long id){
		SysUserDTO data = sysUserService.get(id);

		//用户角色列表
		List<Long> roleIdList = sysRoleUserService.getRoleIdList(id);
		data.setRoleIdList(roleIdList);

		return new Result<SysUserDTO>().ok(data);
	}

	@GetMapping("info")
	@ApiOperation("登录用户信息")
	public Result<SysUserDTO> info(){
		SysUserDTO data = ConvertUtils.sourceToTarget(SecurityUser.getUser(), SysUserDTO.class);
		return new Result<SysUserDTO>().ok(data);
	}

	@PutMapping("password")
	@ApiOperation("修改密码")
	@LogOperation("修改密码")
	public Result password(@RequestBody PasswordDTO dto){
		//效验数据
		ValidatorUtils.validateEntity(dto);

		UserDetail user = SecurityUser.getUser();

		//原密码不正确
		if(!PasswordUtils.matches(dto.getPassword(), user.getPassword())){
			return new Result().error(ErrorCode.PASSWORD_ERROR);
		}

		sysUserService.updatePassword(user.getId(), dto.getNewPassword());

		return new Result();
	}

	@PostMapping("insert")
	@ApiOperation("保存")
	@LogOperation("保存")
	@RequiresPermissions("sys:user:save")
	public Result save(@RequestBody SysUserDTO dto){
		//效验数据
//		ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

		// 权限控制：检查当前用户类型
		UserDetail currentUser = SecurityUser.getUser();
		if (currentUser == null) {
			return new Result().error("用户未登录");
		}

		// 获取当前用户类型
		Integer currentUserType = currentUser.getType();
		Long currentUserId = currentUser.getId();

		// 权限控制逻辑
		if (currentUserType == 1) {
			// 代理用户：只能创建业务员(type=2)，且agent字段必须等于当前用户ID
			if (dto.getType() == null || dto.getType() != 2) {
				return new Result().error("代理只能创建业务员");
			}
			if (dto.getAgent() == null || !dto.getAgent().equals(currentUserId)) {
				return new Result().error("代理只能创建自己名下的业务员");
			}
		} else if (currentUserType == 2) {
			// 业务员：不能创建其他用户
			return new Result().error("业务员无权限创建用户");
		} else if (currentUserType == 0) {
			// 系统管理员：可以创建所有类型的用户
			// 如果创建的是业务员，需要设置正确的代理ID
			if (dto.getType() != null && dto.getType() == 2) {
				if (dto.getAgent() == null) {
					return new Result().error("创建业务员时必须指定代理ID");
				}
			}
		} else {
			// 其他类型用户：无权限
			return new Result().error("无权限创建用户");
		}

		sysUserService.save(dto);

		return new Result();
	}

	@PutMapping
	@ApiOperation("修改")
	@LogOperation("修改")
	@RequiresPermissions("sys:user:update")
	public Result update(@RequestBody SysUserDTO dto){
		//效验数据
		ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

		// 权限控制：检查当前用户类型
		UserDetail currentUser = SecurityUser.getUser();
		if (currentUser == null) {
			return new Result().error("用户未登录");
		}

		// 获取当前用户类型
		Integer currentUserType = currentUser.getType();
		Long currentUserId = currentUser.getId();

		// 权限控制逻辑
		if (currentUserType == 1) {
			// 代理用户：只能修改自己名下的业务员
			if (dto.getType() == null || dto.getType() != 2) {
				return new Result().error("代理只能修改业务员");
			}
			if (dto.getAgent() == null || !dto.getAgent().equals(currentUserId)) {
				return new Result().error("代理只能修改自己名下的业务员");
			}
		} else if (currentUserType == 2) {
			// 业务员：不能修改其他用户
			return new Result().error("业务员无权限修改用户");
		} else if (currentUserType == 0) {
			// 系统管理员：可以修改所有类型的用户
			// 如果修改的是业务员，需要设置正确的代理ID
			if (dto.getType() != null && dto.getType() == 2) {
				if (dto.getAgent() == null) {
					return new Result().error("修改业务员时必须指定代理ID");
				}
			}
		} else {
			// 其他类型用户：无权限
			return new Result().error("无权限修改用户");
		}

		sysUserService.update(dto);

		return new Result();
	}

	@DeleteMapping
	@ApiOperation("删除")
	@LogOperation("删除")
	@RequiresPermissions("sys:user:delete")
	public Result delete(@RequestBody Long[] ids){
		//效验数据
		AssertUtils.isArrayEmpty(ids, "id");

		// 权限控制：检查当前用户类型
		UserDetail currentUser = SecurityUser.getUser();
		if (currentUser == null) {
			return new Result().error("用户未登录");
		}

		// 获取当前用户类型
		Integer currentUserType = currentUser.getType();
		Long currentUserId = currentUser.getId();

		// 权限控制逻辑
		if (currentUserType == 1) {
			// 代理用户：只能删除自己名下的业务员
			if (!sysUserService.checkAgentDeletePermission(currentUserId, Arrays.asList(ids))) {
				return new Result().error("代理只能删除自己名下的业务员");
			}
		} else if (currentUserType == 2) {
			// 业务员：不能删除其他用户
			return new Result().error("业务员无权限删除用户");
		} else if (currentUserType == 0) {
			// 系统管理员：可以删除所有类型的用户
		} else {
			// 其他类型用户：无权限
			return new Result().error("无权限删除用户");
		}

		sysUserService.deleteBatchIds(Arrays.asList(ids));

		return new Result();
	}

	@PutMapping("editws")
	@ApiOperation("设置客服")
	@LogOperation("设置客服")
	@RequiresPermissions("sys:user:update")
	public Result editws(@RequestBody CustomerServiceSettingDTO dto){
		//效验数据
		ValidatorUtils.validateEntity(dto);
		
		// 只更新客服相关字段
		SysUserDTO updateDto = new SysUserDTO();
		updateDto.setId(dto.getId());
		updateDto.setWsimage(dto.getWsimage());
		updateDto.setWsnumber(dto.getWsnumber());
		updateDto.setWsname(dto.getWsname());
		updateDto.setTgnumber(dto.getTgnumber());

		sysUserService.update(updateDto);

		return new Result();
	}
//
//	@GetMapping("export")
//	@ApiOperation("导出")
//	@LogOperation("导出")
//	@RequiresPermissions("sys:user:export")
//	@ApiImplicitParam(name = "username", value = "用户名", paramType = "query", dataType="String")
//	public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
//		List<SysUserDTO> list = sysUserService.list(params);
//
//		ExcelUtils.exportExcelToTarget(response, null, list, SysUserExcel.class);
//	}
}