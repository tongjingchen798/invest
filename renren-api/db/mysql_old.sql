-- 用户表
CREATE TABLE tb_user (
  id bigint NOT NULL COMMENT 'id',
  username varchar(50) NOT NULL COMMENT '用户名',
  mobile varchar(20) NOT NULL COMMENT '手机号',
  password varchar(64) COMMENT '密码',
  create_date datetime COMMENT '创建时间',
  PRIMARY KEY (id),
  UNIQUE INDEX (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户';

-- 用户Token表
CREATE TABLE tb_token (
  id bigint NOT NULL COMMENT 'id',
  user_id bigint NOT NULL COMMENT '用户ID',
  token varchar(100) NOT NULL COMMENT 'token',
  expire_date datetime COMMENT '过期时间',
  update_date datetime COMMENT '更新时间',
  PRIMARY KEY (id),
  UNIQUE INDEX (user_id),
  UNIQUE INDEX (token)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户Token';

-- 账号：13612345678  密码：admin
INSERT INTO tb_user (id, username, mobile, password, create_date) VALUES (1067246875900000001, 'mark', '13612345678', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', now());

-- 广告/图片管理表
CREATE TABLE issues (
  id bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  title varchar(200) NOT NULL COMMENT '名称',
  content text COMMENT '描述',
  images_addr varchar(500) NOT NULL COMMENT '链接地址',
  type tinyint NOT NULL COMMENT '广告类型 1=LOG,2=轮播图，3=个人中心 4=弹窗广告',
  sort int DEFAULT 0 COMMENT '排序',
  status tinyint DEFAULT 1 COMMENT '状态 0=禁用 1=启用',
  create_date datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_date datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  creator bigint COMMENT '创建者',
  updater bigint COMMENT '更新者',
  PRIMARY KEY (id),
  KEY idx_type (type),
  KEY idx_status (status),
  KEY idx_create_date (create_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='广告/图片管理表';

-- 插入测试数据
INSERT INTO issues (title, content, images_addr, type, sort, status, create_date) VALUES
('首页Logo', '网站Logo图片', 'https://example.com/logo.png', 1, 1, 1, NOW()),
('轮播图1', '首页轮播图1', 'https://example.com/banner1.jpg', 2, 1, 1, NOW()),
('轮播图2', '首页轮播图2', 'https://example.com/banner2.jpg', 2, 2, 1, NOW()),
('个人中心背景', '个人中心背景图片', 'https://example.com/profile-bg.jpg', 3, 1, 1, NOW()),
('弹窗广告', '弹窗广告图片', 'https://example.com/popup.jpg', 4, 1, 1, NOW());
