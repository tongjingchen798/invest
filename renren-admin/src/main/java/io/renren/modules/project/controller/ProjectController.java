

package io.renren.modules.project.controller;

import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.utils.Result;
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
@RequestMapping("/investProject")
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

}