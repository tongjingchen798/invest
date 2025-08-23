@echo off
echo 测试用户标签修改接口
echo.

REM 设置测试参数
set USER_ID=1958922146164248577
set BIAOQIAN=1
set TYPE=1

echo 用户ID: %USER_ID%
echo 标签值: %BIAOQIAN%
echo 操作类型: %TYPE% (1-设置标签, 2-清除标签)
echo.

REM 发送PUT请求到标签修改接口
curl -X PUT "http://localhost:8080/user/updateUserBq?id=%USER_ID%&biaoqian=%BIAOQIAN%&type=%TYPE%" ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE" ^
  -v

echo.
echo 测试完成
pause
