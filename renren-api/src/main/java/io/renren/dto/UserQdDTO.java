package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户签到记录
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@ApiModel(value = "用户签到记录")
public class UserQdDTO {
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "签到的连续天数")
    private Long day;

    @ApiModelProperty(value = "今日是否签到;0否，1是")
    private Integer todayQd;
}
