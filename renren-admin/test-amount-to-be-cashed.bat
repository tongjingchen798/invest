@echo off
echo 测试即将兑付金额查询接口
echo.

REM 设置测试参数
set PAGE=1
set LIMIT=10
set AGENT=1
set SALESMANID=1
set START_TIME=1640995200000
set END_TIME=1640995200000
set ORDER=desc
set ORDER_FIELD=create_date

echo ========================================
echo 测试即将兑付金额查询接口
echo ========================================
echo 页码: %PAGE%
echo 每页记录数: %LIMIT%
echo 代理ID: %AGENT%
echo 业务员ID: %SALESMANID%
echo 开始时间: %START_TIME%
echo 结束时间: %END_TIME%
echo 排序: %ORDER%
echo 排序字段: %ORDER_FIELD%
echo.

REM 发送GET请求到即将兑付金额查询接口
curl -X GET "http://localhost:8080/user/amounttobecashed?page=%PAGE%&limit=%LIMIT%&agent=%AGENT%&salesmanid=%SALESMANID%&startTime=%START_TIME%&endTime=%END_TIME%&order=%ORDER%&orderField=%ORDER_FIELD%" ^
  -H "Content-Type: application/x-www-form-urlencoded" ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE" ^
  -v

echo.
echo ========================================
echo 测试完成
echo ========================================
pause
