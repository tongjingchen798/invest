package io.renren.modules.member.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 即将兑付金额DTO
 *
 * @author renren
 * @since 1.0.0
 */
@Data
public class AmountToBeCashedDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 兑付金额
     */
    private BigDecimal profitAmount;

    /**
     * 兑付日期
     */
    private String profitDate;
}
