package io.renren.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis缓存工具类
 *
 * @author renren
 * @since 2024-01-01
 */
@Slf4j
@Component
public class RedisCacheUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 用户信息缓存前缀
    private static final String USER_CACHE_PREFIX = "sys_user:";
    
    // 用户信息缓存过期时间（小时）
    private static final long USER_CACHE_EXPIRE_HOURS = 24;

    /**
     * 设置用户信息缓存
     * @param userId 用户ID
     * @param userInfo 用户信息
     */
    public void setUserCache(Long userId, Object userInfo) {
        try {
            String key = USER_CACHE_PREFIX + userId;
            redisTemplate.opsForValue().set(key, userInfo, USER_CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
            log.debug("设置用户缓存成功，用户ID: {}, 过期时间: {}小时", userId, USER_CACHE_EXPIRE_HOURS);
        } catch (Exception e) {
            log.error("设置用户缓存失败，用户ID: {}", userId, e);
        }
    }

    /**
     * 获取用户信息缓存
     * @param userId 用户ID
     * @param clazz 返回类型
     * @return 用户信息
     */
    @SuppressWarnings("unchecked")
    public <T> T getUserCache(Long userId, Class<T> clazz) {
        try {
            String key = USER_CACHE_PREFIX + userId;
            Object value = redisTemplate.opsForValue().get(key);
            if (value != null) {
                log.debug("从缓存获取用户信息成功，用户ID: {}", userId);
                return (T) value;
            }
        } catch (Exception e) {
            log.error("从缓存获取用户信息失败，用户ID: {}", userId, e);
        }
        return null;
    }

    /**
     * 删除用户信息缓存
     * @param userId 用户ID
     */
    public void deleteUserCache(Long userId) {
        try {
            String key = USER_CACHE_PREFIX + userId;
            redisTemplate.delete(key);
            log.debug("删除用户缓存成功，用户ID: {}", userId);
        } catch (Exception e) {
            log.error("删除用户缓存失败，用户ID: {}", userId, e);
        }
    }

    /**
     * 检查用户缓存是否存在
     * @param userId 用户ID
     * @return 是否存在
     */
    public boolean hasUserCache(Long userId) {
        try {
            String key = USER_CACHE_PREFIX + userId;
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            log.error("检查用户缓存失败，用户ID: {}", userId, e);
            return false;
        }
    }

    /**
     * 刷新用户缓存过期时间
     * @param userId 用户ID
     */
    public void refreshUserCacheExpire(Long userId) {
        try {
            String key = USER_CACHE_PREFIX + userId;
            redisTemplate.expire(key, USER_CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
            log.debug("刷新用户缓存过期时间成功，用户ID: {}", userId);
        } catch (Exception e) {
            log.error("刷新用户缓存过期时间失败，用户ID: {}", userId, e);
        }
    }
}
