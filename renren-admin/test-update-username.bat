@echo off
echo 测试修改用户姓名接口
echo.

REM 设置测试参数
set USER_ID=1958922146164248577
set USERNAME=张三

echo 用户ID: %USER_ID%
echo 新姓名: %USERNAME%
echo.

REM 发送PUT请求到修改用户姓名接口
curl -X PUT "http://localhost:8080/user/updateUserName?id=%USER_ID%&username=%USERNAME%" ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE" ^
  -v

echo.
echo 测试完成
pause
