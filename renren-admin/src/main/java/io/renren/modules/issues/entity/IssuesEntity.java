package io.renren.modules.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 广告/图片管理表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Data
@TableName("issues")
public class IssuesEntity {

    /**
     * 主键ID
     */
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
     * 广告类型 1=LOG,2=轮播图，3=个人中心 4=弹窗广告
     */
	private Integer type;
    /**
     * 排序
     */
	private Integer sort;
    /**
     * 状态 0=禁用 1=启用
     */
	private Integer status;
    /**
     * 创建时间
     */
	private Date createDate;
    /**
     * 更新时间
     */
	private Date updateDate;
    /**
     * 创建者
     */
	private Long creator;
    /**
     * 更新者
     */
	private Long updater;
}