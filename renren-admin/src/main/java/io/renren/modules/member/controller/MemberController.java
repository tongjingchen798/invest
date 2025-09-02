package io.renren.modules.member.controller;

import io.renren.common.annotation.LogOperation;
import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.utils.Result;
import io.renren.modules.member.dto.*;
import io.renren.modules.member.service.MemberService;
import io.renren.modules.member.service.BlacklistService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员查询管理
 *
 * @author renren
 * @since 1.0.0
 */
@RestController
@RequestMapping("/user")
@Api(tags = "会员查询管理")
@Slf4j
public class MemberController {

    @Autowired
    private MemberService memberService;
    
    @Autowired
    private BlacklistService blacklistService;

    @GetMapping("page")
    @ApiOperation("分页查询会员信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = "agent", value = "代理下拉框", paramType = "query", required = false, dataType = "long"),
        @ApiImplicitParam(name = "balanceFlag", value = "可用余额筛选：有余额=1，无余额=0，查全部，不传参", paramType = "query", required = false, dataType = "int"),
        @ApiImplicitParam(name = "biaoqian", value = "标签筛选：传标签", paramType = "query", required = false, dataType = "string"),
        @ApiImplicitParam(name = "biaoqianFlag", value = "标签筛选：1 有 0 无 查全部，不传参", paramType = "query", required = false, dataType = "int"),
        @ApiImplicitParam(name = "channel", value = "渠道筛选：传渠道", paramType = "query", required = false, dataType = "string"),
        @ApiImplicitParam(name = "chargeFlag", value = "充值筛选：有充值=1，无充值=0，查全部，不传参", paramType = "query", required = false, dataType = "int"),
        @ApiImplicitParam(name = "endTime", value = "结束日期:时间戳", paramType = "query", required = false, dataType = "long"),
        @ApiImplicitParam(name = "liebian", value = "裂变筛选：是 =1，否 =0，查全部，不传参", paramType = "query", required = false, dataType = "int"),
        @ApiImplicitParam(name = "mobile", value = "用户账号", paramType = "query", required = false, dataType = "string"),
        @ApiImplicitParam(name = "order", value = "排序方式，可选值(asc、desc)", paramType = "query", required = false, dataType = "string"),
        @ApiImplicitParam(name = "orderField", value = "排序字段", paramType = "query", required = false, dataType = "string"),
        @ApiImplicitParam(name = "salesmanid", value = "业务员下拉框", paramType = "query", required = false, dataType = "long"),
        @ApiImplicitParam(name = "startTime", value = "开始日期:时间戳", paramType = "query", required = false, dataType = "string"),
        @ApiImplicitParam(name = "tzFlag", value = "投资筛选：有投资=1，无投资=0，查全部，不传参", paramType = "query", required = false, dataType = "int"),
        @ApiImplicitParam(name = "username", value = "用户姓名", paramType = "query", required = false, dataType = "string"),
        @ApiImplicitParam(name = "vip", value = "0-6 查全部，不传参", paramType = "query", required = false, dataType = "int"),
        @ApiImplicitParam(name = "viplr", value = "0-4,5,6 查全部，不传参", paramType = "query", required = false, dataType = "int"),
        @ApiImplicitParam(name = "withdrawFlag", value = "提现筛选：有提现=1，无提现=0，查全部，不传参", paramType = "query", required = false, dataType = "int")
    })
