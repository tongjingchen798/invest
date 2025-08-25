-- 添加客服设置权限菜单
-- 在用户管理下添加"设置客服"按钮权限

-- MySQL版本
INSERT INTO sys_menu (id, pid, name, url, permissions, menu_type, icon, sort, creator, create_date, updater, update_date) 
VALUES (1067246875800000100, 1067246875800000055, '设置客服', NULL, 'sys:user:editws', 1, NULL, 5, 1067246875800000001, NOW(), 1067246875800000001, NOW());

-- SQL Server版本
-- INSERT INTO sys_menu (id, pid, name, url, permissions, menu_type, icon, sort, creator, create_date, updater, update_date) 
-- VALUES (1067246875800000100, 1067246875800000055, '设置客服', NULL, 'sys:user:editws', 1, NULL, 5, 1067246875800000001, GETDATE(), 1067246875800000001, GETDATE());

-- PostgreSQL版本
-- INSERT INTO sys_menu (id, pid, name, url, permissions, menu_type, icon, sort, creator, create_date, updater, update_date) 
-- VALUES (1067246875800000100, 1067246875800000055, '设置客服', NULL, 'sys:user:editws', 1, NULL, 5, 1067246875800000001, NOW(), 1067246875800000001, NOW());

-- DM8版本
-- INSERT INTO sys_menu (id, pid, name, url, permissions, menu_type, icon, sort, creator, create_date, updater, update_date) 
-- VALUES (1067246875800000100, 1067246875800000055, '设置客服', NULL, 'sys:user:editws', 1, NULL, 5, 1067246875800000001, NOW(), 1067246875800000001, NOW());
