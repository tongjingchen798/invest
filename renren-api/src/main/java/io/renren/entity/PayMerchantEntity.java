package io.renren.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付商户配置实体类
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pay_merchant")
public class PayMerchantEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商户ID
     */
    @TableId
    private Long merchantid;


    /**
     * 商户号
     */
    private String merchantno;

    private String merchantCode;

    /**
     * 商户名
     */
    private String merchantname;

    /**
     * 密钥
     */
    private String channelkey;

    private String dfKey;

    /**
     * 密码
     */
    private String password;

    /**
     * 代收费率%
     */
    private BigDecimal dsFree;

    /**
     * 代付费率%
     */
    private BigDecimal dfFree;

    /**
     * 代付单笔手续费
     */
    private BigDecimal oneFree;

    /**
     * 后台管理地址
     */
    private String houtaiurl;

    /**
     * 代收-通道代码
     */
    private String channeltypeds;

    /**
     * 代付-通道代码
     */
    private String channeltypedf;

    /**
     * 优先级
     */
    private Integer degreeheat;

    private String notifyUrl;

    /**
     * 状态（0:下架,1:上架）
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createDate;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;
}