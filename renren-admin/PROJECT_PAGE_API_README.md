# 项目分页查询API说明文档

## 概述

本文档详细说明了项目分页查询API的使用方法，包括接口地址、参数说明、返回格式等。

## 接口信息

### 基本信息
- **接口地址**: `GET /investProject/page`
- **接口名称**: 分页查询项目
- **请求方式**: GET
- **数据格式**: JSON

### 接口描述
根据查询条件分页查询投资项目列表，支持多种筛选条件和排序方式。

## 请求参数

### 分页参数

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| page | int | 是 | 当前页码，从1开始 | 1 |
| limit | int | 是 | 每页显示记录数 | 10 |

### 查询参数

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| investName | String | 否 | 项目名称（模糊查询） | "Happy Weekend" |
| status | String | 否 | 项目状态 | "1" |
| projectType | String | 否 | 项目类型 | "0" |
| cycleType | String | 否 | 周期类型 | "2" |

### 排序参数

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| orderField | String | 否 | 排序字段 | "create_date" |
| order | String | 否 | 排序方式（asc/desc） | "desc" |

### 参数说明

#### 项目状态 (status)
- `0`: 下架
- `1`: 上架
- `2`: 删除

#### 项目类型 (projectType)
- `0`: 默认类型
- `1`: 固定金额投资
- `2`: 众筹

#### 周期类型 (cycleType)
- `1`: 到期返还
- `2`: 每日返利
- `3`: 不返本金
- `4`: 复利产品
- `5`: 阶梯日益
- `6`: 拼团

## 请求示例

### 基本分页查询
```
GET /investProject/page?page=1&limit=10
```

### 带条件查询
```
GET /investProject/page?page=1&limit=10&investName=Happy&status=1&projectType=0
```

### 带排序查询
```
GET /investProject/page?page=1&limit=10&orderField=create_date&order=desc
```

### 完整查询示例
```
GET /investProject/page?page=1&limit=10&investName=Happy&status=1&projectType=0&cycleType=2&orderField=sort&order=desc
```

## 返回格式

### 成功响应

```json
{
    "code": 0,
    "msg": "success",
    "data": {
        "total": 1,
        "list": [
            {
                "investId": "1752821238754",
                "investName": "Happy Weekend",
                "abbreviation": "Happy Weekend 2",
                "status": 1,
                "investRepeat": 1,
                "projectType": 0,
                "cycleType": 2,
                "scaleAmount": "200000",
                "cycle": 15,
                "conversion": "2",
                "principalProfit": "4000",
                "totalProfit": "60000",
                "totalCost": "260000",
                "typeId": "1677040832376",
                "img": "http://8.216.132.154/images/tools/610bcad6-1eb7-4293-af18-3fdcbd129fcb.png",
                "couponId": null,
                "couponName": null,
                "projectDescribe": "",
                "sort": -10000,
                "vip": 0,
                "hour": 0,
                "expire": null,
                "sjDate": null,
                "xjDate": null,
                "memberRegistrationTime": 1,
                "registerStartDate": null,
                "registerEndDate": null,
                "returnPrincipal": -1,
                "returnTo": -1,
                "returnRatio": null,
                "returnProject": -1,
                "returnToSup": -1,
                "returnRatioSup": null,
                "returnProjectTo": -1,
                "returnProjectToSup": -1,
                "returnInvest": "1752821238754",
                "returnInvestName": "Happy Weekend",
                "ret": -1,
                "marketDate": null,
                "rushWeek": null,
                "rushHour": null,
                "rushMinute": null,
                "disFlag": null,
                "discount": null,
                "discountStart": null,
                "discountEnd": null,
                "discountNum": null,
                "discountVip": null,
                "joinNum": null,
                "groupLeaderRebate": null,
                "groupMemberRebate": null,
                "groupTime": null,
                "rushbuyFalg": null,
                "rushbuyStart": null,
                "rushbuyEnd": null,
                "rushbuyNum": null,
                "projectExplain": "test\ntest1"
            }
        ],
        "sum": null
    }
}
```

### 错误响应

```json
{
    "code": 500,
    "msg": "查询失败：具体错误信息",
    "data": null
}
```

## 返回字段说明

### 响应结构

| 字段名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码，0表示成功 |
| msg | String | 响应消息 |
| data | Object | 响应数据 |

### 分页数据结构

| 字段名 | 类型 | 说明 |
|--------|------|------|
| total | int | 总记录数 |
| list | Array | 当前页数据列表 |
| sum | Object | 汇总数据（暂未实现） |

### 项目字段说明

