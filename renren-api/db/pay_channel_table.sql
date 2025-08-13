-- 支付通道表结构
-- 创建时间: 2024年

CREATE TABLE `tb_pay_channel` (
  `channelid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `channel_name` varchar(100) NOT NULL COMMENT '通道名,前端显示用的',
  `channel_type` varchar(20) NOT NULL COMMENT '通道类型 UPI/SWIPE/USDT',
  `chargeorwithdraw` varchar(10) NOT NULL COMMENT '充值 1 或者提现 2',
  `merchantid` bigint(20) DEFAULT NULL COMMENT '商户主键',
  `status` tinyint(4) DEFAULT 1 COMMENT '上下架 0：下架 1：上架',
  `usdt_gift_ratio` decimal(10,4) DEFAULT 0.0000 COMMENT 'usdt赠送比例',
  `usdt_local_currency_rate` varchar(50) DEFAULT NULL COMMENT 'usdt兑当地货币汇率',
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`channelid`),
  KEY `idx_status` (`status`),
  KEY `idx_channel_type` (`channel_type`),
  KEY `idx_chargeorwithdraw` (`chargeorwithdraw`),
  KEY `idx_merchantid` (`merchantid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付通道表';

-- 插入示例数据
INSERT INTO `tb_pay_channel` (
  `channel_name`, `channel_type`, `chargeorwithdraw`, `merchantid`, 
  `status`, `usdt_gift_ratio`, `usdt_local_currency_rate`
) VALUES 
('UPI充值通道', 'UPI', '1', 1, 1, 0.0100, '1.00'),
('UPI提现通道', 'UPI', '2', 1, 1, 0.0000, '1.00'),
('SWIPE充值通道', 'SWIPE', '1', 1, 1, 0.0150, '1.00'),
('SWIPE提现通道', 'SWIPE', '2', 1, 1, 0.0000, '1.00'),
('USDT充值通道', 'USDT', '1', 1, 1, 0.0200, '1.00'),
('USDT提现通道', 'USDT', '2', 1, 1, 0.0000, '1.00');

-- 创建索引优化查询性能
CREATE INDEX `idx_status_type` ON `tb_pay_channel` (`status`, `channel_type`);
CREATE INDEX `idx_status_charge` ON `tb_pay_channel` (`status`, `chargeorwithdraw`);
