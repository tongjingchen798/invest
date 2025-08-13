-- 提现订单表
CREATE TABLE `tb_withdraw_order` (
  `id` varchar(64) NOT NULL COMMENT '主键ID',
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `channel` varchar(20) DEFAULT NULL COMMENT '提现渠道',
  `msg` text COMMENT '消息',
  `withdraw_time` datetime DEFAULT NULL COMMENT '提现时间',
  `amount` bigint(20) DEFAULT 0 COMMENT '提现到账金额（分）',
  `inputamount` bigint(20) DEFAULT 0 COMMENT '输入金额（分）',
  `rate` decimal(10,4) DEFAULT 1.0000 COMMENT '汇率',
  `hand_fee` bigint(20) DEFAULT 0 COMMENT '手续费（分）',
  `real_amount` bigint(20) DEFAULT 0 COMMENT '实际到账金额（分）',
  `channel_amount` bigint(20) DEFAULT 0 COMMENT '渠道金额（分）',
  `blank_code` varchar(50) DEFAULT NULL COMMENT '银行代码',
  `blank_name` varchar(100) DEFAULT NULL COMMENT '银行名称',
  `pay_name` varchar(100) DEFAULT NULL COMMENT '收款人姓名',
  `pay_no` varchar(100) DEFAULT NULL COMMENT '收款账号',
  `oper_code` varchar(50) DEFAULT NULL COMMENT '操作代码',
  `remark` text COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `state_time` datetime DEFAULT NULL COMMENT '状态时间',
  `state` tinyint(4) DEFAULT 0 COMMENT '状态：0-待审核，1-审核通过，2-已提现，3-驳回，4-提现失败，5-无效订单',
  `withdraw_type` tinyint(4) DEFAULT 1 COMMENT '提现类型：1-余额提现，2-佣金提现',
  `part_mon` int(11) DEFAULT 0 COMMENT '部分金额',
  `orderno` varchar(64) NOT NULL COMMENT '订单号',
  `threeorder_no` varchar(64) DEFAULT NULL COMMENT '第三方订单号',
  `sourcetype_name` varchar(100) DEFAULT NULL COMMENT '来源类型名称',
  `info_ip` varchar(50) DEFAULT NULL COMMENT '信息IP',
  `ifsc` varchar(20) DEFAULT NULL COMMENT 'IFSC代码',
  `agent_name` varchar(100) DEFAULT NULL COMMENT '代理名称',
  `agent` varchar(64) DEFAULT NULL COMMENT '代理ID',
  `salesmanid` varchar(64) DEFAULT NULL COMMENT '业务员ID',
  `salesman_name` varchar(100) DEFAULT NULL COMMENT '业务员姓名',
  `channelid` varchar(64) DEFAULT NULL COMMENT '渠道ID',
  `merchantid` varchar(64) DEFAULT NULL COMMENT '商户ID',
  `merchantname` varchar(100) DEFAULT NULL COMMENT '商户名称',
  `biaoqian` varchar(100) DEFAULT NULL COMMENT '标签',
  `invite_code_status` tinyint(4) DEFAULT 1 COMMENT '邀请码状态：0-禁用，1-启用',
  `liebian` tinyint(4) DEFAULT 0 COMMENT '是否裂变：0-否，1-是',
  `ctc` varchar(20) DEFAULT '0' COMMENT 'CTC',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_orderno` (`orderno`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_state` (`state`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_withdraw_time` (`withdraw_time`),
  KEY `idx_agent` (`agent`),
  KEY `idx_salesmanid` (`salesmanid`),
  KEY `idx_channelid` (`channelid`),
  KEY `idx_merchantid` (`merchantid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='提现订单表';

-- 插入示例数据
INSERT INTO `tb_withdraw_order` (
  `id`, `user_id`, `mobile`, `username`, `channel`, `msg`, 
  `withdraw_time`, `amount`, `inputamount`, `rate`, `hand_fee`, 
  `real_amount`, `channel_amount`, `blank_code`, `blank_name`, 
  `pay_name`, `pay_no`, `oper_code`, `remark`, `create_time`, 
  `state_time`, `state`, `withdraw_type`, `part_mon`, `orderno`, 
  `threeorder_no`, `sourcetype_name`, `info_ip`, `ifsc`, 
  `agent_name`, `agent`, `salesmanid`, `salesman_name`, 
  `channelid`, `merchantid`, `merchantname`, `biaoqian`, 
  `invite_code_status`, `liebian`, `ctc`
) VALUES (
  '1749881135685', '1927581495798161409', '917777777777', 'panda', '1', 
  '商户可提现余额不足', '2025-06-14 14:02:00', 30000, 30000, 1.0000, 0, 
  31500, 1500, 'State Bank of India', 'State Bank of India', 
  'panda', '8877777777', 'null', '三方提现商户可提现余额不足', '2025-06-14 14:05:36', 
  '2025-06-14 14:06:16', 2, 2, 6, 'W2025061414053581709', 
  NULL, '', '182.239.92.47', 'ABCD0ABCDEF', 
  NULL, '1748403717627', '1748403763980', 'Doris', 
  '1785184156860125186', '1933761948129419266', 'sangepay', NULL, 
  1, 0, '0'
);

-- 创建索引优化查询性能
CREATE INDEX `idx_user_state` ON `tb_withdraw_order` (`user_id`, `state`);
CREATE INDEX `idx_create_state` ON `tb_withdraw_order` (`create_time`, `state`);
CREATE INDEX `idx_withdraw_type_state` ON `tb_withdraw_order` (`withdraw_type`, `state`);
CREATE INDEX `idx_agent_state` ON `tb_withdraw_order` (`agent`, `state`);
CREATE INDEX `idx_salesman_state` ON `tb_withdraw_order` (`salesmanid`, `state`);
