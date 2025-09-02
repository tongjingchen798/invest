-- 在线聊天表
CREATE TABLE `tb_online_chat` (
  `chat_id` varchar(100) NOT NULL COMMENT '聊天ID',
  `sender_id` bigint(20) NOT NULL COMMENT '发送者ID',
  `receiver_id` bigint(20) NOT NULL COMMENT '接收者ID',
  `message` text COMMENT '消息内容',
  `message_type` int(11) DEFAULT '1' COMMENT '消息类型 1:文本 2:图片 3:文件',
  `is_read` int(11) DEFAULT '0' COMMENT '是否已读 0:未读 1:已读',
  `send_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  `read_time` datetime DEFAULT NULL COMMENT '读取时间',
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`chat_id`),
  KEY `idx_sender_id` (`sender_id`),
  KEY `idx_receiver_id` (`receiver_id`),
  KEY `idx_is_read` (`is_read`),
  KEY `idx_send_time` (`send_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='在线聊天表';

-- 插入示例数据
INSERT INTO `tb_online_chat` (`chat_id`, `sender_id`, `receiver_id`, `message`, `message_type`, `is_read`, `send_time`, `read_time`) VALUES
('chat_001', 1, 2, '你好，有什么可以帮助您的吗？', 1, 0, NOW(), NULL),
('chat_002', 2, 1, '我想了解一下投资产品', 1, 0, NOW(), NULL),
('chat_003', 1, 2, '好的，我来为您详细介绍', 1, 1, NOW(), NOW());
