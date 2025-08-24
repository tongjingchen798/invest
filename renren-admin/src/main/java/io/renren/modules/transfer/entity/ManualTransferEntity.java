package io.renren.modules.transfer.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 人工转帐记录表
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@TableName("tb_manual_transfer")
public class ManualTransferEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId
    private String id;

    /**
     * 消息/状态说明
     */
    private String msg;

    /**
     * 转帐时间
     */
    private Date withdrawTime;

    /**
     * 转帐金额（分）
     */
    private Long amount;

    /**
     * 银行代码
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
     * 收款账号
     */
    private String payNo;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 状态时间
     */
    private Date stateTime;

    /**
     * 状态：0-待处理，1-处理中，2-已完成，3-失败，4-已取消
     */
    private Integer state;

    /**
     * 转帐类型：0-余额转帐，1-佣金转帐，2-其他转帐
     */
    private Integer withdrawType;

    /**
     * 订单号
     */
    private String orderno;

    /**
     * 第三方订单号
     */
    private String threeorderNo;

    /**
     * IFSC代码
     */
    private String ifsc;

    /**
     * 代理名称
     */
    private String agentName;

    /**
     * 代理ID
     */
    private String agent;

    /**
     * 业务员ID
     */
    private Long salesmanid;

    /**
     * 业务员姓名
     */
    private String salesmanName;

    /**
     * 渠道ID
     */
    private Long channelid;

    /**
     * 商户ID
     */
    private String merchantid;

    /**
     * 商户名称
     */
    private String merchantname;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 操作人
     */
    private String operCode;

    /**
     * 转帐来源：1-用户申请，2-管理员操作，3-系统自动
     */
    private Integer transferSource;

    /**
     * 手续费（分）
     */
    private Long handFee;

    /**
     * 实际到账金额（分）
     */
    private Long realAmount;

    /**
     * 渠道金额（分）
     */
    private Long channelAmount;
}
