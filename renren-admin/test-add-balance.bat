@echo off
echo 测试手工调整余额接口
echo.

REM 设置测试参数
set USER_ID=1958922146164248577
set BALANCE=10000
set BALANCE_TYPE=1
set REMARK=测试增加余额

echo 用户ID: %USER_ID%
echo 金额: %BALANCE% 分 (100元)
echo 操作类型: %BALANCE_TYPE% (1-增加余额, 2-减少余额)
echo 备注: %REMARK%
echo.

REM 发送PUT请求到余额调整接口
curl -X PUT "http://localhost:8080/user/addBalance?id=%USER_ID%&balance=%BALANCE%&balance_type=%BALANCE_TYPE%&remark=%REMARK%" ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE" ^
  -v

echo.
echo 测试完成
pause
