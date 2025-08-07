# 注册API参数调整说明

## 更新概述
根据API参数表格要求，对注册接口进行了全面的参数调整，新增了多个字段以支持更丰富的注册功能。

## 新增字段

### 1. 基本信息字段
- **username** (真实姓名) - 可选，如果不提供则使用手机号作为用户名
- **twoPwd** (二级密码) - 可选，用于额外的安全验证

### 2. 验证相关字段
- **code** (短信验证码) - 可选，用于手机号验证
- **captcha** (验证码) - 可选，用于图形验证码
- **uuid** (唯一标识) - 可选，用于请求标识

### 3. 渠道和代理字段
- **inviteCode** (邀请码) - 可选，用于邀请注册
- **agent** (代理信息) - 可选，记录代理相关信息
- **channel** (客户渠道号) - 可选，记录注册渠道
- **equipment** (登录端口) - 可选，记录注册设备类型
  - 1: 安卓
  - 2: iOS
  - 3: PC
  - 4: 未知

## API接口信息

### 请求地址
```
POST /api/register
```

### 请求参数
| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型 |
|---------|---------|---------|---------|---------|
| dto | 注册表单 | body | true | 注册表单 |
| mobile | 手机号 | - | true | string |
| password | 密码 | - | true | string |
| username | 真实姓名 | - | false | string |
| twoPwd | 二级密码 | - | false | string |
| inviteCode | 邀请码 | - | false | string |
| code | 短信验证码 | - | false | string |
| captcha | 验证码 | - | false | string |
| uuid | 唯一标识 | - | false | string |
| agent | 代理信息 | - | false | string |
| channel | 客户渠道号 | - | false | string |
| equipment | 登录端口 | - | false | integer(int32) |

### 请求示例
```json
{
  "mobile": "13800138000",
  "password": "123456",
  "username": "张三",
  "twoPwd": "654321",
  "inviteCode": "INV001",
  "code": "123456",
  "captcha": "ABCD",
  "uuid": "uuid-12345",
  "agent": "agent-info",
  "channel": "CH001",
  "equipment": 3
}
```

### 响应示例
```json
{
  "code": 0,
  "msg": "success"
}
```

## 数据库变更

### 表结构更新
为 `tb_user` 表添加了以下字段：
- `two_pwd` VARCHAR(64) - 二级密码
- `invite_code` VARCHAR(50) - 邀请码
- `agent` VARCHAR(200) - 代理信息
- `channel` VARCHAR(100) - 客户渠道号
- `equipment` INT - 登录端口

### 执行脚本
```bash
# 方式1：更新现有表结构
mysql -u admin -p renren_security < update-user-table.sql

# 方式2：重新创建表（会清空数据）
mysql -u admin -p renren_security < create-user-table-complete.sql
```

## 代码变更

### 1. RegisterDTO.java
- 新增了所有API参数对应的字段
- 添加了相应的Swagger注解
- 保持了原有的必填字段验证

### 2. UserEntity.java
- 新增了对应的实体字段
- 添加了字段注释
- 对敏感字段使用了@JsonIgnore注解

### 3. ApiRegisterController.java
- 更新了注册逻辑，处理新字段
- 添加了二级密码的加密处理
- 优化了用户名的设置逻辑

## 安全考虑

1. **密码加密**：主密码和二级密码都使用SHA256加密存储
2. **敏感字段隐藏**：密码相关字段在JSON序列化时会被忽略
3. **参数验证**：保持了原有的参数验证机制
4. **数据长度限制**：数据库字段长度合理设置，防止数据溢出

## 兼容性说明

1. **向后兼容**：所有新字段都是可选的，不影响现有功能
2. **默认值处理**：未提供的字段会设置为NULL
3. **数据库兼容**：支持MySQL 5.7+版本

## 测试建议

1. **基本注册测试**：使用最小参数集进行注册
2. **完整参数测试**：使用所有参数进行注册
3. **字段验证测试**：测试各种字段组合
4. **数据库验证**：确认数据正确存储到数据库

## 部署步骤

1. 执行数据库更新脚本
2. 重新编译并部署应用
3. 测试注册接口功能
4. 验证数据存储正确性
