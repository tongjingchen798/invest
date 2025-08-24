# 代理兑付收益统计功能使用说明

## 功能概述

代理兑付收益统计功能用于统计代理和业务员的兑付金额，基于名下用户的充值提现差额计算。系统每天0点自动统计前一天的兑付金额，并支持手动统计和查询。

## 核心特性

- **自动定时统计**：每天0点自动统计前一天的兑付金额
- **多维度统计**：支持按代理、业务员、时间等维度统计
- **实时计算**：基于充值提现差额实时计算兑付金额
- **数据持久化**：统计结果自动保存到数据库
- **手动补录**：支持手动执行统计，用于数据补录
- **完整API**：提供完整的REST API接口
- **MyBatis-Plus集成**：使用MyBatis-Plus进行数据查询和分页

## 数据库表结构

### tb_agent_commission_stats 表

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | bigint | 主键ID |
| agent_id | varchar(100) | 代理ID |
| agent_name | varchar(100) | 代理名称 |
| salesman_id | varchar(100) | 业务员ID |
| salesman_name | varchar(100) | 业务员名称 |
| statistics_date | date | 统计日期 |
| total_charge_amount | bigint | 总充值金额(分) |
| total_withdraw_amount | bigint | 总提现金额(分) |
| net_amount | bigint | 净额(充值-提现)(分) |
| commission_amount | bigint | 兑付金额(分) |
| commission_rate | decimal(5,4) | 兑付比例(默认5%) |
| user_count | int | 名下用户数量 |
| status | tinyint | 状态：1-正常，0-禁用 |
| remark | varchar(500) | 备注 |
| create_time | datetime | 创建时间 |
| update_time | datetime | 更新时间 |

## 计算公式

### 兑付金额计算
```
净额 = 总充值金额 - 总提现金额
兑付金额 = 净额 × 5%（如果净额 > 0，否则为0）
```

### 统计逻辑
1. 统计指定时间范围内的充值订单（状态为审核通过）
2. 统计指定时间范围内的提现订单（状态为审核通过或已提现）
3. 按业务员分组汇总充值和提现金额
4. 计算每个业务员的净额和兑付金额
5. 将统计结果保存到数据库

## 定时任务配置

### 自动统计任务
- **执行时间**：每天0点（00:00:00）
- **Cron表达式**：`0 0 0 * * ?`
- **执行逻辑**：统计前一天的兑付金额

### 手动统计任务
- **触发方式**：通过API手动调用
- **用途**：数据补录、测试验证、历史数据统计

## API接口说明

### 基础查询接口

#### 1. 根据统计日期查询
```
GET /api/agent-commission-stats/by-date?statisticsDate=2024-01-01
```

#### 2. 根据代理ID查询
```
GET /api/agent-commission-stats/by-agent?agentId=AG001&startDate=2024-01-01&endDate=2024-01-31
```

#### 3. 根据业务员ID查询
```
GET /api/agent-commission-stats/by-salesman?salesmanId=SM001&startDate=2024-01-01&endDate=2024-01-31
```

#### 4. 查询日期范围
```
GET /api/agent-commission-stats/by-date-range?startDate=2024-01-01&endDate=2024-01-31
```

### 统计汇总接口

#### 1. 统计指定日期兑付总额
```
GET /api/agent-commission-stats/total-commission-by-date?statisticsDate=2024-01-01
```

#### 2. 统计日期范围兑付总额
```
GET /api/agent-commission-stats/total-commission-by-date-range?startDate=2024-01-01&endDate=2024-01-31
```

#### 3. 获取统计汇总数据
```
GET /api/agent-commission-stats/summary-stats?startDate=2024-01-01&endDate=2024-01-31
```

### 操作接口

#### 1. 手动执行统计
```
POST /api/agent-commission-stats/calculate-manually?targetDate=2024-01-01
```

#### 2. 获取兑付收益报表
```
GET /api/agent-commission-stats/commission-report?startDate=2024-01-01&endDate=2024-01-31&agentId=AG001&salesmanId=SM001
```

#### 3. 分页查询列表
```
GET /api/agent-commission-stats/list?page=1&limit=10&agentId=AG001&startDate=2024-01-01&endDate=2024-01-31
```

## 使用示例

### 1. 查询昨天的统计数据
```bash
curl -X GET "http://localhost:8080/api/agent-commission-stats/by-date?statisticsDate=2024-01-01"
```

### 2. 手动统计指定日期
```bash
curl -X POST "http://localhost:8080/api/agent-commission-stats/calculate-manually?targetDate=2024-01-01"
```

### 3. 获取代理兑付报表
```bash
curl -X GET "http://localhost:8080/api/agent-commission-stats/commission-report?startDate=2024-01-01&endDate=2024-01-31&agentId=AG001"
```

## 配置说明

### 1. 兑付比例配置
当前系统默认兑付比例为5%，如需修改可在以下位置调整：

```java
// AgentCommissionSchedule.java 第 280 行
statsEntity.setCommissionRate(new BigDecimal("0.05")); // 5%
```

### 2. 定时任务配置
如需修改定时任务执行时间，可调整以下配置：

```java
// AgentCommissionSchedule.java 第 58 行
@Scheduled(cron = "0 0 0 * * ?") // 每天0点执行
```

### 3. 数据库配置
确保在 `application.yml` 中配置了正确的数据库连接信息。

## 注意事项

1. **数据一致性**：定时任务使用事务确保数据一致性，失败时会自动回滚
2. **重复统计防护**：系统会检查是否已存在统计数据，避免重复统计
3. **异常处理**：所有接口都包含完善的异常处理机制
4. **日志记录**：详细记录统计过程和结果，便于问题排查
5. **性能优化**：使用批量插入提高数据保存性能

## 故障排查

### 常见问题

1. **定时任务未执行**
   - 检查Spring Boot定时任务是否启用
   - 查看日志确认任务是否正常启动

2. **统计数据为空**
   - 检查充值和提现订单数据是否存在
   - 确认代理业务员映射关系是否正确

3. **API接口调用失败**
   - 检查请求参数格式是否正确
   - 查看服务日志获取详细错误信息

### 日志查看

关键日志位置：
- 定时任务执行日志：`AgentCommissionSchedule.calculateAgentCommission()`
- 数据统计日志：`getChargeStatistics()`, `getWithdrawStatistics()`
- 兑付计算日志：`calculateCommissionStats()`

## 扩展功能

### 1. 自定义兑付比例
可扩展支持不同代理或业务员的个性化兑付比例。

### 2. 多级代理支持
可扩展支持多级代理的层级关系统计。

### 3. 实时统计
可扩展支持实时统计，而非仅每日统计。

### 4. 数据导出
可扩展支持统计数据导出为Excel等格式。

## 联系支持

如有问题或建议，请联系开发团队或查看项目文档。
