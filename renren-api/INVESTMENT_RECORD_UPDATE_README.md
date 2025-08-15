# 投资记录更新接口说明

## 功能概述

本功能用于更新投资记录的状态和收益信息，支持单个记录更新和批量更新，是投资收益计算定时任务的核心组件。

## 主要特性

- **状态更新**: 将投资记录状态从"未收益"(0)更新为"已收益"(1)
- **收益记录**: 记录投资项目的实际收益金额和收益日期
- **批量操作**: 支持批量更新多个投资记录，提高处理效率
- **统计查询**: 提供多种统计查询方法，支持投资数据分析
- **事务安全**: 使用数据库事务确保数据一致性

## 技术实现

### 1. 核心接口

**文件路径**: `renren-api/src/main/java/io/renren/dao/InvestmentRecordDao.java`

**主要方法**:
- `updateStatusAndProfit()`: 更新单个投资记录状态和收益信息
- `batchUpdateStatusAndProfit()`: 批量更新投资记录状态和收益信息
- `selectCountByStatus()`: 根据状态查询投资记录数量
- `selectByDateRange()`: 查询指定日期范围内的投资记录
- `selectInvestmentStatistics()`: 查询投资记录统计信息

### 2. SQL映射

**文件路径**: `renren-api/src/main/resources/mapper/InvestmentRecordDao.xml`

**主要SQL**:
- 单个更新：使用 `@Update` 注解直接执行
- 批量更新：使用动态SQL和 `foreach` 标签
- 统计查询：使用 `CASE WHEN` 条件统计

## 数据库字段说明

### 1. 核心字段

| 字段名 | 类型 | 说明 | 状态值 |
|--------|------|------|--------|
| `id` | BIGINT | 投资记录主键 | - |
| `user_id` | BIGINT | 用户ID | - |
| `project_id` | BIGINT | 项目ID | - |
| `investment_amount` | BIGINT | 投资金额（分） | - |
| `status` | INT | 投资状态 | 0:未收益, 1:已收益 |
| `profit_amount` | BIGINT | 收益金额（分） | - |
| `profit_date` | DATETIME | 收益日期 | - |
| `order_date` | DATETIME | 投资日期 | - |
| `update_date` | DATETIME | 更新时间 | - |

### 2. 扩展字段

| 字段名 | 类型 | 说明 |
|--------|------|------|
| `cycle` | INT | 投资周期（天） |
| `cycle_type` | INT | 周期类型（1-6） |
| `ddsy` | BIGINT | 待收收益（分） |
| `profit_interest` | BIGINT | 收益利息（分） |
| `profit_principal` | BIGINT | 收益本金（分） |

## 使用方法

### 1. 单个投资记录更新

```java
// 更新投资记录状态和收益信息
Long investmentId = 1L;
BigDecimal profitAmount = new BigDecimal("100.50");
Date profitDate = new Date();

int result = investmentRecordDao.updateStatusAndProfit(
    investmentId, profitAmount, profitDate
);

if (result > 0) {
    log.info("投资记录 {} 状态更新成功", investmentId);
} else {
    log.warn("投资记录 {} 不存在或更新失败", investmentId);
}
```

### 2. 批量投资记录更新

```java
// 批量更新投资记录状态和收益信息
List<Long> investmentIds = Arrays.asList(1L, 2L, 3L);
BigDecimal profitAmount = new BigDecimal("200.00");
Date profitDate = new Date();

int result = investmentRecordDao.batchUpdateStatusAndProfit(
    investmentIds, profitAmount, profitDate
);

log.info("批量更新完成，影响记录数: {}", result);
```

### 3. 在定时任务中使用

```java
@Scheduled(cron = "0 30 9 * * ?")
public void calculateUserInvestmentProfit() {
    // ... 计算投资收益逻辑
    
    // 更新投资记录状态
    updateInvestmentRecordStatus(record.getId(), profitAmount);
    
    // 更新用户余额和收益字段
    updateUserBalance(record.getUserId(), profitAmount);
    
    // 记录账变明细
    recordProfitDetail(record, profitAmount);
}

private void updateInvestmentRecordStatus(Long investmentId, BigDecimal profitAmount) {
    int result = investmentRecordDao.updateStatusAndProfit(
        investmentId, profitAmount, new Date()
    );
    
    if (result > 0) {
        log.debug("投资记录 {} 状态更新完成", investmentId);
    } else {
        log.warn("投资记录 {} 状态更新失败", investmentId);
    }
}
```

