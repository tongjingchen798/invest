package io.renren.modules.usdtrecord.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * USDT收款记录DTO
 *
 * @author renren
 * @since 2024-01-01
 */
@Data
public class UsdtRecordDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 交易Hash
     */
    private String transactionId;

    /**
     * 转账地址
     */
    private String fromAddress;

    /**
     * 收款地址
     */
    private String toAddress;

    /**
     * 合约地址
     */
    private String contractAddress;

    /**
     * 区块时间戳
     */
    private String blockTs;

    /**
     * 区块号
     */
    private String block;

    /**
     * 金额 (接口文档中的quant字段)
     */
    private BigDecimal quant;

    /**
     * 合约类型
     */
    private String contractType;

    /**
     * 是否风险：0-否，1-是
     */
    private Integer isRisk;

    /**
     * 是否已处理：0-否，1-是
     */
    private Integer isProcess;

    /**
     * 区块时间
     */
    private String blockTime;

    /**
     * 订单号 (接口文档中的orderno字段)
     */
    private String orderno;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 更新时间
     */
    private String updateDate;
}
