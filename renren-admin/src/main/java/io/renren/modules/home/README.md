# 首页统计模块 (Home Statistics Module)

## 概述

首页统计模块提供了系统关键数据的统计查询功能，包括充值、提现、项目、用户注册等各项指标的统计信息。

## 功能特性

- **充值统计**: 统计充值金额和订单数量
- **提现统计**: 统计提现金额和订单数量  
- **项目统计**: 统计在线项目数和总项目数
- **用户统计**: 统计注册会员数量
- **销售统计**: 统计销售总额
- **时间范围查询**: 支持按时间范围筛选统计数据

## API 接口

### 获取首页统计数据

**接口地址**: `GET /admin/stats/main`

**请求参数**:
- `startTime` (可选): 开始日期时间戳 (毫秒)
- `endTime` (可选): 结束日期时间戳 (毫秒)

**响应数据**:
```json
{
  "code": 0,
  "data": {
    "chargeOrderAmount": 0,        // 充值金额
    "charge_order_cnt": 0,         // 充值订单数
    "online_privilege_cnt": 0,     // 在线项目数
    "privilege_cnt": 0,            // 购买项目数
    "withdrawOrderAmount": 0,      // 提现金额
    "withdraw_order_cnt": 0,       // 提现订单数
    "xs_amount": 0,                // 销售总额
    "zc_cnt": 0                    // 注册会员数
  },
  "msg": ""
}
```

## 使用示例

### 获取所有统计数据
```bash
GET /admin/stats/main
```

### 获取指定时间范围的统计数据
```bash
GET /admin/stats/main?startTime=1640995200000&endTime=1641081600000
```

## 技术实现

### 架构设计
- **Controller层**: `HomeStatsController` - 处理HTTP请求
- **Service层**: `HomeStatsService` - 业务逻辑处理
- **DAO层**: `HomeStatsDao` - 数据访问接口
- **DTO层**: `MainStatsDTO` - 数据传输对象

### 数据库查询
- 使用MyBatis进行数据库查询
- 支持动态SQL条件查询
- 时间范围查询使用时间戳转换

### 异常处理
- 完善的异常捕获和日志记录
- 异常情况下返回默认值
- 详细的错误信息记录

## 配置说明

### 数据库表
- `tb_charge_order`: 充值订单表
- `tb_withdraw_order`: 提现订单表  
- `tb_project`: 项目表
- `tb_user`: 用户表

### 字段映射
- 金额字段以分为单位存储
- 时间字段使用标准时间格式
- 状态字段使用数字编码

## 测试

运行测试类 `HomeStatsServiceTest` 来验证功能:

```bash
mvn test -Dtest=HomeStatsServiceTest
```

## 注意事项

1. 时间戳参数使用毫秒级精度
2. 金额字段以分为单位，避免浮点数精度问题
3. 统计数据实时查询，未使用缓存
4. 异常情况下会返回默认值(0)，确保接口稳定性

## 扩展功能

未来可以考虑添加:
- 数据缓存机制
- 定时统计任务
- 更多统计维度
- 数据导出功能
- 图表展示接口
