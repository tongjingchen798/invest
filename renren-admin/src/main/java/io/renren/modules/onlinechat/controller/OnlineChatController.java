package io.renren.modules.onlinechat.controller;

import io.renren.common.utils.Result;
import io.renren.modules.onlinechat.service.OnlineChatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 在线聊天管理
 *
 * @author renren
 * @since 1.0.0
 */
@RestController
@RequestMapping("/onlinechat")
@Api(tags = "在线聊天管理")
@Slf4j
public class OnlineChatController {

    @Autowired
    private OnlineChatService onlineChatService;

    @GetMapping("/unread")
    @ApiOperation("获取未读消息数量")
    public Result<Integer> getUnreadCount() {
        try {

            // 获取未读消息数量 todo 可能需要读取wachat
            Integer unreadCount = 0;

            return new Result<Integer>().ok(unreadCount);
            
        } catch (Exception e) {
            return new Result<Integer>().error("获取未读消息数量失败: " + e.getMessage());
        }
    }
}
