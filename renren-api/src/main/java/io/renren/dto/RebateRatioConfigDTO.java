package io.renren.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 返佣比例配置DTO
 *
 * @author renren
 * @since 2024-01-01
 */
@Data
public class RebateRatioConfigDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 1级返佣比例
     */
    private Integer areward;

    /**
     * 2级返佣比例
     */
    private Integer breward;

    /**
     * 3级返佣比例
     */
    private Integer creward;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 更新人
     */
    private String updater;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新时间
     */
    private Date updateDate;
}
