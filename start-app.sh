#!/bin/bash

echo "========================================"
echo "人人权限系统启动脚本"
echo "========================================"

echo ""
echo "1. 检查 MySQL 服务状态..."

# 检查 MySQL 服务状态
if systemctl is-active --quiet mysql; then
    echo "MySQL 服务正在运行"
elif service mysql status > /dev/null 2>&1; then
    echo "MySQL 服务正在运行"
else
    echo "MySQL 服务未启动，尝试启动..."
    if command -v systemctl > /dev/null 2>&1; then
        sudo systemctl start mysql
    else
        sudo service mysql start
    fi
    
    if [ $? -ne 0 ]; then
        echo "MySQL 服务启动失败，请手动启动"
        exit 1
    fi
fi

echo ""
echo "2. 检查数据库连接..."
echo "请确保已创建数据库: renren_security"
echo "请确保已执行初始化脚本: renren-admin/db/mysql.sql"
echo ""

echo "3. 启动应用..."
cd renren-admin
mvn spring-boot:run

