package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 签到记录
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@ApiModel(value = "签到记录")
public class SignInRecordDTO {
    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "签到的连续天数")
    private Integer day;

    @ApiModelProperty(value = "签到奖励金额")
    private Long amount;

    @ApiModelProperty(value = "签到类型")
    private Integer qdtype;

    @ApiModelProperty(value = "创建时间")
    private String createDate;

    @ApiModelProperty(value = "创建者")
    private Long creator;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "代理信息")
    private Long agent;

    @ApiModelProperty(value = "业务员ID")
    private Long salesmanid;

    @ApiModelProperty(value = "天分区")
    private Integer partDay;

    @ApiModelProperty(value = "总天数")
    private Integer sumday;

    @ApiModelProperty(value = "签到奖励的优惠券ID")
    private Long couponid;

    @ApiModelProperty(value = "优惠券名称")
    private String couponName;

    @ApiModelProperty(value = "签到奖励的特权券ID")
    private Long privilegeid;

    @ApiModelProperty(value = "特权券名称")
    private String privilegeName;
}