## 统计查询功能

### 1. 状态统计

```java
// 查询各状态的投资记录数量
int pendingCount = investmentRecordDao.selectCountByStatus(0);  // 未收益
int completedCount = investmentRecordDao.selectCountByStatus(1); // 已收益

log.info("未收益记录数: {}, 已收益记录数: {}", pendingCount, completedCount);
```

### 2. 用户统计

```java
// 查询用户投资统计信息
Long userId = 1L;
Map<String, Object> statistics = investmentRecordDao.selectInvestmentStatistics(userId);

if (statistics != null) {
    log.info("用户 {} 投资统计:", userId);
    log.info("  总投资记录数: {}", statistics.get("totalCount"));
    log.info("  总投资金额: {} 分", statistics.get("totalInvestment"));
    log.info("  待收投资金额: {} 分", statistics.get("pendingInvestment"));
    log.info("  已完成投资金额: {} 分", statistics.get("completedInvestment"));
    log.info("  总收益金额: {} 分", statistics.get("totalProfit"));
    log.info("  待收利息金额: {} 分", statistics.get("pendingInterest"));
}
```

### 3. 日期范围查询

```java
// 查询指定日期范围内的投资记录
Date startDate = DateUtils.addDays(new Date(), -30); // 30天前
Date endDate = new Date();

List<InvestmentRecordEntity> records = investmentRecordDao.selectByDateRange(startDate, endDate);
log.info("最近30天内的投资记录数: {}", records.size());
```

## 性能优化

### 1. 批量更新

对于大量投资记录的状态更新，建议使用批量更新：

```java
// 分批处理，避免一次性处理过多记录
int batchSize = 1000;
List<List<Long>> batches = Lists.partition(investmentIds, batchSize);

for (List<Long> batch : batches) {
    int result = investmentRecordDao.batchUpdateStatusAndProfit(
        batch, profitAmount, profitDate
    );
    log.debug("批量更新完成，批次大小: {}, 影响记录数: {}", batch.size(), result);
}
```

### 2. 索引优化

确保以下字段有适当的数据库索引：

```sql
-- 主键索引（自动创建）
-- PRIMARY KEY (id)

-- 用户ID索引
CREATE INDEX idx_investment_user_id ON tb_investment_record(user_id);

-- 状态索引
CREATE INDEX idx_investment_status ON tb_investment_record(status);

-- 投资日期索引
CREATE INDEX idx_investment_order_date ON tb_investment_record(order_date);

-- 项目ID索引
CREATE INDEX idx_investment_project_id ON tb_investment_record(project_id);

-- 复合索引
CREATE INDEX idx_investment_user_status ON tb_investment_record(user_id, status);
CREATE INDEX idx_investment_user_date ON tb_investment_record(user_id, order_date);
```

## 错误处理

### 1. 更新失败处理

```java
try {
    int result = investmentRecordDao.updateStatusAndProfit(
        investmentId, profitAmount, profitDate
    );
    
    if (result > 0) {
        log.debug("投资记录 {} 状态更新成功", investmentId);
    } else {
        log.warn("投资记录 {} 状态更新失败", investmentId);
        // 可以记录到失败队列，稍后重试
    }
} catch (Exception e) {
    log.error("更新投资记录 {} 状态时发生异常", investmentId, e);
    // 抛出异常，触发事务回滚
    throw e;
}
```

### 2. 批量更新异常处理

```java
try {
    int result = investmentRecordDao.batchUpdateStatusAndProfit(
        investmentIds, profitAmount, profitDate
    );
    
    if (result == investmentIds.size()) {
        log.info("所有投资记录更新成功，共 {} 条", result);
    } else if (result > 0) {
        log.warn("部分投资记录更新成功，成功: {}, 总数: {}", result, investmentIds.size());
        // 可以记录失败的ID，稍后重试
    } else {
        log.error("所有投资记录更新失败");
    }
} catch (Exception e) {
    log.error("批量更新投资记录状态时发生异常", e);
    throw e;
}
```

## 监控和日志

### 1. 日志记录

```java
log.debug("开始更新投资记录 {} 状态，收益金额: {}", investmentId, profitAmount);
log.info("投资记录 {} 状态更新完成，影响行数: {}", investmentId, result);
log.warn("投资记录 {} 状态更新失败", investmentId);
log.error("更新投资记录 {} 状态时发生异常", investmentId, e);
```

