package io.renren.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付通道
 * 
 * @author Mark sunlightcs@gmail.com
 */
@Data
@TableName("tb_pay_channel")
public class PayChannelEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId
    private Long channelid;

    /**
     * 通道名,前端显示用的
     */
    private String channelName;

    /**
     * 通道类型 UPI/SWIPE/USDT
     */
    private String channelType;

    /**
     * 渠道编码
     */
    @TableField("channel_code")
    private String channelCode;

    /**
     * 充值 1 或者提现 2
     */
    private String chargeorwithdraw;

    /**
     * 商户主键
     */
    private Long merchantid;

    /**
     * 最小限额
     */
    private Long minLimit;


    /**
     * 最大限额
     */
    private Long maxLimit;

    /**
     * 上下架 0：下架 1：上架
     */
    private Integer status;

    /**
     * usdt赠送比例
     */
    private String usdtGiftRatio;

    /**
     * usdt兑当地货币汇率
     */
    private String usdtLocalCurrencyRate;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新时间
     */
    private Date updateDate;
}