package io.renren.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 用户登录日志表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-20
 */
@Data
@TableName("tb_user_log")
public class UserLogEntity {

    /**
     * 主键ID
     */
    @TableId
	private Long id;
    /**
     * 用户ID
     */
	private Long userId;
    /**
     * 销售员姓名
     */
	private String salesmanName;
    /**
     * 手机号
     */
	private String mobile;
    /**
     * 登录时间
     */
	private Date loginTime;
    /**
     * 登出时间
     */
	private Date logoutTime;
    /**
     * 登录IP地址
     */
	private String loginIp;
    /**
     * 设备类型 1:PC 2:移动端 3:APP
     */
	private Integer equipment;
    /**
     * 销售员ID
     */
	private Long salesmanid;
    /**
     * 代理ID
     */
	private Long agent;
    /**
     * 标签
     */
	private String biaoqian;
    /**
     * 创建时间
     */
	private Date createTime;
    /**
     * 更新时间
     */
	private Date updateTime;
}