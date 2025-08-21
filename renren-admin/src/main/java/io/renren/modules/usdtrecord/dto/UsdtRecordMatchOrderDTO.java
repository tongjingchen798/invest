package io.renren.modules.usdtrecord.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * USDT记录匹配订单DTO
 *
 * @author renren
 * @since 2024-01-01
 */
@Data
public class UsdtRecordMatchOrderDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * U记录id
     */
    private Long id;

    /**
     * 订单号
     */
    private String orderno;
}
