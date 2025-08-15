@echo off
echo 测试邀请码生成功能...
echo.

cd renren-api

echo 编译项目...
call mvn clean compile -q

if %errorlevel% neq 0 (
    echo 编译失败！
    pause
    exit /b 1
)

echo 运行邀请码生成演示...
call mvn exec:java -Dexec.mainClass="io.renren.utils.InviteCodeDemo" -q

if %errorlevel% neq 0 (
    echo 运行演示失败！
    pause
    exit /b 1
)

echo.
echo 测试完成！
pause
