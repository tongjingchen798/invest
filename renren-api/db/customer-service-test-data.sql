-- 客服号测试数据
-- 插入到sys_user表中，type=2（代理），status=1（正常）

INSERT INTO sys_user (id, username, real_name, status, type, create_date, update_date) VALUES
(1748403763980, 'Doris', 'Doris客服', 1, 2, NOW(), NOW()),
(1748403763981, 'Alice', 'Alice客服', 1, 2, NOW(), NOW()),
(1748403763982, 'Bob', 'Bob客服', 1, 2, NOW(), NOW()),
(1748403763983, 'Charlie', 'Charlie客服', 1, 2, NOW(), NOW()),
(1748403763984, 'David', 'David客服', 1, 2, NOW(), NOW());

-- 查询验证
SELECT id, username, real_name, status, type, create_date 
FROM sys_user 
WHERE type = 2 AND status = 1;