//    @RequiresPermissions("member:user:page")
    public Result<PageData<MemberInfoDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        try {
            // 调用自定义分页查询方法
            PageData<MemberInfoDTO> pageData = memberService.customMemberPage(params);

            return new Result<PageData<MemberInfoDTO>>().ok(pageData);

        } catch (Exception e) {
            return new Result<PageData<MemberInfoDTO>>().error("查询会员信息失败: " + e.getMessage());
        }
    }

    @GetMapping("settlement")
    @ApiOperation("结算报表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
        @ApiImplicitParam(name = "limit", value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
        @ApiImplicitParam(name = "agent", value = "代理下拉框", paramType = "query", required = false, dataType = "long"),
        @ApiImplicitParam(name = "endTime", value = "结束日期:时间戳", paramType = "query", required = false, dataType = "long"),
        @ApiImplicitParam(name = "order", value = "排序方式，可选值(asc、desc)", paramType = "query", required = false, dataType = "string"),
        @ApiImplicitParam(name = "orderField", value = "排序字段", paramType = "query", required = false, dataType = "string"),
        @ApiImplicitParam(name = "salesmanid", value = "业务员下拉框", paramType = "query", required = false, dataType = "long"),
        @ApiImplicitParam(name = "startTime", value = "开始日期:时间戳", paramType = "query", required = false, dataType = "long")
    })
//    @RequiresPermissions("member:user:settlement")
    public Result<PageData<SettlementReportDTO>> settlement(
            @RequestParam("page") Integer page,
            @RequestParam("limit") Integer limit,
            @RequestParam(value = "agent", required = false) Long agent,
            @RequestParam(value = "endTime", required = false) Long endTime,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "orderField", required = false) String orderField,
            @RequestParam(value = "salesmanid", required = false) Long salesmanid,
            @RequestParam(value = "startTime", required = false) Long startTime) {

        try {
            //TODO 还需要优化
            // 参数验证
            if (page == null || page < 1) {
                return new Result<PageData<SettlementReportDTO>>().error("页码必须大于0");
            }
            if (limit == null || limit < 1 || limit > 1000) {
                return new Result<PageData<SettlementReportDTO>>().error("每页记录数必须在1-1000之间");
            }

            // 调用服务查询结算报表数据
            PageData<SettlementReportDTO> pageData = memberService.getSettlementReport(
                page, limit, agent, endTime, order, orderField, salesmanid, startTime
            );

            return new Result<PageData<SettlementReportDTO>>().ok(pageData);

        } catch (Exception e) {
            return new Result<PageData<SettlementReportDTO>>().error("查询结算报表失败: " + e.getMessage());
        }
    }

    @GetMapping("getBiaoQianList")
    @ApiOperation("获取标签列表")
    public Result<java.util.List<String>> getBiaoQianList() {
        try {
            // 调用服务获取所有标签列表
            List<String> tagList = memberService.getBiaoQianList();
            return new Result<java.util.List<String>>().ok(tagList);
        } catch (Exception e) {
            return new Result<java.util.List<String>>().error("获取标签列表失败: " + e.getMessage());
        }
    }

    @GetMapping("getAgentList")
    @ApiOperation("获取代理下拉列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "agent", value = "代理ID", paramType = "query", dataType = "string",required = false),
        @ApiImplicitParam(name = "type", value = "0 总代 1 代理 2业务员", paramType = "query", required = true, dataType = "integer")
    })
    public Result<List<AgentDTO>> getAgentList(
            @RequestParam(value = "agent", required = false) String agent,
            @RequestParam("type") Integer type) {
        try {
            // 调用服务获取代理列表
            List<AgentDTO> agentList = memberService.getAgentList(agent, type);
            return new Result<List<AgentDTO>>().ok(agentList);

        } catch (Exception e) {
            log.error("获取代理列表失败，代理ID: {}, 类型: {}", agent, type, e);
            return new Result<List<AgentDTO>>().error("获取代理列表失败: " + e.getMessage());
        }
    }

    @PutMapping("fgz")
    @ApiOperation("付工资")
    @LogOperation("付工资")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", paramType = "query", required = true, dataType = "long"),
            @ApiImplicitParam(name = "balance", value = "工资金额（分）", paramType = "query", required = true, dataType = "long")
    })
