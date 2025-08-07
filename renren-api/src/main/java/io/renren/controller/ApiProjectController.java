package io.renren.controller;

import io.renren.common.utils.Result;
import io.renren.dto.ProjectDTO;
import io.renren.dto.ProjectTypeDTO;
import io.renren.service.ProjectService;
import io.renren.service.ProjectTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import io.renren.dto.PlaceOrderDTO;
import io.renren.common.validator.ValidatorUtils;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;
import java.util.HashMap;

/**
 * 投资项目接口
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/api/project")
@Api(tags = "投资项目接口")
public class ApiProjectController {
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
    
    @GetMapping("{id}")
    @ApiOperation("投资项目信息")
    public Result<ProjectDTO> getById(
            @ApiParam(value = "项目ID", required = true) @PathVariable("id") Long id) {
        ProjectDTO project = projectService.getById(id);
        return new Result<ProjectDTO>().ok(project);
    }

    @PostMapping("placeAnOrder")
    @ApiOperation("下单")
    public Result<Map<String, String>> placeAnOrder(@RequestBody PlaceOrderDTO dto) {
        // 参数校验
        ValidatorUtils.validateEntity(dto);
        
        // TODO: 实现下单逻辑
        // 这里需要根据业务需求实现具体的下单逻辑
        // 包括：验证用户余额、验证项目状态、创建订单、扣除余额等
        
        Map<String, String> result = new HashMap<>();
        result.put("orderId", "ORDER_" + System.currentTimeMillis());
        result.put("status", "success");
        
        return new Result<Map<String, String>>().ok(result);
    }
}