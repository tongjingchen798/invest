package io.renren.modules.advertisement.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 广告素材表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Data
@TableName("tb_advertisement")
public class AdvertisementEntity {


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
     * 创建时间
     */
    private Date createDate;

}