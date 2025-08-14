package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 推荐返利流水详情DTO
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@ApiModel(value = "推荐返利流水详情")
public class ReferralRewardDetailDTO {

    @ApiModelProperty(value = "流水ID")
    private String streamId;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "返利金额（分）")
    private Long rewardAmount;

    @ApiModelProperty(value = "返利金额（卢比）")
    private String rewardAmountRupee;

    @ApiModelProperty(value = "新注册用户ID")
    private Long newUserId;

    @ApiModelProperty(value = "新注册用户手机号")
    private String newUserMobile;

    @ApiModelProperty(value = "交易时间")
    private String transactionDate;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "状态 0:失败 1:成功")
    private Integer status;

    @ApiModelProperty(value = "状态描述")
    private String statusDesc;

    @ApiModelProperty(value = "创建时间")
    private String createDate;
}
