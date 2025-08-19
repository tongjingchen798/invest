package io.renren.modules.commissionconfig.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 返佣比例配置表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Data
@TableName("tb_commission_config")
public class CommissionConfigEntity {

    /**
     * 主键ID
     */
	private Long id;
    /**
     * 状态 0:禁用 1:启用
     */
	private Integer status;
    /**
     * 创建人
     */
	private String creator;
    /**
     * 创建时间
     */
	private Date createDate;
    /**
     * 更新人
     */
	private String updater;
    /**
     * 更新时间
     */
	private Date updateDate;
    /**
     * 一级返佣比例(%)
     */
	private BigDecimal areward;
    /**
     * 二级返佣比例(%)
     */
	private BigDecimal breward;
    /**
     * 三级返佣比例(%)
     */
	private BigDecimal creward;
}