//    @RequiresPermissions("sys:user:fgz")
    public Result paySalary(@RequestParam("id") Long userId, @RequestParam("balance") Long amount) {
        try {
            // 参数验证
            if (userId == null || userId <= 0) {
                return new Result<String>().error("用户ID不能为空");
            }
            if (amount == null || amount <= 0) {
                return new Result<String>().error("工资金额必须大于0");
            }
            // 调用服务发放工资
            return memberService.paySalary(userId, amount);

        } catch (Exception e) {
            log.error("工资发放异常，用户ID: {}, 金额: {} 分", userId, amount, e);
            return new Result().error("工资发放失败");
        }
    }


    @PutMapping("updateUserBq")
    @ApiOperation("设置用户标签")
    @LogOperation("设置用户标签")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", paramType = "query", required = true, dataType = "long"),
            @ApiImplicitParam(name = "biaoqian", value = "标签值", paramType = "query", required = true, dataType = "string"),
            @ApiImplicitParam(name = "type", value = "操作类型：1-设置标签，2-清除标签", paramType = "query", required = true, dataType = "int")
    })
//    @RequiresPermissions("member:user:updateBq")
    public Result updateUserBq(@RequestParam("id") Long userId, @RequestParam("biaoqian") String biaoqian, @RequestParam("type") Integer type) {
        try {
            // 参数验证
            if (userId == null || userId <= 0) {
                return new Result().error("用户ID不能为空");
            }
            if (biaoqian == null) {
                return new Result().error("标签值不能为空");
            }
            if (type == null || (type != 1 && type != 2)) {
                return new Result().error("操作类型必须为1(设置标签)或2(清除标签)");
            }
            
            // 调用服务修改用户标签
            Result result = memberService.updateUserBiaoqian(userId, biaoqian, type);
            return result;

        } catch (Exception e) {
            log.error("用户标签修改异常，用户ID: {}, 标签: {}, 操作类型: {}", userId, biaoqian, type, e);
            return new Result().error("标签修改失败: " + e.getMessage());
        }
    }

    @PutMapping("updateUserToAgent")
    @ApiOperation("修改用户业务员")
    @LogOperation("修改用户业务员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", paramType = "query", required = true, dataType = "long"),
            @ApiImplicitParam(name = "salesmanid", value = "业务员ID", paramType = "query", required = true, dataType = "long")
    })
//    @RequiresPermissions("member:user:updateToAgent")
    public Result updateUserToAgent(@RequestParam("id") Long userId, @RequestParam("salesmanid") Long salesmanid) {
        try {
            // 参数验证
            if (userId == null || userId <= 0) {
                return new Result().error("用户ID不能为空");
            }
            if (salesmanid == null || salesmanid <= 0) {
                return new Result().error("业务员ID不能为空");
            }

            // 调用服务修改用户业务员
            Result result = memberService.updateUserToAgent(userId, salesmanid);
            return result;

        } catch (Exception e) {
            log.error("用户业务员修改异常，用户ID: {}, 业务员ID: {}", userId, salesmanid, e);
            return new Result().error("业务员修改失败: " + e.getMessage());
        }
    }

    @PutMapping("updateUp")
    @ApiOperation("设置用户上级")
    @LogOperation("设置用户上级")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", paramType = "query", required = true, dataType = "long"),
            @ApiImplicitParam(name = "mobile", value = "上级用户手机号", paramType = "query", required = true, dataType = "string")
    })
//    @RequiresPermissions("member:user:updateUp")
    public Result updateUp(@RequestParam("id") Long userId, @RequestParam("mobile") String mobile) {
        try {
            // 参数验证
            if (userId == null || userId <= 0) {
                return new Result().error("用户ID不能为空");
            }
            if (mobile == null || mobile.trim().isEmpty()) {
                return new Result().error("上级用户手机号不能为空");
            }

            return memberService.updateUp(userId, mobile);

        } catch (Exception e) {
            log.error("设置用户上级异常，用户ID: {}, 上级手机号: {}", userId, mobile, e);
            return new Result().error("设置上级失败: " + e.getMessage());
        }
    }

    @PutMapping("updateUserName")
    @ApiOperation("修改用户真实姓名")
    @LogOperation("修改用户真实姓名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", paramType = "query", required = true, dataType = "long"),
            @ApiImplicitParam(name = "username", value = "用户真实姓名", paramType = "query", required = true, dataType = "string")
    })
