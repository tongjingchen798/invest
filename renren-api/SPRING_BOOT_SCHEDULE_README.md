# Spring Boot定时任务使用说明

## 功能概述

本功能使用Spring Boot的`@Scheduled`注解实现定时任务，用于每天9点半自动计算用户的投资收益。

### 主要特性
- **智能收益计算**: 根据投资记录的周期类型、投资天数、投资金额等字段自动计算收益
- **多种周期类型支持**: 支持6种不同的投资周期类型，每种类型使用不同的收益计算算法
- **可配置收益率**: 通过配置文件灵活设置各种投资类型的收益率参数
- **账变记录**: 自动记录投资收益到用户余额明细表，便于用户查看收益流水

## 技术实现

### 1. 核心注解

- **`@EnableScheduling`**: 在主应用类上启用定时任务功能
- **`@Scheduled`**: 在方法上定义定时执行规则
- **`@Component`**: 将定时任务类注册为Spring Bean

### 2. 数据库查询

- **投资记录表**: `tb_investment_record` - 存储用户投资信息
- **收益明细表**: `tb_investment_profit_detail` - 存储收益明细
- **用户表**: `tb_user` - 存储用户信息和余额

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

## 投资收益计算逻辑

### 1. 支持的周期类型

本系统支持6种不同的投资周期类型，每种类型使用不同的收益计算算法：

#### 1.1 到期收益含本金 (cycleType = 1)
- **计算方式**: 年化收益率 × 投资天数 ÷ 365 × 投资金额
- **适用场景**: 传统定期投资，到期一次性还本付息
- **配置参数**: `investment.profit.maturity-annual-rate`

#### 1.2 每日返本金到期收益 (cycleType = 2)
- **计算方式**: 年化收益率 ÷ 365 × 投资天数 × 投资金额
- **适用场景**: 每日返还本金，到期支付收益
- **配置参数**: `investment.profit.daily-return-annual-rate`

#### 1.3 不返本金 (cycleType = 3)
- **计算方式**: 年化收益率 × 投资天数 ÷ 365 × 投资金额
- **适用场景**: 只计算收益，不返还本金
- **配置参数**: `investment.profit.no-principal-annual-rate`

#### 1.4 复利产品 (cycleType = 4)
- **计算方式**: 投资金额 × (1 + 日收益率)^投资天数 - 投资金额
- **适用场景**: 收益再投资，利滚利
- **配置参数**: `investment.profit.compound-daily-rate`

#### 1.5 阶梯日益 (cycleType = 5)
- **计算方式**: 根据投资天数分段计算，不同阶段使用不同日收益率
- **适用场景**: 投资时间越长，收益率越高
- **配置参数**: 
  - `investment.profit.stepped-daily-rate.first-week-rate` (前7天)
  - `investment.profit.stepped-daily-rate.second-week-rate` (8-15天)
  - `investment.profit.stepped-daily-rate.later-rate` (16天以后)

#### 1.6 拼团 (cycleType = 6)
- **计算方式**: 基础收益 + 拼团奖励
- **适用场景**: 多人拼团投资，享受额外奖励
- **配置参数**:
  - `investment.profit.group-buy-rate.base-annual-rate` (基础年化收益率)
  - `investment.profit.group-buy-rate.group-bonus-rate` (拼团奖励比例)

### 2. 收益计算核心逻辑

```java
// 1. 验证投资记录基本信息
if (record.getInvestmentAmount() == null || record.getInvestmentAmount() <= 0) {
    return BigDecimal.ZERO;
}

// 2. 计算投资天数
int investmentDays = calculateInvestmentDays(record.getOrderDate());

// 3. 根据周期类型选择计算算法
switch (record.getCycleType()) {
    case 1: // 到期收益含本金
        profitAmount = calculateMaturityProfit(investmentAmount, cycle, investmentDays);
        break;
    case 2: // 每日返本金到期收益
        profitAmount = calculateDailyReturnProfit(investmentAmount, cycle, investmentDays);
        break;
    // ... 其他类型
}

// 4. 记录账变流水
recordProfitDetail(record, profitAmount);
```

### 3. 配置参数说明

所有收益率参数都可以在 `application.yml` 中配置：

```yaml
investment:
  profit:
    maturity-annual-rate: 0.12        # 12%年化收益率
    daily-return-annual-rate: 0.08    # 8%年化收益率
    no-principal-annual-rate: 0.15    # 15%年化收益率
    compound-daily-rate: 0.0003       # 0.03%日收益率
    stepped-daily-rate:
      first-week-rate: 0.001          # 0.1%日收益率
      second-week-rate: 0.0015        # 0.15%日收益率
      later-rate: 0.002               # 0.2%日收益率
    group-buy-rate:
      base-annual-rate: 0.10          # 10%年化收益率
      group-bonus-rate: 0.02          # 2%拼团奖励
```

## 数据库查询说明

### 1. 主要查询方法

- **`selectDistinctUserIdsWithInvestment()`**: 查询所有有投资记录的用户ID（去重）
- **`selectByUserId()`**: 根据用户ID查询投资记录列表
- **`selectTotalInvestmentByUserId()`**: 查询用户总投资金额
- **`selectPendingInterestByUserId()`**: 查询用户待收利息金额

### 2. 查询逻辑

```sql
-- 查询有投资的用户ID（去重）
SELECT DISTINCT user_id 
FROM tb_investment_record 
WHERE investment_amount > 0
ORDER BY user_id

-- 查询用户投资记录
SELECT * FROM tb_investment_record 
WHERE user_id = #{userId}
ORDER BY order_date DESC
```

### 3. 数据表结构

- **`tb_investment_record`**: 投资记录表，包含投资金额、状态、周期等信息
- **`tb_investment_profit_detail`**: 收益明细表，记录每次收益的详细信息
- **`tb_user`**: 用户表，包含用户余额、投资统计等信息

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
- **投资收益配置类**: `InvestmentProfitConfig.java`
- **测试类**: 
  - `UserInvestmentProfitScheduleTest.java` - 基本功能测试
  - `UserInvestmentProfitScheduleDatabaseTest.java` - 数据库查询测试
  - `InvestmentProfitCalculationTest.java` - 收益计算逻辑测试

## 测试说明

### 1. 运行测试
```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=InvestmentProfitCalculationTest

# 运行特定测试方法
mvn test -Dtest=InvestmentProfitCalculationTest#testCalculateProfitByCycleType
```

### 2. 测试覆盖范围
- **基本功能测试**: 验证定时任务的基本启动和执行
- **数据库查询测试**: 验证数据库查询方法的正确性
- **收益计算逻辑测试**: 验证各种周期类型的收益计算算法
- **边界条件测试**: 验证无效数据的处理
- **参数配置测试**: 验证不同投资金额和周期的收益计算

### 3. 测试数据示例
```java
// 测试投资记录
InvestmentRecordEntity testRecord = new InvestmentRecordEntity();
testRecord.setInvestmentAmount(100000L); // 1000元（分）
testRecord.setCycle(30); // 30天
testRecord.setCycleType(1); // 到期收益含本金

// 预期收益计算
// 年化收益率12%，投资30天
// 收益 = 1000 × 12% × 30 ÷ 365 ≈ 9.86元
```

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
