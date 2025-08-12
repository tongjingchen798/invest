package io.renren.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * U收款记录表
 *
 * @author renren
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("usdtrecord")
public class UsdtRecordEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 交易Hash
     */
    private String transactionId;

    /**
     * 转账地址
     */
    private String fromAddress;

    /**
     * 收款地址
     */
    private String toAddress;

    /**
     * 合约地址
     */
    private String contractAddress;

    /**
     * 区块时间戳
     */
    private Long blockTs;

    /**
     * 区块号
     */
    private Long blockNumber;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 合约类型
     */
    private String contractType;

    /**
     * 是否风险：0-否，1-是
     */
    private Integer isRisk;

    /**
     * 是否已处理：0-否，1-是
     */
    private Integer isProcess;

    /**
     * 区块时间
     */
    private Date blockTime;

    /**
     * 订单号
     */
    private String orderNo;

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
