package io.renren.modules.issues.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 问题表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Data
@TableName("issues")
public class IssuesEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId
	private Long id;
    /**
     * 名称
     */
	private String title;
    /**
     * 描述
     */
	private String content;
    /**
     * 链接地址
     */
	private String imagesAddr;

    /**
     * 创建时间
     */
	private Date createDate;

}