-- 为sys_menu表添加权限控制字段
-- 代理是否展示(0否 1是)
-- 业务员是否展示(0否 1是)

-- MySQL版本
ALTER TABLE sys_menu ADD COLUMN agent_purview TINYINT DEFAULT 1 COMMENT '代理是否展示(0否 1是)';
ALTER TABLE sys_menu ADD COLUMN ywy_purview TINYINT DEFAULT 1 COMMENT '业务员是否展示(0否 1是)';

-- SQL Server版本
-- ALTER TABLE sys_menu ADD agent_purview INT DEFAULT 1;
-- ALTER TABLE sys_menu ADD ywy_purview INT DEFAULT 1;
-- EXEC sp_addextendedproperty 'MS_Description', '代理是否展示(0否 1是)', 'SCHEMA', 'dbo', 'TABLE', 'sys_menu', 'COLUMN', 'agent_purview';
-- EXEC sp_addextendedproperty 'MS_Description', '业务员是否展示(0否 1是)', 'SCHEMA', 'dbo', 'TABLE', 'sys_menu', 'COLUMN', 'ywy_purview';

-- PostgreSQL版本
-- ALTER TABLE sys_menu ADD COLUMN agent_purview INTEGER DEFAULT 1;
-- ALTER TABLE sys_menu ADD COLUMN ywy_purview INTEGER DEFAULT 1;
-- COMMENT ON COLUMN sys_menu.agent_purview IS '代理是否展示(0否 1是)';
-- COMMENT ON COLUMN sys_menu.ywy_purview IS '业务员是否展示(0否 1是)';

-- DM8版本
-- ALTER TABLE sys_menu ADD agent_purview INT DEFAULT 1;
-- ALTER TABLE sys_menu ADD ywy_purview INT DEFAULT 1;
-- COMMENT ON COLUMN sys_menu.agent_purview IS '代理是否展示(0否 1是)';
-- COMMENT ON COLUMN sys_menu.ywy_purview IS '业务员是否展示(0否 1是)';

-- 更新现有菜单的权限设置
-- 默认所有菜单对代理和业务员都可见
UPDATE sys_menu SET agent_purview = 1, ywy_purview = 1;

-- 示例：设置某些菜单只对代理可见
-- UPDATE sys_menu SET agent_purview = 1, ywy_purview = 0 WHERE name LIKE '%代理%';

-- 示例：设置某些菜单只对业务员可见
-- UPDATE sys_menu SET agent_purview = 0, ywy_purview = 1 WHERE name LIKE '%业务员%';
