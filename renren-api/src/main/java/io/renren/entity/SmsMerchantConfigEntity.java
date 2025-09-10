package io.renren.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 短信商户配置表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Data
@TableName("tb_sms_merchant_config")
public class SmsMerchantConfigEntity {

    /**
     * 主键ID
     */
    @TableId
	private Long captchaId;

    /**
     * 商户名称
     */
	private String captchaName;

    /**
     * api Secret
     */
    private String apiSecret;
    /**
     * 商户编号
     */
	private String captchaNo;
    /**
     * 商户密钥
     */
	private String captchaKey;
    /**
     * 短信模板内容
     */
	private String msg;
    /**
     * 状态 0:禁用 1:启用
     */
	private Integer status;
    /**
     * 创建时间
     */
	private Date createTime;
    /**
     * 更新时间
     */
	private Date updateTime;
}