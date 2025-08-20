-- 站内信表
CREATE TABLE `tb_mail` (
  `mail_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '站内信ID',
  `theme` varchar(255) NOT NULL COMMENT '标题',
  `content` text COMMENT '内容',
  `from_user` varchar(100) DEFAULT 'system' COMMENT '发件人，如果是前端发过来的，就是前端的用户账号，如果是后台发的，默认叫system',
  `addresseename` varchar(100) DEFAULT NULL COMMENT '收件人账号，为空就是所有人',
  `data_type` int(11) DEFAULT '2' COMMENT '消息类型，1 是指定代理 2 是所有人',
  `is_websend` int(11) DEFAULT '2' COMMENT '是否前端发送 1 是 2 不是 2是后台',
  `send_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  `expiration_time` datetime DEFAULT NULL COMMENT '过期时间，默认发送时间加7天',
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`mail_id`),
  KEY `idx_theme` (`theme`),
  KEY `idx_from_user` (`from_user`),
  KEY `idx_send_time` (`send_time`),
  KEY `idx_data_type` (`data_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='站内信表';

-- 插入示例数据
INSERT INTO `tb_mail` (`theme`, `content`, `from_user`, `addresseename`, `data_type`, `is_websend`, `send_time`, `expiration_time`) VALUES
('系统维护通知', '系统将于今晚22:00-24:00进行维护，期间可能影响正常使用，请提前做好准备。', 'system', '', 2, 2, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY)),
('新功能上线', '我们推出了新的投资功能，现在可以享受更高的收益率，详情请查看最新公告。', 'system', '', 2, 2, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY)),
('账户安全提醒', '为了保障您的账户安全，请定期修改密码，不要将密码告诉他人。', 'system', '', 2, 2, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY));
