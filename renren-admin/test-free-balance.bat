@echo off
echo 测试操作冻结金额接口
echo.

REM 设置测试参数
set USER_ID=1958922146164248577
set BALANCE=5000
set BALANCE_TYPE=1
set REMARK=测试冻结金额

echo 用户ID: %USER_ID%
echo 金额: %BALANCE% 分 (50元)
echo 操作类型: %BALANCE_TYPE% (1-冻结金额, 2-解冻金额)
echo 备注: %REMARK%
echo.

REM 发送PUT请求到冻结金额操作接口
curl -X PUT "http://localhost:8080/user/freeBalance?id=%USER_ID%&balance=%BALANCE%&balance_type=%BALANCE_TYPE%&remark=%REMARK%" ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE" ^
  -v

echo.
echo 测试完成
pause
