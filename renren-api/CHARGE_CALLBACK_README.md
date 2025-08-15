# 充值回调接口说明文档

## 概述

充值回调接口用于处理第三方支付平台的回调通知，支持多种支付方式，包括银行卡、虚拟币、UPI、Paytm等。当用户完成支付后，第三方支付平台会向我们的回调接口发送支付结果通知。

## 接口列表

### 1. 银行卡充值回调

**接口地址：** `POST /api/charge/callback/bank`

**请求参数：**
- `thirdOrderNo`: 第三方订单号
- `orderNo`: 平台订单号
- `amount`: 支付金额
- `status`: 支付状态（SUCCESS/FAILED）
- `payTime`: 支付时间
- `sign`: 签名

**响应：**
- 成功：`SUCCESS`
- 失败：`FAIL`
- 异常：`ERROR`

**示例：**
```bash
curl -X POST "http://localhost:8080/api/charge/callback/bank" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "thirdOrderNo=BANK987654321&orderNo=CHG123456789&amount=100.00&status=SUCCESS&sign=valid_sign_123"
```

### 2. 虚拟币充值回调

**接口地址：** `POST /api/charge/callback/crypto`

**请求参数：**
- `thirdOrderNo`: 第三方订单号
- `orderNo`: 平台订单号
- `uAmount`: USDT数量
- `status`: 支付状态
- `txHash`: 交易哈希
- `walletAddr`: 钱包地址
- `sign`: 签名

**响应：** 同银行卡回调

### 3. UPI充值回调

**接口地址：** `POST /api/charge/callback/upi`

**请求参数：**
- `thirdOrderNo`: 第三方订单号
- `orderNo`: 平台订单号
- `amount`: 支付金额
- `status`: 支付状态
- `upiId`: UPI ID
- `sign`: 签名

**响应：** 同银行卡回调

### 4. Paytm充值回调

**接口地址：** `POST /api/charge/callback/paytm`

**请求参数：**
- `thirdOrderNo`: 第三方订单号
- `orderNo`: 平台订单号
- `amount`: 支付金额
- `status`: 支付状态
- `paytmOrderId`: Paytm订单ID
- `sign`: 签名

**响应：** 同银行卡回调

### 5. 通用充值回调

**接口地址：** `POST /api/charge/callback/common`

**请求参数：**
- `paymentMethod`: 支付方式
- `thirdOrderNo`: 第三方订单号
- `orderNo`: 平台订单号
- `amount`: 支付金额
- `status`: 支付状态
- `sign`: 签名

**响应：** 同银行卡回调

### 6. 查询订单状态

**接口地址：** `GET /api/charge/callback/query`

**请求参数：**
- `orderNo`: 平台订单号（必填）

**响应：**
```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "orderNo": "CHG123456789",
    "thirdOrderNo": "BANK987654321",
    "userId": 123,
    "amount": 10000,
    "state": 1,
    "stateText": "审核通过",
    "status": "SUCCESS",
    "message": "充值成功"
  }
}
```

### 7. 健康检查

**接口地址：** `GET /api/charge/callback/health`

**响应：** `OK`

## 签名验证

所有回调接口都支持签名验证，确保数据的安全性。

### 签名算法

1. 将所有参数按key排序（排除sign参数）
2. 拼接成 `key1=value1&key2=value2&...&key=secretKey` 格式
3. 使用MD5算法计算签名

### 签名示例

```java
// 参数
Map<String, String> params = new HashMap<>();
params.put("orderNo", "CHG123456789");
params.put("amount", "100.00");
params.put("status", "SUCCESS");
params.put("key", "your_secret_key");

// 构建签名字符串
StringBuilder signString = new StringBuilder();
params.entrySet().stream()
    .filter(entry -> !"sign".equals(entry.getKey()))
    .sorted(Map.Entry.comparingByKey())
    .forEach(entry -> signString.append(entry.getKey()).append("=").append(entry.getValue()).append("&"));
signString.append("key=").append("your_secret_key");

// 计算MD5签名
String sign = DigestUtils.md5DigestAsHex(signString.toString().getBytes(StandardCharsets.UTF_8));
```

## 配置说明

### 支付配置

