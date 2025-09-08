package io.renren.modules.paychannel.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付渠道表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Data
@TableName("tb_pay_channel")
public class PayChannelEntity {

    /**
     * 渠道ID
     */
	private Long channelid;
    /**
     * 渠道名称
     */
    @TableField("channel_name")
	private String channelName;
    /**
     * 渠道类型
     */
    @TableField("channel_type")
	private String channelType;
    /**
     * 商户ID
     */
	private String merchantid;
    /**
     * 商户名称
     */
	private String merchantname;
    /**
     * 状态（0:禁用,1:启用）
     */
	private Integer status;
    /**
     * usdt赠送比例
     */
    @TableField("usdt_gift_ratio")
	private String usdtGiftRatio;
    /**
     * usdt兑当地货币汇率	
     */
    @TableField("usdt_local_currency_rate")
	private String usdtLocalCurrencyRate;
    /**
     * 充提类型（1:充值,2:提现,3:充提）
     */
	private String chargeorwithdraw;
    /**
     * 创建时间
     */
    @TableField("create_date")
	private Date createDate;
    /**
     * 更新时间
     */
    @TableField("update_date")
	private Date updateDate;
}