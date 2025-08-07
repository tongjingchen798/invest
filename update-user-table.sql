-- 用户表结构更新脚本
-- 为 tb_user 表添加新字段

USE renren_security;

-- 检查表是否存在
SELECT COUNT(*) as table_exists FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = 'renren_security' AND TABLE_NAME = 'tb_user';

-- 添加新字段（如果不存在）
ALTER TABLE tb_user 
ADD COLUMN IF NOT EXISTS two_pwd VARCHAR(64) COMMENT '二级密码' AFTER password,
ADD COLUMN IF NOT EXISTS invite_code VARCHAR(50) COMMENT '邀请码' AFTER two_pwd,
ADD COLUMN IF NOT EXISTS agent VARCHAR(200) COMMENT '代理信息' AFTER invite_code,
ADD COLUMN IF NOT EXISTS channel VARCHAR(100) COMMENT '客户渠道号' AFTER agent,
ADD COLUMN IF NOT EXISTS equipment INT COMMENT '登录端口(1:安卓, 2:ios, 3:pc, 4:未知)' AFTER channel;

-- 验证字段是否添加成功
DESCRIBE tb_user;

-- 显示更新后的表结构
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    COLUMN_DEFAULT,
    COLUMN_COMMENT
FROM information_schema.COLUMNS 
WHERE TABLE_SCHEMA = 'renren_security' 
AND TABLE_NAME = 'tb_user'
ORDER BY ORDINAL_POSITION;
