package io.renren.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 广告素材表
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2025-07-10 20:05:01
 */
@Data
@TableName("tb_advertisement")
public class AdvertisementEntity {
    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 广告类型 1=LOG,2=轮播图，3=个人中心 4=弹窗广告
     */
    private Integer type;

    /**
     * 广告标题
     */
    private String title;

    /**
     * 图片地址
     */
    private String logosAddr;

    /**
     * 链接地址
     */
    private String logosLinkaddr;

    /**
     * 备注描述
     */
    private String remark;

    /**
     * 生效时间
     */
    private Date sxDate;

    /**
     * 展示时长（小时）
     */
    private Integer hour;

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
     * 创建人
     */
    private String creator;

    /**
     * 更新人
     */
    private String updater;
}
