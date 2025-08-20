package io.renren.modules.home.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 日报表数据DTO
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@ApiModel(value = "日报表数据")
public class DailyReportDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 日期 (格式: yyyy-MM-dd)
     */
    @ApiModelProperty(value = "日期", example = "2025-05-29")
    private String date;

    /**
     * 总充值金额
     */
    @ApiModelProperty(value = "总充值金额")
    private String totalCharges;

    /**
     * 总提现金额
     */
    @ApiModelProperty(value = "总提现金额")
    private String totalWithdraws;

    /**
     * 总订单金额
     */
    @ApiModelProperty(value = "总订单金额")
    private String totalOrders;
}
