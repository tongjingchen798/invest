@echo off
echo 测试删除黑白名单接口
echo.

REM 设置测试参数
set RECORD_ID=1

echo ========================================
echo 测试删除黑白名单接口
echo ========================================
echo 记录ID: %RECORD_ID%
echo.

REM 发送DELETE请求到删除黑白名单接口
curl -X DELETE "http://localhost:8080/user/deleteblack?id=%RECORD_ID%" ^
  -H "Content-Type: application/x-www-form-urlencoded" ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE" ^
  -v

echo.
echo ========================================
echo 测试完成
echo ========================================
pause
