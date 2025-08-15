-- 为投资记录表添加派发状态字段
-- 执行时间：2024-01-01

-- 添加最后派发日期字段
ALTER TABLE tb_investment_record 
ADD COLUMN last_profit_date TIMESTAMP NULL COMMENT '最后派发日期';

-- 添加已派发天数字段
ALTER TABLE tb_investment_record 
ADD COLUMN profit_days INT DEFAULT 0 COMMENT '已派发天数';

-- 添加累计派发金额字段
ALTER TABLE tb_investment_record 
ADD COLUMN total_profit_amount BIGINT DEFAULT 0 COMMENT '累计派发金额(分)';

-- 添加索引以提高查询性能
CREATE INDEX idx_investment_record_profit_date ON tb_investment_record(last_profit_date);
CREATE INDEX idx_investment_record_profit_days ON tb_investment_record(profit_days);

-- 更新现有记录的默认值
UPDATE tb_investment_record 
SET profit_days = 0, total_profit_amount = 0 
WHERE profit_days IS NULL OR total_profit_amount IS NULL;

-- 验证字段添加成功
SELECT 
    COLUMN_NAME, 
    DATA_TYPE, 
    IS_NULLABLE, 
    COLUMN_DEFAULT, 
    COLUMN_COMMENT
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'tb_investment_record' 
AND COLUMN_NAME IN ('last_profit_date', 'profit_days', 'total_profit_amount');
