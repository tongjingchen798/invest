package io.renren.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 充值接口返回DTO
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@ApiModel(value = "充值接口返回")
public class ChargeResponseDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单号", example = "TP25081413441135131")
    private String orderNo;

    @ApiModelProperty(value = "支付订单号", example = "TP25081413441135131")
    private String pOrderNo;

    @ApiModelProperty(value = "支付链接", example = "https://pay-v2.bankkpay.com/cashier?orderId=TP25081413441135131&amount=30000")
    private String payUrl;

    @ApiModelProperty(value = "商户号", example = "R2025081416141168564")
    private String merchantNo;

    @ApiModelProperty(value = "USDT地址", example = "TVsCfPDWy8EZCFBgjzEXRtSZvr5r96w3U3")
    private String addr;

    @ApiModelProperty(value = "u数量", example = "299.951")
    private BigDecimal uamount;

    @ApiModelProperty(value = "u兑换法币汇率(1U=多少法币)", example = "97.50")
    private BigDecimal uprice;

    @ApiModelProperty(value = "实际充值成功得到的金额", example = "30000")
    private Long amount;

    @ApiModelProperty(value = "实际支付u数量(后台有个充值折扣比例, 每次会扣减)", example = "299.951")
    private BigDecimal uRealAmount;

    @ApiModelProperty(value = "钱包地址", example = "钱包地址")
    private String walletAddr;

    @ApiModelProperty(value = "订单状态", example = "0")
    @JsonProperty("status")
    private Integer status;

    @ApiModelProperty(value = "异常码")
    private  Integer errorCode;

    @ApiModelProperty(value = "flag")
    private  Integer flag;


    /**
     * 设置USDT相关信息
     */
    public void setUsdtInfo(String addr, BigDecimal uamount, BigDecimal uprice, BigDecimal uRealAmount) {
        this.addr = addr;
        this.uamount = uamount;
        this.uprice = uprice;
        this.uRealAmount = uRealAmount;
    }

    /**
     * 设置银行卡相关信息
     */
    public void setBankCardInfo(String payUrl) {
        this.payUrl = payUrl;
    }
}
