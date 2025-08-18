# 账变明细查询接口 - DTO结构说明

## 概述

账变明细查询接口已重构，使用新的响应DTO结构，提供更丰富的数据返回和更好的用户体验。

## 新的响应DTO结构

### 1. 主响应结构 (BalanceDetailResponseDTO)

```json
{
  "code": 0,                    // 响应码：0成功，其他失败
  "data": {                     // 响应数据
    "list": [...],              // 账变明细列表
    "sum": {...},               // 汇总信息
    "total": 100                // 总记录数
  },
  "msg": "查询成功"             // 响应消息
}
```

### 2. 账变明细项 (BalanceDetailItem)

每个账变明细项包含以下字段：

#### 基础信息
- `id`: 记录ID
- `userId`: 用户ID
- `agent`: 代理编号
- `agentName`: 代理名称
- `biaoqian`: 用户标签
- `mobile`: 用户账号

#### 交易信息
- `busiType`: 业务类型代码
- `businessTypeDesc`: 业务类型描述（自动转换）
- `channel`: 交易渠道
- `streamId`: 交易流水号
- `status`: 交易状态（0失败，1成功）
- `statusDesc`: 状态描述（自动转换）

#### 金额信息
- `originalAmount`: 原始金额（分）
- `transactionAmount`: 交易后金额（分）
- `useAmount`: 使用金额（分）
- `amountYuan`: 交易后金额（元，自动转换）
- `originalAmountYuan`: 原始金额（元，自动转换）
- `useAmountYuan`: 使用金额（元，自动转换）

#### 时间信息
- `transactionDate`: 交易时间（格式化后的字符串）
- `createDate`: 创建时间
- `updateDate`: 更新时间

#### 其他信息
- `remarks`: 备注
- `formuserid`: 返佣来源用户ID
- `inviteCodeStatus`: 邀请码状态
- `salesmanName`: 业务员姓名
- `salesmanid`: 业务员编号
- `yhqAmount`: 优惠券金额

### 3. 汇总信息 (BalanceDetailSummary)

```json
{
  "totalTransactionAmount": 1000000,  // 总交易金额（分）
  "totalUseAmount": 500000,           // 总使用金额（分）
  "totalCount": 100,                  // 总记录数
  "successCount": 95,                 // 成功交易数
  "failCount": 5                      // 失败交易数
}
```

## 业务类型映射

系统自动将业务类型代码转换为可读描述：

| 代码 | 描述 | 代码 | 描述 |
|------|------|------|------|
| 1 | 购买流水 | 18 | 领取红包 |
| 2 | 余额提现流水 | 19 | 今日福利 |
| 3 | 返佣A | 20 | 出售产品 |
| 5 | 冻结金额 | 21 | 转入投资 |
| 6 | 解冻金额 | 22 | 代理转出 |
| 7 | 手工充值 | 23 | 投资账户转出 |
| 8 | 手工扣款 | 24 | 转给投资账户 |
| 10 | 收益 | 26 | 拼团奖励 |
| 11 | 线上充值 | 27 | 注册奖励 |
| 12 | 工资 | 28 | 任务奖励 |
| 13 | 签到奖励 | 30 | 返佣B |
| 14 | 邀请福利 | 31 | 项目返自己 |
| 15 | 返现 | 32 | 项目返上级 |
| 33 | 佣金提现流水 | | |

## 金额转换

系统自动将"分"转换为"元"：

- 原始金额：10000分 → 100.00元
- 交易金额：5000分 → 50.00元
- 使用金额：2500分 → 25.00元

## 日期格式化

交易时间自动格式化为：`MM/dd/yyyy HH:mm:ss`

例如：`01/15/2024 14:30:25`

## 使用示例

### 1. 基础查询
```http
GET /userbalancedetail/page?page=1&limit=20
```

### 2. 带筛选条件查询
```http
GET /userbalancedetail/page?page=1&limit=20&busiType=1&biaoqian=VIP&startTime=1640908800000
```

### 3. 响应示例
```json
{
  "code": 0,
  "data": {
    "list": [
      {
        "id": 1,
        "userId": 10001,
        "agent": "A001",
        "agentName": "北京代理",
        "biaoqian": "VIP用户",
        "busiType": 1,
        "businessTypeDesc": "购买流水",
        "channel": "支付宝",
        "status": 1,
        "statusDesc": "正常",
        "transactionAmount": 11000,
        "amountYuan": 110.00,
        "transactionDate": "01/15/2024 14:30:25"
      }
    ],
    "sum": {
      "totalTransactionAmount": 11000,
      "totalUseAmount": 1000,
      "totalCount": 1,
      "successCount": 1,
      "failCount": 0
    },
    "total": 1
  },
  "msg": "查询成功"
}
```

## 优势特性

1. **数据完整性**: 包含所有必要的字段信息
2. **自动转换**: 业务类型、状态、金额、日期自动转换
3. **汇总统计**: 提供当前页面的汇总信息
4. **错误处理**: 统一的错误响应格式
5. **类型安全**: 使用强类型DTO，避免运行时错误
6. **扩展性**: 易于添加新的字段和功能

## 注意事项

1. **金额单位**: 数据库存储为"分"，接口返回同时提供"分"和"元"
2. **时间格式**: 交易时间返回格式化字符串，便于前端显示
3. **状态描述**: 自动转换状态码为可读描述
4. **业务类型**: 支持33种不同的业务类型，自动映射描述
5. **汇总计算**: 汇总信息基于当前页面数据，非全局统计

## 后续优化建议

1. 添加全局汇总统计接口
2. 支持导出功能
3. 添加图表数据接口
4. 支持批量操作
5. 添加缓存机制提升性能
