package io.renren.service;

import io.renren.dto.UserTaskInfoDTO;

/**
 * 用户任务信息服务接口
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
public interface UserTaskInfoService {

    /**
     * 获取用户任务完成信息
     * @param userId 用户ID
     * @return 用户任务信息
     */
    UserTaskInfoDTO getUserTaskInfo(Long userId);


}
