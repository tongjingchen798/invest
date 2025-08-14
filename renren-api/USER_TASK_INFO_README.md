# 用户任务信息功能说明

## 功能概述

本功能提供了用户任务完成情况的查询接口，返回用户在各个任务上的完成数量。

## 接口信息

### 接口地址
```
GET /api/userTaskInfo
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 否 | 用户ID，如果不提供则获取当前登录用户信息 |

### 返回格式
```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "completeNum1": 0,
    "completeNum2": 0,
    "completeNum3": 0,
    "completeNum4": 0,
    "completeNum5": 0
  }
}
```

### 返回字段说明
| 字段名 | 类型 | 说明 |
|--------|------|------|
| completeNum1 | Integer | 任务1完成数量 |
| completeNum2 | Integer | 任务2完成数量 |
| completeNum3 | Integer | 任务3完成数量 |
| completeNum4 | Integer | 任务4完成数量 |
| completeNum5 | Integer | 任务5完成数量 |

## 技术实现

### 1. 核心服务类

- `UserTaskInfoService`: 用户任务信息服务接口
- `UserTaskInfoServiceImpl`: 用户任务信息服务实现类

### 2. 主要方法

#### getUserTaskInfo(Long userId)
- 根据用户ID获取任务完成信息
- 支持空值处理，返回默认值

#### getCurrentUserTaskInfo()
- 获取当前登录用户的任务完成信息
- 需要集成用户认证系统

### 3. 任务计算逻辑

**重要说明：所有任务都根据用户总推荐人数来判断，而不是根据投资、充值、收益等数据**

#### 任务1：推荐人数任务（最大1）
- 条件：推荐人数 > 0
- 完成数量：显示推荐人数，但最大不超过1
- 计算规则：Math.min(推荐人数, 1)

#### 任务2：推荐人数额外任务（最大2）
- 条件：剩余推荐人数 > 0（推荐人数 - 任务1显示数量）
- 完成数量：显示剩余推荐人数，但最大不超过2
- 计算规则：Math.min(剩余推荐人数, 2)

#### 任务3：投资金额任务（最大2）
- 条件：剩余推荐人数 > 0（推荐人数 - 任务1显示数量 - 任务2显示数量）
- 完成数量：显示剩余推荐人数，但最大不超过2
- 计算规则：Math.min(剩余推荐人数, 2)

#### 任务4：充值任务（最大2）
- 条件：剩余推荐人数 > 0（推荐人数 - 任务1显示数量 - 任务2显示数量 - 任务3显示数量）
- 完成数量：显示剩余推荐人数，但最大不超过2
- 计算规则：Math.min(剩余推荐人数, 2)

#### 任务5：收益任务（最大5）
- 条件：剩余推荐人数 > 0（推荐人数 - 任务1显示数量 - 任务2显示数量 - 任务3显示数量 - 任务4显示数量）
- 完成数量：显示剩余推荐人数，但最大不超过5
- 计算规则：Math.min(剩余推荐人数, 5)

## 使用方式

### 1. 获取指定用户任务信息
```bash
GET /api/userTaskInfo?userId=123
```

### 2. 获取当前用户任务信息
```bash
GET /api/userTaskInfo
```

### 3. 在代码中调用
```java
@Autowired
private UserTaskInfoService userTaskInfoService;

// 获取指定用户任务信息
UserTaskInfoDTO taskInfo = userTaskInfoService.getUserTaskInfo(123L);

// 获取当前用户任务信息
UserTaskInfoDTO currentTaskInfo = userTaskInfoService.getCurrentUserTaskInfo();
```

## 数据库字段

### 用户表 (tb_user)
- `tgrs`: 推广人数
- `history_investment`: 历史投资金额（分）
- `charge_sum`: 累计充值金额（分）
- `history_profit`: 历史收益金额（分）

## 注意事项

1. **任务条件**: 任务完成条件可以根据实际业务需求调整
2. **用户认证**: 获取当前用户信息需要集成用户认证系统
3. **异常处理**: 接口异常时返回默认值，不影响系统稳定性
4. **性能优化**: 可以根据需要添加缓存机制
5. **扩展性**: 任务类型和数量可以灵活配置

## 测试

运行 `UserTaskInfoServiceTest` 类可以验证任务信息获取逻辑的正确性。

## 扩展建议

1. **任务配置化**: 将任务条件配置到数据库或配置文件中
2. **任务类型扩展**: 支持更多类型的任务
3. **任务进度**: 显示任务完成进度百分比
4. **任务奖励**: 集成任务完成后的奖励机制
5. **任务统计**: 提供任务完成情况的统计报表
6. **实时更新**: 支持任务状态的实时更新
