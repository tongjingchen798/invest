-- 创建黑白名单表（简化版）
CREATE TABLE `tb_blacklist` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `mobile` varchar(20) NOT NULL COMMENT '会员账号',
  `type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '类型：1-白名单，2-黑名单',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  KEY `idx_mobile` (`mobile`),
  KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='黑白名单表';

-- 插入示例数据
INSERT INTO `tb_blacklist` (`user_id`, `mobile`, `type`) VALUES
(1, '13800138000', 1),
(2, '13800138001', 2);
