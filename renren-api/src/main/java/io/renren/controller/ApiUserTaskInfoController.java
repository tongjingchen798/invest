package io.renren.controller;

import io.renren.common.utils.Result;
import io.renren.dto.UserTaskInfoDTO;
import io.renren.service.UserTaskInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户任务信息接口
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Slf4j
@RestController
@RequestMapping("/api")
@Api(tags="用户任务信息接口")
public class ApiUserTaskInfoController {

    @Autowired
    private UserTaskInfoService userTaskInfoService;

    @GetMapping("userTaskInfo")
    @ApiOperation("获取用户任务完成信息")
    public Result getUserTaskInfo(
            @ApiParam(value = "用户ID", required = false) 
            @RequestParam(value = "userId", required = false) Long userId) {
        
        try {
            UserTaskInfoDTO taskInfo;
            
            if (userId != null) {
                // 如果提供了用户ID，获取指定用户的任务信息
                taskInfo = userTaskInfoService.getUserTaskInfo(userId);
            } else {
                // 如果没有提供用户ID，获取当前登录用户的任务信息
                taskInfo = userTaskInfoService.getCurrentUserTaskInfo();
            }
            
            return new Result().ok(taskInfo);
            
        } catch (Exception e) {
            log.error("获取用户任务信息失败，用户ID: {}", userId, e);
            return new Result().error("获取用户任务信息失败: " + e.getMessage());
        }
    }
}
