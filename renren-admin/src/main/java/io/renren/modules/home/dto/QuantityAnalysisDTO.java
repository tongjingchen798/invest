package io.renren.modules.home.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 数量统计分析图数据传输对象
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
public class QuantityAnalysisDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 统计日期（格式：yyyy-MM-dd 或 yyyy-MM 或 yyyy）
     */
    private String date;

    /**
     * 新增注册会员数量
     */
    private Long totalRegUser;

    /**
     * 新增充值会员数量
     */
    private Long totalChargeUser;

    /**
     * 充值订单数量
     */
    private Long totalCharge;

    /**
     * 签到人数
     */
    private Long totalSignIn;

    /**
     * 统计类型标识（d: 日, w: 周, m: 月）
     */
    private String type;
}
