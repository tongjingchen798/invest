package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * VIP领取状态
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@ApiModel(value = "VIP领取状态")
public class VipClaimStatusDTO {
    
    @ApiModelProperty(value = "购买1000以上产品的会员数")
    private Integer paycount;
    
    @ApiModelProperty(value = "购买的设备总额")
    private Long paysumamount;
    
    @ApiModelProperty(value = "SVIP4状态: 0=不可领取，1=可领取，2=已领取")
    private Long svip4state;
    
    @ApiModelProperty(value = "SVIP5状态: 0=不可领取，1=可领取，2=已领取")
    private Long svip5state;
    
    @ApiModelProperty(value = "SVIP6状态: 0=不可领取，1=可领取，2=已领取")
    private Long svip6state;
    
    @ApiModelProperty(value = "当前VIP等级")
    private Integer vip;
    
    @ApiModelProperty(value = "VIP1状态: 0=不可领取，1=可领取，2=已领取")
    private Long vip1state;
    
    @ApiModelProperty(value = "VIP2状态: 0=不可领取，1=可领取，2=已领取")
    private Long vip2state;
    
    @ApiModelProperty(value = "VIP3状态: 0=不可领取，1=可领取，2=已领取")
    private Long vip3state;
    
    @ApiModelProperty(value = "VIP4状态: 0=不可领取，1=可领取，2=已领取")
    private Long vip4state;
    
    @ApiModelProperty(value = "VIP5状态: 0=不可领取，1=可领取，2=已领取")
    private Long vip5state;
    
    @ApiModelProperty(value = "VIP6状态: 0=不可领取，1=可领取，2=已领取")
    private Long vip6state;
    
    @ApiModelProperty(value = "VIP等级达到3级的会员数")
    private Integer vipcont;
    
    @ApiModelProperty(value = "SVIP等级")
    private Integer viplr;
}
