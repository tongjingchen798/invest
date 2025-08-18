-- =====================================================
-- 用户余额明细表建表脚本
-- 数据库：renren_security
-- 用途：存储用户账户余额变动明细记录
-- =====================================================

USE renren_security;

-- 删除表（如果存在）
DROP TABLE IF EXISTS `user_balance_detail`;

-- 创建用户余额明细表
CREATE TABLE `user_balance_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `agent_id` bigint(20) DEFAULT NULL COMMENT '代理ID',
  `agent_name` varchar(100) DEFAULT NULL COMMENT '代理名称',
  `label` varchar(50) DEFAULT NULL COMMENT '标签',
  `busi_type` int(11) NOT NULL COMMENT '业务类型 1购买流水 2余额提现流水 3返佣A 5冻结金额 6解冻金额 7手工充值 8手工扣款 10收益 11线上充值 12工资 32项目返上级 31项目返自己 30返佣B 28任务奖励 27注册奖励 26拼团奖励 24转给投资账户 23投资账户转出 22代理转出 21转入投资 20出售产品 18领取红包 19今日福利 14邀请福利 13签到奖励 33佣金提现流水 15返现',
  `channel` varchar(50) DEFAULT NULL COMMENT '渠道',
  `form_user_id` bigint(20) DEFAULT NULL COMMENT '返佣来源,谁返给userid的',
  `invite_code_status` int(11) DEFAULT NULL COMMENT '邀请码状态',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号码',
  `original_amount` bigint(20) DEFAULT NULL COMMENT '原始金额（分）',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注',
  `salesman_name` varchar(100) DEFAULT NULL COMMENT '业务员姓名',
  `salesman_id` bigint(20) DEFAULT NULL COMMENT '业务员ID',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态 0:交易失败 1:正常',
  `stream_id` varchar(100) DEFAULT NULL COMMENT '交易流水id',
  `transaction_amount` bigint(20) NOT NULL COMMENT '交易后金额（分）',
  `transaction_date` datetime NOT NULL COMMENT '交易时间',
  `use_amount` bigint(20) DEFAULT NULL COMMENT '使用金额（分）',
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_business_type` (`business_type`),
  KEY `idx_transaction_date` (`transaction_date`),
  KEY `idx_mobile` (`mobile`),
  KEY `idx_agent_id` (`agent_id`),
  KEY `idx_salesman_id` (`salesman_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户余额明细表';

-- 插入测试数据
INSERT INTO `user_balance_detail` (`user_id`, `agent_id`, `agent_name`, `label`, `business_type`, `channel`, `mobile`, `original_amount`, `remarks`, `status`, `stream_id`, `transaction_amount`, `transaction_date`, `use_amount`, `create_date`) VALUES
(10001, 1, '北京代理', 'VIP用户', 1, '支付宝', '13800138001', 10000, '购买产品', 1, 'ST001', 11000, '2024-01-01 10:00:00', 1000, NOW()),
(10002, 2, '上海代理', '普通用户', 2, '微信支付', '13800138002', 5000, '余额提现', 1, 'ST002', 4500, '2024-01-01 11:00:00', 500, NOW()),
(10003, 1, '北京代理', 'VIP用户', 3, '系统', '13800138003', 0, '返佣A', 1, 'ST003', 1000, '2024-01-01 12:00:00', 0, NOW()),
(10004, 3, '广州代理', '新用户', 11, '银行卡', '13800138004', 0, '线上充值', 1, 'ST004', 20000, '2024-01-01 13:00:00', 0, NOW()),
(10005, 2, '上海代理', '活跃用户', 10, '系统', '13800138005', 0, '投资收益', 1, 'ST005', 500, '2024-01-01 14:00:00', 0, NOW());

-- 验证表创建成功
SELECT 'user_balance_detail' as table_name, COUNT(*) as record_count FROM user_balance_detail;
SHOW CREATE TABLE user_balance_detail;
