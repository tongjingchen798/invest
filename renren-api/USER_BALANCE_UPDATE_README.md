# 用户余额更新功能说明

## 功能概述

本功能用于在用户投资收益计算完成后，自动更新用户表中的相关余额和收益字段，确保用户账户信息的准确性和实时性。

## 主要特性

- **自动余额更新**: 投资收益计算完成后自动更新用户可用余额
- **收益统计更新**: 同步更新今日收益、历史收益、总收益等统计字段
- **佣金余额管理**: 支持佣金余额和历史佣金余额的更新
- **投资统计更新**: 支持投资金额、项目数、总本金等投资相关字段的更新
- **每日重置**: 自动重置用户的今日收益和投资字段
- **事务安全**: 使用数据库事务确保数据一致性

## 技术实现

### 1. 核心类

**文件路径**: `renren-api/src/main/java/io/renren/dao/UserDao.java`

**主要方法**:
- `addUserBalance()`: 更新用户可用余额
- `addTodayProfit()`: 更新用户今日收益
- `addHistoryProfit()`: 更新用户历史收益
- `addTotalProfit()`: 更新用户总收益
- `addCommissionBalance()`: 更新用户佣金余额
- `updateAllProfitFields()`: 综合更新收益相关字段
- `resetTodayFields()`: 重置今日字段

### 2. 定时任务集成

**文件路径**: `renren-api/src/main/java/io/renren/schedule/UserInvestmentProfitSchedule.java`

**主要方法**:
- `updateUserBalance()`: 更新用户余额和收益字段
- `resetTodayFields()`: 每日重置今日字段
- `getUserBalanceInfo()`: 获取用户余额信息

## 数据库字段说明

### 1. 余额相关字段

| 字段名 | 类型 | 说明 | 单位 |
|--------|------|------|------|
| `assets` | BIGINT | 用户可用余额 | 分 |
| `balance` | BIGINT | 用户余额 | 分 |
| `freeze_balance` | BIGINT | 冻结余额 | 分 |

### 2. 收益相关字段

| 字段名 | 类型 | 说明 | 单位 |
|--------|------|------|------|
| `today_profit` | BIGINT | 今日收益 | 分 |
| `history_profit` | BIGINT | 历史收益 | 分 |
| `total_profit` | BIGINT | 总收益 | 分 |
| `ended_profit` | BIGINT | 已结束收益 | 分 |
| `jr_profit` | BIGINT | 加入收益 | 分 |

### 3. 佣金相关字段

| 字段名 | 类型 | 说明 | 单位 |
|--------|------|------|------|
| `commission_balance` | BIGINT | 佣金余额 | 分 |
| `history_commission_balance` | BIGINT | 历史佣金余额 | 分 |

### 4. 投资相关字段

| 字段名 | 类型 | 说明 | 单位 |
|--------|------|------|------|
| `history_investment` | BIGINT | 历史投资金额 | 分 |
| `today_investment` | BIGINT | 今日投资金额 | 分 |
| `itmes` | BIGINT | 投资项目数 | 个 |
| `total_principal` | BIGINT | 总本金 | 分 |

## 使用方法

### 1. 基本余额更新

```java
// 更新用户可用余额
int result = userDao.addUserBalance(userId, amountInCents);

// 更新用户今日收益
int result = userDao.addTodayProfit(userId, amountInCents);

// 更新用户历史收益
int result = userDao.addHistoryProfit(userId, amountInCents);
```

### 2. 综合字段更新

```java
// 综合更新收益相关字段
int result = userDao.updateAllProfitFields(userId, amountInCents);

// 综合更新佣金相关字段
int result = userDao.updateAllCommissionFields(userId, amountInCents);

// 综合更新投资相关字段
int result = userDao.updateAllInvestmentFields(userId, amountInCents);
```

### 3. 在定时任务中使用

```java
@Scheduled(cron = "0 30 9 * * ?")
public void calculateUserInvestmentProfit() {
    // ... 计算投资收益逻辑
    
    // 记录账变明细
    recordProfitDetail(record, profitAmount);
    
    // 更新用户余额和收益字段（自动调用）
    // updateUserBalance(record.getUserId(), profitAmount);
}
```

## 定时任务配置

### 1. 投资收益计算

```java
@Scheduled(cron = "0 30 9 * * ?")  // 每天9:30执行
public void calculateUserInvestmentProfit()
```

### 2. 今日字段重置

```java
@Scheduled(cron = "0 0 0 * * ?")   // 每天0:00执行
public void resetTodayFields()
```

## 数据单位说明

### 1. 金额单位

- **数据库存储**: 所有金额字段都以"分"为单位存储
- **业务计算**: 使用BigDecimal进行精确计算
- **转换公式**: 1元 = 100分

### 2. 转换示例

