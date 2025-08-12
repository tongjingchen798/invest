package io.renren.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * U地址配置DTO
 *
 * @author renren
 * @since 2024-01-01
 */
@Data
public class UAddressConfigDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 地址
     */
    private String addr;

    /**
     * 图片地址
     */
    private String img;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer state;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新时间
     */
    private Date updateDate;
}