//    @RequiresPermissions("member:user:updateUserName")
    public Result updateUserName(@RequestParam("id") Long userId, @RequestParam("username") String username) {
        try {
            // 参数验证
            if (userId == null || userId <= 0) {
                return new Result().error("用户ID不能为空");
            }
            if (username == null || username.trim().isEmpty()) {
                return new Result().error("用户姓名不能为空");
            }

            // 调用服务修改用户姓名
            return memberService.updateUserName(userId, username);

        } catch (Exception e) {
            log.error("修改用户姓名异常，用户ID: {}, 姓名: {}", userId, username, e);
            return new Result().error("姓名修改失败: " + e.getMessage());
        }
    }

    @PutMapping("addBalance")
    @ApiOperation("手工调整余额")
    @LogOperation("手工调整余额")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", paramType = "body", required = true, dataType = "string"),
            @ApiImplicitParam(name = "balance", value = "金额（分）", paramType = "body", required = true, dataType = "long"),
            @ApiImplicitParam(name = "balance_type", value = "操作类型：1-增加余额，2-减少余额", paramType = "body", required = true, dataType = "int"),
            @ApiImplicitParam(name = "remark", value = "备注", paramType = "body", required = false, dataType = "string")
    })
//    @RequiresPermissions("member:user:addBalance")
    public Result addBalance(@RequestBody Map<String, Object> params) {
        try {
            // 参数解析和验证
            Long userId = null;
            Long amount = null;
            Integer balanceType = null;
            String remark = null;
            
            // 解析用户ID
            if (params.get("id") != null) {
                try {
                    userId = Long.valueOf(params.get("id").toString());
                } catch (NumberFormatException e) {
                    return new Result().error("用户ID格式错误");
                }
            }
            
            // 解析金额
            if (params.get("balance") != null) {
                try {
                    amount = Long.valueOf(params.get("balance").toString());
                } catch (NumberFormatException e) {
                    return new Result().error("金额格式错误");
                }
            }
            
            // 解析操作类型
            if (params.get("balance_type") != null) {
                try {
                    balanceType = Integer.valueOf(params.get("balance_type").toString());
                } catch (NumberFormatException e) {
                    return new Result().error("操作类型格式错误");
                }
            }
            
            // 解析备注
            if (params.get("remark") != null) {
                remark = params.get("remark").toString();
            }
            
            // 参数验证
            if (userId == null || userId <= 0) {
                return new Result().error("用户ID不能为空");
            }
            if (amount == null || amount <= 0) {
                return new Result().error("金额必须大于0");
            }
            if (balanceType == null || (balanceType != 1 && balanceType != 2)) {
                return new Result().error("操作类型必须为1(增加余额)或2(减少余额)");
            }

            // 调用服务调整余额
            return memberService.addBalance(userId, amount, balanceType, remark);

        } catch (Exception e) {
            log.error("手工调整余额异常，参数: {}", params, e);
            return new Result().error("余额调整失败: " + e.getMessage());
        }
    }

    @PutMapping("freeBalance")
    @ApiOperation("操作冻结金额")
    @LogOperation("操作冻结金额")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", paramType = "body", required = true, dataType = "string"),
            @ApiImplicitParam(name = "balance", value = "金额（分）", paramType = "body", required = true, dataType = "long"),
            @ApiImplicitParam(name = "balance_type", value = "操作类型：1-冻结金额，2-解冻金额", paramType = "body", required = true, dataType = "int"),
            @ApiImplicitParam(name = "remark", value = "备注", paramType = "body", required = false, dataType = "string")
    })
