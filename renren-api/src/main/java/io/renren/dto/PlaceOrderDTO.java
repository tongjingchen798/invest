 

package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 下单表单
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@ApiModel(value = "下单表单")
public class PlaceOrderDTO {
    
    @ApiModelProperty(value = "投资总金额(优惠价无需计算，后端做处理 单位分)")
    @NotNull(message = "投资金额不能为空")
    private Long amount;
    
    @ApiModelProperty(value = "支付渠道")
    private String channel;
    
    @ApiModelProperty(value = "购买份数")
    @NotNull(message = "购买份数不能为空")
    private Integer count;
    
    @ApiModelProperty(value = "是否使用优惠券(1使用 2不使用)")
    private Integer couponType;
    
    @ApiModelProperty(value = "赠送金额")
    private Integer gift;
    
    @ApiModelProperty(value = "投资理财项id")
    @NotNull(message = "投资项目ID不能为空")
    private Long investId;
    
    @ApiModelProperty(value = "订单ID")
    private Long orderId;
    
    @ApiModelProperty(value = "订单号简称")
    private String partSn;
    
    @ApiModelProperty(value = "支付密码")
    private String password;
    
    @ApiModelProperty(value = "特权劵id")
    private List<Long> privilegeId;
    
    @ApiModelProperty(value = "优惠券id，如果没使用优惠券可不填写")
    private List<Long> userCouponId;
}
