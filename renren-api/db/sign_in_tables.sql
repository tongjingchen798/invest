-- 签到相关表结构
-- 创建时间: 2024年

-- 1. 签到奖励配置表
CREATE TABLE `tb_sign_reward_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `oneDay` bigint(20) DEFAULT 100 COMMENT '第1天奖励金额(分)',
  `twoDay` bigint(20) DEFAULT 200 COMMENT '第2天奖励金额(分)',
  `threeDay` bigint(20) DEFAULT 300 COMMENT '第3天奖励金额(分)',
  `fourDay` bigint(20) DEFAULT 400 COMMENT '第4天奖励金额(分)',
  `fiveDay` bigint(20) DEFAULT 500 COMMENT '第5天奖励金额(分)',
  `sixDay` bigint(20) DEFAULT 600 COMMENT '第6天奖励金额(分)',
  `sevenDay` bigint(20) DEFAULT 800 COMMENT '第7天奖励金额(分)',
  `firstsevenDay` bigint(20) DEFAULT 700 COMMENT '首次7天奖励金额(分)',
  `status` tinyint(4) DEFAULT 1 COMMENT '状态 0:禁用 1:启用',
  `oneDayqdtype` tinyint(4) DEFAULT 2 COMMENT '第1天签到类型 1:积分 2:余额',
  `twoDayqdtype` tinyint(4) DEFAULT 2 COMMENT '第2天签到类型 1:积分 2:余额',
  `threeDayqdtype` tinyint(4) DEFAULT 2 COMMENT '第3天签到类型 1:积分 2:余额',
  `fourDayqdtype` tinyint(4) DEFAULT 2 COMMENT '第4天签到类型 1:积分 2:余额',
  `fiveDayqdtype` tinyint(4) DEFAULT 2 COMMENT '第5天签到类型 1:积分 2:余额',
  `sixDayqdtype` tinyint(4) DEFAULT 2 COMMENT '第6天签到类型 1:积分 2:余额',
  `sevenDayqdtype` tinyint(4) DEFAULT 2 COMMENT '第7天签到类型 1:积分 2:余额',
  `firstsevenDayqdtype` tinyint(4) DEFAULT 2 COMMENT '首次7天签到类型 1:积分 2:余额',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='签到奖励配置表';

-- 2. 用户签到记录表
CREATE TABLE `tb_user_sign_in` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `sign_date` date NOT NULL COMMENT '签到日期',
  `sign_time` datetime NOT NULL COMMENT '签到时间',
  `continuous_days` int(11) DEFAULT 1 COMMENT '连续签到天数',
  `reward_amount` bigint(20) DEFAULT 0 COMMENT '奖励金额(分)',
  `reward_type` tinyint(4) DEFAULT 2 COMMENT '奖励类型 1:积分 2:余额',
  `status` tinyint(4) DEFAULT 1 COMMENT '状态 0:无效 1:有效',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_date` (`user_id`, `sign_date`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_sign_date` (`sign_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户签到记录表';

-- 3. 用户签到统计表
CREATE TABLE `tb_user_sign_statistics` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `total_sign_days` int(11) DEFAULT 0 COMMENT '总签到天数',
  `continuous_days` int(11) DEFAULT 0 COMMENT '当前连续签到天数',
  `max_continuous_days` int(11) DEFAULT 0 COMMENT '历史最大连续签到天数',
  `total_reward_amount` bigint(20) DEFAULT 0 COMMENT '累计获得奖励金额(分)',
  `last_sign_date` date DEFAULT NULL COMMENT '最后签到日期',
  `last_sign_time` datetime DEFAULT NULL COMMENT '最后签到时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户签到统计表';

-- 插入默认签到奖励配置数据
INSERT INTO `tb_sign_reward_config` (
  `oneDay`, `twoDay`, `threeDay`, `fourDay`, `fiveDay`, `sixDay`, `sevenDay`, `firstsevenDay`,
  `status`, `oneDayqdtype`, `twoDayqdtype`, `threeDayqdtype`, `fourDayqdtype`, 
  `fiveDayqdtype`, `sixDayqdtype`, `sevenDayqdtype`, `firstsevenDayqdtype`
) VALUES (
  100, 200, 300, 400, 500, 600, 800, 700,
  1, 2, 2, 2, 2, 2, 2, 2, 2
);

-- 创建索引优化查询性能
CREATE INDEX `idx_user_sign_user_date` ON `tb_user_sign_in` (`user_id`, `sign_date`);
CREATE INDEX `idx_user_sign_continuous` ON `tb_user_sign_in` (`user_id`, `continuous_days`);
CREATE INDEX `idx_user_statistics_user` ON `tb_user_sign_statistics` (`user_id`);
