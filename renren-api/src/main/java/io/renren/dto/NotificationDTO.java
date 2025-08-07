/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 通知消息DTO
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@ApiModel(value = "通知消息")
public class NotificationDTO {
    @ApiModelProperty(value = "消息ID")
    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "类型(1:系统通知, 2:活动通知, 3:安全通知)")
    private Integer type;

    @ApiModelProperty(value = "是否已读")
    private Boolean isRead;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;
}
