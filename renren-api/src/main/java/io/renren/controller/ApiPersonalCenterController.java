
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
import io.renren.dto.ProfitEndedDTO;
import io.renren.entity.UserEntity;
import io.renren.service.ProfitService;
import io.renren.service.UserService;
import io.renren.service.MyInvestmentService;
import io.renren.service.BalanceDetailService;
import io.renren.service.TeamPointsDetailService;
import io.renren.service.ProfitEndedService;
import io.renren.dao.UserDao;
import io.renren.dao.InvestmentRecordDao;
import io.renren.dao.WithdrawOrderDao;
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
    private ProfitService profitService;

    @Autowired
    private UserService userService;

    @Autowired
    private MyInvestmentService myInvestmentService;

    @Autowired
    private BalanceDetailService balanceDetailService;

    @Autowired
    private TeamPointsDetailService teamPointsDetailService;

    @Autowired
    private ProfitEndedService profitEndedService;

    @Autowired
    private UserDao userDao;
    
    @Autowired
    private InvestmentRecordDao investmentRecordDao;
    
    @Autowired
    private WithdrawOrderDao withdrawOrderDao;


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

    //TODO 还需要调整
//    @Login
//    @GetMapping("agetBalanceDetail")
//    @ApiOperation("资金明细")
//    public Result<TransactionDetailPageData<TransactionDetailDTO>> getAgetBalanceDetail(
//            @ApiParam(value = "每页显示记录数", required = true) @RequestParam(Constant.LIMIT) Integer limit,
//            @ApiParam(value = "当前页码，从1开始", required = true) @RequestParam(Constant.PAGE) Integer page,
//            @LoginUser UserEntity user) {
//
//        // 直接使用MyBatis-Plus分页，无需构建Map
//        TransactionDetailPageData<TransactionDetailDTO> pageData = balanceDetailService.getBalanceDetailPageData(user.getId(), page, limit);
//
//        return new Result<TransactionDetailPageData<TransactionDetailDTO>>().ok(pageData);
//    }

