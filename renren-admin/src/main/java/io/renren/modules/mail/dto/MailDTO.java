package io.renren.modules.mail.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 站内信
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Data
@ApiModel(value = "站内信")
public class MailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "站内信ID")
    private Long mailId;

    @ApiModelProperty(value = "标题")
    private String theme;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "发件人，如果是前端发过来的，就是前端的用户账号，如果是后台发的，默认叫system")
    private String fromUser;

    @ApiModelProperty(value = "收件人账号，为空就是所有人")
    private String addresseename;

    @ApiModelProperty(value = "消息类型，1 是指定代理 2 是所有人")
    private Integer dataType;

    @ApiModelProperty(value = "是否前端发送 1 是 2 不是 2是后台")
    private Integer isWebsend;

    @ApiModelProperty(value = "发送时间")
    private String sendTime;

    @ApiModelProperty(value = "过期时间，默认发送时间加7天")
    private String expirationTime;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "更新时间")
    private Date updateDate;
}
