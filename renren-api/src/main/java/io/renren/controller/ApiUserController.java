

package io.renren.controller;

import io.renren.annotation.Login;
import io.renren.annotation.LoginUser;
import io.renren.common.utils.Result;
import io.renren.dao.InvestmentRecordDao;
import io.renren.dto.BalanceDTO;
import io.renren.dto.UserInfoDTO;
import io.renren.entity.UserEntity;
import io.renren.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@Api(tags = "用户管理接口")
public class ApiUserController {
    @Autowired
    private UserService userService;

    @Autowired
    private InvestmentRecordDao investmentRecordDao;

    @Login
    @GetMapping("/userInfo")
    @ApiOperation("获取用户信息")
    public Result<UserInfoDTO> getUserInfo(@LoginUser UserEntity user) {
        UserInfoDTO userInfo = userService.getUserInfoWithSuperior(user.getId());
        
        if (userInfo == null) {
            return new Result<UserInfoDTO>().error("用户不存在");
        }
        
        return new Result<UserInfoDTO>().ok(userInfo);
    }

    @Login
    @GetMapping("/balance")
    @ApiOperation("获取用户余额")
    public Result<BalanceDTO> getUserBalance(@LoginUser UserEntity user) {
        try {
            UserEntity userBalance = userService.selectById(user.getId());
            
            if (userBalance == null) {
                return new Result<BalanceDTO>().error("用户不存在");
            }
            
            BalanceDTO balanceDTO = new BalanceDTO();
            balanceDTO.setId(userBalance.getId());
            balanceDTO.setUserId(userBalance.getId());
            balanceDTO.setAssets(userBalance.getAssets() != null ? userBalance.getAssets() : 0L);
            Long dsAmount = investmentRecordDao.selectPendingInterestByUserId(user.getId());         // 待收利息
            Long balance=balanceDTO.getAssets()+user.getCashwithdrawable()+dsAmount;
            balanceDTO.setBalance(balance);
            balanceDTO.setCashwithdrawable(userBalance.getCashwithdrawable() != null ? userBalance.getCashwithdrawable() : 0L);
            
            // 累计收益 = 代收收益 + 已收收益
            Long cumulative = (userBalance.getHistoryProfit() != null ? userBalance.getHistoryProfit() : 0L) + 
                            (userBalance.getTodayProfit() != null ? userBalance.getTodayProfit() : 0L);
            balanceDTO.setCumulative(cumulative);
            
            // 累计充值
            balanceDTO.setCzAmount(userBalance.getChargeSum() != null ? userBalance.getChargeSum() : 0L);
            
            // 待收利息/等待回收 (使用今日收益作为待收)
            balanceDTO.setDsAmount(userBalance.getTodayProfit() != null ? userBalance.getTodayProfit() : 0L);
            
            // 待收本金 (使用今日投资作为待收)
            balanceDTO.setDsbjAmount(userBalance.getTodayInvestment() != null ? userBalance.getTodayInvestment() : 0L);
            
            // 今日收益
            balanceDTO.setJrAmount(userBalance.getTodayProfit() != null ? userBalance.getTodayProfit() : 0L);
            
            // 累计投资
            balanceDTO.setTzAmount(userBalance.getHistoryInvestment() != null ? userBalance.getHistoryInvestment() : 0L);
            
            // 已收利息 (使用历史收益)
            balanceDTO.setYsAmount(userBalance.getHistoryProfit() != null ? userBalance.getHistoryProfit() : 0L);
            
            // 已收本金 (使用历史投资)
            balanceDTO.setYsbjAmount(userBalance.getHistoryInvestment() != null ? userBalance.getHistoryInvestment() : 0L);
            
            // 已提现
            balanceDTO.setYtxAmount(userBalance.getWithdrawSum() != null ? userBalance.getWithdrawSum() : 0L);
            
            // 正在提现 (使用今日提现)
            balanceDTO.setZztxAmount(userBalance.getTodayWithdraw() != null ? userBalance.getTodayWithdraw() : 0L);
            
            // 转盘次数 (暂时设为0，如果表中有相关字段可以替换)
            balanceDTO.setWheelTimes(0);
            
            // 更新日期 (使用最后登录时间)
            balanceDTO.setUpdateDate(userBalance.getLastDate() != null ? userBalance.getLastDate().toString() : "");
            
            return new Result<BalanceDTO>().ok(balanceDTO);
            
        } catch (Exception e) {
            return new Result<BalanceDTO>().error("获取余额信息失败");
        }
    }

    

//
//    @PostMapping("update")
//    @ApiOperation("更新用户信息")
//    public Result updateUserInfo(@LoginUser UserEntity user, @RequestBody UpdateUserDTO dto) {
//        // 表单校验
//        ValidatorUtils.validateEntity(dto);
//
//        // 更新用户信息
//        if (dto.getUsername() != null) {
//            user.setUsername(dto.getUsername());
//        }
//        if (dto.getInviteCode() != null) {
//            user.setInviteCode(dto.getInviteCode());
//        }
//        if (dto.getAgent() != null) {
//            user.setAgent(dto.getAgent());
//        }
//        if (dto.getChannel() != null) {
//            user.setChannel(dto.getChannel());
//        }
//        if (dto.getEquipment() != null) {
//            user.setEquipment(dto.getEquipment());
//        }
//
//        userService.updateById(user);
//        return new Result();
//    }
//
//    @PostMapping("changePassword")
//    @ApiOperation("修改密码")
//    public Result changePassword(@LoginUser UserEntity user, @RequestBody ChangePasswordDTO dto) {
//        // 表单校验
//        ValidatorUtils.validateEntity(dto);
//
//        // 验证原密码
//        if (!user.getPassword().equals(DigestUtils.sha256Hex(dto.getOldPassword()))) {
//            return new Result().error("原密码不正确");
//        }
//
//        // 验证新密码确认
//        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
//            return new Result().error("两次输入的新密码不一致");
//        }
//
//        // 更新密码
//        user.setPassword(DigestUtils.sha256Hex(dto.getNewPassword()));
//        userService.updateById(user);
//
//        return new Result();
//    }
//
//    @PostMapping("changeTwoPassword")
//    @ApiOperation("修改二级密码")
//    public Result changeTwoPassword(@LoginUser UserEntity user, @RequestBody ChangeTwoPasswordDTO dto) {
//        // 表单校验
//        ValidatorUtils.validateEntity(dto);
//
//        // 验证原二级密码
//        if (user.getTwoPwd() != null && !user.getTwoPwd().equals(DigestUtils.sha256Hex(dto.getOldTwoPassword()))) {
//            return new Result().error("原二级密码不正确");
//        }
//
//        // 验证新二级密码确认
//        if (!dto.getNewTwoPassword().equals(dto.getConfirmTwoPassword())) {
//            return new Result().error("两次输入的新二级密码不一致");
//        }
//
//        // 更新二级密码
//        user.setTwoPwd(DigestUtils.sha256Hex(dto.getNewTwoPassword()));
//        userService.updateById(user);
//
//        return new Result();
//    }


}
