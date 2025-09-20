package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * QePay代付回调DTO
 * 根据QePay代付异步通知参数定义
 * 
 * @author renren
 * @date 2024-01-01
 */
@Data
@ApiModel(value = "QePay代付回调DTO")
public class QePayCallbackDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单状态：1-代付成功，2-代付失败")
    private String tradeResult;

    @ApiModelProperty(value = "商家转账单号")
    private String merTransferId;

    @ApiModelProperty(value = "商户代码")
    private String merNo;

    @ApiModelProperty(value = "平台订单号")
    private String tradeNo;

    @ApiModelProperty(value = "代付金额（元为单位保留两位小数）")
    private String transferAmount;

    @ApiModelProperty(value = "订单时间")
    private String applyDate;

    @ApiModelProperty(value = "版本号")
    private String version;

    @ApiModelProperty(value = "回调状态")
    private String respCode;

    @ApiModelProperty(value = "签名")
    private String sign;

    @ApiModelProperty(value = "签名方式")
    private String signType;
}
