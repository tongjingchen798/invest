package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户任务信息DTO
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@ApiModel(value = "用户任务信息")
public class UserTaskInfoDTO {

    @ApiModelProperty(value = "任务1完成数量")
    private Integer completeNum1;

    @ApiModelProperty(value = "任务2完成数量")
    private Integer completeNum2;

    @ApiModelProperty(value = "任务3完成数量")
    private Integer completeNum3;

    @ApiModelProperty(value = "任务4完成数量")
    private Integer completeNum4;

    @ApiModelProperty(value = "任务5完成数量")
    private Integer completeNum5;

    public UserTaskInfoDTO() {
        // 初始化默认值
        this.completeNum1 = 0;
        this.completeNum2 = 0;
        this.completeNum3 = 0;
        this.completeNum4 = 0;
        this.completeNum5 = 0;
    }
}
