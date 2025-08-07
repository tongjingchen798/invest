@echo off
echo ========================================
echo 人人权限系统启动脚本
echo ========================================

echo.
echo 1. 检查 MySQL 服务状态...
sc query mysql > nul 2>&1
if %errorlevel% neq 0 (
    echo MySQL 服务未安装或未启动
    echo 请确保 MySQL 服务正在运行
    pause
    exit /b 1
)

echo MySQL 服务状态正常

echo.
echo 2. 检查数据库连接...
echo 请确保已创建数据库: renren_security
echo 请确保已执行初始化脚本: renren-admin/db/mysql.sql
echo.

echo 3. 启动应用...
cd renren-admin
mvn spring-boot:run

pause

