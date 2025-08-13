-- 测试找回密码功能
-- 执行时间: 2024年

-- 1. 查看现有用户表结构
-- DESCRIBE tb_user;

-- 2. 查看现有用户数据
-- SELECT id, mobile, username, password FROM tb_user LIMIT 5;

-- 3. 测试找回密码流程
-- 步骤1: 发送短信验证码到手机号
-- 步骤2: 用户输入验证码和新密码
-- 步骤3: 调用 /api/retrieve 接口

-- 4. 验证密码是否更新成功
-- SELECT id, mobile, username, password FROM tb_user WHERE mobile = '测试手机号';

-- 5. 测试用例
-- 测试数据示例:
-- {
--   "captcha": "1234",
--   "code": "123456",
--   "mobile": "13800138000",
--   "password": "newpassword123",
--   "password2": "newpassword123",
--   "username": "测试用户",
--   "uuid": "test-uuid-123"
-- }

-- 6. 注意事项
-- - 确保手机号在用户表中存在
-- - 密码会进行SHA256加密存储
-- - 需要实现短信验证码验证逻辑
-- - 建议添加密码强度验证
-- - 建议添加频率限制防止暴力破解
