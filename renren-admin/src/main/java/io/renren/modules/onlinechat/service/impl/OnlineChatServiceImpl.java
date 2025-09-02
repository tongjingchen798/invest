package io.renren.modules.onlinechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.onlinechat.dao.OnlineChatDao;
import io.renren.modules.onlinechat.entity.OnlineChatEntity;
import io.renren.modules.onlinechat.service.OnlineChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 在线聊天服务实现类
 *
 * @author renren
 * @since 1.0.0
 */
@Service
@Slf4j
public class OnlineChatServiceImpl extends ServiceImpl<OnlineChatDao, OnlineChatEntity> implements OnlineChatService {

    @Autowired
    private OnlineChatDao onlineChatDao;

    @Override
    public Integer getUnreadCount(String chatId) {
        try {
            log.debug("查询聊天 {} 中的未读消息数量", chatId);
            
            // 查询未读消息数量
            Integer unreadCount = onlineChatDao.getUnreadCount(chatId);
            
            // 如果查询结果为null，返回0
            return unreadCount != null ? unreadCount : 0;
            
        } catch (Exception e) {
            log.error("查询未读消息数量失败，聊天ID: {}", chatId, e);
            // 发生异常时返回0，避免影响用户体验
            return 0;
        }
    }
}
