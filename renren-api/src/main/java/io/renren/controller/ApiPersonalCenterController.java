
package io.renren.controller;

import io.renren.annotation.Login;
import io.renren.annotation.LoginUser;
import io.renren.common.constant.Constant;
import io.renren.common.utils.Result;
import io.renren.dto.BalanceDTO;
import io.renren.dto.TransactionDetailDTO;
import io.renren.dto.TransactionDetailPageData;
import io.renren.dto.ProfitDTO;
import io.renren.dto.ProfitPageData;
import io.renren.dto.UserDataSummaryDTO;
import io.renren.dto.MyInvestmentDTO;
import io.renren.dto.MyInvestmentPageData;
import io.renren.dto.BalanceDetailPageData;
import io.renren.dto.TeamPointsDetailPageData;
import io.renren.entity.UserEntity;
import io.renren.service.TransactionDetailService;
import io.renren.service.ProfitService;
import io.renren.service.UserService;
import io.renren.service.MyInvestmentService;
import io.renren.service.BalanceDetailService;
import io.renren.service.TeamPointsDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 个人中心接口
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/api/grzx")
@Api(tags = "个人中心接口")
public class ApiPersonalCenterController {

    @Autowired
    private TransactionDetailService transactionDetailService;

    @Autowired
    private ProfitService profitService;

    @Autowired
    private UserService userService;

    @Autowired
    private MyInvestmentService myInvestmentService;

    @Autowired
    private BalanceDetailService balanceDetailService;

    @Autowired
    private TeamPointsDetailService teamPointsDetailService;

    @Login
    @PostMapping("BalanceTransfers")
    @ApiOperation("投资账户转让")
    public Result<Map<String, Object>> balanceTransfers(@LoginUser UserEntity user) {
        
        // TODO: 实现投资账户转让逻辑
        // 这里需要根据业务需求实现具体的账户转让逻辑
        // 包括：验证用户权限、检查账户余额、执行转让操作、记录交易日志等
        
        Map<String, Object> result = new HashMap<>();
        result.put("transferId", "TRANSFER_" + System.currentTimeMillis());
        result.put("status", "success");
        result.put("message", "账户转让申请已提交");
        
        return new Result<Map<String, Object>>().ok(result);
    }

    @Login
    @GetMapping("agetBalanceDetail")
    @ApiOperation("资金明细")
    public Result<TransactionDetailPageData<TransactionDetailDTO>> getAgetBalanceDetail(
            @ApiParam(value = "每页显示记录数", required = true) @RequestParam(Constant.LIMIT) Integer limit,
            @ApiParam(value = "当前页码，从1开始", required = true) @RequestParam(Constant.PAGE) Integer page,
            @LoginUser UserEntity user) {
        
        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, page);
        params.put(Constant.LIMIT, limit);
        // 只查询当前用户的账变明细
        params.put("userId", user.getId().toString());

        TransactionDetailPageData<TransactionDetailDTO> pageData = transactionDetailService.queryPageData(params);
        