//    @Login
//    @GetMapping("balanceDetail")
//    @ApiOperation("资金明细")
//    public Result<TransactionDetailPageData<TransactionDetailDTO>> getBalanceDetail(
//            @ApiParam(value = "每页显示记录数", required = true) @RequestParam(Constant.LIMIT) Integer limit,
//            @ApiParam(value = "当前页码，从1开始", required = true) @RequestParam(Constant.PAGE) Integer page,
//            @LoginUser UserEntity user) {
//
//        // 直接使用MyBatis-Plus分页，无需构建Map
//        TransactionDetailPageData<TransactionDetailDTO> pageData = transactionDetailService.queryPageData(user.getId(), page, limit);
//
//        return new Result<TransactionDetailPageData<TransactionDetailDTO>>().ok(pageData);
//    }

    @Login
    @GetMapping("balance")
    @ApiOperation("账户概览")
    public Result<BalanceDTO> getBalance(@LoginUser UserEntity user) {
        
        try {
            BalanceDTO balanceDTO = new BalanceDTO();
            balanceDTO.setId(user.getId());
            balanceDTO.setUserId(user.getId());
            balanceDTO.setAssets(user.getAssets() != null ? user.getAssets() : 0L);           // 可用余额
            Long dsAmount = investmentRecordDao.selectPendingInterestByUserId(user.getId());         // 待收利息
            Long balance=balanceDTO.getAssets()+user.getCashwithdrawable()+dsAmount;
            balanceDTO.setBalance(balance);
            balanceDTO.setCashwithdrawable(user.getCashwithdrawable() != null ? user.getCashwithdrawable() : 0L); // 可提现
            balanceDTO.setCumulative(user.getHistoryProfit() != null ? user.getHistoryProfit() : 0L);       // 累计收益
            balanceDTO.setCzAmount(user.getChargeSum() != null ? user.getChargeSum() : 0L);         // 累计充值
            
            // 从投资记录表查询投资相关数据
            Long dsbjAmount = investmentRecordDao.selectPendingPrincipalByUserId(user.getId());     // 待收本金
            Long ysAmount = investmentRecordDao.selectReceivedInterestByUserId(user.getId());        // 已收利息
            Long ysbjAmount = investmentRecordDao.selectReceivedPrincipalByUserId(user.getId());    // 已收本金
            
            balanceDTO.setDsAmount(dsAmount != null ? dsAmount : 0L);
            balanceDTO.setDsbjAmount(dsbjAmount != null ? dsbjAmount : 0L);
            balanceDTO.setYsAmount(ysAmount != null ? ysAmount : 0L);
            balanceDTO.setYsbjAmount(ysbjAmount != null ? ysbjAmount : 0L);
            
            balanceDTO.setJrAmount(user.getTodayProfit() != null ? user.getTodayProfit() : 0L);         // 今日收益
            balanceDTO.setTzAmount(user.getHistoryInvestment() != null ? user.getHistoryInvestment() : 0L);         // 累计投资
            balanceDTO.setYtxAmount(user.getWithdrawSum() != null ? user.getWithdrawSum() : 0L);        // 已提现
            
            // 从提现记录表查询正在提现金额
            Long zztxAmount = withdrawOrderDao.selectPendingWithdrawAmountByUserId(user.getId());
            balanceDTO.setZztxAmount(zztxAmount != null ? zztxAmount : 0L);
            
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
        
        // 直接使用MyBatis-Plus分页，无需构建Map
        ProfitPageData<ProfitDTO> pageData = profitService.queryPageData(user.getId(), page, limit);
        
        return new Result<ProfitPageData<ProfitDTO>>().ok(pageData);
    }

    @Login
    @GetMapping("profitEnded")
    @ApiOperation("付息还本【代收，已收】")
    public Result<ProfitEndedDTO> getProfitEnded(@LoginUser UserEntity user) {
        try {
            ProfitEndedDTO profitEndedDTO = profitEndedService.getProfitEndedRecord(user.getId());
            return new Result<ProfitEndedDTO>().ok(profitEndedDTO);
        } catch (Exception e) {
            return new Result<ProfitEndedDTO>().error("获取付息还本记录失败: " + e.getMessage());
        }
    }

    @Login
    @GetMapping("profitInvesting")
    @ApiOperation("付息还本【代收，已收】- 投资中项目")
    public Result<ProfitEndedDTO> getProfitInvesting(@LoginUser UserEntity user) {
        try {
            ProfitEndedDTO profitEndedDTO = profitEndedService.getProfitInvestingRecord(user.getId());
            return new Result<ProfitEndedDTO>().ok(profitEndedDTO);
        } catch (Exception e) {
            return new Result<ProfitEndedDTO>().error("获取投资中项目统计失败: " + e.getMessage());
        }
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
            @ApiParam(value = "排序方式，可选值(asc、desc)") @RequestParam(value = Constant.ORDER, required = false) String order,
            @ApiParam(value = "排序字段") @RequestParam(value = Constant.ORDER_FIELD, required = false) String orderField,
            @LoginUser UserEntity user) {
        
        try {
            // 参数验证
            if (page == null || page < 1) {
                return new Result<MyInvestmentPageData<MyInvestmentDTO>>().error("页码必须大于0");
            }
            if (limit == null || limit < 1 || limit > 100) {
                return new Result<MyInvestmentPageData<MyInvestmentDTO>>().error("每页记录数必须在1-100之间");
            }
//            if (status == null || (status != 0 && status != 1)) {
//                return new Result<MyInvestmentPageData<MyInvestmentDTO>>().error("状态参数无效，只能是0(正在生产)或1(生产结束)");
//            }

            // 直接使用MyBatis-Plus分页，无需构建Map
            MyInvestmentPageData<MyInvestmentDTO> pageData = myInvestmentService.queryPageData(
                user.getId(), page, limit, status, order, orderField);
            
            if (pageData == null) {
                return new Result<MyInvestmentPageData<MyInvestmentDTO>>().error("查询结果为空");
            }
            
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
