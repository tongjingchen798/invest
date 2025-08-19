package io.renren.controller;

import io.renren.annotation.Login;
import io.renren.annotation.LoginUser;
import io.renren.common.utils.Result;
import io.renren.dto.UserTaskInfoDTO;
import io.renren.entity.UserEntity;
import io.renren.service.UserTaskInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @Login
    @PostMapping("userTaskInfo")
    @ApiOperation("获取用户任务完成信息")
    public Result getUserTaskInfo(@LoginUser UserEntity user) {
        try {
            UserTaskInfoDTO taskInfo = userTaskInfoService.getUserTaskInfo(user.getId());
            return new Result().ok(taskInfo);
        } catch (Exception e) {
            return new Result().error("获取用户任务信息失败: " + e.getMessage());
        }
    }
}
