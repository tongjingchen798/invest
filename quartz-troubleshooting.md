# Quartz 定时任务问题排查指南

## 问题描述
你遇到的 SQL 查询：
```sql
SELECT * FROM QRTZ_CRON_TRIGGERS WHERE SCHED_NAME = 'RenrenScheduler' AND TRIGGER_NAME = ? AND TRIGGER_GROUP = ?
```

这个查询是 Quartz 调度器在查找定时任务触发器时执行的。如果出现错误，通常是因为：

1. **表不存在** - Quartz 相关表未创建
2. **表结构错误** - 表结构与 Quartz 版本不匹配
3. **数据不一致** - 触发器数据与任务数据不匹配
4. **调度器配置问题** - 调度器名称或配置有误

## 解决步骤

### 第一步：检查数据库表状态

执行 `check-quartz-tables.sql` 脚本来诊断问题：

```bash
mysql -u admin -p renren_security < check-quartz-tables.sql
```

### 第二步：确保数据库表存在

如果表不存在，执行完整的初始化脚本：

```bash
mysql -u admin -p renren_security < renren-admin/db/mysql.sql
```

### 第三步：检查应用配置

确认 `ScheduleConfig.java` 中的配置正确：

```java
prop.put("org.quartz.scheduler.instanceName", "RenrenScheduler");
prop.put("org.quartz.jobStore.tablePrefix", "QRTZ_");
```

### 第四步：清理和重置（如果需要）

如果数据不一致，可以执行清理脚本：

```bash
mysql -u admin -p renren_security < reset-quartz-tables.sql
```

**注意：这会清空所有定时任务数据，请谨慎使用**

### 第五步：重启应用

清理后重启应用，让系统重新初始化定时任务。

## 常见问题及解决方案

### 问题1：表不存在
**错误信息**：`Table 'renren_security.QRTZ_CRON_TRIGGERS' doesn't exist`

**解决方案**：
1. 执行完整的 `mysql.sql` 脚本
2. 确保包含所有 `QRTZ_` 开头的表

### 问题2：表结构不匹配
**错误信息**：`Unknown column 'xxx' in 'field list'`

**解决方案**：
1. 检查 Quartz 版本与表结构是否匹配
2. 重新执行表创建脚本

### 问题3：数据不一致
**错误信息**：触发器存在但任务不存在

**解决方案**：
1. 执行清理脚本重置数据
2. 重启应用重新初始化

### 问题4：调度器名称不匹配
**错误信息**：查询不到数据

**解决方案**：
1. 检查 `ScheduleConfig.java` 中的 `scheduler.instanceName`
2. 确保与数据库中的 `SCHED_NAME` 一致

## 验证修复

启动应用后，检查日志中是否有以下信息：

```
开始初始化定时任务...
查询到 X 个定时任务
创建定时任务: xxx
定时任务初始化完成
```

## 手动测试

可以通过以下 SQL 查询手动测试：

```sql
-- 检查调度器配置
SELECT * FROM QRTZ_SCHEDULER_STATE WHERE SCHED_NAME = 'RenrenScheduler';

-- 检查任务详情
SELECT * FROM QRTZ_JOB_DETAILS WHERE SCHED_NAME = 'RenrenScheduler';

-- 检查触发器
SELECT * FROM QRTZ_TRIGGERS WHERE SCHED_NAME = 'RenrenScheduler';

-- 检查 Cron 触发器
SELECT * FROM QRTZ_CRON_TRIGGERS WHERE SCHED_NAME = 'RenrenScheduler';
```

## 预防措施

1. **定期备份**：定期备份数据库，特别是定时任务相关数据
2. **版本管理**：确保 Quartz 版本与表结构匹配
3. **监控日志**：监控应用启动日志，及时发现问题
4. **测试环境**：在测试环境中验证配置变更

## 联系支持

如果问题仍然存在，请提供以下信息：
1. 完整的错误日志
2. 数据库表状态检查结果
3. 应用配置文件
4. Quartz 版本信息

