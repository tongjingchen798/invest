package io.renren.controller;

import io.renren.annotation.Login;
import io.renren.annotation.LoginUser;
import io.renren.common.page.PageData;
import io.renren.common.utils.Result;
import io.renren.common.validator.ValidatorUtils;
import io.renren.dto.PlaceOrderDTO;
import io.renren.dto.ProjectDTO;
import io.renren.dto.ProjectDetailDTO;
import io.renren.dto.ProjectTypeDTO;
import io.renren.entity.ProjectEntity;
import io.renren.entity.UserEntity;
import io.renren.service.OrderService;
import io.renren.service.ProjectService;
import io.renren.service.ProjectTypeService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 项目相关接口
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@RestController
@RequestMapping("/api/project")
@Api(tags = "项目相关接口")
public class ApiProjectController {
    
    @Autowired
    private OrderService orderService;


    @Resource
    private ProjectService projectService;

    @Resource
    private ProjectTypeService projectTypeService;

    @GetMapping("listGroup")
    @ApiOperation("投资项目列表")
    public Result<List<ProjectDTO>> listGroup() {
        List<ProjectDTO> list = projectService.queryListGroup();
        return new Result<List<ProjectDTO>>().ok(list);
    }

    @GetMapping("list")
    @ApiOperation("投资项目类型列表")
    public Result<List<ProjectTypeDTO>> list() {
        List<ProjectTypeDTO> list = projectTypeService.queryList();
        return new Result<List<ProjectTypeDTO>>().ok(list);
    }
    
    	@Login
	@PostMapping("placeAnOrder")
	@ApiOperation("下单")
	public Result<Map<String, String>> placeOrder(@RequestBody PlaceOrderDTO dto, @LoginUser UserEntity user) {
		try {
			// 参数验证
			ValidatorUtils.validateEntity(dto);
			
			// 调用下单服务
			Map<String, String> result = orderService.placeOrder(dto, user.getId());
			
			if ("success".equals(result.get("status"))) {
				return new Result<Map<String, String>>().ok(result);
			} else {
				return new Result<Map<String, String>>().error(result.get("message"));
			}
			
		} catch (Exception e) {
			return new Result<Map<String, String>>().error("下单失败: " + e.getMessage());
		}
	}

	@GetMapping("page")
	@ApiOperation("投资项目数据分页查询")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "page", value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
		@ApiImplicitParam(name = "limit", value = "每页显示记录数", paramType = "query", required = true, dataType = "int")
	})
	public Result<PageData<ProjectEntity>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
		try {
			PageData<ProjectEntity> pageData = projectService.getProjectPage(params);
			return new Result<PageData<ProjectEntity>>().ok(pageData);
		} catch (Exception e) {
			return new Result<PageData<ProjectEntity>>().error("查询失败：" + e.getMessage());
		}
	}

    @GetMapping("/{projectId}")
    @ApiOperation("获取项目详情")
    public Result<ProjectDetailDTO> getProjectDetail(
            @ApiParam(value = "项目ID", required = true) @PathVariable Long projectId) {

        try {
            // 参数验证
            if (projectId == null || projectId <= 0) {
                return new Result<ProjectDetailDTO>().error("项目ID无效");
            }

            // 获取项目详情
            ProjectDetailDTO projectDetail = projectService.getProjectDetail(projectId);

            if (projectDetail == null) {
                return new Result<ProjectDetailDTO>().error("项目不存在");
            }

            return new Result<ProjectDetailDTO>().ok(projectDetail);

        } catch (Exception e) {
            return new Result<ProjectDetailDTO>().error("获取项目详情失败: " + e.getMessage());
        }
    }

	
}
