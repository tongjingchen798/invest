@echo off
chcp 65001 >nul
echo ====================================================
echo 用户余额明细表建表脚本
echo 数据库：renren_security
echo 用途：创建用户余额明细表
echo ====================================================
echo.

REM 检查MySQL是否安装
mysql --version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误：未找到MySQL客户端，请确保MySQL已安装并添加到PATH环境变量
    echo 或者手动执行 user_balance_detail_table.sql 文件
    pause
    exit /b 1
)

echo 正在连接MySQL数据库...
echo 请输入MySQL root用户密码：

REM 执行建表SQL脚本
mysql -u root -p renren_security < user_balance_detail_table.sql

if %errorlevel% equ 0 (
    echo.
    echo ====================================================
    echo 用户余额明细表创建成功！
    echo 现在可以测试账变查询接口了
    echo ====================================================
) else (
    echo.
    echo ====================================================
    echo 创建表失败，请检查：
    echo 1. MySQL服务是否启动
    echo 2. 数据库renren_security是否存在
    echo 3. 用户名密码是否正确
    echo ====================================================
)

echo.
pause
