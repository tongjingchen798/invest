package io.renren.service.impl;

import io.renren.dao.UserDao;
import io.renren.dto.UserTaskInfoDTO;
import io.renren.entity.UserEntity;
import io.renren.service.UserTaskInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户任务信息服务实现类
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Slf4j
@Service("userTaskInfoService")
public class UserTaskInfoServiceImpl implements UserTaskInfoService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserTaskInfoDTO getUserTaskInfo(Long userId) {
        try {
            if (userId == null) {
                log.warn("用户ID为空，返回默认任务信息");
                return new UserTaskInfoDTO();
            }

            // 获取用户信息
            UserEntity user = userDao.selectById(userId);
            if (user == null) {
                log.warn("用户不存在，用户ID: {}", userId);
                return new UserTaskInfoDTO();
            }

            UserTaskInfoDTO taskInfo = new UserTaskInfoDTO();

            // 根据用户的各种数据计算任务完成数量
            // 这里根据具体的业务需求来定义任务完成的数量计算规则
            
            // 所有任务都根据用户总推荐人数来判断
            if (user.getTgrs() != null && user.getTgrs() > 0) {
                int recommendCount = user.getTgrs().intValue();
                int remainingCount = recommendCount;
                
                // 任务1：推荐人数任务（最大1）
                int task1Count = Math.min(remainingCount, 1);
                taskInfo.setCompleteNum1(task1Count);
                remainingCount -= task1Count;
                
                // 任务2：推荐人数额外任务（最大2）
                if (remainingCount > 0) {
                    int task2Count = Math.min(remainingCount, 2);
                    taskInfo.setCompleteNum2(task2Count);
                    remainingCount -= task2Count;
                }
                
                // 任务3：投资金额任务（最大2）
                if (remainingCount > 0) {
                    int task3Count = Math.min(remainingCount, 2);
                    taskInfo.setCompleteNum3(task3Count);
                    remainingCount -= task3Count;
                }
                
                // 任务4：充值任务（最大2）
                if (remainingCount > 0) {
                    int task4Count = Math.min(remainingCount, 2);
                    taskInfo.setCompleteNum4(task4Count);
                    remainingCount -= task4Count;
                }
                
                // 任务5：收益任务（最大5）
                if (remainingCount > 0) {
                    int task5Count = Math.min(remainingCount, 5);
                    taskInfo.setCompleteNum5(task5Count);
                }
            }

            log.info("获取用户任务信息成功，用户ID: {}, 任务信息: {}", userId, taskInfo);
            return taskInfo;

        } catch (Exception e) {
            log.error("获取用户任务信息失败，用户ID: {}", userId, e);
            // 发生异常时返回默认值
            return new UserTaskInfoDTO();
        }
    }

    @Override
    public UserTaskInfoDTO getCurrentUserTaskInfo() {
        try {
            // 这里需要根据实际的用户认证机制来获取当前用户ID
            // 暂时返回默认值，实际使用时需要集成认证系统
            
            log.info("获取当前用户任务信息，暂未实现用户认证");
            return new UserTaskInfoDTO();
            
        } catch (Exception e) {
            log.error("获取当前用户任务信息失败", e);
            return new UserTaskInfoDTO();
        }
    }
}
