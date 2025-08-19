package io.renren.modules.blackip.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-20
 */
@Data
@TableName("blackip")
public class BlackipEntity {

    /**
     * 
     */
	private Long id;
    /**
     * 
     */
	private String ip;
    /**
     * 
     */
	private String remark;
}