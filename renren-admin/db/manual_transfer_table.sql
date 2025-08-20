-- 人工转帐记录表
CREATE TABLE `tb_manual_transfer` (
  `id` varchar(50) NOT NULL COMMENT '主键ID',
  `msg` varchar(500) DEFAULT NULL COMMENT '消息/状态说明',
  `withdraw_time` datetime DEFAULT NULL COMMENT '转帐时间',
  `amount` bigint(20) DEFAULT NULL COMMENT '转帐金额（分）',
  `blank_code` varchar(100) DEFAULT NULL COMMENT '银行代码',
  `blank_name` varchar(200) DEFAULT NULL COMMENT '银行名称',
  `pay_name` varchar(100) DEFAULT NULL COMMENT '收款人姓名',
  `pay_no` varchar(100) DEFAULT NULL COMMENT '收款账号',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `state_time` datetime DEFAULT NULL COMMENT '状态时间',
  `state` int(11) DEFAULT '0' COMMENT '状态：0-待处理，1-处理中，2-已完成，3-失败，4-已取消',
  `withdraw_type` int(11) DEFAULT '0' COMMENT '转帐类型：0-余额转帐，1-佣金转帐，2-其他转帐',
  `orderno` varchar(100) DEFAULT NULL COMMENT '订单号',
  `threeorder_no` varchar(100) DEFAULT NULL COMMENT '第三方订单号',
  `ifsc` varchar(100) DEFAULT NULL COMMENT 'IFSC代码',
  `agent_name` varchar(100) DEFAULT NULL COMMENT '代理名称',
  `agent` varchar(50) DEFAULT NULL COMMENT '代理ID',
  `salesmanid` varchar(50) DEFAULT NULL COMMENT '业务员ID',
  `salesman_name` varchar(100) DEFAULT NULL COMMENT '业务员姓名',
  `channelid` varchar(50) DEFAULT NULL COMMENT '渠道ID',
  `merchantid` varchar(50) DEFAULT NULL COMMENT '商户ID',
  `merchantname` varchar(200) DEFAULT NULL COMMENT '商户名称',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `oper_code` varchar(100) DEFAULT NULL COMMENT '操作人',
  `transfer_source` int(11) DEFAULT '1' COMMENT '转帐来源：1-用户申请，2-管理员操作，3-系统自动',
  `hand_fee` bigint(20) DEFAULT '0' COMMENT '手续费（分）',
  `real_amount` bigint(20) DEFAULT NULL COMMENT '实际到账金额（分）',
  `channel_amount` bigint(20) DEFAULT NULL COMMENT '渠道金额（分）',
  PRIMARY KEY (`id`),
  KEY `idx_orderno` (`orderno`),
  KEY `idx_threeorder_no` (`threeorder_no`),
  KEY `idx_state` (`state`),
  KEY `idx_withdraw_type` (`withdraw_type`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_agent` (`agent`),
  KEY `idx_salesmanid` (`salesmanid`),
  KEY `idx_merchantid` (`merchantid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人工转帐记录表';

-- 插入示例数据
INSERT INTO `tb_manual_transfer` (
  `id`, `msg`, `withdraw_time`, `amount`, `blank_code`, `blank_name`, 
  `pay_name`, `pay_no`, `remark`, `create_time`, `state_time`, 
  `state`, `withdraw_type`, `orderno`, `threeorder_no`, `ifsc`, 
  `agent_name`, `agent`, `salesmanid`, `salesman_name`, `channelid`, 
  `merchantid`, `merchantname`, `update_time`, `oper_code`, 
  `transfer_source`, `hand_fee`, `real_amount`, `channel_amount`
) VALUES (
  '1753344001331', NULL, '2025-07-24 16:00:01', 35000, NULL, NULL,
  'dssdd', '2333333333', '三方提现null', '2025-07-24 16:00:01', '2025-07-24 16:01:08',
  2, 0, 'M2025072416000151431', '3202507241330015863875863956686', 'SBIN0011218',
  NULL, NULL, '1067246875800000001', 'admin', '1785184156860125186',
  '1948278686040588289', 'pepay', '2025-07-24 16:01:08', NULL,
  1, 0, 35000, 0
);
