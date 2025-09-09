# USDT充值二维码生成API使用说明

## 功能概述

实现了USDT充值渠道的二维码生成功能，用户可以通过扫描二维码进行USDT-TRC20转账。

## 系统配置

### 1. 系统参数配置

在 `sys_params` 表中添加以下参数：

| 参数编码 | 参数值 | 备注 |
|---------|--------|------|
| `usdt-trc20-address` | `Txxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx` | USDT-TRC20收款地址 |
| `usdtsysprice` | `97` | USDT系统价格 |
| `usdtrealprice` | `86` | USDT实际价格 |

## API接口

### 1. 生成USDT充值二维码

**接口地址：** `POST /api/paychannel/generateUSDTQRCode`

**请求参数：**
- `amount` (可选): 充值金额

**响应示例：**
```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "usdtAddress": "Txxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
    "qrCode": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA...",
    "amount": "100",
    "message": "请使用支持TRC20的钱包扫描二维码进行USDT转账"
  }
}
```

### 2. 获取USDT地址

**接口地址：** `GET /api/paychannel/getUSDTAddress`

**响应示例：**
```json
{
  "code": 0,
  "msg": "success",
  "data": "Txxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
}
```

### 3. 查询支付方式（已更新）

**接口地址：** `GET /api/paychannel/getPay`

**响应示例：**
```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "UPI": [...],
    "SWIPE": [...],
    "USDT": [
      {
        "channelid": 1,
        "channelName": "USDT充值",
        "channelType": "USDT",
        "usdtGiftRatio": "97",
        "usdtLocalCurrencyRate": "86"
      }
    ]
  }
}
```

## 前端使用示例

### 1. 生成二维码

```javascript
// 生成USDT充值二维码
async function generateUSDTQRCode(amount) {
  try {
    const response = await fetch('/api/paychannel/generateUSDTQRCode', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: `amount=${amount || ''}`
    });
    
    const result = await response.json();
    
    if (result.code === 0) {
      // 显示二维码
      document.getElementById('qrCodeImage').src = result.data.qrCode;
      document.getElementById('usdtAddress').textContent = result.data.usdtAddress;
      document.getElementById('message').textContent = result.data.message;
    } else {
      console.error('生成二维码失败:', result.msg);
    }
  } catch (error) {
    console.error('请求失败:', error);
  }
}
```

### 2. 显示二维码页面

```html
<!DOCTYPE html>
<html>
<head>
    <title>USDT充值</title>
</head>
<body>
    <div class="usdt-recharge">
        <h2>USDT充值</h2>
        
        <!-- 二维码显示区域 -->
        <div class="qr-code-section">
            <img id="qrCodeImage" alt="USDT充值二维码" style="width: 300px; height: 300px;">
        </div>
        
        <!-- 地址显示区域 -->
        <div class="address-section">
            <label>USDT-TRC20地址：</label>
            <input type="text" id="usdtAddress" readonly style="width: 400px;">
            <button onclick="copyAddress()">复制地址</button>
        </div>
        
        <!-- 提示信息 -->
        <div class="message-section">
            <p id="message">请使用支持TRC20的钱包扫描二维码进行USDT转账</p>
        </div>
        
        <!-- 操作按钮 -->
        <div class="action-section">
            <button onclick="generateUSDTQRCode()">生成新二维码</button>
            <button onclick="generateUSDTQRCode('100')">生成100 USDT二维码</button>
        </div>
    </div>

    <script>
        // 复制地址到剪贴板
        function copyAddress() {
            const addressInput = document.getElementById('usdtAddress');
            addressInput.select();
            document.execCommand('copy');
            alert('地址已复制到剪贴板');
        }
        
        // 页面加载时生成二维码
        window.onload = function() {
            generateUSDTQRCode();
        };
    </script>
</body>
</html>
```

## 二维码内容格式

生成的二维码包含以下格式的链接：

- **仅地址**: `tron:Txxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx`
- **包含金额**: `tron:Txxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx?amount=100`

## 支持的TRC20钱包

用户可以使用以下钱包扫描二维码进行USDT转账：

1. **TronLink** - 浏览器插件钱包
2. **TronWallet** - 移动端钱包
3. **Trust Wallet** - 多链钱包
4. **TokenPocket** - 多链钱包
5. **其他支持TRC20的钱包**

## 注意事项

1. **地址配置**: 确保在系统参数中正确配置USDT-TRC20地址
2. **网络确认**: 用户转账后需要等待TRON网络确认
3. **金额精度**: USDT支持6位小数精度
4. **手续费**: TRC20转账需要消耗TRX作为手续费
5. **安全提醒**: 提醒用户确认地址正确性，避免转账到错误地址

## 错误处理

- 如果USDT地址未配置，返回错误信息
- 如果二维码生成失败，返回具体错误原因
- 前端应处理网络错误和服务器错误
