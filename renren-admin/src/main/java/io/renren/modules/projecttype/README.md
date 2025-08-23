# 项目类型分页查询优化

## 概述

本项目类型分页查询功能已经过优化，使用MyBatis-Plus的分页插件提供更高效、更灵活的分页查询支持。

## 主要优化点

### 1. **使用MyBatis-Plus分页插件**
- 替换原有的基础分页实现
- 使用`Page<T>`对象进行分页参数管理
- 利用MyBatis-Plus的内置分页功能

### 2. **增强查询条件支持**
- **typeId**: 按项目分类ID精确查询
- **typeName**: 按分类名称模糊查询
- **status**: 按状态查询（0=禁用，1=启用）
- **排序支持**: 支持自定义排序字段和排序方式

### 3. **参数验证和默认值**
- 分页参数自动验证和修正
- 提供合理的默认值（页码1，每页10条）
- 限制每页最大记录数为100条

### 4. **异常处理和日志记录**
- 完善的异常处理机制
- 详细的查询日志记录
- 用户友好的错误信息返回

## API接口说明

### 分页查询接口

**接口地址**: `GET /investprojecttype/page`

**请求参数**:

| 参数名称 | 类型 | 必填 | 说明 | 示例值 |
|---------|------|------|------|--------|
| page | int | 是 | 当前页码，从1开始 | 1 |
| limit | int | 是 | 每页显示记录数 | 10 |
| typeId | String | 否 | 项目分类ID | "1" |
| typeName | String | 否 | 分类名称(支持模糊查询) | "投资" |
| status | String | 否 | 状态 0=禁用 1=启用 | "1" |
| orderField | String | 否 | 排序字段 | "create_date" |
| order | String | 否 | 排序方式(asc/desc) | "desc" |

**响应示例**:

```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "list": [
      {
        "typeId": 1,
        "typeName": "股票投资",
        "sort": 1,
        "status": 1,
        "createDate": "2025-08-20 10:00:00"
      }
    ],
    "total": 1,
    "page": 1,
    "limit": 10
  }
}
```

## 使用示例

### 1. 基础分页查询
```bash
GET /investprojecttype/page?page=1&limit=10
```

### 2. 按名称模糊查询
```bash
GET /investprojecttype/page?page=1&limit=10&typeName=投资
```

### 3. 按状态查询
```bash
GET /investprojecttype/page?page=1&limit=10&status=1
```

### 4. 自定义排序
```bash
GET /investprojecttype/page?page=1&limit=10&orderField=create_date&order=desc
```

### 5. 组合查询
```bash
GET /investprojecttype/page?page=1&limit=10&typeName=投资&status=1&orderField=sort&order=asc
```

## 技术实现

### 核心类结构

- **ProjectTypeController**: 控制器层，处理HTTP请求
- **ProjectTypeService**: 服务接口层
- **ProjectTypeServiceImpl**: 服务实现层，包含分页逻辑
- **ProjectTypeDao**: 数据访问层，继承MyBatis-Plus的BaseMapper

### 分页查询流程

1. **参数接收**: 接收前端传递的查询参数
2. **参数验证**: 验证和修正分页参数
3. **条件构建**: 根据参数构建QueryWrapper查询条件
4. **分页执行**: 使用MyBatis-Plus分页插件执行查询
5. **数据转换**: 将Entity转换为DTO
6. **结果返回**: 返回PageData分页数据

### 查询条件构建

```java
@Override
public QueryWrapper<ProjectTypeEntity> getWrapper(Map<String, Object> params){
    QueryWrapper<ProjectTypeEntity> wrapper = new QueryWrapper<>();
    
    // 项目分类ID查询
    String typeId = (String)params.get("typeId");
    if (StringUtils.isNotBlank(typeId)) {
        wrapper.eq("type_id", typeId);
    }
    
    // 分类名称模糊查询
    String typeName = (String)params.get("typeName");
    if (StringUtils.isNotBlank(typeName)) {
        wrapper.like("type_name", "%" + typeName + "%");
    }
    
    // 状态查询
    String status = (String)params.get("status");
    if (StringUtils.isNotBlank(status)) {
        wrapper.eq("status", status);
    }
    
    // 排序处理
    String orderField = (String)params.get("orderField");
    String order = (String)params.get("order");
    
    if (StringUtils.isNotBlank(orderField) && StringUtils.isNotBlank(order)) {
        if ("asc".equalsIgnoreCase(order)) {
            wrapper.orderByAsc(orderField);
        } else {
            wrapper.orderByDesc(orderField);
        }
    } else {
        // 默认排序
        wrapper.orderByAsc("sort").orderByDesc("create_date");
    }
    
    return wrapper;
}
```

## 性能优化

### 1. **数据库索引建议**
- 在`type_id`字段上建立索引
- 在`type_name`字段上建立索引
- 在`status`字段上建立索引
- 在`sort`和`create_date`字段上建立复合索引

### 2. **查询优化**
- 使用MyBatis-Plus的分页插件，避免手动计算分页
- 合理使用查询条件，避免全表扫描
- 支持模糊查询，但建议限制查询范围

### 3. **缓存策略**
- 对于不经常变化的数据，可以考虑添加缓存
- 分页结果可以考虑短期缓存

## 注意事项

1. **分页参数限制**: 每页最大记录数限制为100条，防止查询过大数据量
2. **参数验证**: 所有分页参数都会进行验证和修正
3. **异常处理**: 查询异常会被捕获并返回友好的错误信息
4. **日志记录**: 所有查询操作都会记录详细日志，便于问题排查

## 扩展功能

### 待实现功能
- [ ] 批量操作支持
- [ ] 数据导出功能
- [ ] 高级搜索（日期范围、数值范围等）
- [ ] 查询结果缓存

### 建议改进
1. 添加查询结果统计功能
2. 支持多字段组合排序
3. 实现查询条件模板保存
4. 添加查询性能监控

