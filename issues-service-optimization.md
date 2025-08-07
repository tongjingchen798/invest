# Issues Service 优化说明

## 优化前的问题

1. **分页实现不规范**：使用 `queryWrapper.last("LIMIT ...")` 手动拼接 SQL，这不是 MyBatis-Plus 推荐的做法
2. **代码重复**：查询条件构建逻辑在多个方法中重复
3. **性能问题**：分页查询时先查总数再查数据，需要两次数据库查询
4. **可维护性差**：查询逻辑分散在多个方法中，难以维护

## 优化后的改进

### 1. 使用 MyBatis-Plus 标准分页

```java
// 优化前：手动拼接 LIMIT
queryWrapper.last("LIMIT " + (page - 1) * limit + ", " + limit);

// 优化后：使用 MyBatis-Plus 标准分页
Page<IssuesEntity> pageParam = new Page<>(page, limit);
IPage<IssuesEntity> pageResult = this.page(pageParam, queryWrapper);
```

### 2. 抽取公共查询条件构建方法

```java
/**
 * 构建查询条件
 */
private QueryWrapper<IssuesEntity> buildQueryWrapper(Map<String, Object> params) {
    String type = (String) params.get("type");
    String order = (String) params.get("order");
    String orderField = (String) params.get("orderField");

    QueryWrapper<IssuesEntity> queryWrapper = new QueryWrapper<IssuesEntity>();
    
    // 类型筛选
    if (StringUtils.isNotBlank(type)) {
        queryWrapper.eq("type", type);
    }
    
    // 只查询启用的数据
    queryWrapper.eq("status", 1);
    
    // 排序
    if (StringUtils.isNotBlank(orderField)) {
        if ("desc".equalsIgnoreCase(order)) {
            queryWrapper.orderByDesc(orderField);
        } else {
            queryWrapper.orderByAsc(orderField);
        }
    } else {
        // 默认按排序字段和创建时间排序
        queryWrapper.orderByAsc("sort").orderByDesc("create_date");
    }

    return queryWrapper;
}
```

### 3. 优化分页查询逻辑

```java
@Override
public IssuesPageData<IssuesDTO> queryPageData(Map<String, Object> params) {
    // 构建查询条件
    QueryWrapper<IssuesEntity> queryWrapper = buildQueryWrapper(params);
    
    // 获取分页参数
    Integer page = (Integer) params.get("page");
    Integer limit = (Integer) params.get("limit");
    
    if (page != null && limit != null) {
        // 使用 MyBatis-Plus 标准分页
        Page<IssuesEntity> pageParam = new Page<>(page, limit);
        IPage<IssuesEntity> pageResult = this.page(pageParam, queryWrapper);
        
        // 转换为DTO
        List<IssuesDTO> dtoList = convertToDto(pageResult.getRecords());
        
        return new IssuesPageData<>(dtoList, (int) pageResult.getTotal());
    } else {
        // 不分页查询
        List<IssuesEntity> entityList = this.list(queryWrapper);
        List<IssuesDTO> dtoList = convertToDto(entityList);
        
        return new IssuesPageData<>(dtoList, entityList.size());
    }
}
```

## 优化效果

### 1. 性能提升
- **减少数据库查询次数**：MyBatis-Plus 分页插件会自动优化查询，减少不必要的 COUNT 查询
- **更好的 SQL 优化**：使用标准分页方式，数据库可以更好地优化执行计划

### 2. 代码质量提升
- **消除代码重复**：查询条件构建逻辑统一管理
- **提高可维护性**：逻辑清晰，易于理解和修改
- **符合框架规范**：使用 MyBatis-Plus 推荐的分页方式

### 3. 功能增强
- **支持动态分页**：可以灵活控制是否启用分页
- **更好的错误处理**：使用框架标准方式，错误处理更完善
- **扩展性更好**：后续添加新的查询条件更容易

## 使用示例

```java
// 分页查询
Map<String, Object> params = new HashMap<>();
params.put("page", 1);
params.put("limit", 10);
params.put("type", "2"); // 查询轮播图
params.put("orderField", "create_date");
params.put("order", "desc");

IssuesPageData<IssuesDTO> result = issuesService.queryPageData(params);

// 不分页查询
Map<String, Object> params = new HashMap<>();
params.put("type", "1"); // 查询Logo

IssuesPageData<IssuesDTO> result = issuesService.queryPageData(params);
```

## 总结

通过这次优化，我们：
1. 使用了 MyBatis-Plus 标准的分页方式
2. 消除了代码重复
3. 提高了代码的可维护性和性能
4. 保持了功能的完整性

这样的代码更加规范、高效，也更符合企业级开发的标准。
