package io.renren.modules.mail.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 站内信
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Data
@TableName("tb_mail")
public class MailEntity {

    /**
     * 站内信ID
     */
    @TableId
    private Long mailId;

    /**
     * 标题
     */
    private String theme;

    /**
     * 内容
     */
    private String content;

    /**
     * 发件人，如果是前端发过来的，就是前端的用户账号，如果是后台发的，默认叫system
     */
    private String fromUser;

    /**
     * 收件人账号，为空就是所有人
     */
    private String addresseename;

    /**
     * 消息类型，1 是指定代理 2 是所有人
     */
    private Integer dataType;

    /**
     * 是否前端发送 1 是 2 不是 2是后台
     */
    private Integer isWebsend;

    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 过期时间，默认发送时间加7天
     */
    private Date expirationTime;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新时间
     */
    private Date updateDate;
}
