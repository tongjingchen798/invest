-- 代理兑付收益统计表
CREATE TABLE IF NOT EXISTS `tb_agent_commission_stats` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `agent_id` varchar(100) NOT NULL COMMENT '代理ID',
  `agent_name` varchar(100) DEFAULT NULL COMMENT '代理名称',
  `salesman_id` varchar(100) NOT NULL COMMENT '业务员ID',
  `salesman_name` varchar(100) DEFAULT NULL COMMENT '业务员名称',
  `statistics_date` date NOT NULL COMMENT '统计日期',
  `total_charge_amount` bigint(20) DEFAULT 0 COMMENT '总充值金额(分)',
  `total_withdraw_amount` bigint(20) DEFAULT 0 COMMENT '总提现金额(分)',
  `net_amount` bigint(20) DEFAULT 0 COMMENT '净额(充值-提现)(分)',
  `commission_amount` bigint(20) DEFAULT 0 COMMENT '兑付金额(分)',
  `commission_rate` decimal(5,4) DEFAULT 0.0500 COMMENT '兑付比例(默认5%)',
  `user_count` int(11) DEFAULT 0 COMMENT '名下用户数量',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_agent_salesman_date` (`agent_id`, `salesman_id`, `statistics_date`),
  KEY `idx_statistics_date` (`statistics_date`),
  KEY `idx_agent_id` (`agent_id`),
  KEY `idx_salesman_id` (`salesman_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代理兑付收益统计表';

-- 插入测试数据（可选）
INSERT INTO `tb_agent_commission_stats` 
(`agent_id`, `agent_name`, `salesman_id`, `salesman_name`, `statistics_date`, `total_charge_amount`, `total_withdraw_amount`, `net_amount`, `commission_amount`, `user_count`) 
VALUES 
('AG001', '测试代理1', 'SM001', '测试业务员1', CURDATE()-1, 1000000, 500000, 500000, 25000, 10),
('AG001', '测试代理1', 'SM002', '测试业务员2', CURDATE()-1, 800000, 300000, 500000, 25000, 8),
('AG002', '测试代理2', 'SM003', '测试业务员3', CURDATE()-1, 1200000, 600000, 600000, 30000, 12);
