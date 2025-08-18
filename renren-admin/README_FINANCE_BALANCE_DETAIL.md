# 财务管理模块 - 账变查询接口

## 功能概述

账变查询接口是财务管理模块的核心功能之一，用于查询用户的账户余额变动明细记录。该接口支持多种筛选条件和分页查询，为财务管理人员提供详细的账务流水信息。

## 接口信息

- **接口地址**: `/admin/userbalancedetail/page`
- **请求方式**: `GET`
- **请求类型**: `application/x-www-form-urlencoded`
- **响应类型**: `*/*`

## 请求参数

| 参数名称 | 参数说明 | 是否必须 | 数据类型 | 示例值 |
|---------|---------|---------|---------|---------|
| page | 当前页码，从1开始 | 是 | integer | 1 |
| limit | 每页显示记录数 | 是 | integer | 20 |
| biaoqian | 标签筛选：传标签 | 否 | string | "VIP" |
| biaoqianFlag | 标签筛选：1有 0无 查全部，不传参 | 否 | integer | 1 |
| busiType | 账务类型 | 否 | integer | 1 |
| endTime | 结束日期:时间戳 | 否 | long | 1640995200000 |
| mobile | 用户账号 | 否 | string | "13800138000" |
| order | 排序方式，可选值(asc、desc) | 否 | string | "desc" |
| orderField | 排序字段 | 否 | string | "transaction_date" |
| startTime | 开始日期:时间戳 | 否 | long | 1640908800000 |

## 响应参数

### 响应结构
```json
{
  "code": 0,
  "data": {
    "list": [...],
    "sum": {},
    "total": 100
  },
  "msg": ""
}
```

### 账变明细字段说明

| 字段名称 | 字段说明 | 数据类型 | 示例值 |
|---------|---------|---------|---------|
| id | 主键ID | integer | 12345 |
| agent | 代理 | string | "AG001" |
| agentName | 代理名称 | string | "北京代理" |
| biaoqian | 标签 | string | "VIP用户" |
| busiType | 交易类型 | integer | 1 |
| channel | 渠道 | string | "支付宝" |
| formuserid | 返佣来源用户ID | integer | 10001 |
| inviteCodeStatus | 邀请码状态 | integer | 1 |
| mobile | 用户账号 | string | "13800138000" |
| originalAmount | 原始金额 | integer | 10000 |
| remarks | 备注 | string | "充值成功" |
| salesmanName | 业务员名称 | string | "张三" |
| salesmanid | 业务员编号 | string | "S001" |
| status | 状态 0：交易失败 1：正常 | integer | 1 |
| streamId | 交易流水id | integer | 98765 |
| transactionAmount | 交易后金额 | integer | 11000 |
| transactionDate | 交易时间 | string | "2024-01-01 10:00:00" |
| useAmount | 使用金额 | integer | 1000 |
| userId | 用户id | integer | 20001 |
| yhqAmount | 优惠券金额 | integer | 500 |

## 业务类型说明

| 业务类型代码 | 业务类型名称 | 说明 |
|-------------|-------------|------|
| 1 | 购买流水 | 用户购买产品或服务 |
| 2 | 提现流水 | 用户提现操作 |
| 3 | 佣金流水 | 代理佣金相关 |
| 4 | 签到流水 | 用户签到奖励 |
| 5 | 冻结金额 | 账户资金冻结 |
| 6 | 解冻金额 | 账户资金解冻 |
| 7 | 手工充值 | 管理员手工充值 |
| 8 | 手工扣款 | 管理员手工扣款 |
| 10 | 收益 | 投资收益 |
| 11 | 线上充值 | 用户线上充值 |
| 12 | 工资 | 员工工资发放 |
| 15 | 返现 | 活动返现 |

## 使用示例

### 1. 基础分页查询
```http
GET /admin/userbalancedetail/page?page=1&limit=20
```

### 2. 按时间范围查询
```http
GET /admin/userbalancedetail/page?page=1&limit=20&startTime=1640908800000&endTime=1640995200000
```

### 3. 按用户账号查询
```http
GET /admin/userbalancedetail/page?page=1&limit=20&mobile=13800138000
```

### 4. 按业务类型查询
```http
GET /admin/userbalancedetail/page?page=1&limit=20&busiType=1
```