```java
// 将BigDecimal金额转换为分
Long amountInCents = profitAmount.multiply(new BigDecimal("100")).longValue();

// 将分转换为元（用于显示）
BigDecimal amountInYuan = new BigDecimal(amountInCents).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
```

## 错误处理

### 1. 更新失败处理

```java
int result = userDao.addUserBalance(userId, amount);
if (result > 0) {
    log.debug("用户 {} 可用余额更新成功，增加: {} 分", userId, amount);
} else {
    log.warn("用户 {} 可用余额更新失败", userId);
}
```

### 2. 异常处理

```java
try {
    updateUserBalance(userId, profitAmount);
} catch (Exception e) {
    log.error("更新用户 {} 余额和收益字段失败", userId, e);
    throw e;
}
```

## 性能优化

### 1. 批量更新

对于大量用户的更新操作，建议使用批量更新：

```java
// 批量更新用户余额（需要扩展实现）
int[] results = userDao.batchUpdateUserBalance(userIds, amounts);
```

### 2. 索引优化

确保以下字段有适当的数据库索引：

```sql
-- 用户表主键索引
CREATE INDEX idx_user_id ON tb_user(id);

-- 余额相关字段索引（可选）
CREATE INDEX idx_user_assets ON tb_user(assets);
CREATE INDEX idx_user_profit ON tb_user(today_profit, history_profit);
```

## 监控和日志

### 1. 日志记录

```java
log.debug("更新用户 {} 余额和收益字段，收益金额: {}", userId, profitAmount);
log.info("用户 {} 余额和收益字段更新完成", userId);
log.warn("用户 {} 可用余额更新失败", userId);
log.error("更新用户 {} 余额和收益字段失败", userId, e);
```

### 2. 监控指标

建议监控以下指标：

- 用户余额更新成功率
- 用户余额更新响应时间
- 每日收益计算完成率
- 用户余额异常变化

## 测试覆盖

### 1. 测试类

**文件路径**: `renren-api/src/test/java/io/renren/schedule/UserInvestmentProfitScheduleBalanceTest.java`

**测试覆盖**:
- 用户余额更新方法测试
- 用户收益更新方法测试
- 用户佣金更新方法测试
- 用户投资更新方法测试
- 今日字段重置测试
- 综合字段更新测试

### 2. 运行测试

```bash
# 运行所有余额更新测试
mvn test -Dtest=UserInvestmentProfitScheduleBalanceTest

# 运行特定测试方法
mvn test -Dtest=UserInvestmentProfitScheduleBalanceTest#testUserBalanceUpdateMethods
```

## 注意事项

### 1. 数据一致性

- 使用数据库事务确保余额更新的一致性
- 在更新余额前验证用户是否存在
- 处理并发更新可能导致的竞态条件

### 2. 性能考虑

- 避免在循环中频繁调用数据库更新方法
- 考虑使用批量更新提高性能
- 监控数据库连接池使用情况

### 3. 安全考虑

- 验证用户权限和身份
- 记录所有余额变更操作
- 防止恶意操作导致的余额异常

## 扩展功能

### 1. 余额冻结

```java
// 冻结用户余额
int result = userDao.freezeUserBalance(userId, amount);

// 解冻用户余额
int result = userDao.unfreezeUserBalance(userId, amount);
```

### 2. 余额流水

```java
// 记录余额变更流水
int result = userBalanceDetailDao.insert(balanceDetail);
```

### 3. 余额预警

```java
// 检查用户余额是否低于预警阈值
if (userBalance < warningThreshold) {
    sendBalanceWarningNotification(userId, userBalance);
}
```

## 故障排查

### 1. 常见问题

**问题**: 用户余额更新失败
**原因**: 用户不存在、数据库连接异常、SQL语法错误
**解决**: 检查用户ID、数据库连接、SQL语句

**问题**: 余额计算不准确
**原因**: 数据类型转换错误、精度丢失、并发更新冲突
**解决**: 使用BigDecimal、添加锁机制、事务控制

### 2. 调试方法

```java
// 获取用户当前余额信息
UserEntity balanceInfo = schedule.getUserBalanceInfo(userId);
System.out.println("用户余额信息: " + balanceInfo);

// 检查数据库更新结果
int result = userDao.addUserBalance(userId, amount);
System.out.println("更新结果: " + result);
```

## 版本历史

- **v1.0.0**: 基础用户余额更新功能
- 支持可用余额、收益、佣金等字段更新
- 集成到投资收益计算定时任务
- 添加每日字段重置功能

## 技术支持

如有问题，请检查：

1. 数据库连接是否正常
2. 用户表结构是否正确
3. SQL语句是否有语法错误
4. 事务配置是否正确
5. 日志中是否有异常信息

---

*本文档最后更新: 2024年1月*
