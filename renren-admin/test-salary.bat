@echo off
echo 测试付工资接口
echo.

REM 设置测试参数
set USER_ID=1958922146164248577
set AMOUNT=10000

echo 用户ID: %USER_ID%
echo 工资金额: %AMOUNT% 分 (100元)
echo.

REM 发送PUT请求到付工资接口
curl -X PUT "http://localhost:8080/user/fgz?id=%USER_ID%&balance=%AMOUNT%" ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE" ^
  -v

echo.
echo 测试完成
pause
