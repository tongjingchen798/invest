package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户数据汇总
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@ApiModel(value = "用户数据汇总")
public class UserDataSummaryDTO {
    
    @ApiModelProperty(value = "总充值金额(分)")
    private Long chargeMoney;
    
    @ApiModelProperty(value = "充值次数")
    private Long chargeNum;
    
    @ApiModelProperty(value = "总投资收益(分)")
    private Long investmentIncome;
    
    @ApiModelProperty(value = "总邀请收益(分)")
    private Long inviteIncome;
    
    @ApiModelProperty(value = "邀请人数")
    private Integer inviteNum;
    
    @ApiModelProperty(value = "等级")
    private Integer vip;
    
    @ApiModelProperty(value = "总提现金额(分)")
    private Long withdrawMoney;
}
