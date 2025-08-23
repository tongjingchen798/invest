@echo off
echo 测试新增黑白名单接口
echo.

REM 设置测试参数
set USER_ID=123
set MOBILE=13800138000
set TYPE=1

echo ========================================
echo 测试新增黑白名单接口
echo ========================================
echo 用户ID: %USER_ID%
echo 会员账号: %MOBILE%
echo 类型: %TYPE% (1-白名单, 2-黑名单)
echo.

REM 发送POST请求到新增黑白名单接口
curl -X POST "http://localhost:8080/user/addblack" ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE" ^
  -d "{\"userId\":%USER_ID%,\"mobile\":\"%MOBILE%\",\"type\":%TYPE%}" ^
  -v

echo.
echo ========================================
echo 测试完成
echo ========================================
pause
