 

package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 下单表单
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Data
@ApiModel(value = "下单表单")
public class PlaceOrderDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "投资总金额(优惠价无需计算，后端做处理 单位分)")
    private Long amount;

    @ApiModelProperty(value = "channel")
    private String channel;

    @ApiModelProperty(value = "购买份数")
    private Integer count;

    @ApiModelProperty(value = "是否使用优惠券(1使用 2不使用)")
    private Integer couponType;

    @ApiModelProperty(value = "gift")
    private Integer gift;

    @ApiModelProperty(value = "投资理财项id")
    @NotNull(message = "投资理财项id不能为空")
    private Long investId;

    @ApiModelProperty(value = "orderId")
    private Long orderId;

    @ApiModelProperty(value = "partSn")
    private String partSn;

    @ApiModelProperty(value = "支付密码")
    @NotBlank(message = "支付密码不能为空")
    private String password;

    @ApiModelProperty(value = "特权劵id")
    private List<Integer> privilegeId;

    @ApiModelProperty(value = "优惠券id，如果没使用优惠券可不填写")
    private List<Integer> userCouponId;
}
