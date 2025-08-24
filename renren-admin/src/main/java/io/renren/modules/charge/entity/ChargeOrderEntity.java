package io.renren.modules.charge.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 充值订单实体类
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@TableName("tb_charge_order")
public class ChargeOrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 充值ID
     */
    @TableId
    private Long chargeId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户账号
     */
    private String mobile;

    /**
     * 代理编号
     */
    private String agent;

    /**
     * 业务员编号
     */
    private Long salesmanid;

    /**
     * 渠道
     */
    private Long channel;

    /**
     * 通道类型
     */
    private String channelType;

    /**
     * 支付通道主键
     */
    private Long channelid;

    /**
     * 商户主键
     */
    private Long merchantid;

    /**
     * 商户名
     */
    private String merchantname;

    /**
     * 平台订单号
     */
    private String orderno;

    /**
     * 第三方订单号
     */
    private String threeorderNo;

    /**
     * 渠道编码
     */
    private String platform;

    /**
     * 充值金币(分)
     */
    private Long amount;

    /**
     * 真实充值额(分)
     */
    private Long realAmount;

    /**
     * U金额
     */
    private Long uamout;

    /**
     * U价格
     */
    private Long uprice;

    /**
     * U实际支付金额
     */
    private Long uRealAmout;

    /**
     * 钱包ID
     */
    private Long walletId;

    /**
     * 钱包地址
     */
    private String walletAddr;

    /**
     * 状态 0 待审核  1 审核通过  2 失败
     */
    private Integer state;

    /**
     * 用户IP
     */
    private String infoIp;

    /**
     * 操作人
     */
    private String operCode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 充值渠道名称
     */
    private String sourcetypeName;

    /**
     * 充值日期
     */
    private Date chargeTime;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
