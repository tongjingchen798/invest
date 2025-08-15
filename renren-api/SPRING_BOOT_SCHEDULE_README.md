# Spring Boot定时任务使用说明

## 功能概述

本功能使用Spring Boot的`@Scheduled`注解实现定时任务，用于每天9点半自动计算用户的投资收益。

## 技术实现

### 1. 核心注解

- **`@EnableScheduling`**: 在主应用类上启用定时任务功能
- **`@Scheduled`**: 在方法上定义定时执行规则
- **`@Component`**: 将定时任务类注册为Spring Bean

### 2. 定时任务类

**文件路径**: `renren-api/src/main/java/io/renren/schedule/UserInvestmentProfitSchedule.java`

**主要方法**:
- `calculateUserInvestmentProfit()`: 每天9点半执行的主任务
- `testSchedule()`: 每分钟执行的测试任务（仅用于开发测试）

### 3. 配置类

**文件路径**: `renren-api/src/main/java/io/renren/config/ScheduleConfig.java`

**功能**: 配置定时任务的线程池和执行策略

## 定时规则配置

### 1. Cron表达式

```java
@Scheduled(cron = "0 30 9 * * ?")
```

**Cron表达式说明**:
```
0 30 9 * * ?
│ │  │ │ │ │
│ │  │ │ │ └─ 星期几（?表示不指定）
│ │  │ │ └─── 月份（*表示每月）
│ │  │ └───── 日期（*表示每天）
│ │  └─────── 小时（9表示9点）
│ └────────── 分钟（30表示30分）
└──────────── 秒（0表示0秒）
```

### 2. 固定频率执行

```java
@Scheduled(fixedRate = 60000) // 每分钟执行一次
```

**其他选项**:
- `fixedRate`: 固定频率执行（毫秒）
- `fixedDelay`: 固定延迟执行（毫秒）
- `initialDelay`: 初始延迟时间（毫秒）

## 部署步骤

### 1. 编译项目
```bash
mvn clean compile
```

### 2. 启动应用
```bash
# 开发环境
mvn spring-boot:run

# 生产环境
java -jar -Dspring.profiles.active=prod renren-api.jar
```

### 3. 验证定时任务
启动后，定时任务将自动开始执行。

## 配置说明

### 1. 线程池配置

```java
@Bean
public ThreadPoolTaskScheduler taskScheduler() {
    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    
    // 线程池大小
    scheduler.setPoolSize(10);
    
    // 线程名前缀
    scheduler.setThreadNamePrefix("scheduled-task-");
    
    // 关闭时等待任务完成
    scheduler.setWaitForTasksToCompleteOnShutdown(true);
    
    // 等待时间
    scheduler.setAwaitTerminationSeconds(60);
    
    return scheduler;
}
```

### 2. 错误处理

```java
scheduler.setErrorHandler(throwable -> {
    // 记录定时任务执行错误
    System.err.println("定时任务执行出错: " + throwable.getMessage());
    throwable.printStackTrace();
});
```

## 监控和日志

### 1. 日志级别

- **INFO**: 任务开始、完成、处理用户数量等关键信息
- **DEBUG**: 详细的处理过程信息
- **ERROR**: 任务执行失败的错误信息

### 2. 日志示例

```
2024-01-01 09:30:00.000 INFO  - 开始执行用户投资收益计算定时任务，执行时间：2024-01-01 09:30:00
2024-01-01 09:30:00.100 INFO  - 找到 150 个有投资的用户，开始计算收益
2024-01-01 09:30:05.500 INFO  - 用户投资收益计算定时任务执行完成，共处理 150 个用户
```

## 扩展和定制

### 1. 修改执行时间

```java
// 修改为每天10点执行
@Scheduled(cron = "0 0 10 * * ?")

// 修改为每小时执行一次
@Scheduled(fixedRate = 3600000)

// 修改为每天凌晨2点执行
@Scheduled(cron = "0 0 2 * * ?")
```

### 2. 添加新的定时任务

```java
@Component
public class NewSchedule {
    
    @Scheduled(cron = "0 0 12 * * ?") // 每天中午12点执行
    public void newTask() {
        // 新任务的逻辑
    }
}
```

### 3. 条件执行

```java
@Scheduled(cron = "0 30 9 * * ?")
@ConditionalOnProperty(name = "app.schedule.enabled", havingValue = "true")
public void conditionalTask() {
    // 只有在配置启用时才执行
}
```

## 注意事项

1. **事务管理**: 使用`@Transactional`注解确保数据一致性
2. **异常处理**: 单个用户处理失败不影响其他用户
3. **线程安全**: 定时任务方法应该是线程安全的
4. **性能考虑**: 避免在定时任务中执行耗时操作
5. **资源管理**: 及时释放数据库连接等资源

## 故障排查

### 1. 定时任务未执行

- 检查`@EnableScheduling`注解是否添加
- 检查定时任务类是否被Spring管理
- 查看启动日志是否有错误

### 2. 定时任务执行失败

- 查看应用日志中的错误信息
- 检查数据库连接是否正常
- 验证相关服务类是否正确注入

### 3. 性能问题

- 调整线程池大小
- 优化数据库查询
- 考虑分批处理大量数据

## 相关文件

- **主应用类**: `ApiApplication.java`
- **定时任务类**: `UserInvestmentProfitSchedule.java`
- **配置类**: `ScheduleConfig.java`
- **测试类**: `UserInvestmentProfitScheduleTest.java`

## 优势对比

### Spring Boot定时任务 vs Quartz

| 特性 | Spring Boot定时任务 | Quartz |
|------|-------------------|---------|
| 配置复杂度 | 简单 | 复杂 |
| 数据库依赖 | 无 | 需要数据库表 |
| 功能丰富度 | 基础 | 高级 |
| 学习成本 | 低 | 高 |
| 适用场景 | 简单定时任务 | 复杂调度需求 |

## 技术支持

如果问题仍然存在，请检查：
1. Spring Boot版本兼容性
2. 应用配置文件
3. 完整的错误堆栈信息
4. 定时任务类的依赖注入
