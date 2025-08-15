#!/bin/bash

echo "测试邀请码生成功能..."
echo

cd renren-api

echo "编译项目..."
mvn clean compile -q

if [ $? -ne 0 ]; then
    echo "编译失败！"
    exit 1
fi

echo "运行邀请码生成演示..."
mvn exec:java -Dexec.mainClass="io.renren.utils.InviteCodeDemo" -q

if [ $? -ne 0 ]; then
    echo "运行演示失败！"
    exit 1
fi

echo
echo "测试完成！"
