package io.renren.controller;

import io.renren.common.exception.ErrorCode;
import io.renren.common.exception.RenException;
import io.renren.common.utils.Result;
import io.renren.common.validator.ValidatorUtils;
import io.renren.dao.UserBalanceDetailDao;
import io.renren.dao.UserDao;
import io.renren.entity.SysUserEntity;
import io.renren.entity.TokenEntity;
import io.renren.entity.UserBalanceDetailEntity;
import io.renren.entity.UserEntity;
import io.renren.dto.RegisterDTO;
import io.renren.enums.BusinessTypeEnum;
import io.renren.service.TokenService;
import io.renren.service.UserService;
import io.renren.service.ReferralRewardService;
import io.renren.service.SysUserService;
import io.renren.dto.ChannelAllocationResult;
import io.renren.utils.InviteCodeGenerator;
import io.renren.utils.IpAddressUtil;
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

    @PostMapping("register")
    @ApiOperation("注册")
    public Result<Map<String, Object>> register(@RequestBody RegisterDTO dto){
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
                UserEntity userEntity=userDao.selectByInviteCode(dto.getInviteCode());
                if (Objects.nonNull(userEntity)) {
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
        return new Result().ok(map);
    }

    @PostMapping("verificationBnkCode")
    @ApiOperation("发送短信验证码")
    public Result sendVerificationCode(
            @ApiParam(value = "手机号码", required = false) 
            @RequestParam(value = "mobile", required = false) String mobile) {
        
        // 验证手机号格式
        if (mobile == null || mobile.trim().isEmpty()) {
            return new Result().error("手机号不能为空");
        }
        
        // 验证手机号格式（10位数字）
        if (!mobile.matches("^\\d{10}$")) {
            return new Result().error("手机号格式错误");
        }
        
        try {
            // TODO: 这里需要集成具体的短信服务商API
            String verificationCode = generateVerificationCode();
            
            // 发送短信验证码的逻辑
            boolean sendResult = sendSmsCode(mobile, verificationCode);
            
            if (sendResult) {
                // 将验证码存储到Redis或数据库中，设置过期时间
                // TODO: 实现验证码存储逻辑
                
                return new Result().ok("验证码发送成功");
            } else {
                return new Result().error("验证码发送失败，请稍后重试");
            }
            
        } catch (Exception e) {
            return new Result().error("系统异常，请稍后重试");
        }
    }
    
    /**
     * 生成6位随机验证码
     */
    private String generateVerificationCode() {
        return String.valueOf((int)((Math.random() * 9 + 1) * 100000));
    }
    
    /**
     * 发送短信验证码
     * TODO: 需要集成具体的短信服务商
     */
    private boolean sendSmsCode(String mobile, String code) {
        // 这里应该调用具体的短信服务商API
        // 例如：阿里云短信、腾讯云短信等
        // 暂时返回true，实际使用时需要替换为真实的短信发送逻辑
        
        // 模拟发送成功
        System.out.println("向手机号 " + mobile + " 发送验证码: " + code);
        return true;
    }
}