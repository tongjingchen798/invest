# 广告素材API接口说明文档

## 概述

本文档详细说明了新的广告素材API接口，该接口基于新的 `tb_advertisement` 表，提供了更丰富的广告管理功能。

## 数据库表结构

### tb_advertisement 表

| 字段名 | 类型 | 说明 | 示例值 |
|--------|------|------|--------|
| id | bigint | 主键ID | 1752149100638 |
| type | int | 广告类型 1=LOG,2=轮播图，3=个人中心 4=弹窗广告 | 4 |
| title | varchar(255) | 广告标题 | "弹窗" |
| logos_addr | varchar(500) | 图片地址 | "http://8.216.132.154/images/tools/13e3d8ff-61c9-4cb7-b572-6c37d6194ae2.png" |
| logos_linkaddr | varchar(500) | 链接地址 | "--" |
| remark | text | 备注描述 | "--" |
| sx_date | datetime | 生效时间 | "2025-07-12 02:46:00" |
| hour | int | 展示时长（小时） | 48 |
| sort | int | 排序 | 1 |
| status | int | 状态 0=禁用 1=启用 | 1 |
| create_date | datetime | 创建时间 | "2025-07-10 20:05:01" |
| update_date | datetime | 更新时间 | "2025-07-10 20:05:01" |
| creator | varchar(50) | 创建人 | "admin" |
| updater | varchar(50) | 更新人 | "admin" |

## API接口列表

### 1. 根据类型查询广告列表

**接口地址：** `GET /api/advertisement/listByType`

**请求参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| type | int | 是 | 广告类型 1=LOG,2=轮播图，3=个人中心 4=弹窗广告 |

**请求示例：**
```bash
GET /api/advertisement/listByType?type=4
```

**响应示例：**
```json
{
    "code": 0,
    "msg": "",
    "data": [
        {
            "id": "1752149100638",
            "type": 4,
            "title": "弹窗",
            "logosAddr": "http://8.216.132.154/images/tools/13e3d8ff-61c9-4cb7-b572-6c37d6194ae2.png",
            "logosLinkaddr": "--",
            "remark": "--",
            "createDate": "2025-07-10 20:05:01",
            "isPop": 0,
            "sxDate": "2025-07-12 02:46:00",
            "hour": 48
        }
    ]
}
```

### 2. 查询所有启用的广告

**接口地址：** `GET /api/advertisement/listEnabled`

**请求参数：** 无

**请求示例：**
```bash
GET /api/advertisement/listEnabled
```

**响应示例：**
```json
{
    "code": 0,
    "msg": "",
    "data": [
        {
            "id": "1",
            "type": 1,
            "title": "首页Logo",
            "logosAddr": "http://8.216.132.154/images/logo.png",
            "logosLinkaddr": "http://8.216.132.154",
            "remark": "网站Logo图片",
            "createDate": "2025-07-10 20:05:01",
            "isPop": 0,
            "sxDate": "2025-07-10 20:05:01",
            "hour": 24
        },
        {
            "id": "2",
            "type": 2,
            "title": "轮播图1",
            "logosAddr": "http://8.216.132.154/images/banner1.jpg",
            "logosLinkaddr": "http://8.216.132.154/banner1",
            "remark": "首页轮播图",
            "createDate": "2025-07-10 20:05:01",
            "isPop": 0,
            "sxDate": "2025-07-10 20:05:01",
            "hour": 48
        }
    ]
}
```

### 3. 查询当前生效的广告

**接口地址：** `GET /api/advertisement/listEffective`

**请求参数：** 无

**请求示例：**
```bash
GET /api/advertisement/listEffective
```

**响应示例：**
```json
{
    "code": 0,
    "msg": "",
    "data": [
        {
            "id": "3",
            "type": 3,
            "title": "个人中心背景",
            "logosAddr": "http://8.216.132.154/images/profile.jpg",
            "logosLinkaddr": "http://8.216.132.154/profile",
            "remark": "个人中心背景图",
            "createDate": "2025-07-10 20:05:01",
            "isPop": 0,
            "sxDate": "2025-07-10 20:05:01",
            "hour": 72
        }
    ]
}
```

## 响应数据结构

### 基本响应结构

```json
{
    "code": 0,
    "msg": "",
    "data": []
}
```

### 广告对象字段说明

| 字段名 | 类型 | 说明 | 示例值 |
|--------|------|------|--------|
| id | String | 主键ID（字符串格式） | "1752149100638" |
| type | Integer | 广告类型 | 4 |
| title | String | 广告标题 | "弹窗" |
| logosAddr | String | 图片地址 | "http://8.216.132.154/images/tools/13e3d8ff-61c9-4cb7-b572-6c37d6194ae2.png" |
| logosLinkaddr | String | 链接地址 | "--" |
| remark | String | 备注描述 | "--" |
| createDate | String | 创建时间（格式：yyyy-MM-dd HH:mm:ss） | "2025-07-10 20:05:01" |
| isPop | Integer | 是否弹窗（根据type字段计算：4=弹窗广告） | 0 |
| sxDate | String | 生效时间（格式：yyyy-MM-dd HH:mm:ss） | "2025-07-12 02:46:00" |
| hour | Integer | 展示时长（小时） | 48 |

## 广告类型说明