| 字段名 | 类型 | 说明 |
|--------|------|------|
| investId | String | 项目ID |
| investName | String | 项目名称 |
| abbreviation | String | 项目简称 |
| status | Integer | 项目状态 |
| investRepeat | Integer | 可买台数 |
| projectType | Integer | 项目类型 |
| cycleType | Integer | 周期类型 |
| scaleAmount | String | 项目规模金额 |
| cycle | Integer | 投资周期(天) |
| conversion | String | 日收益率(%) |
| principalProfit | String | 每日收益 |
| totalProfit | String | 总收益 |
| totalCost | String | 本金+收益 |
| typeId | String | 分类ID |
| img | String | 项目图片地址 |
| sort | Integer | 排序权重 |
| vip | Integer | VIP等级要求 |
| projectExplain | String | 项目说明 |

## 使用示例

### JavaScript/TypeScript

```typescript
// 基本分页查询
async function getProjects(page: number = 1, limit: number = 10) {
    try {
        const response = await fetch(`/investProject/page?page=${page}&limit=${limit}`);
        const result = await response.json();
        
        if (result.code === 0) {
            return result.data;
        } else {
            throw new Error(result.msg);
        }
    } catch (error) {
        console.error('查询项目失败:', error);
        throw error;
    }
}

// 带条件查询
async function searchProjects(params: {
    page?: number;
    limit?: number;
    investName?: string;
    status?: string;
    projectType?: string;
    cycleType?: string;
    orderField?: string;
    order?: string;
}) {
    const queryParams = new URLSearchParams();
    
    Object.entries(params).forEach(([key, value]) => {
        if (value !== undefined && value !== null) {
            queryParams.append(key, value.toString());
        }
    });
    
    try {
        const response = await fetch(`/investProject/page?${queryParams.toString()}`);
        const result = await response.json();
        
        if (result.code === 0) {
            return result.data;
        } else {
            throw new Error(result.msg);
        }
    } catch (error) {
        console.error('搜索项目失败:', error);
        throw error;
    }
}

// 使用示例
async function example() {
    try {
        // 查询第一页，每页10条
        const page1 = await getProjects(1, 10);
        console.log('第一页数据:', page1);
        
        // 搜索包含"Happy"的上架项目
        const searchResult = await searchProjects({
            page: 1,
            limit: 10,
            investName: 'Happy',
            status: '1',
            orderField: 'create_date',
            order: 'desc'
        });
        console.log('搜索结果:', searchResult);
        
    } catch (error) {
        console.error('操作失败:', error);
    }
}
```

### Java

```java
@RestController
@RequestMapping("/api/project")
public class ProjectApiController {
    
    @Autowired
    private ProjectService projectService;
    
    @GetMapping("/list")
    public Result<PageData<ProjectEntity>> getProjectList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) String investName,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String projectType,
            @RequestParam(required = false) String cycleType,
            @RequestParam(required = false) String orderField,
            @RequestParam(required = false) String order) {
        
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("page", page);
            params.put("limit", limit);
            params.put("investName", investName);
            params.put("status", status);
            params.put("projectType", projectType);
            params.put("cycleType", cycleType);
            params.put("orderField", orderField);
            params.put("order", order);
            
            PageData<ProjectEntity> pageData = projectService.getProjectPage(params);
            return new Result<PageData<ProjectEntity>>().ok(pageData);
            
        } catch (Exception e) {
            return new Result<PageData<ProjectEntity>>().error("查询失败：" + e.getMessage());
        }
    }
}
```

## 注意事项

### 1. 分页参数
- `page` 参数从1开始，不是从0开始
- `limit` 参数建议不要设置过大，建议最大值为100

### 2. 查询性能
- 项目名称模糊查询会影响查询性能，建议添加适当的索引
- 排序字段建议使用有索引的字段

### 3. 数据格式
- 金额字段（如scaleAmount、principalProfit等）以分为单位存储
- 日期字段使用标准格式

### 4. 错误处理
- 接口返回的错误信息需要在前端进行适当的处理和显示
- 网络异常等错误需要在前端进行重试或提示用户

## 扩展功能

### 1. 汇总统计
- 可以扩展返回汇总数据，如总投资金额、平均收益率等
- 汇总数据可以按项目类型、状态等维度进行统计

### 2. 缓存优化
- 对于不经常变化的数据，可以添加缓存机制
- 缓存时间可以根据业务需求进行配置

### 3. 权限控制
- 可以根据用户角色限制查询的项目范围
- 敏感项目信息需要进行权限验证

## 更新日志

| 版本 | 日期 | 更新内容 |
|------|------|----------|
| 1.0.0 | 2024-01-01 | 初始版本，实现基本分页查询功能 |
| 1.1.0 | 2024-01-01 | 新增多种查询条件和排序功能 |
| 1.2.0 | 2024-01-01 | 完善错误处理和参数验证 |
