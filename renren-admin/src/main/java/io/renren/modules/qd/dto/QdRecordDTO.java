package io.renren.modules.qd.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 签到记录DTO
 *
 * @author renren
 * @since 2024-01-01
 */
@Data
public class QdRecordDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 签到的连续天数
     */
    private Integer day;

    /**
     * 签到奖励金额
     */
    private Long amount;

    /**
     * 签到类型
     */
    private Integer qdtype;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 用户账号
     */
    private String mobile;

    /**
     * 代理编号
     */
    private String agent;

    /**
     * 代理名称
     */
    private String agentName;

    /**
     * 业务员编号
     */
    private String salesmanid;

    /**
     * 业务员名称
     */
    private String salesmanName;

    /**
     * 天分区
     */
    private Integer partDay;

    /**
     * 总天数
     */
    private Integer sumday;

    /**
     * 签到奖励的优惠券
     */
    private Long couponid;

    /**
     * 优惠券名称
     */
    private String couponName;

    /**
     * 签到奖励的特权券
     */
    private Long privilegeid;

    /**
     * 特权券名称
     */
    private String privilegeName;

    /**
     * 标签
     */
    private String biaoqian;

    /**
     * 裂变
     */
    private Integer liebian;

    /**
     * 邀请码状态
     */
    private Integer inviteCodeStatus;
}
