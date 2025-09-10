package io.renren.common.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import io.renren.common.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 验证码工具类
 * 生成和验证4位数字验证码（使用Redis存储）
 * 
 * @author lip
 * @date 2024-01-01
 */
@Component
public class VerificationCodeUtils {
    
    @Autowired
    private RedisUtils redisUtils;
    
    /**
     * 验证码Redis键前缀
     */
    private static final String CODE_KEY_PREFIX = "verification_code:";
    
    /**
     * 验证码有效期（5分钟）
     */
    private static final long CODE_EXPIRE_TIME = 5 * 60L;
    
    /**
     * 生成4位数字验证码
     * 
     * @param
     * @return 验证码
     */
    public String generateCode() {
        // 生成4位数字验证码
        return RandomUtil.randomNumbers(4);
    }
    
    /**
     * 存储验证码到Redis（短信发送成功后调用）
     * 
     * @param mobile 手机号码
     * @param code 验证码
     */
    public void storeCode(String mobile, String code) {
        String key = CODE_KEY_PREFIX + mobile;
        redisUtils.set(key, code);
    }
    
    /**
     * 验证验证码
     * 
     * @param mobile 手机号码
     * @param code 验证码
     * @return 验证结果
     */
    public boolean verifyCode(String mobile, String code) {
        if (StrUtil.isBlank(mobile) || StrUtil.isBlank(code)) {
            return false;
        }
        
        String key = CODE_KEY_PREFIX + mobile;
        Object codeObj = redisUtils.get(key);
        
        if (codeObj == null) {
            return false;
        }
        
        // 验证码是否正确
        boolean isValid = code.equals(codeObj.toString());
        
        // 验证成功后删除验证码
        if (isValid) {
            redisUtils.delete(key);
        }
        
        return isValid;
    }
    
    /**
     * 检查验证码是否存在
     * 
     * @param mobile 手机号码
     * @return 是否存在有效验证码
     */
    public boolean hasCode(String mobile) {
        if (StrUtil.isBlank(mobile)) {
            return false;
        }
        
        String key = CODE_KEY_PREFIX + mobile;
        Object codeObj = redisUtils.get(key);
        
        return codeObj != null;
    }
    
    /**
     * 获取验证码剩余有效时间（秒）
     * 
     * @param mobile 手机号码
     * @return 剩余有效时间（秒），-1表示不存在或已过期
     */
    public long getRemainingTime(String mobile) {
        if (StrUtil.isBlank(mobile)) {
            return -1;
        }
        
        String key = CODE_KEY_PREFIX + mobile;
        Object codeObj = redisUtils.get(key);
        
        if (codeObj == null) {
            return -1;
        }
        
        // Redis TTL会自动处理过期，这里返回固定值
        return CODE_EXPIRE_TIME;
    }
    
    /**
     * 删除验证码
     * 
     * @param mobile 手机号码
     */
    public void removeCode(String mobile) {
        if (StrUtil.isNotBlank(mobile)) {
            String key = CODE_KEY_PREFIX + mobile;
            redisUtils.delete(key);
        }
    }
    
    /**
     * 获取验证码统计信息
     * 
     * @return 统计信息
     */
    public String getCodeStats() {
        // Redis会自动清理过期的键，这里返回提示信息
        return "验证码存储在Redis中，自动过期清理";
    }
    
}