//    @RequiresPermissions("member:user:freeBalance")
    public Result freeBalance(@RequestBody Map<String, Object> params) {
        try {
            // 参数解析和验证
            Long userId = null;
            Long amount = null;
            Integer balanceType = null;
            String remark = null;
            
            // 解析用户ID
            if (params.get("id") != null) {
                try {
                    userId = Long.valueOf(params.get("id").toString());
                } catch (NumberFormatException e) {
                    return new Result().error("用户ID格式错误");
                }
            }
            
            // 解析金额
            if (params.get("balance") != null) {
                try {
                    amount = Long.valueOf(params.get("balance").toString());
                } catch (NumberFormatException e) {
                    return new Result().error("金额格式错误");
                }
            }
            
            // 解析操作类型
            if (params.get("balance_type") != null) {
                try {
                    balanceType = Integer.valueOf(params.get("balance_type").toString());
                } catch (NumberFormatException e) {
                    return new Result().error("操作类型格式错误");
                }
            }
            
            // 解析备注
            if (params.get("remark") != null) {
                remark = params.get("remark").toString();
            }
            
            // 参数验证
            if (userId == null || userId <= 0) {
                return new Result().error("用户ID不能为空");
            }
            if (amount == null || amount <= 0) {
                return new Result().error("金额必须大于0");
            }
            if (balanceType == null || (balanceType != 1 && balanceType != 2)) {
                return new Result().error("操作类型必须为1(冻结金额)或2(解冻金额)");
            }

            // 调用服务操作冻结金额
            return memberService.freeBalance(userId, amount, balanceType, remark);

        } catch (Exception e) {
            log.error("操作冻结金额异常，参数: {}", params, e);
            return new Result().error("冻结金额操作失败: " + e.getMessage());
        }
    }

    @PutMapping("updatePws")
    @ApiOperation("修改登录密码")
    @LogOperation("修改登录密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", paramType = "query", required = true, dataType = "long"),
            @ApiImplicitParam(name = "password", value = "新密码", paramType = "query", required = true, dataType = "string")
    })
//    @RequiresPermissions("member:user:updatePws")
    public Result updatePws(@RequestParam("id") Long userId, @RequestParam("password") String password) {
        try {
            // 参数验证
            if (userId == null || userId <= 0) {
                return new Result().error("用户ID不能为空");
            }
            if (password == null || password.trim().isEmpty()) {
                return new Result().error("新密码不能为空");
            }

            // 调用服务修改用户密码
            Result result = memberService.updatePws(userId, password);
            return result;

        } catch (Exception e) {
            log.error("修改用户密码异常，用户ID: {}, 密码: {}", userId, password, e);
            return new Result().error("密码修改失败: " + e.getMessage());
        }
    }

    @PutMapping("updatestatus")
    @ApiOperation("用户启用禁用")
    @LogOperation("用户启用禁用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", paramType = "query", required = true, dataType = "long"),
            @ApiImplicitParam(name = "status", value = "状态：1-启用，0-禁用", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "biaoqian", value = "标签", paramType = "query", required = false, dataType = "string")
    })
//    @RequiresPermissions("member:user:updatestatus")
    public Result updatestatus(@RequestParam("id") Long userId, 
                              @RequestParam("status") Integer status,
                              @RequestParam(value = "biaoqian", required = false) String biaoqian) {
        try {
            // 参数验证
            if (userId == null || userId <= 0) {
                return new Result().error("用户ID不能为空");
            }
            if (status == null || (status != 0 && status != 1)) {
                return new Result().error("状态值必须为0(禁用)或1(启用)");
            }

            // 调用服务更新用户状态
            Result result = memberService.updatestatus(userId, status, biaoqian);
            return result;

        } catch (Exception e) {
            log.error("用户状态更新异常，用户ID: {}, 状态: {}, 标签: {}", userId, status, biaoqian, e);
            return new Result().error("状态更新失败: " + e.getMessage());
        }
    }

    @PutMapping("updatetzrewardWithdrawStatus")
    @ApiOperation("启用禁用佣金账户的提现")
    @LogOperation("启用禁用佣金账户的提现")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", paramType = "query", required = true, dataType = "long"),
            @ApiImplicitParam(name = "status", value = "状态：1-启用提现，0-禁用提现", paramType = "query", required = true, dataType = "int")
    })
