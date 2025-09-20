package io.renren.modules.paymerchant.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付商户配置表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Data
@TableName("pay_merchant")
public class PayMerchantEntity {

    /**
     * 主键ID
     */
    @TableId
	private Long merchantid;

    /**
     * 商户号
     */
	private String merchantno;

    /**
     * 唯一商户编码
     */
    private String merchantCode;
    /**
     * 商户名
     */
	private String merchantname;
    /**
     * 密钥
     */
	private String channelkey;

    /**
     * 代付密钥
     */
    private String dfKey;
    /**
     * 密码
     */
	private String password;
    /**
     * 代收费率%
     */
	private BigDecimal dsFree;
    /**
     * 代付费率%
     */
	private BigDecimal dfFree;
    /**
     * 代付单笔手续费
     */
	private BigDecimal oneFree;
    /**
     * 后台管理地址
     */
	private String houtaiurl;
    /**
     * 代收-通道代码
     */
	private String channeltypeds;
    /**
     * 代付-通道代码
     */
	private String channeltypedf;

    /**
     * 回调地址
     */
    private String notifyUrl;
    /**
     * 优先级
     */
	private Integer degreeheat;
    /**
     * 状态（0:下架,1:上架）
     */
	private Integer status;
    /**
     * 创建时间
     */
	private Date createDate;
    /**
     * 更新时间
     */
	private Date updateDate;
}