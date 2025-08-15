@echo off
echo 测试用户投资收益计算定时任务
echo ================================

echo.
echo 1. 编译项目...
call mvn clean compile -q

if %errorlevel% neq 0 (
    echo 编译失败！
    pause
    exit /b 1
)

echo 编译成功！

echo.
echo 2. 启动应用（测试环境）...
echo 应用将在后台启动，请查看控制台输出...

start "定时任务测试" cmd /k "mvn spring-boot:run -Dspring-boot.run.profiles=test"

echo.
echo 应用已启动，请等待几分钟观察定时任务执行情况...
echo 测试任务每分钟执行一次，主任务每天9点半执行
echo.
echo 按任意键退出...
pause > nul
