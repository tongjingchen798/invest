-- 广告素材表
CREATE TABLE `tb_advertisement` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `type` int(11) NOT NULL DEFAULT '1' COMMENT '广告类型 1=LOG,2=轮播图，3=个人中心 4=弹窗广告',
  `title` varchar(255) NOT NULL COMMENT '广告标题',
  `logos_addr` varchar(500) DEFAULT NULL COMMENT '图片地址',
  `logos_linkaddr` varchar(500) DEFAULT NULL COMMENT '链接地址',
  `remark` text COMMENT '备注描述',
  `sx_date` datetime DEFAULT NULL COMMENT '生效时间',
  `hour` int(11) DEFAULT '0' COMMENT '展示时长（小时）',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `status` int(11) DEFAULT '1' COMMENT '状态 0=禁用 1=启用',
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `updater` varchar(50) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`),
  KEY `idx_status` (`status`),
  KEY `idx_sx_date` (`sx_date`),
  KEY `idx_create_date` (`create_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='广告素材表';

-- 插入测试数据
INSERT INTO `tb_advertisement` (`type`, `title`, `logos_addr`, `logos_linkaddr`, `remark`, `sx_date`, `hour`, `sort`, `status`, `create_date`) VALUES
(1, '首页Logo', 'http://8.216.132.154/images/logo.png', 'http://8.216.132.154', '网站Logo图片', '2025-07-10 20:05:01', 24, 1, 1, '2025-07-10 20:05:01'),
(2, '轮播图1', 'http://8.216.132.154/images/banner1.jpg', 'http://8.216.132.154/banner1', '首页轮播图', '2025-07-10 20:05:01', 48, 1, 1, '2025-07-10 20:05:01'),
(3, '个人中心背景', 'http://8.216.132.154/images/profile.jpg', 'http://8.216.132.154/profile', '个人中心背景图', '2025-07-10 20:05:01', 72, 1, 1, '2025-07-10 20:05:01'),
(4, '弹窗', 'http://8.216.132.154/images/tools/13e3d8ff-61c9-4cb7-b572-6c37d6194ae2.png', '--', '--', '2025-07-12 02:46:00', 48, 1, 1, '2025-07-10 20:05:01');