在 `application.yml` 中配置各种支付方式的参数：

```yaml
payment:
  bank:
    merchant-id: "your_bank_merchant_id"
    secret-key: "your_bank_secret_key"
    gateway-url: "https://bank.gateway.com"
    callback-url: "http://your.domain/api/charge/callback/bank"
    query-url: "https://bank.gateway.com/query"
  
  crypto:
    merchant-id: "your_crypto_merchant_id"
    secret-key: "your_crypto_secret_key"
    gateway-url: "https://crypto.gateway.com"
    callback-url: "http://your.domain/api/charge/callback/crypto"
    query-url: "https://crypto.gateway.com/query"
    supported-coins: ["USDT", "BTC", "ETH"]
    networks: ["TRC20", "ERC20", "BEP20"]
  
  upi:
    merchant-id: "your_upi_merchant_id"
    secret-key: "your_upi_secret_key"
    gateway-url: "https://upi.gateway.com"
    callback-url: "http://your.domain/api/charge/callback/upi"
    query-url: "https://upi.gateway.com/query"
    app-name: "YourApp"
    merchant-name: "YourCompany"
  
  paytm:
    merchant-id: "your_paytm_merchant_id"
    secret-key: "your_paytm_secret_key"
    gateway-url: "https://paytm.gateway.com"
    callback-url: "http://your.domain/api/charge/callback/paytm"
    query-url: "https://paytm.gateway.com/query"
    industry-type: "Retail"
    website-name: "YourWebsite"
  
  common:
    default-secret-key: "your_default_secret_key"
    sign-algorithm: "MD5"
    charset: "UTF-8"
    timeout: 30
    retry-count: 3
```

## 业务流程

### 充值成功流程

1. 第三方支付平台发送成功回调
2. 验证回调签名
3. 查询充值订单
4. 检查订单状态（避免重复处理）
5. 更新订单状态为成功
6. 更新用户余额
7. 更新用户充值统计
8. 记录账变明细
9. 返回成功响应

### 充值失败流程

1. 第三方支付平台发送失败回调
2. 验证回调签名
3. 查询充值订单
4. 更新订单状态为失败
5. 记录失败原因
6. 记录账变明细（失败记录）
7. 返回成功响应

## 安全考虑

1. **签名验证**：所有回调都必须通过签名验证
2. **幂等性**：同一订单的多次回调只处理一次
3. **参数验证**：验证必要参数的有效性
4. **异常处理**：完善的异常处理和日志记录
5. **事务管理**：使用事务确保数据一致性

## 错误处理

### 常见错误

1. **签名验证失败**：检查密钥配置和签名算法
2. **订单不存在**：检查订单号是否正确
3. **订单已处理**：正常情况，返回成功
4. **参数无效**：检查回调参数格式

### 重试机制

建议第三方支付平台在收到非成功响应时进行重试，重试间隔建议为：
- 第1次重试：1分钟后
- 第2次重试：5分钟后
- 第3次重试：15分钟后

## 监控和日志

### 日志记录

- 所有回调请求都会记录详细日志
- 包含请求参数、处理结果、错误信息等
- 使用不同日志级别区分重要程度

### 监控指标

- 回调成功率
- 回调响应时间
- 错误类型统计
- 订单处理状态分布

## 测试

### 单元测试

运行充值回调接口的单元测试：

```bash
mvn test -Dtest=ApiChargeCallbackControllerTest
```

### 集成测试

1. 启动应用
2. 使用Postman或其他工具模拟回调请求
3. 验证数据库中的订单状态和用户余额
4. 检查账变明细记录

## 部署说明

### 环境要求

- Java 8+
- Spring Boot 2.x
- MySQL 5.7+
- Redis（可选，用于缓存）

### 部署步骤

1. 配置数据库连接
2. 配置支付相关参数
3. 启动应用
4. 配置第三方支付平台的回调地址
5. 测试回调接口

### 注意事项

1. 确保回调接口可以被第三方支付平台访问
2. 配置正确的HTTPS证书（生产环境）
3. 设置合适的超时时间和重试策略
4. 监控接口的可用性和性能

## 联系支持

如有问题或需要技术支持，请联系开发团队。
