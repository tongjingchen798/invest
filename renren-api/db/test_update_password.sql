-- 测试修改密码功能
-- 执行时间: 2024年

-- 1. 查看现有用户表结构
-- DESCRIBE tb_user;

-- 2. 查看现有用户数据
-- SELECT id, mobile, username, password FROM tb_user LIMIT 5;

-- 3. 测试修改密码流程
-- 步骤1: 用户登录获取token
-- 步骤2: 发送短信验证码到用户手机号
-- 步骤3: 调用 /api/updatePws 接口（需要登录状态）

-- 4. 验证密码是否更新成功
-- SELECT id, mobile, username, password FROM tb_user WHERE id = 用户ID;

-- 5. 测试用例
-- 测试数据示例:
-- {
--   "captcha": "1234",
--   "code": "123456",
--   "password": "newpassword123",
--   "password2": "newpassword123",
--   "uuid": "test-uuid-123"
-- }

-- 6. 请求头要求
-- Authorization: Bearer {token}
-- Content-Type: application/json

-- 7. 注意事项
-- - 用户必须已登录（需要有效的token）
-- - 密码会进行SHA256加密存储
-- - 需要实现短信验证码验证逻辑
-- - 建议添加密码强度验证
-- - 建议添加频率限制防止暴力破解
-- - 修改密码后可能需要重新登录
