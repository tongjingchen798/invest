package io.renren.modules.note.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 公告表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Data
@TableName("tb_note")
public class NoteEntity {

    /**
     * 主键ID
     */
	private Long id;
    /**
     * 公告标题
     */
	private String title;
    /**
     * 状态 0:禁用 1:启用
     */
	private Integer status;
    /**
     * 创建时间
     */
	private Date createDate;
}