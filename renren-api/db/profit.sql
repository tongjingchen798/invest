-- 付息还本表
CREATE TABLE `tb_profit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `invest_id` bigint(20) NOT NULL COMMENT '投资项目ID',
  `invest_name` varchar(100) NOT NULL COMMENT '项目名称',
  `investment_amount` bigint(20) NOT NULL COMMENT '投资金额(分)',
  `profit_amount` bigint(20) DEFAULT 0 COMMENT '收益金额(分)',
  `profit_interest` bigint(20) DEFAULT 0 COMMENT '收益利息(分)',
  `profit_principal` bigint(20) DEFAULT 0 COMMENT '收益本金(分)',
  `ddsy` bigint(20) DEFAULT 0 COMMENT '等待收益(分)',
  `cycle` int(11) NOT NULL COMMENT '项目周期(天)',
  `cycle_type` tinyint(4) NOT NULL DEFAULT 1 COMMENT '周期类型（1:到期收益含本金,2:每日返本金到期收益）',
  `order_date` datetime NOT NULL COMMENT '投资日期',
  `profit_date` datetime DEFAULT NULL COMMENT '收益日期',
  `order_id` bigint(20) NOT NULL COMMENT '下单ID',
  `order_abbr` varchar(50) DEFAULT NULL COMMENT '订单号简称',
  `invest_count` int(11) DEFAULT 1 COMMENT '投资总数',
  `img` varchar(200) DEFAULT NULL COMMENT '图片',
  `status` tinyint(4) DEFAULT 0 COMMENT '状态 0：未收益 1：已收益',
  `agent` varchar(50) DEFAULT NULL COMMENT '代理ID',
  `salesmanid` varchar(50) DEFAULT NULL COMMENT '业务员ID',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
  `rush_minute` int(11) DEFAULT NULL COMMENT '抢购分钟',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_invest_id` (`invest_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_status` (`status`),
  KEY `idx_order_date` (`order_date`),
  KEY `idx_profit_date` (`profit_date`),
  KEY `idx_agent` (`agent`),
  KEY `idx_salesmanid` (`salesmanid`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='付息还本表';

-- 插入测试数据
INSERT INTO `tb_profit` (
    `user_id`, `invest_id`, `invest_name`, `investment_amount`, 
    `profit_amount`, `profit_interest`, `profit_principal`, `ddsy`,
    `cycle`, `cycle_type`, `order_date`, `profit_date`, `order_id`,
    `order_abbr`, `invest_count`, `img`, `status`, `agent`, 
    `salesmanid`, `mobile`, `rush_minute`
) VALUES 
(1938603271541919746, 1001, 'ANDAR BAHAR项目', 100000, 1500, 1500, 0, 5000, 30, 2, '2025-01-20 10:00:00', '2025-01-27 10:00:00', 10001, 'AB001', 1, 'project1.jpg', 1, '1748403717627', '1748403763980', '917777777713', 30),
(1938603271541919746, 1002, 'Teen Patti项目', 200000, 3000, 3000, 0, 8000, 45, 1, '2025-01-15 14:30:00', '2025-01-25 14:30:00', 10002, 'TP001', 2, 'project2.jpg', 1, '1748403717627', '1748403763980', '917777777713', 45),
(1938603271541919746, 1003, 'Rummy项目', 150000, 0, 0, 0, 3000, 60, 2, '2025-01-10 09:15:00', NULL, 10003, 'RM001', 1, 'project3.jpg', 0, '1748403717627', '1748403763980', '917777777713', 60);
