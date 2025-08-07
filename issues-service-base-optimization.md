# Issues Service 基于 BaseServiceImpl 的分页优化

## 优化说明

本次优化将 `IssuesServiceImpl` 改为继承 `BaseServiceImpl`，并使用项目中标准的分页处理方式，提高代码的一致性和可维护性。

## 主要改进

### 1. 继承 BaseServiceImpl

```java
// 优化前
public class IssuesServiceImpl extends ServiceImpl<IssuesDao, IssuesEntity> implements IssuesService

// 优化后
public class IssuesServiceImpl extends BaseServiceImpl<IssuesDao, IssuesEntity> implements IssuesService
```

### 2. 使用标准分页处理

```java
// 优化前：手动处理分页参数
Integer page = (Integer) params.get("page");
Integer limit = (Integer) params.get("limit");
Page<IssuesEntity> pageParam = new Page<>(page, limit);
IPage<IssuesEntity> pageResult = this.page(pageParam, queryWrapper);

// 优化后：使用 BaseServiceImpl 标准分页
IPage<IssuesEntity> pageResult = baseDao.selectPage(
    getPage(params, Constant.CREATE_DATE, false),
    queryWrapper
);
```

### 3. 使用 Constant 常量

```java
// 优化前：硬编码字符串
params.put("page", page);
params.put("limit", limit);
params.put("order", order);
params.put("orderField", orderField);

// 优化后：使用常量
params.put(Constant.PAGE, page);
params.put(Constant.LIMIT, limit);
params.put(Constant.ORDER, order);
params.put(Constant.ORDER_FIELD, orderField);
```

### 4. 简化查询条件构建

```java
// 优化前：手动处理排序
if (StringUtils.isNotBlank(orderField)) {
    if ("desc".equalsIgnoreCase(order)) {
        queryWrapper.orderByDesc(orderField);
    } else {
        queryWrapper.orderByAsc(orderField);
    }
} else {
    queryWrapper.orderByAsc("sort").orderByDesc("create_date");
}

// 优化后：使用 BaseServiceImpl 的排序处理
// 排序逻辑由 getPage() 方法统一处理
queryWrapper.orderByAsc("sort").orderByDesc(Constant.CREATE_DATE);
```

## 优化效果

### 1. 代码一致性
- 与项目中其他服务保持一致的代码风格
- 使用统一的分页处理方式
- 遵循项目的编码规范

### 2. 可维护性提升
- 减少重复代码
- 使用标准的分页处理逻辑
- 统一的参数命名规范

### 3. 功能增强
- 自动处理分页参数验证
- 统一的排序逻辑处理
- 更好的错误处理机制

### 4. 性能优化
- 使用 `BaseServiceImpl` 优化的分页查询
- 减少不必要的参数处理
- 更高效的数据库查询

## 使用示例

### 控制器参数映射

```java
@GetMapping("page")
@ApiOperation("分页查询广告/图片")
public Result<IssuesPageData<IssuesDTO>> page(
        @ApiParam(value = "每页显示记录数", required = true) @RequestParam(Constant.LIMIT) Integer limit,
        @ApiParam(value = "当前页码，从1开始", required = true) @RequestParam(Constant.PAGE) Integer page,
        @ApiParam(value = "排序方式，可选值(asc、desc)") @RequestParam(value = Constant.ORDER, required = false) String order,
        @ApiParam(value = "排序字段") @RequestParam(value = Constant.ORDER_FIELD, required = false) String orderField,
        @ApiParam(value = "广告类型 1=LOG,2=轮播图，3=个人中心 4=弹窗广告") @RequestParam(required = false) String type) {
    
    Map<String, Object> params = new HashMap<>();
    params.put(Constant.PAGE, page);
    params.put(Constant.LIMIT, limit);
    params.put(Constant.ORDER, order);
    params.put(Constant.ORDER_FIELD, orderField);
    params.put("type", type);

    IssuesPageData<IssuesDTO> pageData = issuesService.queryPageData(params);
    
    return new Result<IssuesPageData<IssuesDTO>>().ok(pageData);
}
```

### 服务层分页处理

```java
@Override
public IssuesPageData<IssuesDTO> queryPageData(Map<String, Object> params) {
    // 构建查询条件
    QueryWrapper<IssuesEntity> queryWrapper = buildQueryWrapper(params);
    
    // 使用 BaseServiceImpl 的标准分页处理
    IPage<IssuesEntity> pageResult = baseDao.selectPage(
        getPage(params, Constant.CREATE_DATE, false),
        queryWrapper
    );
    
    // 转换为DTO
    List<IssuesDTO> dtoList = convertToDto(pageResult.getRecords());
    
    return new IssuesPageData<>(dtoList, (int) pageResult.getTotal());
}
```

## 总结

通过这次优化，我们：

1. **统一了代码风格**：与项目中其他服务保持一致
2. **提高了可维护性**：使用标准的分页处理方式
3. **增强了功能**：自动处理分页和排序逻辑
4. **优化了性能**：使用更高效的分页查询
5. **遵循了规范**：使用项目定义的常量和方法

这样的代码更加规范、高效，也更符合项目的整体架构设计。