| 类型值 | 说明 | 用途 | isPop值 |
|--------|------|------|---------|
| 1 | LOG | 网站Logo图片 | 0 |
| 2 | 轮播图 | 首页轮播图展示 | 0 |
| 3 | 个人中心 | 个人中心背景图片 | 0 |
| 4 | 弹窗广告 | 弹窗形式的广告 | 1 |

## 特殊字段说明

### 1. isPop字段
- 这是一个计算字段，不在数据库中存储
- 当 `type = 4` 时，`isPop = 1`
- 其他类型时，`isPop = 0`

### 2. sxDate字段（生效时间）
- 表示广告开始生效的时间
- 如果为空或小于等于当前时间，则表示广告已生效
- 用于控制广告的展示时机

### 3. hour字段（展示时长）
- 表示广告的展示时长，单位为小时
- 可以用于计算广告的过期时间
- 结合 `sxDate` 可以判断广告是否仍在有效期内

## 使用示例

### JavaScript/TypeScript 示例

```javascript
// 获取弹窗广告
async function getPopupAds() {
    try {
        const response = await fetch('/api/advertisement/listByType?type=4');
        const result = await response.json();
        
        if (result.code === 0) {
            const popupAds = result.data.filter(ad => ad.isPop === 1);
            return popupAds;
        } else {
            console.error('获取弹窗广告失败:', result.msg);
            return [];
        }
    } catch (error) {
        console.error('请求失败:', error);
        return [];
    }
}

// 获取所有启用的广告
async function getAllEnabledAds() {
    try {
        const response = await fetch('/api/advertisement/listEnabled');
        const result = await response.json();
        
        if (result.code === 0) {
            return result.data;
        } else {
            console.error('获取广告失败:', result.msg);
            return [];
        }
    } catch (error) {
        console.error('请求失败:', error);
        return [];
    }
}

// 获取当前生效的广告
async function getEffectiveAds() {
    try {
        const response = await fetch('/api/advertisement/listEffective');
        const result = await response.json();
        
        if (result.code === 0) {
            return result.data;
        } else {
            console.error('获取生效广告失败:', result.msg);
            return [];
        }
    } catch (error) {
        console.error('请求失败:', error);
        return [];
    }
}
```

### Java 示例

```java
@RestController
@RequestMapping("/api/client")
public class ClientController {
    
    @Autowired
    private AdvertisementService advertisementService;
    
    @GetMapping("/ads")
    public Result<List<AdvertisementDTO>> getAdvertisements() {
        try {
            // 获取所有启用的广告
            List<AdvertisementDTO> ads = advertisementService.getAllEnabled();
            return new Result<List<AdvertisementDTO>>().ok(ads);
        } catch (Exception e) {
            return new Result<List<AdvertisementDTO>>().error("获取广告失败：" + e.getMessage());
        }
    }
    
    @GetMapping("/ads/popup")
    public Result<List<AdvertisementDTO>> getPopupAds() {
        try {
            // 获取弹窗广告
            List<AdvertisementDTO> ads = advertisementService.getByType(4);
            return new Result<List<AdvertisementDTO>>().ok(ads);
        } catch (Exception e) {
            return new Result<List<AdvertisementDTO>>().error("获取弹窗广告失败：" + e.getMessage());
        }
    }
}
```

### cURL 示例

```bash
# 获取弹窗广告
curl -X GET "http://localhost:8080/api/advertisement/listByType?type=4" \
  -H "Content-Type: application/json"

# 获取所有启用的广告
curl -X GET "http://localhost:8080/api/advertisement/listEnabled" \
  -H "Content-Type: application/json"

# 获取当前生效的广告
curl -X GET "http://localhost:8080/api/advertisement/listEffective" \
  -H "Content-Type: application/json"
```

## 错误处理

### 错误响应格式

```json
{
    "code": 500,
    "msg": "查询失败：数据库连接失败",
    "data": null
}
```

### 常见错误码

| 错误码 | 说明 | 处理建议 |
|--------|------|----------|
| 0 | 成功 | 正常处理返回数据 |
| 500 | 服务器内部错误 | 检查服务器日志，稍后重试 |
| 400 | 请求参数错误 | 检查请求参数格式 |
| 404 | 接口不存在 | 检查接口地址是否正确 |

## 注意事项

### 1. 数据格式
- 所有时间字段都使用 `yyyy-MM-dd HH:mm:ss` 格式
- ID字段返回为字符串格式，便于前端处理
- 数值字段（type、isPop、hour）为整数类型

### 2. 性能考虑
- 建议对广告数据进行缓存，减少数据库查询
- 可以根据 `sxDate` 和 `hour` 字段实现广告的自动过期
- 大量广告数据时建议使用分页查询

### 3. 安全性
- 图片地址建议使用HTTPS协议
- 链接地址需要进行安全验证，防止恶意跳转
- 建议对广告内容进行审核

## 更新日志

| 版本 | 日期 | 更新内容 |
|------|------|----------|
| 1.0.0 | 2025-07-10 | 初始版本，基于新的tb_advertisement表 |
| 1.1.0 | 2025-07-10 | 添加生效时间和展示时长字段 |
| 1.2.0 | 2025-07-10 | 完善API接口和测试用例 |
| 1.3.0 | 2025-07-10 | 添加详细的使用说明和示例 |
