-- Quartz 表状态检查脚本
-- 用于诊断定时任务相关问题

USE renren_security;

-- 1. 检查所有 Quartz 相关表是否存在
SELECT 
    TABLE_NAME,
    TABLE_ROWS,
    CREATE_TIME,
    UPDATE_TIME
FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = 'renren_security' 
AND TABLE_NAME LIKE 'QRTZ_%'
ORDER BY TABLE_NAME;

-- 2. 检查定时任务表
SELECT 
    TABLE_NAME,
    TABLE_ROWS,
    CREATE_TIME,
    UPDATE_TIME
FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = 'renren_security' 
AND TABLE_NAME IN ('schedule_job', 'schedule_job_log')
ORDER BY TABLE_NAME;

-- 3. 检查 QRTZ_CRON_TRIGGERS 表结构
DESCRIBE QRTZ_CRON_TRIGGERS;

-- 4. 检查 QRTZ_CRON_TRIGGERS 表中的数据
SELECT 
    SCHED_NAME,
    TRIGGER_NAME,
    TRIGGER_GROUP,
    CRON_EXPRESSION,
    TIME_ZONE_ID
FROM QRTZ_CRON_TRIGGERS 
WHERE SCHED_NAME = 'RenrenScheduler'
ORDER BY TRIGGER_NAME;

-- 5. 检查 QRTZ_TRIGGERS 表中的数据
SELECT 
    SCHED_NAME,
    TRIGGER_NAME,
    TRIGGER_GROUP,
    JOB_NAME,
    JOB_GROUP,
    TRIGGER_STATE,
    NEXT_FIRE_TIME,
    PREV_FIRE_TIME
FROM QRTZ_TRIGGERS 
WHERE SCHED_NAME = 'RenrenScheduler'
ORDER BY TRIGGER_NAME;

-- 6. 检查 QRTZ_JOB_DETAILS 表中的数据
SELECT 
    SCHED_NAME,
    JOB_NAME,
    JOB_GROUP,
    DESCRIPTION,
    JOB_CLASS_NAME,
    IS_DURABLE,
    IS_NONCONCURRENT,
    IS_UPDATE_DATA,
    REQUESTS_RECOVERY
FROM QRTZ_JOB_DETAILS 
WHERE SCHED_NAME = 'RenrenScheduler'
ORDER BY JOB_NAME;

-- 7. 检查 schedule_job 表中的数据
SELECT 
    id,
    bean_name,
    params,
    cron_expression,
    status,
    remark,
    create_date
FROM schedule_job
ORDER BY create_date;

-- 8. 检查是否有孤立的数据（触发器存在但任务不存在）
SELECT 
    t.TRIGGER_NAME,
    t.TRIGGER_GROUP,
    t.JOB_NAME,
    t.JOB_GROUP
FROM QRTZ_TRIGGERS t
LEFT JOIN QRTZ_JOB_DETAILS j ON t.SCHED_NAME = j.SCHED_NAME 
    AND t.JOB_NAME = j.JOB_NAME 
    AND t.JOB_GROUP = j.JOB_GROUP
WHERE t.SCHED_NAME = 'RenrenScheduler'
AND j.JOB_NAME IS NULL;

-- 9. 检查调度器状态
SELECT 
    SCHED_NAME,
    INSTANCE_NAME,
    LAST_CHECKIN_TIME,
    CHECKIN_INTERVAL
FROM QRTZ_SCHEDULER_STATE
WHERE SCHED_NAME = 'RenrenScheduler';

-- 10. 检查锁状态
SELECT 
    SCHED_NAME,
    LOCK_NAME
FROM QRTZ_LOCKS
WHERE SCHED_NAME = 'RenrenScheduler';

