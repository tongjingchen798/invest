# MyBatis-Plus分页功能使用说明

## 概述

本文档详细说明了如何在 `renren-api` 模块中使用MyBatis-Plus的分页功能，包括配置、使用方法和最佳实践。

## MyBatis-Plus分页配置

### 1. 分页插件配置

MyBatis-Plus的分页功能通过分页插件实现，通常在配置类中进行配置：

```java
@Configuration
public class MybatisPlusConfig {
    
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
```

### 2. 分页参数常量

在 `Constant` 类中定义了分页相关的常量：

```java
public class Constant {
    public static final String PAGE = "page";
    public static final String LIMIT = "limit";
    public static final String ORDER_FIELD = "orderField";
    public static final String ORDER = "order";
    public static final String CREATE_DATE = "create_date";
}
```

## 分页实现方式

### 1. 使用 BaseServiceImpl 的分页方法

`BaseServiceImpl` 提供了标准的分页处理方法：

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

### 2. 手动创建分页对象

也可以手动创建分页对象：

```java
@Override
public PageData<ProjectEntity> getProjectPage(Map<String, Object> params) {
    // 获取分页参数
    long page = Long.parseLong(params.getOrDefault("page", "1").toString());
    long limit = Long.parseLong(params.getOrDefault("limit", "10").toString());
    
    // 创建分页对象
    IPage<ProjectEntity> pageParam = new Page<>(page, limit);
    
    // 构建查询条件
    QueryWrapper<ProjectEntity> queryWrapper = buildQueryWrapper(params);
    
    // 执行分页查询
    IPage<ProjectEntity> result = baseDao.selectPage(pageParam, queryWrapper);
    
    // 转换为PageData
    return new PageData<>(result.getRecords(), result.getTotal());
}
```

## 分页数据结构

### 1. IssuesPageData

专门为广告/图片管理设计的分页数据结构：

```java
@Data
@ApiModel(value = "分页数据")
public class IssuesPageData<T> implements Serializable {
    @ApiModelProperty(value = "列表数据")
    private List<T> list;

    @ApiModelProperty(value = "汇总")
    private Map<String, Object> sum;

    @ApiModelProperty(value = "总记录数")
    private Integer total;
}
```

### 2. PageData

通用的分页数据结构：

```java
@Data
public class PageData<T> implements Serializable {
    private List<T> list;
    private long total;
    private Map<String, Object> sum;
}
```

## 分页查询示例

### 1. 基本分页查询

```java
@GetMapping("page")
public Result<IssuesPageData<IssuesDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
    try {
        // 确保分页参数存在
        if (!params.containsKey("page")) {
            params.put("page", "1");
        }
        if (!params.containsKey("limit")) {
            params.put("limit", "10");
        }
        
        // 调用服务层分页查询
        IssuesPageData<IssuesDTO> pageData = issuesService.queryPageData(params);
        return new Result<IssuesPageData<IssuesDTO>>().ok(pageData);
    } catch (Exception e) {
        return new Result<IssuesPageData<IssuesDTO>>().error("查询失败：" + e.getMessage());
    }
}
```

### 2. 带条件的分页查询

```java
@GetMapping("page")
public Result<PageData<ProjectEntity>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
    try {
        PageData<ProjectEntity> pageData = projectService.getProjectPage(params);
        return new Result<PageData<ProjectEntity>>().ok(pageData);
    } catch (Exception e) {
        return new Result<PageData<ProjectEntity>>().error("查询失败：" + e.getMessage());
    }
}
```

## 查询条件构建

### 1. 使用 QueryWrapper

```java
private QueryWrapper<IssuesEntity> buildQueryWrapper(Map<String, Object> params) {
    String type = (String) params.get("type");

    QueryWrapper<IssuesEntity> queryWrapper = new QueryWrapper<IssuesEntity>();
    
    // 类型筛选
    if (StringUtils.isNotBlank(type)) {
        queryWrapper.eq("type", type);
    }
    
    // 只查询启用的数据
    queryWrapper.eq("status", 1);
    
    // 默认按排序字段和创建时间排序
    queryWrapper.orderByAsc("sort").orderByDesc(Constant.CREATE_DATE);

    return queryWrapper;
}
```

### 2. 动态查询条件

```java
private QueryWrapper<ProjectEntity> buildQueryWrapper(Map<String, Object> params) {
    QueryWrapper<ProjectEntity> queryWrapper = new QueryWrapper<>();
    
    // 项目名称模糊查询
    Object investNameObj = params.get("investName");
    if (investNameObj != null && StringUtils.isNotBlank(investNameObj.toString())) {
        queryWrapper.like("invest_name", investNameObj.toString());
    }
    
    // 项目状态查询
    Object statusObj = params.get("status");
    if (statusObj != null) {
        if (statusObj instanceof Integer) {
            queryWrapper.eq("status", statusObj);
        } else if (statusObj instanceof String && StringUtils.isNotBlank((String) statusObj)) {
            try {
                queryWrapper.eq("status", Integer.parseInt((String) statusObj));
            } catch (NumberFormatException e) {
                // 忽略无效的状态值
            }
        }
    }
    
    // 排序
    Object orderFieldObj = params.get("orderField");
    Object orderObj = params.get("order");
    if (orderFieldObj != null && StringUtils.isNotBlank(orderFieldObj.toString()) && 
        orderObj != null && StringUtils.isNotBlank(orderObj.toString())) {
        boolean isAsc = "asc".equalsIgnoreCase(orderObj.toString());
        queryWrapper.orderBy(true, isAsc, orderFieldObj.toString());
    } else {
        // 默认按创建时间倒序
        queryWrapper.orderByDesc("create_date");
    }
    
    return queryWrapper;
}
```

