# 客服设置接口文档

## 接口概述

客服设置接口用于管理系统用户的客服相关信息，包括客服头像、客服号码、客服名称和TG号码等。

## 接口详情

### 设置客服

**接口地址**: `/admin/sys/user/editws`

**请求方式**: `PUT`

**请求数据类型**: `application/json`

**响应数据类型**: `*/*`

**接口描述**: 设置指定用户的客服信息

**权限要求**: `sys:user:editws`

## 请求参数

### 请求体参数

| 参数名称 | 参数说明 | 是否必须 | 数据类型 | 示例值 |
| -------- | -------- | -------- | -------- | ------ |
| id | 用户ID | 是 | Long | 123456 |
| tgnumber | 客服的TG号码 | 否 | String | "@customer_service" |
| wsimage | 客服的头像 | 否 | String | "https://example.com/avatar.jpg" |
| wsname | 客服的名字 | 否 | String | "客服小王" |
| wsnumber | 客服的号码 | 否 | String | "13800138000" |

### 请求示例

```json
{
  "id": 123456,
  "tgnumber": "@customer_service",
  "wsimage": "https://example.com/avatar.jpg",
  "wsname": "客服小王",
  "wsnumber": "13800138000"
}
```

## 响应参数

### 响应状态码

| 状态码 | 说明 |
| ------ | ---- |
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 权限不足 |
| 500 | 服务器内部错误 |

### 响应示例

**成功响应**:
```json
{
  "code": 0,
  "msg": "success",
  "data": {}
}
```

**错误响应**:
```json
{
  "code": 400,
  "msg": "用户ID不能为空",
  "data": null
}
```

## 使用说明

### 1. 权限配置

在使用此接口前，需要确保：

1. 用户已登录并具有相应权限
2. 已在系统中配置 `sys:user:editws` 权限
3. 用户角色已分配该权限

### 2. 数据库配置

执行以下SQL语句添加权限菜单：

```sql
-- MySQL版本
INSERT INTO sys_menu (id, pid, name, url, permissions, menu_type, icon, sort, creator, create_date, updater, update_date) 
VALUES (1067246875800000100, 1067246875800000055, '设置客服', NULL, 'sys:user:editws', 1, NULL, 5, 1067246875800000001, NOW(), 1067246875800000001, NOW());
```

### 3. 调用示例

#### JavaScript (前端)

```javascript
// 设置客服信息
const setCustomerService = async (customerServiceData) => {
  try {
    const response = await fetch('/admin/sys/user/editws', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + token
      },
      body: JSON.stringify(customerServiceData)
    });
    
    const result = await response.json();
    if (result.code === 0) {
      console.log('客服设置成功');
    } else {
      console.error('客服设置失败:', result.msg);
    }
  } catch (error) {
    console.error('请求失败:', error);
  }
};

// 使用示例
const customerServiceData = {
  id: 123456,
  tgnumber: "@customer_service",
  wsimage: "https://example.com/avatar.jpg",
  wsname: "客服小王",
  wsnumber: "13800138000"
};

setCustomerService(customerServiceData);
```

#### Java (后端)

```java
// 调用客服设置接口
@Autowired
private RestTemplate restTemplate;

public void setCustomerService(CustomerServiceSettingDTO dto) {
    String url = "http://localhost:8080/admin/sys/user/editws";
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);
    
    HttpEntity<CustomerServiceSettingDTO> request = new HttpEntity<>(dto, headers);
    
    ResponseEntity<Result> response = restTemplate.exchange(
        url, 
        HttpMethod.PUT, 
        request, 
        Result.class
    );
    
    if (response.getStatusCode() == HttpStatus.OK) {
        System.out.println("客服设置成功");
    }
}
```

## 注意事项

1. **权限验证**: 确保调用方具有 `sys:user:editws` 权限
2. **参数验证**: 用户ID为必填字段，其他字段为可选
3. **数据安全**: 接口会记录操作日志，便于审计
4. **错误处理**: 建议在调用时做好异常处理和错误提示
5. **字段说明**: 
   - `tgnumber`: Telegram号码，通常以@开头
   - `wsimage`: 头像图片URL，建议使用HTTPS链接
   - `wsname`: 客服显示名称
   - `wsnumber`: 客服联系方式（手机号、微信号等）

## 相关接口

- **获取用户信息**: `GET /admin/sys/user/{id}` - 获取用户详细信息
- **修改用户信息**: `PUT /admin/sys/user` - 修改用户基本信息
- **用户分页查询**: `GET /admin/sys/user/page` - 分页查询用户列表

## 更新日志

- **v1.0.0** (2024-01-01): 初始版本，支持基本的客服信息设置
