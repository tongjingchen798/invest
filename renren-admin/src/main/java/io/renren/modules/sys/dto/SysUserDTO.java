 

package io.renren.modules.sys.dto;

import lombok.Data;

import java.util.Date;

/**
 * 系统用户DTO
 *
 * @author renren
 * @since 2024-01-01
 */
@Data
public class SysUserDTO {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 姓名
     */
    private String realName;

    /**
     * 头像
     */
    private String headUrl;

    /**
     * 性别   0：男   1：女    2：保密
     */
    private Integer gender;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 状态  0：停用   1：正常
     */
    private Integer status;

    /**
     * 超级管理员   0：否   1：是
     */
    private Integer superAdmin;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 渠道
     */
    private String channel;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 代理ID
     */
    private Long agent;

    /**
     * 域名
     */
    private String domainname;

    /**
     * 下载码
     */
    private String dowloadCode;

    /**
     * 微信图片
     */
    private String wsimage;

    /**
     * 微信号
     */
    private String wsnumber;

    /**
     * 微信名称
     */
    private String wsname;

    /**
     * 推广号
     */
    private String tgnumber;

    /**
     * 二次验证码
     */
    private String twoFactorCode;

    /**
     * 代理邀请码
     */
    private String agentInviteCode;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 角色ID列表
     */
    private Long[] roleIdList;
}