## 分页参数处理

### 1. 参数验证和默认值

```java
// 确保分页参数存在
if (!params.containsKey("page")) {
    params.put("page", "1");
}
if (!params.containsKey("limit")) {
    params.put("limit", "10");
}

// 参数类型转换
long page = Long.parseLong(params.getOrDefault("page", "1").toString());
long limit = Long.parseLong(params.getOrDefault("limit", "10").toString());
```

### 2. 参数类型安全处理

```java
// 安全的类型转换
Object statusObj = params.get("status");
if (statusObj != null) {
    if (statusObj instanceof Integer) {
        queryWrapper.eq("status", statusObj);
    } else if (statusObj instanceof String && StringUtils.isNotBlank((String) statusObj)) {
        try {
            queryWrapper.eq("status", Integer.parseInt((String) statusObj));
        } catch (NumberFormatException e) {
            // 忽略无效的状态值
        }
    }
}
```

## 排序功能

### 1. 动态排序

```java
// 排序
Object orderFieldObj = params.get("orderField");
Object orderObj = params.get("order");
if (orderFieldObj != null && StringUtils.isNotBlank(orderFieldObj.toString()) && 
    orderObj != null && StringUtils.isNotBlank(orderObj.toString())) {
    boolean isAsc = "asc".equalsIgnoreCase(orderObj.toString());
    queryWrapper.orderBy(true, isAsc, orderFieldObj.toString());
} else {
    // 默认按创建时间倒序
    queryWrapper.orderByDesc("create_date");
}
```

### 2. 多字段排序

```java
// 多字段排序
queryWrapper.orderByAsc("sort").orderByDesc("create_date");
```

## 性能优化建议

### 1. 分页大小限制

```java
// 限制每页最大记录数
long limit = Math.min(Long.parseLong(params.getOrDefault("limit", "10").toString()), 100L);
```

### 2. 索引优化

确保查询字段有适当的数据库索引：

```sql
-- 为常用查询字段创建索引
CREATE INDEX idx_type ON issues(type);
CREATE INDEX idx_status ON issues(status);
CREATE INDEX idx_create_date ON issues(create_date);
```

### 3. 查询条件优化

```java
// 避免使用 SELECT *
// 只查询需要的字段
queryWrapper.select("id", "title", "type", "status", "create_date");

// 使用 EXISTS 而不是 IN（当数据量大时）
queryWrapper.exists("SELECT 1 FROM other_table WHERE other_table.id = main_table.id");
```

## 错误处理

### 1. 异常捕获

```java
try {
    PageData<ProjectEntity> pageData = projectService.getProjectPage(params);
    return new Result<PageData<ProjectEntity>>().ok(pageData);
} catch (Exception e) {
    return new Result<PageData<ProjectEntity>>().error("查询失败：" + e.getMessage());
}
```

### 2. 参数验证

```java
// 验证分页参数
if (page < 1) {
    throw new IllegalArgumentException("页码必须大于0");
}
if (limit < 1 || limit > 100) {
    throw new IllegalArgumentException("每页记录数必须在1-100之间");
}
```

## 测试

### 1. 单元测试

```java
@Test
public void testGetAdvertisementPage() throws Exception {
    // 准备测试数据
    List<IssuesDTO> advertisementList = new ArrayList<>();
    IssuesDTO advertisement = new IssuesDTO();
    advertisement.setId(1L);
    advertisement.setTitle("首页Logo");
    advertisement.setType(1);
    advertisement.setStatus(1);
    advertisementList.add(advertisement);

    IssuesPageData<IssuesDTO> pageData = new IssuesPageData<>(advertisementList, 1);

    // Mock服务层返回
    when(issuesService.queryPageData(any(Map.class))).thenReturn(pageData);

    // 执行测试
    mockMvc.perform(get("/api/advertisement/page")
            .param("page", "1")
            .param("limit", "10")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(0))
            .andExpect(jsonPath("$.data.total").value(1));
}
```

### 2. 集成测试

```java
@SpringBootTest
@ActiveProfiles("test")
public class AdvertisementServiceIntegrationTest {

    @Autowired
    private IssuesService issuesService;

    @Test
    public void testQueryPageData() {
        Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("limit", 10);
        params.put("type", "1");

        IssuesPageData<IssuesDTO> result = issuesService.queryPageData(params);
        
        assertNotNull(result);
        assertNotNull(result.getList());
        assertTrue(result.getTotal() >= 0);
    }
}
```

## 最佳实践

### 1. 分页参数标准化

- 使用统一的参数名称：`page`、`limit`
- 页码从1开始，不是从0开始
- 限制每页最大记录数（建议100以内）

### 2. 查询条件构建

- 使用 `QueryWrapper` 构建动态查询条件
- 处理参数类型转换的异常情况
- 提供合理的默认排序规则

### 3. 性能考虑

- 为常用查询字段创建数据库索引
- 避免在分页查询中使用复杂的JOIN操作
- 考虑使用缓存减少数据库查询

### 4. 错误处理

- 捕获并处理所有可能的异常
- 提供有意义的错误信息
- 记录详细的错误日志

## 总结

MyBatis-Plus的分页功能提供了强大而灵活的分页查询能力。通过合理使用分页插件、构建查询条件和处理分页参数，可以轻松实现高效的分页查询功能。记住要处理好参数验证、异常情况和性能优化，确保分页查询的稳定性和效率。
