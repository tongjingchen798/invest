package io.renren.modules.member.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 黑白名单实体
 *
 * @author renren
 * @since 1.0.0
 */
@Data
@TableName("tb_blacklist")
public class BlacklistEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId
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
}
