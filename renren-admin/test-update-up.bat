@echo off
echo 测试设置用户上级接口
echo.

REM 设置测试参数
set USER_ID=1958922146164248577
set UP_MOBILE=13800138000

echo 用户ID: %USER_ID%
echo 上级手机号: %UP_MOBILE%
echo.

REM 发送PUT请求到设置用户上级接口
curl -X PUT "http://localhost:8080/user/updateUp?id=%USER_ID%&mobile=%UP_MOBILE%" ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE" ^
  -v

echo.
echo 测试完成
pause
