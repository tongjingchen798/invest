package io.renren.modules.sys.entity;

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
	private Long id;
    /**
     * 商户ID
     */
	private String merchantId;
    /**
     * 商户号
     */
	private String merchantNo;
    /**
     * 商户名
     */
	private String merchantName;
    /**
     * 密钥
     */
	private String channelKey;
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
     * 单笔手续费
     */
	private BigDecimal oneFree;
    /**
     * 后台管理地址
     */
	private String houtaiUrl;
    /**
     * 代收-通道代码
     */
	private String channelTypeDs;
    /**
     * 代付-通道代码
     */
	private String channelTypeDf;
    /**
     * 优先级
     */
	private Integer degreeHeat;
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