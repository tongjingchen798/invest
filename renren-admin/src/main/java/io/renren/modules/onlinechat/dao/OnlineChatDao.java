package io.renren.modules.onlinechat.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.onlinechat.entity.OnlineChatEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 在线聊天数据访问接口
 *
 * @author renren
 * @since 1.0.0
 */
@Mapper
public interface OnlineChatDao extends BaseDao<OnlineChatEntity> {

    /**
     * 获取未读消息数量
     *
     * @param chatId 聊天ID
     * @return 未读消息数量
     */
    Integer getUnreadCount(@Param("chatId") String chatId);
}
