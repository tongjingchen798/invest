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
@JsonInclude(JsonInclude.Include.NON_NULL) // 不序列化null值
public class ChargeResponseDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单号", example = "TP25081413441135131")
    @JsonProperty("orderNo")
    private String orderNo;

    @ApiModelProperty(value = "支付订单号", example = "TP25081413441135131")
    @JsonProperty("pOrderNo")
    private String pOrderNo;

    @ApiModelProperty(value = "支付链接", example = "https://pay-v2.bankkpay.com/cashier?orderId=TP25081413441135131&amount=30000")
    @JsonProperty("payUrl")
    private String payUrl;

    @ApiModelProperty(value = "商户号", example = "R2025081416141168564")
    @JsonProperty("merchantNo")
    private String merchantNo;

    @ApiModelProperty(value = "USDT地址", example = "TVsCfPDWy8EZCFBgjzEXRtSZvr5r96w3U3")
    @JsonProperty("addr")
    private String addr;

    @ApiModelProperty(value = "u数量", example = "299.951")
    @JsonProperty("uamount")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#0.000")
    private BigDecimal uamount;

    @ApiModelProperty(value = "u兑换法币汇率(1U=多少法币)", example = "97.50")
    @JsonProperty("uprice")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#0.00")
    private BigDecimal uprice;

    @ApiModelProperty(value = "实际充值成功得到的金额", example = "30000")
    @JsonProperty("amount")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#0")
    private BigDecimal amount;

    @ApiModelProperty(value = "实际支付u数量(后台有个充值折扣比例, 每次会扣减)", example = "299.951")
    @JsonProperty("uRealAmount")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#0.000")
    private BigDecimal uRealAmount;

    @ApiModelProperty(value = "钱包地址", example = "钱包地址")
    @JsonProperty("walletAddr")
    private String walletAddr;

    @ApiModelProperty(value = "充值类型", example = "1")
    @JsonProperty("chargeType")
    private Integer chargeType;

    @ApiModelProperty(value = "充值类型名称", example = "银行卡")
    @JsonProperty("chargeTypeName")
    private String chargeTypeName;

    @ApiModelProperty(value = "订单状态", example = "0")
    @JsonProperty("status")
    private Integer status;

    @ApiModelProperty(value = "订单状态描述", example = "待支付")
    @JsonProperty("statusText")
    private String statusText;

    @ApiModelProperty(value = "创建时间", example = "2024-12-01 12:00:00")
    @JsonProperty("createTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

    @ApiModelProperty(value = "更新时间", example = "2024-12-01 12:00:00")
    @JsonProperty("updateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String updateTime;

    @ApiModelProperty(value = "异常码")
    @JsonProperty("updateTime")
    private  Integer errorCode;

    /**
     * 设置充值类型信息
     */
    public void setChargeTypeInfo(Integer chargeType, String chargeTypeName) {
        this.chargeType = chargeType;
        this.chargeTypeName = chargeTypeName;
    }

    /**
     * 设置订单状态信息
     */
    public void setStatusInfo(Integer status, String statusText) {
        this.status = status;
        this.statusText = statusText;
    }

    /**
     * 设置时间信息
     */
    public void setTimeInfo(String createTime, String updateTime) {
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

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
