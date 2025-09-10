package io.renren.controller;

import io.renren.common.exception.ErrorCode;
import io.renren.common.exception.RenException;
import io.renren.common.utils.Result;
import io.renren.common.utils.VerificationCodeUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.dao.UserBalanceDetailDao;
import io.renren.dao.UserDao;
import io.renren.dao.UserLogDao;
import io.renren.entity.SysUserEntity;
import io.renren.entity.TokenEntity;
import io.renren.entity.UserBalanceDetailEntity;
import io.renren.entity.UserEntity;
import io.renren.entity.UserLogEntity;
import io.renren.dto.RegisterDTO;
import io.renren.enums.BusinessTypeEnum;
import io.renren.service.*;
import io.renren.utils.InviteCodeGenerator;
import io.renren.utils.IpAddressUtil;
import io.renren.common.utils.IpUtils;
import io.renren.utils.SmsUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 注册接口
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/api")
@Api(tags="注册接口")
@Slf4j
public class ApiRegisterController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private ReferralRewardService referralRewardService;

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserBalanceDetailDao userBalanceDetailDao;

    @Autowired
    private SysUserService sysUserService;
    
    @Autowired
    private UserLogDao userLogDao;

//    @Autowired
//    private SmsService smsService;

    @Autowired
    private VerificationCodeUtils verificationCodeUtils;

    @Autowired
    private SmsUtils smsUtils;

    @PostMapping("register")
    @ApiOperation("注册")
    public Result<Map<String, Object>> register(@RequestBody RegisterDTO dto, HttpServletRequest request){
        //表单校验
        ValidatorUtils.validateEntity(dto);

        // 检查手机号是否已经注册
        if (userService.isMobileRegistered(dto.getMobile())) {
            throw new RenException(ErrorCode.PHONE_NUMBER_HAS_BEEN_REGISTERED);
        }

        UserEntity user = new UserEntity();
        user.setMobile(dto.getMobile());
        // 如果提供了真实姓名，使用真实姓名，否则使用手机号作为用户名
        user.setUsername(dto.getMobile());
        user.setPassword(DigestUtils.sha256Hex(dto.getPassword()));
        
        // 设置新字段
        if (dto.getTwoPwd() != null) {
            user.setTwoPwd(DigestUtils.sha256Hex(dto.getTwoPwd()));
        }
        String newInviteCode = InviteCodeGenerator.generateInviteCode();
        user.setInviteCode(newInviteCode);
        //根据渠道查询对应代理
        if(Objects.nonNull(dto.getInviteCode())){
            SysUserEntity sysUserEntity= sysUserService.selectByAgentInviteCode(dto.getInviteCode());
            if (Objects.nonNull(sysUserEntity)) {
                // 设置业务员信息
                user.setSalesmanid(sysUserEntity.getId());
                user.setSalesmanName(sysUserEntity.getUsername());
                // 设置代理信息
                user.setAgent(sysUserEntity.getAgent());
//                user.setAgentName(allocationResult.getAgentName());
            }else {
                user.setUpinviteCode(dto.getInviteCode());
                user.setSuperiorCode(dto.getInviteCode());
                UserEntity userEntity=userDao.selectByInviteCode(dto.getInviteCode());
                if (Objects.nonNull(userEntity)) {
                    user.setSuperiorName(userEntity.getMobile());
                    user.setLiebian(1);
                    // 设置业务员信息
                    user.setSalesmanid(userEntity.getSalesmanid());
                    user.setSalesmanName(userEntity.getSalesmanName());
                    // 设置代理信息
                    user.setAgent(userEntity.getAgent());
                    user.setAgentName(userEntity.getAgentName());
                }
            }
        }
        user.setChannel(dto.getChannel());
        user.setEquipment(dto.getEquipment());
        // 获取用户注册IP地址
        String registerIp = IpAddressUtil.getClientIpAddress();
        user.setRegisterIp(registerIp);
        user.setLastIp(registerIp);
        user.setLastDate(new Date());
        user.setCreateDate(new Date());

        user.setCreateDate(new Date());
        userService.insert(user);

        // 更新邀请人和邀请人上级的会员数
        try {
            if (dto.getInviteCode() != null && !dto.getInviteCode().trim().isEmpty()) {
                updateInviterMemberCounts(dto.getInviteCode());
            }
        } catch (Exception e) {
            log.error("更新邀请人会员数时发生异常，用户ID: {}, 邀请码: {}", user.getId(), dto.getInviteCode(), e);
            // 不影响注册流程，只记录日志
        }

        // 处理推荐返利
        try {
            if (dto.getInviteCode() != null && !dto.getInviteCode().trim().isEmpty()) {
                boolean rewardSuccess = referralRewardService.processReferralReward(user.getId(), dto.getInviteCode());
                if (rewardSuccess) {
                    log.info("用户 {} 注册成功，推荐人返利处理成功", user.getId());
                } else {
                    log.warn("用户 {} 注册成功，但推荐人返利处理失败", user.getId());
                }
            }
        } catch (Exception e) {
            log.error("处理推荐返利时发生异常，用户ID: {}", user.getId(), e);
            // 不影响注册流程，只记录日志
        }
        //注册奖励
        if(Objects.nonNull(user.getId())) {
            Long regAmount=30000L;
            if (userDao.addUserBalance(user.getId(), regAmount) > 0) {
                UserBalanceDetailEntity detail = new UserBalanceDetailEntity();
                detail.setBusiType(BusinessTypeEnum.REGISTRATION_REWARD.getCode());
                detail.setUserId(user.getId());
                detail.setOriginalAmount(0L);
                detail.setUseAmount(regAmount);
                detail.setTransactionAmount(regAmount);
                detail.setStatus(1); // 成功状态
                detail.setTransactionDate(new Date());
                detail.setChannel("1");
                detail.setSalesmanId(user.getSalesmanid());
                detail.setCreateDate(new Date());
                detail.setUpdateDate(new Date());
                detail.setRemarks("注册奖励");
                detail.setStreamId("");
                userBalanceDetailDao.insert(detail);
            }
        }

        //获取登录token
        TokenEntity tokenEntity = tokenService.createToken(user.getId());
        Map<String, Object> map = new HashMap<>(2);
        map.put("token", tokenEntity.getToken());
        map.put("expire", tokenEntity.getExpireDate().getTime() - System.currentTimeMillis());
        
        // 记录注册后的登录日志
        try {
            saveRegisterLoginLog(user, dto, request);
        } catch (Exception e) {
            // 登录日志记录失败不影响注册流程
            log.error("记录注册登录日志失败，用户ID: {}", user.getId(), e);
        }
        
        return new Result().ok(map);
    }


    @PostMapping("verificationBnkCode")
    @ApiOperation("发送短信验证码")
    public Result sendVerificationCode(
            @ApiParam(value = "手机号码", required = false) 
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "register", required = false) Boolean register
            ) {
        
        // 验证手机号格式
        if (mobile == null || mobile.trim().isEmpty()) {
            throw new RenException(ErrorCode.PHONE_NUMBER_EMPTY);
        }

        try {
            // 1. 生成验证码
            String code = verificationCodeUtils.generateCode();
            // 2. 发送短信
            SmsUtils.SmsResult smsResult = smsUtils.sendSms(mobile, code);
            if (smsResult.isSuccess()) {
                // 3. 短信发送成功后，存储验证码到Redis
                verificationCodeUtils.storeCode(mobile, code);
            } else {
                throw new RenException(ErrorCode.VERIFICATION_CODE_SEND_FAILED);
            }

        } catch (Exception e) {
            throw new RenException(ErrorCode.SYSTEM_EXCEPTION);
        }
        return new Result<Object>().ok("success");
    }
    

    /**
     * 更新邀请人和邀请人上级的会员数
     * @param inviteCode 邀请码
     */
    private void updateInviterMemberCounts(String inviteCode) {
        try {
            // 查找邀请人
            UserEntity inviter = userDao.selectByInviteCode(inviteCode);
            if (inviter != null) {
                // 更新邀请人的一级会员数
                userDao.updateUacnt(inviter.getId());
                log.info("更新邀请人 {} 的一级会员数", inviter.getId());
                
                // 查找邀请人的上级（二级邀请人）
                if (inviter.getUpinviteCode() != null && !inviter.getUpinviteCode().trim().isEmpty()) {
                    UserEntity secondLevelInviter = userDao.selectByInviteCode(inviter.getUpinviteCode());
                    if (secondLevelInviter != null) {
                        // 更新二级邀请人的二级会员数
                        userDao.updateUbcnt(secondLevelInviter.getId());
                        log.info("更新二级邀请人 {} 的二级会员数", secondLevelInviter.getId());
                        
                        // 查找二级邀请人的上级（三级邀请人）
                        if (secondLevelInviter.getUpinviteCode() != null && !secondLevelInviter.getUpinviteCode().trim().isEmpty()) {
                            UserEntity thirdLevelInviter = userDao.selectByInviteCode(secondLevelInviter.getUpinviteCode());
                            if (thirdLevelInviter != null) {
                                // 更新三级邀请人的三级会员数
                                userDao.updateUccnt(thirdLevelInviter.getId());
                                log.info("更新三级邀请人 {} 的三级会员数", thirdLevelInviter.getId());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("更新邀请人会员数时发生异常，邀请码: {}", inviteCode, e);
            throw e;
        }
    }
    
    /**
     * 保存注册后的登录日志
     * @param user 用户信息
     * @param dto 注册DTO
     * @param request HTTP请求
     */
    private void saveRegisterLoginLog(UserEntity user, RegisterDTO dto, HttpServletRequest request) {
        try {
            UserLogEntity logEntity = new UserLogEntity();
            logEntity.setUserId(user.getId());
            logEntity.setMobile(user.getMobile());
            logEntity.setLoginTime(new Date());
            
            // 获取客户端IP地址
            String clientIp = IpUtils.getIpAddr(request);
            logEntity.setLoginIp(clientIp);
            
            // 设置设备类型
            logEntity.setEquipment(dto.getEquipment() != null ? dto.getEquipment() : 4); // 4:未知
            
            // 设置用户相关信息
            logEntity.setSalesmanid(user.getSalesmanid());
            logEntity.setAgent(user.getAgent());
            logEntity.setBiaoqian(user.getBiaoqian());
            logEntity.setCreateTime(new Date());
            logEntity.setUpdateTime(new Date());
            
            // 设置销售员姓名
            if (user.getSalesmanName() != null) {
                logEntity.setSalesmanName(user.getSalesmanName());
            }
            
            // 保存登录日志
            userLogDao.insert(logEntity);
            log.info("用户 {} 注册后登录日志记录成功", user.getId());
            
        } catch (Exception e) {
            log.error("保存注册登录日志失败，用户ID: {}", user.getId(), e);
            throw e;
        }
    }
}