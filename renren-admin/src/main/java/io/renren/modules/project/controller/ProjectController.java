

package io.renren.modules.project.controller;

import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.utils.Result;
import io.renren.common.validator.ValidatorUtils;
import io.renren.common.validator.group.AddGroup;
import io.renren.common.validator.group.DefaultGroup;
import io.renren.modules.project.entity.ProjectEntity;
import io.renren.modules.project.service.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * 项目管理
 * 
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@RestController
@RequestMapping("/admin/investProject")
@Api(tags="项目管理")
public class ProjectController {
	@Autowired
	private ProjectService projectService;

	@GetMapping("page")
	@ApiOperation("分页查询项目")
	@ApiImplicitParams({
		@ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
		@ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
		@ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
		@ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String") ,
		@ApiImplicitParam(name = "investName", value = "项目名", paramType = "query", dataType="String") ,
		@ApiImplicitParam(name = "status", value = "项目状态", paramType = "query", dataType="String") ,
		@ApiImplicitParam(name = "projectType", value = "项目类型", paramType = "query", dataType="String") ,
		@ApiImplicitParam(name = "cycleType", value = "周期类型", paramType = "query", dataType="String")
	})
	public Result<PageData<ProjectEntity>> page(@ApiIgnore @RequestParam Map<String, Object> params){
		try {
			PageData<ProjectEntity> pageData = projectService.getProjectPage(params);
			return new Result<PageData<ProjectEntity>>().ok(pageData);
		} catch (Exception e) {
			return new Result<PageData<ProjectEntity>>().error("查询失败：" + e.getMessage());
		}
	}

	@PostMapping
	@ApiOperation("新增项目")
	public Result save(@RequestBody ProjectEntity project){
		try {
			boolean success = projectService.save(project);
			if (success) {
				return new Result().ok("新增项目成功");
			} else {
				return new Result().error("新增项目失败");
			}
		} catch (Exception e) {
			return new Result().error("新增项目失败：" + e.getMessage());
		}
	}

	@PutMapping
	@ApiOperation("修改项目")
	public Result update(@RequestBody ProjectEntity project){
		try {
			// 检查项目ID是否存在
			if (project.getInvestId() == null) {
				return new Result().error("项目ID不能为空");
			}
			
			// 检查项目是否存在
			ProjectEntity existingProject = projectService.getById(project.getInvestId());
			if (existingProject == null) {
				return new Result().error("项目不存在");
			}
			
			// 更新项目
			boolean success = projectService.updateById(project);
			if (success) {
				return new Result().ok("修改项目成功");
			} else {
				return new Result().error("修改项目失败");
			}
		} catch (Exception e) {
			return new Result().error("修改项目失败：" + e.getMessage());
		}
	}

	@DeleteMapping
	@ApiOperation("删除项目")
	public Result delete(@RequestBody Long[] ids){
		try {
			// 参数验证
			if (ids == null || ids.length == 0) {
				return new Result().error("项目ID不能为空");
			}
			
			// 删除项目
			boolean success = projectService.removeByIds(java.util.Arrays.asList(ids));
			if (success) {
				return new Result().ok("删除项目成功");
			} else {
				return new Result().error("删除项目失败");
			}
		} catch (Exception e) {
			return new Result().error("删除项目失败：" + e.getMessage());
		}
	}

//
//	@GetMapping("list")
//	@ApiOperation("获取项目列表")
//	public Result<java.util.List<ProjectEntity>> list(){
//		try {
//			java.util.List<ProjectEntity> list = projectService.list();
//			return new Result<java.util.List<ProjectEntity>>().ok(list);
//		} catch (Exception e) {
//			return new Result<java.util.List<ProjectEntity>>().error("获取项目列表失败：" + e.getMessage());
//		}
//	}
//
//	@GetMapping("active")
//	@ApiOperation("获取上架项目列表")
//	public Result<java.util.List<ProjectEntity>> getActiveProjects(){
//		try {
//			java.util.List<ProjectEntity> list = projectService.list(
//				new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ProjectEntity>()
//					.eq("status", 1)
//					.orderByDesc("sort", "create_date")
//			);
//			return new Result<java.util.List<ProjectEntity>>().ok(list);
//		} catch (Exception e) {
//			return new Result<java.util.List<ProjectEntity>>().error("获取上架项目列表失败：" + e.getMessage());
//		}
//	}
	
}