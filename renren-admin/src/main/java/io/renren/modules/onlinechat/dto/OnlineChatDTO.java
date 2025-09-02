package io.renren.modules.onlinechat.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 在线聊天DTO
 *
 * @author renren
 * @since 1.0.0
 */
@Data
@ApiModel(value = "在线聊天")
public class OnlineChatDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "聊天ID")
    private String chatId;

    @ApiModelProperty(value = "发送者ID")
    private Long senderId;

    @ApiModelProperty(value = "接收者ID")
    private Long receiverId;

    @ApiModelProperty(value = "消息内容")
    private String message;

    @ApiModelProperty(value = "消息类型 1:文本 2:图片 3:文件")
    private Integer messageType;

    @ApiModelProperty(value = "是否已读 0:未读 1:已读")
    private Integer isRead;

    @ApiModelProperty(value = "发送时间")
    private Date sendTime;

    @ApiModelProperty(value = "读取时间")
    private Date readTime;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "更新时间")
    private Date updateDate;
}
