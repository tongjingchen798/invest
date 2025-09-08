-- 更新支付商户表，添加merchant_code字段并设置默认值
-- 执行前请先备份数据库

-- 1. 添加merchant_code字段（如果不存在）
ALTER TABLE tb_pay_merchant ADD COLUMN merchant_code VARCHAR(50) COMMENT '商户代付渠道代码';

-- 2. 为现有商户设置默认的merchant_code值
-- 根据商户名称或其他字段设置对应的代付渠道

-- 示例：为WePay商户设置merchant_code
UPDATE tb_pay_merchant 
SET merchant_code = 'WEPAY_001' 
WHERE merchant_name LIKE '%wepay%' OR merchant_name LIKE '%WePay%';

-- 示例：为支付宝商户设置merchant_code
UPDATE tb_pay_merchant 
SET merchant_code = 'ALIPAY_001' 
WHERE merchant_name LIKE '%alipay%' OR merchant_name LIKE '%支付宝%';

-- 示例：为Stripe商户设置merchant_code
UPDATE tb_pay_merchant 
SET merchant_code = 'STRIPE_001' 
WHERE merchant_name LIKE '%stripe%' OR merchant_name LIKE '%Stripe%';

-- 示例：为PayPal商户设置merchant_code
UPDATE tb_pay_merchant 
SET merchant_code = 'PAYPAL_001' 
WHERE merchant_name LIKE '%paypal%' OR merchant_name LIKE '%PayPal%';

-- 3. 为没有匹配到的商户设置默认值
UPDATE tb_pay_merchant 
SET merchant_code = 'WEPAY_DEFAULT' 
WHERE merchant_code IS NULL OR merchant_code = '';

-- 4. 添加索引以提高查询性能
CREATE INDEX idx_merchant_code ON tb_pay_merchant(merchant_code);

-- 5. 验证更新结果
SELECT id, merchant_name, merchant_code, merchantno 
FROM tb_pay_merchant 
ORDER BY id;