//    @RequiresPermissions("member:user:updatetzrewardWithdrawStatus")
    public Result updatetzrewardWithdrawStatus(@RequestParam("id") Long userId, 
                                             @RequestParam("status") Integer status) {
        try {
            // 参数验证
            if (userId == null || userId <= 0) {
                return new Result().error("用户ID不能为空");
            }
            if (status == null || (status != 0 && status != 1)) {
                return new Result().error("状态值必须为0(禁用提现)或1(启用提现)");
            }

            // 调用服务更新佣金账户提现状态
            Result result = memberService.updatetzrewardWithdrawStatus(userId, status);
            return result;

        } catch (Exception e) {
            log.error("佣金账户提现状态更新异常，用户ID: {}, 状态: {}", userId, status, e);
            return new Result().error("提现状态更新失败: " + e.getMessage());
        }
    }

    @PutMapping("updatetzWithdrawStatus")
    @ApiOperation("启用禁用投资账户提现")
    @LogOperation("启用禁用投资账户提现")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", paramType = "query", required = true, dataType = "long"),
            @ApiImplicitParam(name = "status", value = "状态：1-启用提现，0-禁用提现", paramType = "query", required = true, dataType = "int")
    })
//    @RequiresPermissions("member:user:updatetzWithdrawStatus")
    public Result updatetzWithdrawStatus(@RequestParam("id") Long userId, 
                                       @RequestParam("status") Integer status) {
        try {
            // 参数验证
            if (userId == null || userId <= 0) {
                return new Result().error("用户ID不能为空");
            }
            if (status == null || (status != 0 && status != 1)) {
                return new Result().error("状态值必须为0(禁用提现)或1(启用提现)");
            }

            // 调用服务更新投资账户提现状态
            Result result = memberService.updatetzWithdrawStatus(userId, status);
            return result;

        } catch (Exception e) {
            log.error("投资账户提现状态更新异常，用户ID: {}, 状态: {}", userId, status, e);
            return new Result().error("提现状态更新失败: " + e.getMessage());
        }
    }

    @GetMapping("getAllBlackList")
    @ApiOperation("查询所有黑白名单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "mobile", value = "会员账号", paramType = "query", required = false, dataType = "string"),
            @ApiImplicitParam(name = "type", value = "类型：1-白名单，2-黑名单，不传查所有", paramType = "query", required = false, dataType = "string")
    })
//    @RequiresPermissions("member:user:getAllBlackList")
    public Result<PageData<BlacklistDTO>> getAllBlackList(
            @RequestParam("page") Integer page,
            @RequestParam("limit") Integer limit,
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "type", required = false) String type) {
        try {
            // 参数验证
            if (page == null || page < 1) {
                return new Result<PageData<BlacklistDTO>>().error("页码必须大于0");
            }
            if (limit == null || limit < 1 || limit > 1000) {
                return new Result<PageData<BlacklistDTO>>().error("每页记录数必须在1-1000之间");
            }

            // 调用黑白名单服务查询分页数据
            PageData<BlacklistDTO> pageData = blacklistService.getBlacklistPage(
                    page, limit, mobile, type
            );

            return new Result<PageData<BlacklistDTO>>().ok(pageData);

        } catch (Exception e) {
            log.error("查询黑白名单失败", e);
            return new Result<PageData<BlacklistDTO>>().error("查询黑白名单失败: " + e.getMessage());
        }
    }

    @GetMapping("2")
    @ApiOperation("裂变佣金")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "agent", value = "代理下拉框", paramType = "query", required = false, dataType = "long"),
            @ApiImplicitParam(name = "biaoqian", value = "标签筛选：传标签", paramType = "query", required = false, dataType = "string"),
            @ApiImplicitParam(name = "biaoqianFlag", value = "标签筛选：1-有，0-无，查全部，不传参", paramType = "query", required = false, dataType = "int"),
            @ApiImplicitParam(name = "cce3Flag", value = "CCE3返佣：1-有，0-无，查全部，不传参", paramType = "query", required = false, dataType = "int"),
            @ApiImplicitParam(name = "endTime", value = "结束日期时间戳", paramType = "query", required = false, dataType = "long"),
            @ApiImplicitParam(name = "gzFlag", value = "工资：1-有，0-无，查全部，不传参", paramType = "query", required = false, dataType = "int"),
            @ApiImplicitParam(name = "llFlag", value = "浏览：1-有，0-无，查全部，不传参", paramType = "query", required = false, dataType = "int"),
            @ApiImplicitParam(name = "mobile", value = "用户账号", paramType = "query", required = false, dataType = "string"),
            @ApiImplicitParam(name = "order", value = "排序方式，可选值(asc、desc)", paramType = "query", required = false, dataType = "string"),
            @ApiImplicitParam(name = "rewardFlag", value = "佣金余额：1-有，0-无，查全部，不传参", paramType = "query", required = false, dataType = "int"),
            @ApiImplicitParam(name = "salesmanid", value = "业务员下拉框", paramType = "query", required = false, dataType = "long"),
            @ApiImplicitParam(name = "startTime", value = "开始日期时间戳", paramType = "query", required = false, dataType = "long"),
            @ApiImplicitParam(name = "xjFlag", value = "下级：1-有，0-无，查全部，不传参", paramType = "query", required = false, dataType = "int"),
            @ApiImplicitParam(name = "ytrewardFlag", value = "已提佣金：1-有，0-无，查全部，不传参", paramType = "query", required = false, dataType = "int"),
            @ApiImplicitParam(name = "zcFlag", value = "注册：1-有，0-无，查全部，不传参", paramType = "query", required = false, dataType = "int")
    })
