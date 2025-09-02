package io.renren.modules.onlinechat.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 在线聊天实体类
 *
 * @author renren
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_online_chat")
public class OnlineChatEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 聊天ID
     */
    @TableId
    private String chatId;

    /**
     * 发送者ID
     */
    private Long senderId;

    /**
     * 接收者ID
     */
    private Long receiverId;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 消息类型 1:文本 2:图片 3:文件
     */
    private Integer messageType;

    /**
     * 是否已读 0:未读 1:已读
     */
    private Integer isRead;

    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 读取时间
     */
    private Date readTime;
}
