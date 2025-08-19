package io.renren.modules.paychannel.entity;

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
	private String channelid;
    /**
     * 渠道名称
     */
	private String channelName;
    /**
     * 渠道类型
     */
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
	private BigDecimal usdtGiftRatio;
    /**
     * usdt兑当地货币汇率	
     */
	private String usdtLocalCurrencyRate;
    /**
     * 充提类型（1:充值,2:提现,3:充提）
     */
	private String chargeorwithdraw;
    /**
     * 创建时间
     */
	private Date createDate;
    /**
     * 更新时间
     */
	private Date updateDate;
}