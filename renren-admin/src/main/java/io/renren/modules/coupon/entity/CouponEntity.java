package io.renren.modules.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 优惠卷
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-20
 */
@Data
@TableName("tb_coupon")
public class CouponEntity {

    /**
     * 
     */
    @TableId
	private Long couponId;
    /**
     * 优惠券名称
     */
	private String couponName;
    /**
     * 优惠券描述
     */
	private String couponDescribe;
    /**
     * 创建时间
     */
	private Date createDate;
    /**
     * 开启时间
     */
	private Date openDate;
    /**
     * 结束时间
     */
	private Date endDate;
    /**
     * 修改时间
     */
	private Date updateDate;
    /**
     * 修改人
     */
	private Long sysUpdateUserId;
    /**
     * 创建人id
     */
	private Long sysCreateUserId;
    /**
     * 优惠券使用金额单位分
     */
	private Long amount;
    /**
     * 投资项目id
     */
	private Long investId;
}