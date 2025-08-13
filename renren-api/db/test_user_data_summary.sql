-- 测试用户统计信息功能
-- 执行时间: 2024年

-- 1. 查看用户表结构，确认统计字段
-- DESCRIBE tb_user;

-- 2. 查看现有用户数据，了解统计字段的值
-- SELECT 
--     id, 
--     mobile, 
--     username,
--     total_recharge,      -- 总充值金额
--     recharge_count,      -- 充值次数
--     history_profit,      -- 历史收益
--     invite_profit,       -- 邀请收益
--     invite_count,        -- 邀请人数
--     vip_level,           -- VIP等级
--     total_withdraw       -- 总提现金额
-- FROM tb_user 
-- LIMIT 5;

-- 3. 测试用户统计信息接口
-- 接口地址: GET /api/grzx/userDataSummary
-- 需要登录状态（Authorization: Bearer {token}）

-- 4. 预期返回数据格式
-- {
--     "code": 0,
--     "data": {
--         "chargeMoney": 100000,      -- 总充值金额(分)
--         "chargeNum": 5,             -- 充值次数
--         "investmentIncome": 5000,   -- 总投资收益(分)
--         "inviteIncome": 2000,       -- 总邀请收益(分)
--         "inviteNum": 3,             -- 邀请人数
--         "vip": 2,                   -- 等级
--         "withdrawMoney": 30000      -- 总提现金额(分)
--     },
--     "msg": ""
-- }

-- 5. 测试用例
-- 步骤1: 用户登录获取token
-- 步骤2: 调用 /api/grzx/userDataSummary 接口
-- 步骤3: 验证返回的统计信息是否正确

-- 6. 注意事项
-- - 用户必须已登录（需要有效的token）
-- - 统计字段可能为NULL，需要设置默认值
-- - 金额单位是分，需要在前端转换为元
-- - 建议添加缓存机制提高查询性能
-- - 可以考虑添加统计数据的实时更新机制

-- 7. 字段映射关系
-- UserEntity字段 -> UserDataSummaryDTO字段
-- total_recharge -> chargeMoney
-- recharge_count -> chargeNum  
-- history_profit -> investmentIncome
-- invite_profit -> inviteIncome
-- invite_count -> inviteNum
-- vip_level -> vip
-- total_withdraw -> withdrawMoney
