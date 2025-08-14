# 推荐返利功能说明

## 功能概述

本功能实现了用户注册成功后的推荐人返利机制，根据图片中的规则，为推荐人提供不同层级的返利奖励。

## 返利规则

根据推荐人数，返利金额如下：

| 推荐人数 | 返利金额 | 说明 |
|---------|---------|------|
| 第1个 | 200卢比 (20000分) | 首次推荐奖励 |
| 第2-3个 | 300卢比 (30000分) | 初级推荐奖励 |
| 第4-5个 | 400卢比 (40000分) | 中级推荐奖励 |
| 第6-7个 | 500卢比 (50000分) | 高级推荐奖励 |
| 第8-10个 | 600卢比 (60000分) | 顶级推荐奖励 |
| 超过10个 | 0卢比 | 不再返利 |

## 技术实现

### 1. 核心服务类

- `ReferralRewardService`: 推荐返利服务接口
- `ReferralRewardServiceImpl`: 推荐返利服务实现类
- `BalanceDetailService`: 资金明细服务（扩展）
- `ReferralRewardDetailService`: 推荐返利流水查询服务接口
- `ReferralRewardDetailServiceImpl`: 推荐返利流水查询服务实现类

### 2. 主要方法

#### processReferralReward(Long newUserId, String referrerInviteCode)
- 处理用户注册成功后的推荐返利
- 根据推荐人邀请码查找推荐人
- 计算返利金额并发放

#### calculateReferralReward(Long referralCount)
- 根据推荐人数计算返利金额
- 严格按照图片规则实现

#### grantReferralReward(Long referrerId, Long rewardAmount, Long newUserId)
- 给推荐人发放返利
- 更新用户余额和佣金余额
- 记录资金明细

#### getReferralRewardPageData(Long userId, Integer page, Integer limit)
- 获取用户推荐返利流水分页数据
- 支持分页查询和汇总统计

#### getReferralRewardSummary(Long userId)
- 获取用户推荐返利汇总信息
- 统计总返利金额和成功次数

### 3. 集成点

#### 注册控制器 (ApiRegisterController)
- 在用户注册成功后自动调用推荐返利服务
- 不影响注册流程，异常只记录日志

#### 资金明细记录
- 自动记录推荐返利到资金明细表
- 便于用户查看返利记录

#### 流水查询接口
- 提供推荐返利流水查询API
- 支持分页查询和汇总统计

## 数据库字段

### 用户表 (tb_user)
- `balance`: 用户余额（分）
- `commission_balance`: 佣金余额（分）
- `tgrs`: 推广人数
- `invite_code`: 邀请码

### 资金明细表 (tb_user_balance_detail)
- `user_id`: 用户ID
- `transaction_amount`: 交易金额（分）
- `business_type`: 业务类型（14邀请福利）
- `form_user_id`: 关联用户ID（新注册用户）
- `remarks`: 备注信息
- `stream_id`: 流水ID
- `status`: 状态（0失败，1成功）

## 使用方式

### 1. 自动触发
用户注册时，如果填写了推荐人邀请码，系统会自动处理返利。

### 2. 手动调用
```java
@Autowired
private ReferralRewardService referralRewardService;

// 处理推荐返利
boolean success = referralRewardService.processReferralReward(newUserId, referrerInviteCode);
```

### 3. 查询推荐返利流水
```java
@Autowired
private ReferralRewardDetailService referralRewardDetailService;

// 获取分页数据
ReferralRewardPageData pageData = referralRewardDetailService.getReferralRewardPageData(userId, 1, 10);

// 获取汇总信息
ReferralRewardDetailDTO summary = referralRewardDetailService.getReferralRewardSummary(userId);
```

## 注意事项

1. **金额单位**: 系统内部使用"分"作为单位，1卢比 = 100分
2. **事务处理**: 推荐返利操作使用事务，确保数据一致性
3. **异常处理**: 返利处理异常不影响用户注册流程
4. **日志记录**: 详细记录返利处理过程，便于问题排查
5. **推荐人数限制**: 超过10个推荐用户后不再提供返利

## 测试

运行 `ReferralRewardServiceTest` 类可以验证返利计算逻辑的正确性。

## API接口

### 推荐返利流水查询

#### 1. 获取推荐返利流水列表
```
GET /api/referral/reward/list?userId={userId}&page={page}&limit={limit}
```

**参数说明：**
- `userId`: 用户ID（必填）
- `page`: 页码（可选，默认1）
- `limit`: 每页大小（可选，默认10，最大100）

**返回示例：**
```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "list": [
      {
        "streamId": "REF_1640995200000_123",
        "userId": 123,
        "rewardAmount": 20000,
        "rewardAmountRupee": "200.00",
        "newUserId": 456,
        "newUserMobile": "1234567890",
        "transactionDate": "2024-01-01 12:00:00",
        "remarks": "推荐用户注册成功，获得返利",
        "status": 1,
        "statusDesc": "成功"
      }
    ],
    "sum": {
      "totalRewardAmount": 20000,
      "totalRewardAmountRupee": "200.00",
      "successCount": 1,
      "failedCount": 0,
      "totalCount": 1
    },
    "total": 1,
    "page": 1,
    "limit": 10
  }
}
```

#### 2. 获取推荐返利汇总信息
```
GET /api/referral/reward/summary?userId={userId}
```

**参数说明：**
- `userId`: 用户ID（必填）

**返回示例：**
```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "userId": 123,
    "rewardAmount": 20000,
    "rewardAmountRupee": "200.00"
  }
}
```

## 扩展建议

1. **返利配置化**: 将返利金额配置到数据库或配置文件中
2. **多级推荐**: 支持二级、三级推荐返利
3. **返利条件**: 增加返利发放的条件（如用户完成实名认证等）
4. **返利统计**: 提供推荐返利的统计报表
5. **返利提现**: 支持推荐返利的提现功能
6. **流水导出**: 支持推荐返利流水的Excel导出功能
