-- 创建充值订单表
-- 用于存储用户的充值订单信息

USE renren_security;

-- 删除旧表（如果存在）
DROP TABLE IF EXISTS `tb_charge_order`;

-- 创建充值订单表
CREATE TABLE `tb_charge_order` (
  `charge_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '充值ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `mobile` varchar(20) DEFAULT NULL COMMENT '用户账号',
  `agent` varchar(100) DEFAULT NULL COMMENT '代理编号',
  `salesmanid` varchar(50) DEFAULT NULL COMMENT '业务员编号',
  `channel` varchar(50) DEFAULT NULL COMMENT '渠道',
  `channel_type` varchar(50) DEFAULT NULL COMMENT '通道类型',
  `channelid` bigint(20) DEFAULT NULL COMMENT '支付通道主键',
  `merchantid` bigint(20) DEFAULT NULL COMMENT '商户主键',
  `merchantname` varchar(100) DEFAULT NULL COMMENT '商户名',
  `orderno` varchar(100) NOT NULL COMMENT '平台订单号',
  `threeorder_no` varchar(100) DEFAULT NULL COMMENT '第三方订单号',
  `platform` varchar(50) DEFAULT NULL COMMENT '渠道编码',
  `amount` bigint(20) DEFAULT 0 COMMENT '充值金币(分)',
  `real_amount` bigint(20) DEFAULT 0 COMMENT '真实充值额(分)',
  `uamout` bigint(20) DEFAULT 0 COMMENT 'U金额',
  `uprice` bigint(20) DEFAULT 0 COMMENT 'U价格',
  `u_real_amout` bigint(20) DEFAULT 0 COMMENT 'U实际支付金额',
  `wallet_id` bigint(20) DEFAULT NULL COMMENT '钱包ID',
  `wallet_addr` varchar(200) DEFAULT NULL COMMENT '钱包地址',
  `state` tinyint(4) DEFAULT 0 COMMENT '状态 0 待审核  1 审核通过  2 失败',
  `info_ip` varchar(50) DEFAULT NULL COMMENT '用户IP',
  `oper_code` varchar(50) DEFAULT NULL COMMENT '操作人',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `sourcetype_name` varchar(100) DEFAULT NULL COMMENT '充值渠道名称',
  `charge_time` datetime DEFAULT NULL COMMENT '充值日期',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`charge_id`),
  UNIQUE KEY `uk_orderno` (`orderno`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_state` (`state`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_channel` (`channel`),
  KEY `idx_agent` (`agent`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='充值订单表';

-- 插入测试数据
INSERT INTO `tb_charge_order` (
  `user_id`, `mobile`, `agent`, `salesmanid`, `channel`, `channel_type`,
  `channelid`, `merchantid`, `merchantname`, `orderno`, `threeorder_no`,
  `platform`, `amount`, `real_amount`, `uamout`, `uprice`, `u_real_amout`,
  `wallet_id`, `wallet_addr`, `state`, `info_ip`, `oper_code`, `remark`,
  `sourcetype_name`, `charge_time`, `create_time`
) VALUES 
(1, '13800138000', 'agent001', 'sales001', 'bank', 'bank_transfer', 
 1, 1, '测试商户', 'CHG202401010001', 'THIRD001', 'BANK001', 
 100000, 100000, 100, 1000, 100000, 
 NULL, '', 1, '192.168.1.1', 'admin', '测试充值', 
 '银行转账', NOW(), NOW()),

(1, '13800138000', 'agent001', 'sales001', 'alipay', 'online_payment', 
 2, 1, '测试商户', 'CHG202401010002', 'THIRD002', 'ALIPAY001', 
 50000, 50000, 50, 1000, 50000, 
 NULL, '', 1, '192.168.1.1', 'admin', '支付宝充值', 
 '支付宝', NOW(), NOW()),

(2, '13800138001', 'agent002', 'sales002', 'wechat', 'online_payment', 
 3, 1, '测试商户', 'CHG202401010003', 'THIRD003', 'WECHAT001', 
 200000, 200000, 200, 1000, 200000, 
 NULL, '', 0, '192.168.1.2', 'admin', '微信充值', 
 '微信支付', NOW(), NOW());

-- 验证表结构
DESCRIBE tb_charge_order;

-- 验证数据
SELECT * FROM tb_charge_order;

-- 验证索引
SHOW INDEX FROM tb_charge_order;
