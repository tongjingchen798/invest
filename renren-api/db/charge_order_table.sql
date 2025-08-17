-- 充值订单表建表SQL
-- 根据 ChargeOrderEntity 实体类生成
-- 表名: tb_charge_order

CREATE TABLE `tb_charge_order` (
  `charge_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '充值ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `mobile` varchar(20) DEFAULT NULL COMMENT '用户账号',
  `agent` varchar(50) DEFAULT NULL COMMENT '代理编号',
  `salesmanid` varchar(50) DEFAULT NULL COMMENT '业务员编号',
  `channel` varchar(100) DEFAULT NULL COMMENT '渠道',
  `channel_type` varchar(50) DEFAULT NULL COMMENT '通道类型',
  `channelid` bigint(20) DEFAULT NULL COMMENT '支付通道主键',
  `merchantid` bigint(20) DEFAULT NULL COMMENT '商户主键',
  `merchantname` varchar(200) DEFAULT NULL COMMENT '商户名',
  `orderno` varchar(100) DEFAULT NULL COMMENT '平台订单号',
  `threeorder_no` varchar(100) DEFAULT NULL COMMENT '第三方订单号',
  `platform` varchar(50) DEFAULT NULL COMMENT '渠道编码',
  `amount` bigint(20) DEFAULT NULL COMMENT '充值金币(分)',
  `real_amount` bigint(20) DEFAULT NULL COMMENT '真实充值额(分)',
  `uamout` bigint(20) DEFAULT NULL COMMENT 'U金额',
  `uprice` bigint(20) DEFAULT NULL COMMENT 'U价格',
  `u_real_amout` bigint(20) DEFAULT NULL COMMENT 'U实际支付金额',
  `wallet_id` bigint(20) DEFAULT NULL COMMENT '钱包ID',
  `wallet_addr` varchar(255) DEFAULT NULL COMMENT '钱包地址',
  `state` tinyint(4) DEFAULT '0' COMMENT '状态 0:待审核 1:审核通过 2:失败',
  `info_ip` varchar(50) DEFAULT NULL COMMENT '用户IP',
  `oper_code` varchar(50) DEFAULT NULL COMMENT '操作人',
  `remark` text COMMENT '备注',
  `sourcetype_name` varchar(100) DEFAULT NULL COMMENT '充值渠道名称',
  `charge_time` datetime DEFAULT NULL COMMENT '充值日期',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`charge_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_mobile` (`mobile`),
  KEY `idx_agent` (`agent`),
  KEY `idx_orderno` (`orderno`),
  KEY `idx_threeorder_no` (`threeorder_no`),
  KEY `idx_state` (`state`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_charge_time` (`charge_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='充值订单表';

-- 索引说明:
-- idx_user_id: 用户ID索引，用于按用户查询充值记录
-- idx_mobile: 用户账号索引，用于按手机号查询
-- idx_agent: 代理编号索引，用于按代理查询
-- idx_orderno: 平台订单号索引，用于订单查询
-- idx_threeorder_no: 第三方订单号索引，用于第三方订单查询
-- idx_state: 状态索引，用于按状态筛选
-- idx_create_time: 创建时间索引，用于时间范围查询和排序
-- idx_charge_time: 充值时间索引，用于按充值时间查询和排序
