
 package io.renren.controller;

 import io.renren.common.utils.Result;
 import io.renren.dto.ProjectDTO;
 import io.renren.dto.ProjectTypeDTO;
 import io.renren.service.ProjectService;
 import io.renren.service.ProjectTypeService;
 import io.swagger.annotations.Api;
 import io.swagger.annotations.ApiOperation;
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
 }