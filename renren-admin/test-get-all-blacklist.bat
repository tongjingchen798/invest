@echo off
echo 测试查询所有黑白名单接口
echo.

REM 设置测试参数
set PAGE=1
set LIMIT=10
set TYPE=1

echo 页码: %PAGE%
echo 每页记录数: %LIMIT%
echo 类型: %TYPE% (1-白名单, 2-黑名单, 不传查所有)
echo.

REM 发送GET请求到黑白名单查询接口
curl -X GET "http://localhost:8080/user/getAllBlackList?page=%PAGE%&limit=%LIMIT%&type=%TYPE%" ^
  -H "Content-Type: application/x-www-form-urlencoded" ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE" ^
  -v

echo.
echo 测试完成
pause