//    @RequiresPermissions("member:user:fissionReward")
    public Result<PageData<FissionRewardDTO>> getFissionReward(
            @RequestParam("page") Integer page,
            @RequestParam("limit") Integer limit,
            @RequestParam(value = "agent", required = false) Long agent,
            @RequestParam(value = "biaoqian", required = false) String biaoqian,
            @RequestParam(value = "biaoqianFlag", required = false) Integer biaoqianFlag,
            @RequestParam(value = "cce3Flag", required = false) Integer cce3Flag,
            @RequestParam(value = "endTime", required = false) Long endTime,
            @RequestParam(value = "gzFlag", required = false) Integer gzFlag,
            @RequestParam(value = "llFlag", required = false) Integer llFlag,
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "rewardFlag", required = false) Integer rewardFlag,
            @RequestParam(value = "salesmanid", required = false) Long salesmanid,
            @RequestParam(value = "startTime", required = false) Long startTime,
            @RequestParam(value = "xjFlag", required = false) Integer xjFlag,
            @RequestParam(value = "ytrewardFlag", required = false) Integer ytrewardFlag,
            @RequestParam(value = "zcFlag", required = false) Integer zcFlag) {
        try {
            // 参数验证
            if (page == null || page < 1) {
                return new Result<PageData<FissionRewardDTO>>().error("页码必须大于0");
            }
            if (limit == null || limit < 1 || limit > 1000) {
                return new Result<PageData<FissionRewardDTO>>().error("每页记录数必须在1-1000之间");
            }

            // 调用服务查询裂变佣金分页数据
            PageData<FissionRewardDTO> pageData = memberService.getFissionRewardPage(
                    page, limit, agent, biaoqian, biaoqianFlag, cce3Flag, endTime,
                    gzFlag, llFlag, mobile, order, rewardFlag, salesmanid, startTime,
                    xjFlag, ytrewardFlag, zcFlag
            );

            return new Result<PageData<FissionRewardDTO>>().ok(pageData);

        } catch (Exception e) {
            log.error("查询裂变佣金失败", e);
            return new Result<PageData<FissionRewardDTO>>().error("查询裂变佣金失败: " + e.getMessage());
        }
    }

    @PostMapping("addblack")
    @ApiOperation("新增黑白名单")
    @LogOperation("新增黑白名单")
