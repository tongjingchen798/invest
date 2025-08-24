package io.renren.modules.member.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 裂变佣金DTO
 *
 * @author renren
 * @since 1.0.0
 */
@Data
public class FissionRewardDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 代理编号
     */
    private String agent;

    /**
     * 代理名称
     */
    private String agentName;

    /**
     * 标签
     */
    private String biaoqian;

    /**
     * CCE3奖励
     */
    private Integer cce3fl;

    /**
     * 访问奖励
     */
    private Integer fxfl;

    /**
     * 工资
     */
    private Integer gz;

    /**
     * 用户的投资金额
     */
    private Integer investmentAmount;

    /**
     * 邀请码状态
     */
    private Integer inviteCodeStatus;

    /**
     * 会员账号
     */
    private String mobile;

    /**
     * 一级佣金
     */
    private Integer oneReward;

    /**
     * 佣金余额
     */
    private Integer rewardBalance;

    /**
     * 业务员名称
     */
    private String salesmanName;

    /**
     * 业务员编号
     */
    private Long salesmanid;

    /**
     * 三级佣金
     */
    private Integer threeReward;

    /**
     * 总佣金
     */
    private Integer totalReward;

    /**
     * 二级佣金
     */
    private Integer twoReward;

    /**
     * VIP奖励
     */
    private Integer vipjl;

    /**
     * 用户的总下级人数
     */
    private Integer xjzhCnt;

    /**
     * 已提佣金
     */
    private Integer ytReward;

    /**
     * 注册奖励
     */
    private Integer zcfl;

    /**
     * 正在提佣金
     */
    private Integer zztReward;
}
