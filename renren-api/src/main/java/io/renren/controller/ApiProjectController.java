/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

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
 }