package io.renren.modules.withdraw.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 提现订单实体
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@TableName("tb_withdraw_order")
public class WithdrawOrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId
    private String id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private String userId;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 用户名
     */
    private String username;

    /**
     * 提现渠道
     */
    private Long channel;

    /**
     * 消息
     */
    private String msg;

    /**
     * 提现时间
     */
    @TableField("withdraw_time")
    private Date withdrawTime;

    /**
     * 提现到账金额（分）
     */
    private Long amount;

    /**
     * 输入金额（分）
     */
    private Long inputamount;

    /**
     * 汇率
     */
    private Double rate;

    /**
     * 手续费（分）
     */
    private Long handFee;

    /**
     * 实际到账金额（分）
     */
    @TableField("real_amount")
    private Long realAmount;

    /**
     * 渠道金额（分）
     */
    @TableField("channel_amount")
    private Long channelAmount;

    /**
     * 银行代码
     */
    @TableField("blank_code")
    private String blankCode;

    /**
     * 银行名称
     */
    @TableField("blank_name")
    private String blankName;

    /**
     * 收款人姓名
     */
    @TableField("pay_name")
    private String payName;

    /**
     * 收款账号
     */
    @TableField("pay_no")
    private String payNo;

    /**
     * 操作代码
     */
    @TableField("oper_code")
    private String operCode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 状态时间
     */
    @TableField("state_time")
    private Date stateTime;

    /**
     * 状态：0-待审核，1-审核通过，2-已提现，3-驳回，4-提现失败，5-无效订单
     */
    private Integer state;

    /**
     * 提现类型 1余额提现 2佣金提现
     */
    @TableField("withdraw_type")
    private Integer withdrawType;

    /**
     * 部分金额
     */
    @TableField("part_mon")
    private Integer partMon;

    /**
     * 订单号
     */
    private String orderno;

    /**
     * 第三方订单号
     */
    @TableField("threeorder_no")
    private String threeorderNo;

    /**
     * 来源类型名称
     */
    @TableField("sourcetype_name")
    private String sourcetypeName;

    /**
     * 信息IP
     */
    @TableField("info_ip")
    private String infoIp;

    /**
     * IFSC代码
     */
    private String ifsc;

    /**
     * 代理名称
     */
    @TableField("agent_name")
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
    @TableField("salesman_name")
    private String salesmanName;

    /**
     * 渠道ID
     */
    private String channelid;

    /**
     * 商户ID
     */
    private String merchantid;

    /**
     * 商户名称
     */
    private String merchantname;

    /**
     * 标签
     */
    private String biaoqian;

    /**
     * 邀请码状态
     */
    @TableField("invite_code_status")
    private Integer inviteCodeStatus;

    /**
     * 是否裂变
     */
    private Integer liebian;

    /**
     * CTC
     */
    private String ctc;
}
