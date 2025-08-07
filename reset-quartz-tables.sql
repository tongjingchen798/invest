-- Quartz 表清理和重置脚本
-- 用于解决定时任务相关问题

USE renren_security;

-- 警告：此脚本会清空所有 Quartz 相关数据，请谨慎使用
-- 建议在执行前备份数据库

-- 1. 清空所有 Quartz 相关表
DELETE FROM QRTZ_FIRED_TRIGGERS WHERE SCHED_NAME = 'RenrenScheduler';
DELETE FROM QRTZ_SIMPROP_TRIGGERS WHERE SCHED_NAME = 'RenrenScheduler';
DELETE FROM QRTZ_SIMPLE_TRIGGERS WHERE SCHED_NAME = 'RenrenScheduler';
DELETE FROM QRTZ_CRON_TRIGGERS WHERE SCHED_NAME = 'RenrenScheduler';
DELETE FROM QRTZ_BLOB_TRIGGERS WHERE SCHED_NAME = 'RenrenScheduler';
DELETE FROM QRTZ_TRIGGERS WHERE SCHED_NAME = 'RenrenScheduler';
DELETE FROM QRTZ_JOB_DETAILS WHERE SCHED_NAME = 'RenrenScheduler';
DELETE FROM QRTZ_CALENDARS WHERE SCHED_NAME = 'RenrenScheduler';
DELETE FROM QRTZ_PAUSED_TRIGGER_GRPS WHERE SCHED_NAME = 'RenrenScheduler';
DELETE FROM QRTZ_LOCKS WHERE SCHED_NAME = 'RenrenScheduler';
DELETE FROM QRTZ_SCHEDULER_STATE WHERE SCHED_NAME = 'RenrenScheduler';

-- 2. 重置自增ID（如果需要）
-- 注意：MySQL 中 Quartz 表通常不使用自增ID，但如果有其他表需要重置，可以在这里添加

-- 3. 验证清理结果
SELECT 'QRTZ_FIRED_TRIGGERS' as table_name, COUNT(*) as count FROM QRTZ_FIRED_TRIGGERS WHERE SCHED_NAME = 'RenrenScheduler'
UNION ALL
SELECT 'QRTZ_TRIGGERS' as table_name, COUNT(*) as count FROM QRTZ_TRIGGERS WHERE SCHED_NAME = 'RenrenScheduler'
UNION ALL
SELECT 'QRTZ_JOB_DETAILS' as table_name, COUNT(*) as count FROM QRTZ_JOB_DETAILS WHERE SCHED_NAME = 'RenrenScheduler'
UNION ALL
SELECT 'QRTZ_CRON_TRIGGERS' as table_name, COUNT(*) as count FROM QRTZ_CRON_TRIGGERS WHERE SCHED_NAME = 'RenrenScheduler';

-- 4. 检查清理后的状态
SELECT '清理完成，所有 Quartz 表已重置' as status;

