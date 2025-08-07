# Issues API 接口文档

## 接口概述

Issues API 提供了广告/图片管理功能，支持分页查询、条件筛选等功能。

## 响应格式

接口使用统一的响应格式：

```json
{
  "code": 0,
  "data": {
    "list": [
      {
        "content": "",
        "createDate": "",
        "id": 0,
        "imagesAddr": "",
        "title": ""
      }
    ],
    "sum": {},
    "total": 0
  },
  "msg": ""
}
```

## 接口列表

### 分页查询广告/图片

**接口地址**: `/api/issues/page`

**请求方式**: `GET`

**请求参数**:

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
| limit | 每页显示记录数 | query | true | integer(int32) | |
| page | 当前页码，从1开始 | query | true | integer(int32) | |
| order | 排序方式，可选值(asc、desc) | query | false | string | |
| orderField | 排序字段 | query | false | string | |
| type | 广告类型 1=LOG,2=轮播图，3=个人中心 4=弹窗广告 | query | false | string | |

**响应示例**:

```json
{
  "code": 0,
  "data": {
    "list": [
      {
        "content": "网站Logo图片",
        "createDate": "2024-01-01 10:00:00",
        "id": 1,
        "imagesAddr": "https://example.com/logo.png",
        "title": "首页Logo"
      }
    ],
    "sum": {},
    "total": 1
  },
  "msg": ""
}
```

## 广告类型说明

| 类型值 | 说明 |
| ------ | ---- |
| 1 | LOG |
| 2 | 轮播图 |
| 3 | 个人中心 |
| 4 | 弹窗广告 |

## 使用示例

### 查询所有轮播图
```
GET /api/issues/page?type=2&page=1&limit=10
```

### 查询所有启用的广告，按创建时间倒序
```
GET /api/issues/page?page=1&limit=20&orderField=create_date&order=desc
```

### 查询所有广告
```
GET /api/issues/page?page=1&limit=10
```

## 数据库表结构

```sql
CREATE TABLE issues (
  id bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  title varchar(200) NOT NULL COMMENT '名称',
  content text COMMENT '描述',
  images_addr varchar(500) NOT NULL COMMENT '链接地址',
  type tinyint NOT NULL COMMENT '广告类型 1=LOG,2=轮播图，3=个人中心 4=弹窗广告',
  sort int DEFAULT 0 COMMENT '排序',
  status tinyint DEFAULT 1 COMMENT '状态 0=禁用 1=启用',
  create_date datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_date datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  creator bigint COMMENT '创建者',
  updater bigint COMMENT '更新者',
  PRIMARY KEY (id),
  KEY idx_type (type),
  KEY idx_status (status),
  KEY idx_create_date (create_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='广告/图片管理表';
```
