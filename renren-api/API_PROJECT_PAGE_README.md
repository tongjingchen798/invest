# API项目分页查询接口说明文档

## 概述

本文档详细说明了 `renren-api` 模块中项目分页查询API的使用方法，包括接口地址、参数说明、返回格式等。

## 接口信息

### 基本信息
- **接口地址**: `GET /api/project/page`
- **接口名称**: 投资项目数据分页查询
- **请求方式**: GET
- **数据格式**: `application/x-www-form-urlencoded`
- **响应格式**: JSON

### 接口描述
根据分页参数查询投资项目列表，返回分页数据和项目详细信息。

## 请求参数

### 分页参数

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| page | int | 是 | 当前页码，从1开始 | 1 |
| limit | int | 是 | 每页显示记录数 | 10 |

## 响应格式

### 成功响应

```json
{
    "code": 0,
    "msg": "",
    "data": {
        "total": 1,
        "list": [
            {
                "investId": 1,
                "investName": "测试项目",
                "abbreviation": "",
                "status": 1,
                "investRepeat": 0,
                "projectType": 1,
                "cycleType": 2,
                "scaleAmount": 100000,
                "cycle": 30,
                "conversion": "2.5",
                "principalProfit": 2500,
                "totalProfit": 75000,
                "totalCost": 175000,
                "typeId": 0,
                "img": "",
                "couponId": 0,
                "couponName": "",
                "projectDescribe": "",
                "sort": 0,
                "vip": 0,
                "hour": 0,
                "expire": "",
                "sjDate": "",
                "xjDate": "",
                "memberRegistrationTime": 0,
                "registerStartDate": "",
                "registerEndDate": "",
                "returnPrincipal": 0,
                "returnTo": 0,
                "returnRatio": 0,
                "returnProject": 0,
                "returnToSup": 0,
                "returnRatioSup": 0,
                "returnProjectTo": 0,
                "returnProjectToSup": 0,
                "returnInvest": 0,
                "returnInvestName": "",
                "ret": 0,
                "marketDate": "",
                "rushWeek": 0,
                "rushHour": 0,
                "rushMinute": 0,
                "disFlag": 0,
                "discount": "",
                "discountStart": "",
                "discountEnd": "",
                "discountNum": 0,
                "discountVip": 0,
                "joinNum": 0,
                "groupLeaderRebate": 0,
                "groupMemberRebate": 0,
                "groupTime": 0,
                "rushbuyFalg": 0,
                "rushbuyStart": "",
                "rushbuyEnd": "",
                "rushbuyNum": 0,
                "rushbuyRemainNum": 0,
                "rushbuyStartTime": 0,
                "rushbuyEndTime": 0,
                "taskLimitTime": "",
                "retTo": 0,
                "returnWheel": 0,
                "returnWheelTo": 0,
                "returnWheelToSup": 0
            }
        ],
        "sum": {}
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

## 响应字段说明

### 响应结构

| 字段名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应状态码，0表示成功，其他值表示失败 |
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
| investId | Long | 项目ID |
| investName | String | 项目名称 |
| abbreviation | String | 项目简称 |
| status | Integer | 项目状态（0:下架,1:上架,2:删除） |
| investRepeat | Integer | 可买台数 |
| projectType | Integer | 项目类型（1:固定金额投资,2:众筹） |
| cycleType | Integer | 回款方式（1:到期返还,2:每日返利,3:不返本金,4:复利产品,5:阶梯日益,6:拼团） |
| scaleAmount | Long | 项目规模金额(个人购买) |
| cycle | Integer | 周期(天) |
| conversion | String | 日收益率(%) |
| principalProfit | Long | 每日收益，（每日返还才有） |
| totalProfit | Long | 总收益 = 每日收益 * 周期 |
| totalCost | Long | 本金+收益 = 投资金额 + 每日收益 * 周期 |
| typeId | Long | 分类ID |
| img | String | 设备图片 |
| couponId | Integer | 赠送优惠券id |
| couponName | String | 优惠券名称 |
| projectDescribe | String | 项目描述 |
| sort | Integer | 排序 |
| vip | Integer | VIP等级要求 |
| hour | Integer | 限时抢购几个小时 |
| expire | String | 抢购结束时间，从上架那一刻算，上架的时间加上抢购时间 |
| sjDate | String | 指定上架日期 |
| xjDate | String | 指定下架日期 |
| memberRegistrationTime | Integer | 会员注册时间 |
| registerStartDate | String | 注册起始时间 |
| registerEndDate | String | 注册结束时间 |
| returnPrincipal | Integer | 是否立返本金 |
| returnTo | Integer | 返还到谁 |
| returnRatio | Number | 返还比例 |
| returnProject | Integer | 是否立返项目 |
| returnToSup | Integer | 返还设备ID |
| returnRatioSup | Number | 上级返还比例 |
| returnProjectTo | Integer | 返还项目给谁 |
| returnProjectToSup | Integer | 返还项目给上级 |
| returnInvest | Integer | 返还投资 |
| returnInvestName | String | 返还投资名称 |
| ret | Integer | 返回值 |
| marketDate | String | 二级市场开放时间 |
| rushWeek | Integer | 一级市场开放周几 |
| rushHour | Integer | 一级市场开放时间 |
| rushMinute | Integer | 一级市场可抢购时间 |
| disFlag | Integer | 折扣标志 |
| discount | String | 折扣信息 |
| discountStart | String | 折扣开始时间 |
| discountEnd | String | 折扣结束时间 |
| discountNum | Integer | 折扣数量 |
| discountVip | Integer | 折扣VIP等级 |
| joinNum | Integer | 参与数量 |
| groupLeaderRebate | Integer | 团长返利 |
| groupMemberRebate | Integer | 团员返利 |
| groupTime | Integer | 团购时间 |
| rushbuyFalg | Integer | 是否限时抢购 1是，0不是 |
| rushbuyStart | String | 限时抢购开始时间 |
| rushbuyEnd | String | 限时抢购结束时间 |
| rushbuyNum | Integer | 限时可抢份数 |
| rushbuyRemainNum | Integer | 限时可抢剩余份数 |
| rushbuyStartTime | Integer | 限时抢购开始时间戳 |
| rushbuyEndTime | Integer | 限时抢购结束时间戳 |
| taskLimitTime | String | 任务限制时间 |
| retTo | Integer | 返还给谁 |
| returnWheel | Integer | 是否返还转盘次数 |
| returnWheelTo | Integer | 返还转盘次数给本人 |
| returnWheelToSup | Integer | 返还转盘次数给上级 |

## 使用示例

### JavaScript/TypeScript

```typescript
// 基本分页查询
async function getProjectPage(page: number = 1, limit: number = 10) {
    try {
        const response = await fetch(`/api/project/page?page=${page}&limit=${limit}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        });
        
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

// 使用示例
async function example() {
    try {
        // 查询第一页，每页10条
        const pageData = await getProjectPage(1, 10);
        console.log('总记录数:', pageData.total);
        console.log('当前页记录数:', pageData.list.length);
        
        // 遍历项目列表
        pageData.list.forEach(project => {
            console.log(`项目: ${project.investName}, 状态: ${project.status}, 类型: ${project.projectType}`);
        });
        
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
    
    @GetMapping("/page")
    public Result<PageData<ProjectEntity>> getProjectPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit) {
        
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("page", page);
            params.put("limit", limit);
            
            PageData<ProjectEntity> pageData = projectService.getProjectPage(params);
            return new Result<PageData<ProjectEntity>>().ok(pageData);
            
        } catch (Exception e) {
            return new Result<PageData<ProjectEntity>>().error("查询失败：" + e.getMessage());
        }
    }
}
```

### cURL

```bash
# 基本分页查询
curl -X GET "http://localhost:8080/api/project/page?page=1&limit=10" \
  -H "Content-Type: application/x-www-form-urlencoded"

# 查询第二页，每页5条
curl -X GET "http://localhost:8080/api/project/page?page=2&limit=5" \
  -H "Content-Type: application/x-www-form-urlencoded"
```

## 注意事项

### 1. 分页参数
- `page` 参数从1开始，不是从0开始
- `limit` 参数建议不要设置过大，建议最大值为100

### 2. 数据格式
- 金额字段（如scaleAmount、principalProfit等）以分为单位存储
- 日期字段使用字符串格式

### 3. 错误处理
- 接口返回的错误信息需要在前端进行适当的处理和显示
- 网络异常等错误需要在前端进行重试或提示用户

### 4. 性能考虑
- 建议合理设置每页记录数，避免一次性查询过多数据
- 可以根据需要添加缓存机制

## 扩展功能

### 1. 条件筛选
- 可以扩展支持项目名称、状态、类型等条件筛选
- 支持模糊查询和精确匹配

### 2. 排序功能
- 可以扩展支持按创建时间、排序字段等排序
- 支持升序和降序排列

### 3. 汇总统计
- 可以扩展返回汇总数据，如总投资金额、平均收益率等
- 汇总数据可以按项目类型、状态等维度进行统计

## 测试

项目包含了完整的测试用例，可以通过以下命令运行测试：

```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=ApiProjectControllerTest

# 运行特定测试方法
mvn test -Dtest=ApiProjectControllerTest#testGetProjectPage
```

## 更新日志

| 版本 | 日期 | 更新内容 |
|------|------|----------|
| 1.0.0 | 2024-01-01 | 初始版本，实现基本分页查询功能 |
| 1.1.0 | 2024-01-01 | 完善错误处理和参数验证 |
| 1.2.0 | 2024-01-01 | 添加完整的测试用例 |
