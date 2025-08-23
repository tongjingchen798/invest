@echo off
echo 测试简化版黑白名单功能
echo.

REM 设置测试参数
set PAGE=1
set LIMIT=10
set TYPE=1

echo ========================================
echo 1. 查询黑白名单
echo ========================================
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
echo ========================================
echo 2. 添加用户到白名单
echo ========================================
set USER_ID=123
set MOBILE=13800138000
set TYPE_ADD=1

echo 用户ID: %USER_ID%
echo 会员账号: %MOBILE%
echo 类型: %TYPE_ADD% (1-白名单, 2-黑名单)
echo.

REM 发送POST请求添加用户到白名单
curl -X POST "http://localhost:8080/user/addblack" ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE" ^
  -d "{\"userId\":%USER_ID%,\"mobile\":\"%MOBILE%\",\"type\":%TYPE_ADD%}" ^
  -v

echo.
echo ========================================
echo 3. 添加用户到黑名单
echo ========================================
set USER_ID_2=456
set MOBILE_2=13800138001
set TYPE_ADD_2=2

echo 用户ID: %USER_ID_2%
echo 会员账号: %MOBILE_2%
echo 类型: %TYPE_ADD_2% (1-白名单, 2-黑名单)
echo.

REM 发送POST请求添加用户到黑名单
curl -X POST "http://localhost:8080/user/addblack" ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE" ^
  -d "{\"userId\":%USER_ID_2%,\"mobile\":\"%MOBILE_2%\",\"type\":%TYPE_ADD_2%}" ^
  -v

echo.
echo ========================================
echo 4. 从黑白名单中移除用户
echo ========================================
set RECORD_ID=1

echo 记录ID: %RECORD_ID%
echo.

REM 发送DELETE请求从黑白名单中移除用户
curl -X DELETE "http://localhost:8080/user/removeFromBlacklist?id=%RECORD_ID%" ^
  -H "Content-Type: application/x-www-form-urlencoded" ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE" ^
  -v

echo.
echo ========================================
echo 测试完成
echo ========================================
pause
