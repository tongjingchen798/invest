package io.renren.modules.member.controller;

import io.renren.common.annotation.LogOperation;
import io.renren.common.page.PageData;
import io.renren.common.utils.Result;
import io.renren.common.validator.ValidatorUtils;
import io.renren.common.validator.group.DefaultGroup;
import io.renren.common.validator.group.UpdateGroup;
import io.renren.modules.member.dto.MemberInfoDTO;
import io.renren.modules.member.dto.SettlementReportDTO;
import io.renren.modules.member.service.MemberService;
import io.renren.modules.sys.dto.SysDeptDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

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

    @GetMapping("page")
    @ApiOperation("分页查询会员信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
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
    public Result<PageData<MemberInfoDTO>> page(
            @RequestParam("page") Integer page,
            @RequestParam("limit") Integer limit,
            @RequestParam(value = "agent", required = false) Long agent,
            @RequestParam(value = "balanceFlag", required = false) Integer balanceFlag,
            @RequestParam(value = "biaoqian", required = false) String biaoqian,
            @RequestParam(value = "biaoqianFlag", required = false) Integer biaoqianFlag,
            @RequestParam(value = "channel", required = false) String channel,
            @RequestParam(value = "chargeFlag", required = false) Integer chargeFlag,
            @RequestParam(value = "endTime", required = false) Long endTime,
            @RequestParam(value = "liebian", required = false) Integer liebian,
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "orderField", required = false) String orderField,
            @RequestParam(value = "salesmanid", required = false) Long salesmanid,
            @RequestParam(value = "startTime", required = false) Long startTime,
            @RequestParam(value = "tzFlag", required = false) Integer tzFlag,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "vip", required = false) Integer vip,
            @RequestParam(value = "viplr", required = false) Integer viplr,
            @RequestParam(value = "withdrawFlag", required = false) Integer withdrawFlag) {

        try {
            // 参数验证
            if (page == null || page < 1) {
                return new Result<PageData<MemberInfoDTO>>().error("页码必须大于0");
            }
            if (limit == null || limit < 1 || limit > 1000) {
                return new Result<PageData<MemberInfoDTO>>().error("每页记录数必须在1-1000之间");
            }

            // 调用服务查询分页数据
            PageData<MemberInfoDTO> pageData = memberService.getMemberPage(
                    page, limit, agent, balanceFlag, biaoqian, biaoqianFlag, channel, chargeFlag,
                    endTime, liebian, mobile, order, orderField, salesmanid, startTime, tzFlag,
                    username, vip, viplr, withdrawFlag
            );

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
            @ApiImplicitParam(name = "biaoqian", value = "标签值", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "type", value = "操作类型：1-设置标签，2-清除标签", paramType = "query", required = true, dataType = "int")
    })
//    @RequiresPermissions("member:user:updateBq")
    public Result updateUserBq(@RequestParam("id") Long userId, @RequestParam("biaoqian") Integer biaoqian, @RequestParam("type") Integer type) {
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
            log.error("用户业务员修改异常，用户ID: {}, 业务员ID: {}, 业务员姓名: {}", userId, salesmanid, e);
            return new Result().error("业务员修改失败: " + e.getMessage());
        }
    }


    
}