//    @RequiresPermissions("member:user:addblack")
    public Result addblack(@RequestBody BlacklistDTO dto) {
        try {
            // 参数验证
            if (dto.getUserId() == null || dto.getUserId() <= 0) {
                return new Result().error("用户ID不能为空");
            }
            if (dto.getMobile() == null || dto.getMobile().trim().isEmpty()) {
                return new Result().error("会员账号不能为空");
            }
            if (dto.getType() == null || (dto.getType() != 1 && dto.getType() != 2)) {
                return new Result().error("类型必须为1(白名单)或2(黑名单)");
            }

            // 调用服务添加用户到黑白名单
            return blacklistService.addToBlacklist(dto.getUserId(), dto.getMobile().trim(), dto.getType());

        } catch (Exception e) {
            log.error("新增黑白名单失败，用户ID: {}, 类型: {}", dto.getUserId(), dto.getType(), e);
            return new Result().error("新增黑白名单失败: " + e.getMessage());
        }
    }

    @DeleteMapping("deleteblack")
    @ApiOperation("删除黑白名单")
    @LogOperation("删除黑白名单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "黑白名单记录ID", paramType = "query", required = true, dataType = "long"),
            @ApiImplicitParam(name = "loginUserId", value = "登录用户ID", paramType = "header", required = true, dataType = "long")
    })
//    @RequiresPermissions("member:user:deleteblack")
    public Result deleteblack(@RequestParam("id") Long id) {
        try {
            // 参数验证
            if (id == null || id <= 0) {
                return new Result().error("记录ID不能为空");
            }

            // 调用服务删除黑白名单记录
            return blacklistService.removeFromBlacklist(id);

        } catch (Exception e) {
            log.error("删除黑白名单失败，记录ID: {}", id, e);
            return new Result().error("删除黑白名单失败: " + e.getMessage());
        }
    }

    @GetMapping("amounttobecashed")
    @ApiOperation("即将兑付金额")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "agent", value = "代理下拉框", paramType = "query", required = false, dataType = "long"),
            @ApiImplicitParam(name = "salesmanid", value = "业务员下拉框", paramType = "query", required = false, dataType = "long"),
            @ApiImplicitParam(name = "startTime", value = "开始日期时间戳", paramType = "query", required = false, dataType = "long"),
            @ApiImplicitParam(name = "endTime", value = "结束日期时间戳", paramType = "query", required = false, dataType = "long"),
            @ApiImplicitParam(name = "order", value = "排序方式，可选值(asc、desc)", paramType = "query", required = false, dataType = "string"),
            @ApiImplicitParam(name = "orderField", value = "排序字段", paramType = "query", required = false, dataType = "string")
    })
//    @RequiresPermissions("member:user:amounttobecashed")
    public Result<PageData<AmountToBeCashedDTO>> getAmountToBeCashed(
            @RequestParam("page") Integer page,
            @RequestParam("limit") Integer limit,
            @RequestParam(value = "agent", required = false) Long agent,
            @RequestParam(value = "salesmanid", required = false) Long salesmanid,
            @RequestParam(value = "startTime", required = false) Long startTime,
            @RequestParam(value = "endTime", required = false) Long endTime,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "orderField", required = false) String orderField) {
        try {
            // 参数验证
            if (page == null || page < 1) {
                return new Result<PageData<AmountToBeCashedDTO>>().error("页码必须大于0");
            }
            if (limit == null || limit < 1 || limit > 1000) {
                return new Result<PageData<AmountToBeCashedDTO>>().error("每页记录数必须在1-1000之间");
            }

            // 调用服务查询即将兑付金额分页数据
            PageData<AmountToBeCashedDTO> pageData = memberService.getAmountToBeCashedPage(
                    page, limit, agent, salesmanid, startTime, endTime, order, orderField
            );

            return new Result<PageData<AmountToBeCashedDTO>>().ok(pageData);

        } catch (Exception e) {
            log.error("查询即将兑付金额失败", e);
            return new Result<PageData<AmountToBeCashedDTO>>().error("查询即将兑付金额失败: " + e.getMessage());
        }
    }
}
