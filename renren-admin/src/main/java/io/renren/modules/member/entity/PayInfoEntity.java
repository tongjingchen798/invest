package io.renren.modules.member.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 用户支付信息表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-20
 */
@Data
@TableName("tb_pay_info")
public class PayInfoEntity {

    /**
     * ID
     */
    @TableId
	private Long id;
    /**
     * 用户ID
     */
	private Long userId;
    /**
     * 银行简称
     */
	private String blankCode;
    /**
     * 银行名称
     */
	private String blankName;
    /**
     * 收款人姓名
     */
	private String payName;
    /**
     * 银行账号
     */
	private String payNo;
    /**
     * 收款人手机号码
     */
	private String mobile;
    /**
     * ifsc
     */
	private String ifsc;
    /**
     * 状态 0：停用 1：正常
     */
	private Integer state;
    /**
     * 排序
     */
	private Integer sortV;
    /**
     * 创建时间
     */
	private Date createTime;
    /**
     * 操作时间
     */
	private Date operTime;
    /**
     * 操作工号
     */
	private String operCode;
    /**
     * 状态时间
     */
	private Date stateTime;

}