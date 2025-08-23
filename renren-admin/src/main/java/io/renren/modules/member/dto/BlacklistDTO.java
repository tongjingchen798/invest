package io.renren.modules.member.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 黑白名单DTO
 *
 * @author renren
 * @since 1.0.0
 */
@Data
public class BlacklistDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 会员账号
     */
    private String mobile;

    /**
     * 类型：1-白名单，2-黑名单
     */
    private Integer type;

    /**
     * 类型名称
     */
    private String typeName;
}