### 5. 按标签筛选
```http
GET /admin/userbalancedetail/page?page=1&limit=20&biaoqian=VIP&biaoqianFlag=1
```

### 6. 按时间倒序排列
```http
GET /admin/userbalancedetail/page?page=1&limit=20&order=desc&orderField=transaction_date
```

## 权限要求

- **权限标识**: `finance:userbalancedetail:page`
- **权限说明**: 查询账变明细的权限

## 技术实现

### 架构层次
- **Controller层**: 处理HTTP请求，参数验证，权限控制
- **Service层**: 业务逻辑处理，查询条件构建
- **DAO层**: 数据访问，MyBatis-Plus集成
- **Entity层**: 数据实体映射
- **DTO层**: 数据传输对象

### 核心技术
- **MyBatis-Plus**: 简化数据库操作
- **分页插件**: 支持高效分页查询
- **QueryWrapper**: 动态构建查询条件
- **关联查询**: 多表关联获取完整信息

### 性能优化
- **索引优化**: 关键字段建立数据库索引
- **分页查询**: 避免大数据量查询
- **字段映射**: 精确控制查询字段
- **缓存策略**: 可配置的查询缓存

## 数据库表结构

### 主表: user_balance_detail
```sql
CREATE TABLE `user_balance_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `agent_id` bigint(20) DEFAULT NULL COMMENT '代理ID',
  `agent_name` varchar(100) DEFAULT NULL COMMENT '代理名称',
  `biaoqian` varchar(50) DEFAULT NULL COMMENT '标签',
  `business_type` int(11) NOT NULL COMMENT '业务类型',
  `channel` varchar(50) DEFAULT NULL COMMENT '渠道',
  `form_user_id` bigint(20) DEFAULT NULL COMMENT '返佣来源用户ID',
  `invite_code_status` int(11) DEFAULT NULL COMMENT '邀请码状态',
  `mobile` varchar(20) DEFAULT NULL COMMENT '用户账号',
  `original_amount` bigint(20) DEFAULT NULL COMMENT '原始金额',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注',
  `salesman_name` varchar(100) DEFAULT NULL COMMENT '业务员名称',
  `salesman_id` varchar(50) DEFAULT NULL COMMENT '业务员编号',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态 0：交易失败 1：正常',
  `stream_id` bigint(20) DEFAULT NULL COMMENT '交易流水id',
  `transaction_amount` bigint(20) NOT NULL COMMENT '交易后金额',
  `transaction_date` datetime NOT NULL COMMENT '交易时间',
  `use_amount` bigint(20) DEFAULT NULL COMMENT '使用金额',
  `yhq_amount` bigint(20) DEFAULT NULL COMMENT '优惠券金额',
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_business_type` (`business_type`),
  KEY `idx_transaction_date` (`transaction_date`),
  KEY `idx_mobile` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户余额明细表';
```

### 关联表
- **tb_user**: 用户基础信息
- **tb_agent**: 代理信息
- **tb_salesman**: 业务员信息

## 注意事项

1. **时间格式**: 时间参数使用时间戳格式（毫秒）
2. **金额单位**: 所有金额字段以分为单位
3. **权限控制**: 需要相应的权限才能访问
4. **分页限制**: 每页最大记录数为1000
5. **数据安全**: 敏感信息需要脱敏处理

## 扩展功能

### 可扩展的筛选条件
- 按代理筛选
- 按业务员筛选
- 按金额范围筛选
- 按状态筛选

### 可扩展的统计功能
- 按时间统计交易金额
- 按业务类型统计交易笔数
- 按用户统计交易汇总

### 可扩展的导出功能
- Excel导出
- PDF报表
- 数据备份

## 错误处理

### 常见错误码
- **400**: 参数错误
- **401**: 未授权
- **403**: 权限不足
- **500**: 服务器内部错误

### 错误响应示例
```json
{
  "code": 400,
  "msg": "页码必须大于0",
  "data": null
}
```

## 更新日志

- **v1.0.0**: 初始版本，支持基础分页查询
- **v1.1.0**: 增加标签筛选功能
- **v1.2.0**: 优化关联查询性能
- **v1.3.0**: 增加业务类型筛选

## 联系方式

如有问题或建议，请联系开发团队。
