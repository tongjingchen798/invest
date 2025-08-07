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
import io.renren.service.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("listGroup")
    @ApiOperation("投资项目列表")
    public Result<List<ProjectDTO>> listGroup() {
        List<ProjectDTO> list = projectService.queryListGroup();
        return new Result<List<ProjectDTO>>().ok(list);
    }
}