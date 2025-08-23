@echo off
echo 测试修改用户业务员接口
echo.

REM 设置测试参数
set USER_ID=1958922146164248577
set SALESMAN_ID=12345
set SALESMAN_NAME=张三

echo 用户ID: %USER_ID%
echo 业务员ID: %SALESMAN_ID%
echo 业务员姓名: %SALESMAN_NAME%
echo.

REM 发送PUT请求到修改业务员接口
curl -X PUT "http://localhost:8080/user/updateUserToAgent?id=%USER_ID%&salesmanid=%SALESMAN_ID%&salesmanName=%SALESMAN_NAME%" ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE" ^
  -v

echo.
echo 测试完成
pause