        return new Result<TransactionDetailPageData<TransactionDetailDTO>>().ok(pageData);
    }

    @Login
    @GetMapping("balanceDetail")
    @ApiOperation("资金明细")
    public Result<TransactionDetailPageData<TransactionDetailDTO>> getBalanceDetail(
            @ApiParam(value = "每页显示记录数", required = true) @RequestParam(Constant.LIMIT) Integer limit,
            @ApiParam(value = "当前页码，从1开始", required = true) @RequestParam(Constant.PAGE) Integer page,
            @LoginUser UserEntity user) {
        
        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, page);
        params.put(Constant.LIMIT, limit);
        // 只查询当前用户的账变明细
        params.put("userId", user.getId().toString());

        TransactionDetailPageData<TransactionDetailDTO> pageData = transactionDetailService.queryPageData(params);
        
        return new Result<TransactionDetailPageData<TransactionDetailDTO>>().ok(pageData);
    }

    @Login
    @GetMapping("balance")
    @ApiOperation("账户概览")
    public Result<BalanceDTO> getBalance(@LoginUser UserEntity user) {
        
        try {
            BalanceDTO balanceDTO = new BalanceDTO();
            balanceDTO.setId(user.getId());
            balanceDTO.setUserId(user.getId());
            balanceDTO.setAssets(user.getBalance() != null ? user.getBalance() : 0L);           // 可用余额
            balanceDTO.setBalance(user.getBalance() != null ? user.getBalance() : 0L);          // 账户余额
            balanceDTO.setCashwithdrawable(user.getCashwithdrawable() != null ? user.getCashwithdrawable() : 0L); // 可提现
            balanceDTO.setCumulative(user.getHistoryProfit() != null ? user.getHistoryProfit() : 0L);       // 累计收益
            balanceDTO.setCzAmount(user.getChargeSum() != null ? user.getChargeSum() : 0L);         // 累计充值
            balanceDTO.setDsAmount(0L);         // 待收利息 - 需要从投资记录表查询
            balanceDTO.setDsbjAmount(0L);       // 待收本金 - 需要从投资记录表查询
            balanceDTO.setJrAmount(user.getTodayProfit() != null ? user.getTodayProfit() : 0L);         // 今日收益
            balanceDTO.setTzAmount(user.getHistoryInvestment() != null ? user.getHistoryInvestment() : 0L);         // 累计投资
            balanceDTO.setYsAmount(0L);         // 已收利息 - 需要从投资记录表查询
            balanceDTO.setYsbjAmount(0L);       // 已收本金 - 需要从投资记录表查询
            balanceDTO.setYtxAmount(user.getWithdrawSum() != null ? user.getWithdrawSum() : 0L);        // 已提现
            balanceDTO.setZztxAmount(0L);       // 正在提现 - 需要从提现记录表查询
            balanceDTO.setWheelTimes(0);        // 转盘次数 - 需要从转盘记录表查询
            balanceDTO.setUpdateDate(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
            
            return new Result<BalanceDTO>().ok(balanceDTO);
            
        } catch (Exception e) {
            return new Result<BalanceDTO>().error("获取账户余额失败: " + e.getMessage());
        }
    }

    @Login
    @GetMapping("grzxBalanceDetail")
    @ApiOperation("资金明细")
    public Result<BalanceDetailPageData> grzxBalanceDetail(
            @ApiParam(value = "当前页码，从1开始", required = true) @RequestParam Integer page,
            @ApiParam(value = "每页显示记录数", required = true) @RequestParam Integer limit,
            @LoginUser UserEntity user) {
        try {
            // 参数验证
            if (page == null || page < 1) {
                return new Result<BalanceDetailPageData>().error("页码必须大于0");
            }
            if (limit == null || limit < 1 || limit > 100) {
                return new Result<BalanceDetailPageData>().error("每页记录数必须在1-100之间");
            }

            // 获取资金明细分页数据
            BalanceDetailPageData pageData = balanceDetailService.getBalanceDetailPageData(user.getId(), page, limit);
            
            return new Result<BalanceDetailPageData>().ok(pageData);
            
        } catch (Exception e) {
            return new Result<BalanceDetailPageData>().error("获取资金明细失败: " + e.getMessage());
        }
    }

    @Login
    @GetMapping("profitlist")
    @ApiOperation("付息还本")
    public Result<ProfitPageData<ProfitDTO>> getProfitList(
            @ApiParam(value = "每页显示记录数", required = true) @RequestParam(Constant.LIMIT) Integer limit,
            @ApiParam(value = "当前页码，从1开始", required = true) @RequestParam(Constant.PAGE) Integer page,
            @LoginUser UserEntity user) {
        
        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, page);
        params.put(Constant.LIMIT, limit);
        // 只查询当前用户的收益记录
        params.put("userId", user.getId().toString());

        ProfitPageData<ProfitDTO> pageData = profitService.queryPageData(params);
        
        return new Result<ProfitPageData<ProfitDTO>>().ok(pageData);
    }

    @Login
    @GetMapping("userDataSummary")
    @ApiOperation("查询用户统计信息")
    public Result<UserDataSummaryDTO> getUserDataSummary(@LoginUser UserEntity user) {
        try {
            // 获取用户数据汇总信息
            UserDataSummaryDTO summaryDTO = userService.getUserDataSummary(user.getId());
            
            if (summaryDTO == null) {
                return new Result<UserDataSummaryDTO>().error("获取用户统计信息失败");
            }
            
            return new Result<UserDataSummaryDTO>().ok(summaryDTO);
            
        } catch (Exception e) {
            return new Result<UserDataSummaryDTO>().error("查询用户统计信息失败: " + e.getMessage());
        }
    }

    @Login
    @GetMapping("myprofit")
    @ApiOperation("我的投资")
    public Result<MyInvestmentPageData<MyInvestmentDTO>> getMyInvestment(
            @ApiParam(value = "每页显示记录数", required = true) @RequestParam(Constant.LIMIT) Integer limit,
            @ApiParam(value = "当前页码，从1开始", required = true) @RequestParam(Constant.PAGE) Integer page,
            @ApiParam(value = "状态 0:正在生产 1:生产结束", required = true) @RequestParam Integer status,
            @LoginUser UserEntity user) {
        
        try {
            Map<String, Object> params = new HashMap<>();
            params.put(Constant.PAGE, page);
            params.put(Constant.LIMIT, limit);
            params.put("status", status);
            params.put("userId", user.getId().toString());

            MyInvestmentPageData<MyInvestmentDTO> pageData = myInvestmentService.queryPageData(params);
            
            return new Result<MyInvestmentPageData<MyInvestmentDTO>>().ok(pageData);
            
        } catch (Exception e) {
            return new Result<MyInvestmentPageData<MyInvestmentDTO>>().error("查询我的投资失败: " + e.getMessage());
        }
    }

    @Login
    @GetMapping("teamPointsDetail")
    @ApiOperation("积分明细")
    public Result<TeamPointsDetailPageData> getTeamPointsDetail(
            @ApiParam(value = "当前页码，从1开始", required = true) @RequestParam Integer page,
            @ApiParam(value = "每页显示记录数", required = true) @RequestParam Integer limit,
            @LoginUser UserEntity user) {
        try {
            if (page == null || page < 1) {
                return new Result<TeamPointsDetailPageData>().error("页码必须大于0");
            }
            if (limit == null || limit < 1 || limit > 100) {
                return new Result<TeamPointsDetailPageData>().error("每页记录数必须在1-100之间");
            }
            TeamPointsDetailPageData pageData = teamPointsDetailService.getTeamPointsDetailPageData(user.getId(), page, limit);
            return new Result<TeamPointsDetailPageData>().ok(pageData);
        } catch (Exception e) {
            return new Result<TeamPointsDetailPageData>().error("获取积分明细失败: " + e.getMessage());
        }
    }
}
