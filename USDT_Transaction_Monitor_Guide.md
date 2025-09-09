# USDT转账监控系统使用指南

## 功能概述

实现了USDT转账的自动监控和检测功能，当用户完成USDT转账后，系统会自动检测到转账并更新用户余额。

## 系统架构

### 1. 核心组件

- **USDTTransactionMonitorService**: USDT转账监控服务接口
- **USDTTransactionMonitorServiceImpl**: 监控服务实现类
- **USDTTransactionMonitorTask**: 定时任务，每30秒检查一次转账
- **HttpUtils**: HTTP请求工具类
- **ChargeOrderDao**: 充值订单数据访问层

### 2. 监控流程

```
用户扫码转账 → TRON网络确认 → 定时任务检测 → 创建充值订单 → 更新用户余额
```

## 配置说明

### 1. 系统配置

**USDT地址配置**：
在 `u_address_config` 表中配置USDT-TRC20收款地址：

| 字段 | 值 | 备注 |
|------|-----|------|
| `name` | `USDT-TRC20` | 地址名称 |
| `addr` | `Txxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx` | USDT-TRC20收款地址 |
| `state` | `1` | 状态：1-启用，0-禁用 |

**系统参数配置**：
在 `sys_params` 表中配置以下参数：

| 参数编码 | 参数值 | 备注 |
|---------|--------|------|
| `usdtsysprice` | `97` | USDT系统价格 |
| `usdtrealprice` | `86` | USDT实际价格 |

### 2. 数据库配置

确保 `tb_charge_order` 表包含以下字段：
- `channel_type`: 支付渠道类型
- `state`: 订单状态 (0-待支付, 1-已支付)
- `create_time`: 创建时间
- `remark`: 备注信息

## API接口

### 1. 检查USDT转账

**接口地址：** `POST /api/paychannel/checkUSDTTransaction`

**请求参数：**
- `txHash`: 交易哈希
- `usdtAddress`: USDT地址
- `amount`: 金额

**响应示例：**
```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "success": true,
    "message": "交易验证成功",
    "txHash": "0x1234567890abcdef..."
  }
}
```

### 2. 检查USDT余额

**接口地址：** `GET /api/paychannel/checkUSDTBalance`

**响应示例：**
```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "success": true,
    "balance": "1000.50",
    "address": "Txxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
  }
}
```

## 定时任务

### 1. 转账监控任务

- **执行频率**: 每30秒
- **功能**: 检查待处理的USDT充值订单
- **处理逻辑**: 调用TRON API检查转账状态

### 2. 余额检查任务

- **执行频率**: 每小时
- **功能**: 检查USDT地址余额
- **用途**: 监控收款地址状态

## 前端集成示例

### 1. 用户转账后检查

```javascript
// 用户转账后，前端可以调用检查接口
async function checkTransaction(txHash, amount) {
  try {
    const response = await fetch('/api/paychannel/checkUSDTTransaction', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: `txHash=${txHash}&usdtAddress=${usdtAddress}&amount=${amount}`
    });
    
    const result = await response.json();
    
    if (result.code === 0 && result.data.success) {
      // 转账验证成功
      alert('转账验证成功，余额已更新');
      // 刷新用户余额显示
      refreshUserBalance();
    } else {
      // 转账验证失败
      alert('转账验证失败: ' + result.data.message);
    }
  } catch (error) {
    console.error('检查转账失败:', error);
  }
}
```

### 2. 轮询检查转账状态

```javascript
// 轮询检查转账状态
function pollTransactionStatus(txHash, amount, maxAttempts = 60) {
  let attempts = 0;
  
  const checkInterval = setInterval(async () => {
    attempts++;
    
    if (attempts > maxAttempts) {
      clearInterval(checkInterval);
      alert('转账检查超时，请稍后手动检查');
      return;
    }
    
    try {
      const result = await checkTransaction(txHash, amount);
      if (result.success) {
        clearInterval(checkInterval);
        // 转账成功，停止轮询
      }
    } catch (error) {
      console.error('检查转账失败:', error);
    }
  }, 5000); // 每5秒检查一次
}
```

## 监控机制

### 1. 自动监控

系统通过定时任务自动监控USDT转账：

1. **扫描待处理订单**: 查询状态为0的USDT充值订单
2. **调用TRON API**: 获取地址的USDT交易记录
3. **验证转账**: 检查交易是否匹配订单金额
4. **更新状态**: 创建充值订单并更新用户余额

### 2. 手动检查

用户或管理员可以手动检查特定交易：

1. **提供交易哈希**: 用户输入交易哈希
2. **调用验证接口**: 系统验证交易有效性
3. **返回结果**: 显示验证结果

## 错误处理

### 1. 网络错误

- **API调用失败**: 记录错误日志，继续监控其他订单
- **超时处理**: 设置合理的超时时间
- **重试机制**: 失败后自动重试

### 2. 数据错误

- **交易不存在**: 返回相应错误信息
- **金额不匹配**: 记录日志，不处理该订单
- **地址错误**: 验证地址格式

## 安全考虑

### 1. 交易验证

- **多重验证**: 验证交易哈希、地址、金额
- **防重放**: 避免重复处理同一交易
- **时间窗口**: 只处理最近1小时内的订单

### 2. 数据安全

- **事务处理**: 使用数据库事务确保数据一致性
- **日志记录**: 记录所有监控和验证操作
- **权限控制**: 限制敏感接口的访问权限

## 性能优化

### 1. 监控频率

- **合理间隔**: 30秒检查一次，平衡实时性和性能
- **批量处理**: 一次处理多个订单
- **异步处理**: 使用异步方式处理监控任务

### 2. 缓存机制

- **地址缓存**: 缓存USDT地址信息
- **交易缓存**: 缓存已处理的交易
- **结果缓存**: 缓存API调用结果

## 监控和日志

### 1. 监控指标

- **监控频率**: 每30秒执行一次
- **成功率**: 监控API调用成功率
- **处理时间**: 记录每次处理耗时
- **错误率**: 统计错误发生频率

### 2. 日志记录

- **操作日志**: 记录所有监控操作
- **错误日志**: 记录错误详情
- **性能日志**: 记录性能指标

## 故障排除

### 1. 常见问题

**问题**: 转账检测不到
**解决**: 检查USDT地址配置、网络连接、API调用

**问题**: 重复处理交易
**解决**: 检查防重放机制、数据库约束

**问题**: 性能问题
**解决**: 调整监控频率、优化查询、增加缓存

### 2. 调试方法

1. **查看日志**: 检查应用日志和错误信息
2. **手动测试**: 使用API接口手动测试
3. **数据库检查**: 查看订单状态和余额变化
4. **网络测试**: 测试TRON API连接

## 部署注意事项

1. **服务器时间**: 确保服务器时间准确
2. **网络环境**: 确保能访问TRON API
3. **数据库性能**: 确保数据库性能良好
4. **监控告警**: 设置监控告警机制

通过以上配置和监控机制，系统可以自动检测USDT转账并更新用户余额，提供良好的用户体验。
