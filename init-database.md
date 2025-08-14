zh# 数据库初始化指南

## 问题描述
`CommandLineRunner` 执行失败通常是因为数据库连接问题或数据库表不存在。

## 解决步骤

### 1. 确保 MySQL 服务运行
```bash
# Windows
net start mysql

# Linux/Mac
sudo systemctl start mysql
# 或
sudo service mysql start
```

### 2. 创建数据库
```sql
CREATE DATABASE renren_security CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

### 3. 执行初始化脚本
```bash
# 进入 MySQL 命令行
mysql -u admin -p renren_security

# 或者直接执行 SQL 文件
mysql -u admin -p renren_security < renren-admin/db/mysql_old.sql
```

### 4. 验证数据库连接
检查 `application-dev.yml` 中的数据库配置：
```yaml
spring:
  datasource:
    druid:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306/renren_security?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&nullCatalogMeansCurrent=true
      username: admin
      password: 123456
```

### 5. 检查必要的表是否存在
```sql
-- 检查定时任务表
SHOW TABLES LIKE 'schedule_job';

-- 检查 Quartz 相关表
SHOW TABLES LIKE 'QRTZ_%';

-- 检查系统用户表
SHOW TABLES LIKE 'sys_user';
```

### 6. 如果表不存在，手动执行 SQL
如果自动执行失败，可以手动执行 `renren-admin/db/mysql_old.sql` 文件中的 SQL 语句。

## 常见问题

### 问题1: 数据库连接失败
- 检查 MySQL 服务是否启动
- 检查用户名密码是否正确
- 检查数据库名是否正确
- 检查端口是否正确（默认 3306）

### 问题2: 表不存在
- 确保执行了完整的 `mysql_old.sql` 脚本
- 检查是否有权限创建表
- 检查数据库字符集是否正确

### 问题3: Quartz 表缺失
- 确保执行了 `mysql_old.sql` 中所有以 `QRTZ_` 开头的表创建语句
- 这些表是定时任务功能必需的

## 验证修复
启动应用后，查看日志中是否有以下信息：
```
开始初始化定时任务...
查询到 X 个定时任务
定时任务初始化完成
```

如果看到这些日志，说明定时任务初始化成功。