### 2. 监控指标

建议监控以下指标：

- 投资记录状态更新成功率
- 批量更新处理时间
- 数据库更新响应时间
- 投资记录状态分布
- 异常更新记录数量

## 测试覆盖

### 1. 测试类

**文件路径**: `renren-api/src/test/java/io/renren/dao/InvestmentRecordDaoTest.java`

**测试覆盖**:
- 单个投资记录状态更新测试
- 批量投资记录状态更新测试
- 各种统计查询方法测试
- 日期范围查询测试
- 异常情况处理测试

### 2. 运行测试

```bash
# 运行所有投资记录DAO测试
mvn test -Dtest=InvestmentRecordDaoTest

# 运行特定测试方法
mvn test -Dtest=InvestmentRecordDaoTest#testUpdateStatusAndProfit
mvn test -Dtest=InvestmentRecordDaoTest#testBatchUpdateStatusAndProfit
```

## 注意事项

### 1. 数据一致性

- 使用数据库事务确保状态更新的一致性
- 在更新状态前验证投资记录是否存在
- 处理并发更新可能导致的竞态条件

### 2. 性能考虑

- 避免在循环中频繁调用单个更新方法
- 使用批量更新提高大量记录的处理效率
- 监控数据库连接池使用情况

### 3. 业务逻辑

- 确保只有未收益的投资记录才能更新状态
- 验证收益金额的合理性
- 记录所有状态变更操作

## 扩展功能

### 1. 状态回滚

```java
// 回滚投资记录状态（从已收益回到未收益）
@Update("UPDATE tb_investment_record SET " +
        "status = 0, " +
        "profit_amount = NULL, " +
        "profit_date = NULL, " +
        "update_date = #{updateDate} " +
        "WHERE id = #{investmentId}")
int rollbackInvestmentStatus(@Param("investmentId") Long investmentId, 
                            @Param("updateDate") Date updateDate);
```

### 2. 收益调整

```java
// 调整投资记录收益金额
@Update("UPDATE tb_investment_record SET " +
        "profit_amount = #{newProfitAmount}, " +
        "update_date = #{updateDate} " +
        "WHERE id = #{investmentId} AND status = 1")
int adjustProfitAmount(@Param("investmentId") Long investmentId, 
                       @Param("newProfitAmount") BigDecimal newProfitAmount,
                       @Param("updateDate") Date updateDate);
```

### 3. 状态查询增强

```java
// 查询指定状态和日期范围的投资记录
List<InvestmentRecordEntity> selectByStatusAndDateRange(
    @Param("status") Integer status,
    @Param("startDate") Date startDate,
    @Param("endDate") Date endDate
);
```

## 故障排查

### 1. 常见问题

**问题**: 投资记录状态更新失败
**原因**: 投资记录不存在、数据库连接异常、SQL语法错误
**解决**: 检查投资记录ID、数据库连接、SQL语句

**问题**: 批量更新部分成功
**原因**: 部分投资记录ID不存在、数据库约束冲突
**解决**: 验证所有投资记录ID、检查数据库约束

**问题**: 统计查询结果异常
**原因**: 数据库字段类型不匹配、SQL聚合函数错误
**解决**: 检查字段类型、验证SQL语句

### 2. 调试方法

```java
// 检查投资记录是否存在
InvestmentRecordEntity record = investmentRecordDao.selectById(investmentId);
if (record == null) {
    log.error("投资记录 {} 不存在", investmentId);
    return;
}

// 检查当前状态
if (record.getStatus() == 1) {
    log.warn("投资记录 {} 已经是已收益状态", investmentId);
    return;
}

// 执行更新
int result = investmentRecordDao.updateStatusAndProfit(
    investmentId, profitAmount, profitDate
);
log.info("更新结果: {}", result);
```

## 版本历史

- **v1.0.0**: 基础投资记录状态更新功能
- 支持单个和批量状态更新
- 添加多种统计查询方法
- 集成到投资收益计算定时任务

## 技术支持

如有问题，请检查：

1. 数据库连接是否正常
2. 投资记录表结构是否正确
3. SQL语句是否有语法错误
4. 事务配置是否正确
5. 日志中是否有异常信息

---

*本文档最后更新: 2024年1月*
