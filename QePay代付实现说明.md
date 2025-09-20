# QePay代付服务实现说明

## 概述

本文档描述了QePay代付服务的实现，专门处理印度代付业务。该实现遵循QePay官方API规范，支持印度银行代付功能。

## 实现文件

### 1. 核心服务类
- `QePayPayoutServiceImpl.java` - QePay代付服务实现类
- `QePaySignatureUtils.java` - QePay签名工具类
- `FormDataUtils.java` - 表单数据构建工具类

### 2. 测试类
- `QePayPayoutServiceImplTest.java` - 单元测试类

## API规范

### 请求地址
```
https://pay.qeawapay.com/pay/transfer
```

### 请求方式
- 方法：POST
- Content-Type：application/x-www-form-urlencoded

### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| sign_type | String | Y | 固定值MD5，不参与签名 |
| sign | String | Y | 签名，不参与签名 |
| mch_id | String | Y | 商户代码 |
| mch_transferId | String | Y | 商家转账订单号 |
| transfer_amount | String | Y | 转账金额（以当地货币精确到元） |
| apply_date | String | Y | 申请时间（北京时间：yyyy-MM-dd HH:mm:ss） |
| bank_code | String | Y | 收款银行代码 |
| receive_name | String | Y | 收款银行户名 |
| receive_account | String | Y | 收款银行账号 |
| remark | String | N | 印度代付必填IFSC码 |
| back_url | String | N | 异步通知地址 |
| receiver_telephone | String | N | 收款人手机号码 |

### 响应参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| respCode | String | Y | 响应状态（SUCCESS/FAIL） |
| errorMsg | String | Y | 响应失败原因 |
| signType | String | Y | 签名方式（MD5） |
| sign | String | Y | 签名 |
| mchId | String | Y | 商户代码 |
| merTransferId | String | Y | 商家转账单号 |
| transferAmount | String | Y | 转账金额 |
| applyDate | String | Y | 订单时间 |
| tradeNo | String | Y | 平台转账单号 |
| tradeResult | String | Y | 是否转账成功状态 |

## 签名规则

1. 将所有需要签名的字段按照ASCII码从小到大进行排序
2. 按照k=v&k=v的格式拼接字符串
3. 在字符串后面拼接商户私钥用&key=x进行拼接
4. 对生成的queryString字符串进行MD5签名，得到小写签名串
5. 除了sign和sign_type以外不为空的参数都需要参与签名

## 回调处理

### 回调接口地址
```
POST /api/payout/qepay/notify
Content-Type: application/x-www-form-urlencoded
```

### 回调参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| tradeResult | String | Y | 订单状态：1-代付成功，2-代付失败 |
| merTransferId | String | Y | 商家转账单号 |
| merNo | String | Y | 商户代码 |
| tradeNo | String | Y | 平台订单号 |
| transferAmount | String | Y | 代付金额（元为单位保留两位小数） |
| applyDate | String | Y | 订单时间 |
| version | String | Y | 版本号（默认1.0） |
| respCode | String | Y | 回调状态（默认SUCCESS） |
| sign | String | N | 签名 |
| signType | String | N | 签名方式（MD5） |

### 回调处理流程

1. **接收回调**：接收QePay发送的表单格式回调数据
2. **解析数据**：将表单参数解析为QePayCallbackDTO对象
3. **验证签名**：使用QePaySignatureUtils验证回调签名
4. **处理结果**：根据tradeResult字段处理代付成功或失败
5. **返回响应**：处理成功返回"success"，失败返回"fail"

### 回调示例

```bash
# 代付成功回调示例
curl -X POST "http://your-domain.com/api/payout/qepay/notify" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "tradeResult=1&merTransferId=QEPAY_TEST_001&merNo=QEPAY123456&tradeNo=3000025&transferAmount=100.00&applyDate=2024-01-01 11:33:59&version=1.0&respCode=SUCCESS&sign=abc123&signType=MD5"
```

## 使用示例

### 1. 基本使用

```java
@Autowired
private QePayPayoutServiceImpl qePayPayoutService;

// 创建代付订单
PayAgentResponse response = qePayPayoutService.createPayoutOrder(withdrawOrder, payMerchant);
```

### 2. 商户配置

商户需要配置以下信息：
- `merchantCode`: 必须以"QEPAY"开头
- `merchantno`: QePay分配的商户代码
- `channelkey`: QePay分配的密钥

### 3. 订单数据要求

WithdrawOrderEntity需要包含以下字段：
- `orderno`: 订单号
- `realAmount`: 实际到账金额（分）
- `blankCode`: 银行代码
- `payName`: 收款人姓名
- `payNo`: 收款账号
- `ifsc`: IFSC代码（印度代付必填）

## 错误处理

### 1. 网络错误
- 连接超时：30秒
- 读取超时：60秒
- 自动重试机制

### 2. 业务错误
- 参数验证失败
- 签名验证失败
- 渠道不存在
- 金额格式错误

### 3. 响应解析错误
- JSON解析失败
- 必填字段缺失
- 数据类型错误

## 日志记录

### 1. 请求日志
- 订单号
- 请求参数
- 请求时间

### 2. 响应日志
- 响应状态码
- 响应内容
- 处理时间

### 3. 错误日志
- 错误类型
- 错误详情
- 堆栈信息

## 测试

### 1. 单元测试
运行测试类验证基本功能：
```bash
mvn test -Dtest=QePayPayoutServiceImplTest
```

### 2. 集成测试
需要配置真实的QePay商户信息进行测试。

## 注意事项

1. **印度代付特殊要求**：
   - 必须填写IFSC代码
   - 金额以印度卢比为单位
   - 银行代码必须符合QePay规范

2. **安全性**：
   - 密钥不能硬编码
   - 签名验证必须严格
   - 敏感信息不能记录到日志

3. **性能优化**：
   - 连接池配置
   - 超时时间设置
   - 异步处理考虑

4. **监控告警**：
   - 成功率监控
   - 响应时间监控
   - 错误率告警

## 扩展功能

### 1. 支持更多国家
可以扩展支持其他国家的代付业务，需要根据QePay文档添加相应的参数。

### 2. 批量代付
可以实现批量代付功能，提高处理效率。

### 3. 状态查询
可以添加代付状态查询接口，实时了解代付进度。

## 联系信息

如有问题，请联系开发团队或查看QePay官方文档。
