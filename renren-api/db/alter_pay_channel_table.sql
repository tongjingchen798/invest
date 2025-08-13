-- 支付通道表结构更新SQL (ALTER TABLE方式)
-- 在保留现有数据的情况下更新表结构
-- 执行时间: 2024年

-- 注意：在执行前请先备份数据
-- CREATE TABLE tb_pay_channel_backup AS SELECT * FROM tb_pay_channel;

-- 1. 添加新字段
ALTER TABLE `tb_pay_channel` 
ADD COLUMN `channel_id` varchar(50) NOT NULL COMMENT '渠道ID' AFTER `id`,
ADD COLUMN `merchant_name` varchar(100) DEFAULT NULL COMMENT '商户名称' AFTER `merchantid`;

-- 2. 修改字段类型和约束
ALTER TABLE `tb_pay_channel` 
MODIFY COLUMN `channel_name` varchar(100) DEFAULT NULL COMMENT '渠道名称',
MODIFY COLUMN `channel_type` varchar(50) NOT NULL COMMENT '渠道类型',
MODIFY COLUMN `merchant_id` varchar(50) DEFAULT NULL COMMENT '商户ID',
MODIFY COLUMN `status` int(11) DEFAULT NULL COMMENT '状态 (0:禁用,1:启用)',
MODIFY COLUMN `charge_or_withdraw` varchar(10) DEFAULT NULL COMMENT '充提类型 (1:充值,2:提现,3:充提)';

-- 3. 重命名字段（如果需要）
-- ALTER TABLE `tb_pay_channel` CHANGE `chargeorwithdraw` `charge_or_withdraw` varchar(10) DEFAULT NULL COMMENT '充提类型 (1:充值,2:提现,3:充提)';

-- 4. 删除不需要的字段（如果存在）
-- ALTER TABLE `tb_pay_channel` DROP COLUMN `usdt_gift_ratio`;
-- ALTER TABLE `tb_pay_channel` DROP COLUMN `usdt_local_currency_rate`;

-- 5. 更新现有数据（为新字段设置默认值）
UPDATE `tb_pay_channel` SET 
`channel_id` = CONCAT('CH', LPAD(id, 3, '0')),
`merchant_name` = '默认商户'
WHERE `channel_id` IS NULL OR `merchant_name` IS NULL;

-- 6. 添加索引
CREATE INDEX IF NOT EXISTS `idx_channel_id` ON `tb_pay_channel` (`channel_id`);
CREATE INDEX IF NOT EXISTS `idx_merchant_id` ON `tb_pay_channel` (`merchant_id`);
CREATE INDEX IF NOT EXISTS `idx_charge_or_withdraw` ON `tb_pay_channel` (`charge_or_withdraw`);

-- 7. 创建复合索引
CREATE INDEX IF NOT EXISTS `idx_status_type` ON `tb_pay_channel` (`status`, `channel_type`);
CREATE INDEX IF NOT EXISTS `idx_status_charge` ON `tb_pay_channel` (`status`, `charge_or_withdraw`);

-- 8. 验证表结构
-- DESCRIBE tb_pay_channel;
-- SELECT * FROM tb_pay_channel LIMIT 5;
