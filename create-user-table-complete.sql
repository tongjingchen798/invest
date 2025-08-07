-- 完整的用户表创建脚本
-- 包含所有新字段

USE renren_security;

-- 删除旧表（如果存在）
DROP TABLE IF EXISTS tb_user;

-- 创建新的用户表
CREATE TABLE tb_user (
  id bigint NOT NULL COMMENT 'id',
  username varchar(50) NOT NULL COMMENT '用户名',
  mobile varchar(20) NOT NULL COMMENT '手机号',
  password varchar(64) COMMENT '密码',
  two_pwd varchar(64) COMMENT '二级密码',
  invite_code varchar(50) COMMENT '邀请码',
  agent varchar(200) COMMENT '代理信息',
  channel varchar(100) COMMENT '客户渠道号',
  equipment int COMMENT '登录端口(1:安卓, 2:ios, 3:pc, 4:未知)',
  create_date datetime COMMENT '创建时间',
  PRIMARY KEY (id),
  UNIQUE INDEX (username),
  UNIQUE INDEX (mobile)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户';

-- 插入测试数据
INSERT INTO tb_user (
    id, 
    username, 
    mobile, 
    password, 
    two_pwd,
    invite_code,
    agent,
    channel,
    equipment,
    create_date
) VALUES (
    1067246875900000001, 
    'mark', 
    '13612345678', 
    '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918',
    NULL,
    NULL,
    NULL,
    NULL,
    3,
    now()
);

-- 验证表结构
DESCRIBE tb_user;

-- 验证数据
SELECT * FROM tb_user;
