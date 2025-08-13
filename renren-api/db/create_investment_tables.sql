-- 用户投资记录相关表结构
-- 创建时间: 2024年
-- 注意：tb_project表已存在，此处只创建投资记录相关表

-- 1. 创建用户投资记录表
CREATE TABLE IF NOT EXISTS `tb_investment_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `project_id` bigint(20) NOT NULL COMMENT '项目ID',
  `investment_amount` bigint(20) NOT NULL COMMENT '投资金额(分)',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `order_abbr` varchar(50) DEFAULT NULL COMMENT '订单号简称',
  `order_date` datetime NOT NULL COMMENT '投资日期',
  `profit_amount` bigint(20) DEFAULT 0 COMMENT '收益金额(分)',
  `profit_date` datetime DEFAULT NULL COMMENT '收益日期',
  `profit_interest` bigint(20) DEFAULT 0 COMMENT '收益利息(分)',
  `profit_principal` bigint(20) DEFAULT 0 COMMENT '收益本金(分)',
  `status` tinyint(4) DEFAULT 0 COMMENT '状态 0:未收益 1:已收益',
  `cycle` int(11) DEFAULT NULL COMMENT '项目周期(天)',
  `cycle_type` tinyint(4) DEFAULT 1 COMMENT '周期类型 1:到期收益含本金 2:每日返本金到期收益',
  `ddsy` bigint(20) DEFAULT 0 COMMENT '等待收益(分)',
  `invest_count` int(11) DEFAULT 1 COMMENT '投资次数',
  `rush_minute` int(11) DEFAULT 0 COMMENT '抢购分钟数',
  `agent` varchar(100) DEFAULT NULL COMMENT '代理',
  `salesmanid` varchar(50) DEFAULT NULL COMMENT '销售员ID',
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_id` (`order_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_project_id` (`project_id`),
  KEY `idx_status` (`status`),
  KEY `idx_order_date` (`order_date`),
  KEY `idx_user_project` (`user_id`, `project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户投资记录表';

-- 2. 创建投资收益明细表
CREATE TABLE IF NOT EXISTS `tb_investment_profit_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `investment_id` bigint(20) NOT NULL COMMENT '投资记录ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `project_id` bigint(20) NOT NULL COMMENT '项目ID',
  `profit_type` tinyint(4) DEFAULT 1 COMMENT '收益类型 1:利息 2:本金 3:其他',
  `profit_amount` bigint(20) NOT NULL COMMENT '收益金额(分)',
  `profit_date` datetime NOT NULL COMMENT '收益日期',
  `status` tinyint(4) DEFAULT 0 COMMENT '状态 0:未到账 1:已到账',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_investment_id` (`investment_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_project_id` (`project_id`),
  KEY `idx_profit_date` (`profit_date`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='投资收益明细表';

-- 3. 插入示例投资记录数据
INSERT INTO `tb_investment_record` (
  `user_id`, `project_id`, `investment_amount`, `order_id`, `order_abbr`, 
  `order_date`, `profit_amount`, `profit_date`, `profit_interest`, 
  `profit_principal`, `status`, `cycle`, `cycle_type`, `ddsy`, 
  `invest_count`, `rush_minute`, `agent`, `salesmanid`
) VALUES 
(1, 1, 1000000, 1001, 'INV001', '2024-01-15 10:00:00', 50000, '2024-02-15 10:00:00', 50000, 1000000, 1, 30, 1, 0, 1, 0, '代理A', 'SALES001'),
(1, 2, 2000000, 1002, 'INV002', '2024-01-20 14:30:00', 0, NULL, 0, 0, 0, 60, 2, 100000, 1, 0, '代理B', 'SALES002'),
(1, 3, 1500000, 1003, 'INV003', '2024-01-25 16:45:00', 0, NULL, 0, 0, 0, 45, 1, 75000, 1, 0, '代理C', 'SALES003'),
(2, 1, 800000, 1004, 'INV004', '2024-01-16 09:15:00', 40000, '2024-02-16 09:15:00', 40000, 800000, 1, 30, 1, 0, 1, 0, '代理A', 'SALES001'),
(2, 4, 1200000, 1005, 'INV005', '2024-01-22 11:20:00', 0, NULL, 0, 0, 0, 90, 1, 108000, 1, 0, '代理D', 'SALES004'),
(3, 2, 1500000, 1006, 'INV006', '2024-01-18 15:30:00', 0, NULL, 0, 0, 0, 60, 2, 75000, 1, 0, '代理B', 'SALES002'),
(3, 5, 900000, 1007, 'INV007', '2024-01-28 13:45:00', 0, NULL, 0, 0, 0, 75, 2, 67500, 1, 0, '代理E', 'SALES005');

-- 4. 插入示例收益明细数据
INSERT INTO `tb_investment_profit_detail` (
  `investment_id`, `user_id`, `project_id`, `profit_type`, `profit_amount`, 
  `profit_date`, `status`, `remark`
) VALUES 
(1, 1, 1, 1, 50000, '2024-02-15 10:00:00', 1, '新能源项目A利息收益'),
(1, 1, 1, 2, 1000000, '2024-02-15 10:00:00', 1, '新能源项目A本金返还'),
(4, 2, 1, 1, 40000, '2024-02-16 09:15:00', 1, '新能源项目A利息收益'),
(4, 2, 1, 2, 800000, '2024-02-16 09:15:00', 1, '新能源项目A本金返还');

-- 5. 创建索引优化查询性能
CREATE INDEX `idx_investment_user_status` ON `tb_investment_record` (`user_id`, `status`);
CREATE INDEX `idx_investment_project_status` ON `tb_investment_record` (`project_id`, `status`);
CREATE INDEX `idx_profit_investment_type` ON `tb_investment_profit_detail` (`investment_id`, `profit_type`);

-- 6. 添加外键约束（可选，根据实际需求决定）
-- ALTER TABLE `tb_investment_record` ADD CONSTRAINT `fk_investment_user` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`);
-- ALTER TABLE `tb_investment_record` ADD CONSTRAINT `fk_investment_project` FOREIGN KEY (`project_id`) REFERENCES `tb_project` (`id`);
-- ALTER TABLE `tb_investment_profit_detail` ADD CONSTRAINT `fk_profit_investment` FOREIGN KEY (`investment_id`) REFERENCES `tb_investment_record` (`id`);

-- 7. 验证表结构
-- DESCRIBE tb_investment_record;
-- DESCRIBE tb_investment_profit_detail;

-- 8. 验证数据
-- SELECT COUNT(*) FROM tb_investment_record;
-- SELECT COUNT(*) FROM tb_investment_profit_detail;
