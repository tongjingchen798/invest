# 广告API数据结构说明文档

## 概述

本文档详细说明了广告API接口返回的数据结构，包括字段定义、数据类型和示例。

## 返回数据结构

### 基本结构

```json
{
    "code": 0,
    "data": {
        "list": [
            {
                "createDate": "",
                "id": 0,
                "isPop": 0,
                "logosAddr": "",
                "logosLinkaddr": "",
                "remark": "",
                "title": "",
                "type": 0
            }
        ],
        "sum": {},
        "total": 0
    },
    "msg": ""
}
```

### 字段说明

| 字段名 | 类型 | 说明 | 示例值 |
|--------|------|------|--------|
| code | int | 响应状态码，0表示成功，其他值表示失败 | 0 |
| msg | String | 响应消息 | "" |
| data | Object | 响应数据 | - |
| data.list | Array | 广告列表数据 | - |
| data.total | int | 总记录数 | 10 |
| data.sum | Object | 汇总数据（暂未实现） | {} |

### 广告对象字段说明

| 字段名 | 类型 | 说明 | 示例值 |
|--------|------|------|--------|
| id | Long | 主键ID | 1 |
| title | String | 图片名称 | "首页Logo" |
| remark | String | 描述 | "网站Logo图片" |
| logosAddr | String | 图片地址 | "https://example.com/logo.png" |
| logosLinkaddr | String | 链接地址 | "https://example.com" |
| type | Integer | 广告类型 | 1 |
| isPop | Integer | 是否弹窗 | 0 |
| sort | Integer | 排序 | 1 |
| status | Integer | 状态 | 1 |
| createDate | Date | 创建时间 | "2024-01-01T00:00:00.000+00:00" |

## 广告类型说明

| 类型值 | 说明 | 用途 | isPop值 |
|--------|------|------|---------|
| 1 | LOG | 网站Logo图片 | 0 |
| 2 | 轮播图 | 首页轮播图展示 | 0 |
| 3 | 个人中心 | 个人中心背景图片 | 0 |
| 4 | 弹窗广告 | 弹窗形式的广告 | 1 |

## 字段映射关系

### 数据库字段到DTO字段的映射

| 数据库字段 | DTO字段 | 说明 |
|------------|---------|------|
| id | id | 主键ID |
| title | title | 图片名称 |
| content | remark | 描述信息 |
| images_addr | logosAddr | 图片地址 |
| content | logosLinkaddr | 链接地址（复用content字段） |
| type | type | 广告类型 |
| - | isPop | 计算字段，type=4时为1，其他为0 |
| sort | sort | 排序字段 |
| status | status | 状态字段 |
| create_date | createDate | 创建时间 |

### isPop字段计算逻辑

```java
// 在IssuesServiceImpl.convertToDto方法中计算
for (IssuesDTO dto : dtoList) {
    if (dto.getType() != null && dto.getType() == 4) {
        dto.setIsPop(1); // 弹窗广告
    } else {
        dto.setIsPop(0); // 非弹窗广告
    }
}
```

## 完整示例

### 成功响应示例

```json
{
    "code": 0,
    "msg": "",
    "data": {
        "total": 2,
        "list": [
            {
                "id": 1,
                "title": "首页Logo",
                "remark": "网站Logo图片",
                "logosAddr": "https://example.com/logo.png",
                "logosLinkaddr": "https://example.com",
                "type": 1,
                "isPop": 0,
                "sort": 1,
                "status": 1,
                "createDate": "2024-01-01T00:00:00.000+00:00"
            },
            {
                "id": 2,
                "title": "轮播图1",
                "remark": "首页轮播图",
                "logosAddr": "https://example.com/banner1.jpg",
                "logosLinkaddr": "https://example.com/banner1",
                "type": 2,
                "isPop": 0,
                "sort": 1,
                "status": 1,
                "createDate": "2024-01-01T00:00:00.000+00:00"
            }
        ],
        "sum": {}
    }
}
```

### 弹窗广告示例

```json
{
    "code": 0,
    "msg": "",
    "data": {
        "total": 1,
        "list": [
            {
                "id": 3,
                "title": "弹窗广告",
                "remark": "弹窗形式的广告",
                "logosAddr": "https://example.com/popup.jpg",
                "logosLinkaddr": "https://example.com/popup",
                "type": 4,
                "isPop": 1,
                "sort": 1,
                "status": 1,
                "createDate": "2024-01-01T00:00:00.000+00:00"
            }
        ],
        "sum": {}
    }
}
```

### 空结果示例

```json
{
    "code": 0,
    "msg": "",
    "data": {
        "total": 0,
        "list": [],
        "sum": {}
    }
}
```

### 错误响应示例

```json
{
    "code": 500,
    "msg": "查询失败：数据库连接失败",
    "data": null
}
```

## 注意事项

### 1. 字段命名
- 所有字段名都使用驼峰命名法
- 图片地址字段为 `logosAddr`，不是 `imagesAddr`
- 描述字段为 `remark`，不是 `content`
- 链接地址字段为 `logosLinkaddr`

### 2. isPop字段
- 这是一个计算字段，不在数据库中存储
- 当 `type = 4` 时，`isPop = 1`
- 其他类型时，`isPop = 0`

### 3. 数据类型
- 所有ID字段都是Long类型
- 状态和类型字段都是Integer类型
- 时间字段是Date类型，会转换为ISO 8601格式的字符串

### 4. 默认值处理
- 如果请求中没有传递分页参数，会使用默认值：`page=1`, `limit=10`
- 默认只查询状态为1（启用）的广告
- 默认按sort字段升序，create_date字段降序排列

## 使用建议

### 1. 前端处理
```javascript
// 检查是否为弹窗广告
function isPopupAd(ad) {
    return ad.isPop === 1;
}

// 根据类型过滤广告
function filterAdsByType(ads, type) {
    return ads.filter(ad => ad.type === type);
}

// 获取图片地址
function getImageUrl(ad) {
    return ad.logosAddr || '';
}

// 获取链接地址
function getLinkUrl(ad) {
    return ad.logosLinkaddr || '';
}
```

### 2. 错误处理
```javascript
if (response.code !== 0) {
    console.error('查询失败:', response.msg);
    return;
}

const { total, list } = response.data;
console.log(`总共 ${total} 条广告记录`);
```

### 3. 分页处理
```javascript
function handlePagination(page, limit) {
    const params = new URLSearchParams();
    params.append('page', page);
    params.append('limit', limit);
    
    // 发送请求...
}
```

## 更新日志

| 版本 | 日期 | 更新内容 |
|------|------|----------|
| 1.0.0 | 2024-01-01 | 初始版本，基本数据结构 |
| 1.1.0 | 2024-01-01 | 添加isPop字段，完善字段映射 |
| 1.2.0 | 2024-01-01 | 统一字段命名规范 |
| 1.3.0 | 2024-01-01 | 完善示例和说明文档 |
