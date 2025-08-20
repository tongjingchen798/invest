package io.renren.modules.coupon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 优惠卷
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-20
 */
@Data
@ApiModel(value = "优惠卷")
public class CouponDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	private Long couponId;

	@ApiModelProperty(value = "优惠券名称")
	private String couponName;

	@ApiModelProperty(value = "优惠券描述")
	private String couponDescribe;

	@ApiModelProperty(value = "创建时间")
	private Date createDate;

	@ApiModelProperty(value = "开启时间")
	private Date openDate;

	@ApiModelProperty(value = "结束时间")
	private Date endDate;

	@ApiModelProperty(value = "修改时间")
	private Date updateDate;

	@ApiModelProperty(value = "修改人")
	private Long sysUpdateUserId;

	@ApiModelProperty(value = "创建人id")
	private Long sysCreateUserId;

	@ApiModelProperty(value = "优惠券使用金额单位分")
	private Long amount;

	@ApiModelProperty(value = "投资项目id")
	private Long investId;


}