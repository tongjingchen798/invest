package io.renren.modules.onlinechat.service;

/**
 * 在线聊天服务接口
 *
 * @author renren
 * @since 1.0.0
 */
public interface OnlineChatService {

    /**
     * 获取未读消息数量
     *
     * @param chatId 聊天ID
     * @return 未读消息数量
     */
    Integer getUnreadCount(String chatId);
}
