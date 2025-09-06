package io.renren.utils;

import io.renren.common.redis.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Redis分布式锁工具类
 * 
 * @author renren
 * @date 2024-01-01
 */
@Component
public class RedisDistributedLock {
    
    private static final Logger logger = LoggerFactory.getLogger(RedisDistributedLock.class);
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private RedisUtils redisUtils;
    
    /**
     * 锁的默认过期时间（秒）
     */
    private static final long DEFAULT_EXPIRE_TIME = 30;
    
    /**
     * 锁的默认等待时间（毫秒）
     */
    private static final long DEFAULT_WAIT_TIME = 5000;
    
    /**
     * 锁的默认重试间隔（毫秒）
     */
    private static final long DEFAULT_RETRY_INTERVAL = 100;
    
    /**
     * 释放锁的Lua脚本
     */
    private static final String UNLOCK_SCRIPT = 
        "if redis.call('get', KEYS[1]) == ARGV[1] then " +
        "return redis.call('del', KEYS[1]) " +
        "else return 0 end";
    
    /**
     * 尝试获取锁（不等待）
     * 
     * @param lockKey 锁的key
     * @param expireTime 锁的过期时间（秒）
     * @return 锁的值，如果获取失败返回null
     */
    public String tryLock(String lockKey, long expireTime) {
        String lockValue = UUID.randomUUID().toString();
        Boolean success = redisTemplate.opsForValue()
            .setIfAbsent(lockKey, lockValue, expireTime, TimeUnit.SECONDS);
        
        if (Boolean.TRUE.equals(success)) {
            logger.debug("成功获取锁: {}", lockKey);
            return lockValue;
        } else {
            logger.debug("获取锁失败: {}", lockKey);
            return null;
        }
    }
    
    /**
     * 尝试获取锁（不等待，使用默认过期时间）
     * 
     * @param lockKey 锁的key
     * @return 锁的值，如果获取失败返回null
     */
    public String tryLock(String lockKey) {
        return tryLock(lockKey, DEFAULT_EXPIRE_TIME);
    }
    
    /**
     * 获取锁（等待）
     * 
     * @param lockKey 锁的key
     * @param waitTime 等待时间（毫秒）
     * @param expireTime 锁的过期时间（秒）
     * @return 锁的值，如果获取失败返回null
     */
    public String lock(String lockKey, long waitTime, long expireTime) {
        long startTime = System.currentTimeMillis();
        String lockValue;
        
        while (System.currentTimeMillis() - startTime < waitTime) {
            lockValue = tryLock(lockKey, expireTime);
            if (lockValue != null) {
                return lockValue;
            }
            
            try {
                Thread.sleep(DEFAULT_RETRY_INTERVAL);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warn("获取锁被中断: {}", lockKey);
                return null;
            }
        }
        
        logger.warn("获取锁超时: {}, 等待时间: {}ms", lockKey, waitTime);
        return null;
    }
    
    /**
     * 获取锁（等待，使用默认参数）
     * 
     * @param lockKey 锁的key
     * @return 锁的值，如果获取失败返回null
     */
    public String lock(String lockKey) {
        return lock(lockKey, DEFAULT_WAIT_TIME, DEFAULT_EXPIRE_TIME);
    }
    
    /**
     * 释放锁
     * 
     * @param lockKey 锁的key
     * @param lockValue 锁的值
     * @return 是否释放成功
     */
    public boolean unlock(String lockKey, String lockValue) {
        if (lockKey == null || lockValue == null) {
            return false;
        }
        
        try {
            DefaultRedisScript<Long> script = new DefaultRedisScript<>();
            script.setScriptText(UNLOCK_SCRIPT);
            script.setResultType(Long.class);
            
            Long result = redisTemplate.execute(script, 
                Collections.singletonList(lockKey), lockValue);
            
            boolean success = result != null && result == 1L;
            if (success) {
                logger.debug("成功释放锁: {}", lockKey);
            } else {
                logger.warn("释放锁失败，锁可能已过期或被其他线程释放: {}", lockKey);
            }
            
            return success;
            
        } catch (Exception e) {
            logger.error("释放锁异常: {}", lockKey, e);
            return false;
        }
    }
    
    /**
     * 执行带锁的操作
     * 
     * @param lockKey 锁的key
     * @param action 要执行的操作
     * @param <T> 返回值类型
     * @return 操作结果
     */
    public <T> T executeWithLock(String lockKey, LockAction<T> action) throws Exception {
        return executeWithLock(lockKey, DEFAULT_WAIT_TIME, DEFAULT_EXPIRE_TIME, action);
    }
    
    /**
     * 执行带锁的操作（自定义参数）
     * 
     * @param lockKey 锁的key
     * @param waitTime 等待时间（毫秒）
     * @param expireTime 锁的过期时间（秒）
     * @param action 要执行的操作
     * @param <T> 返回值类型
     * @return 操作结果
     */
    public <T> T executeWithLock(String lockKey, long waitTime, long expireTime, LockAction<T> action) throws Exception {
        String lockValue = lock(lockKey, waitTime, expireTime);
        if (lockValue == null) {
            throw new RuntimeException("获取锁失败: " + lockKey);
        }
        
        try {
            return action.execute();
        } finally {
            unlock(lockKey, lockValue);
        }
    }
    
    /**
     * 执行带锁的操作（无返回值）
     * 
     * @param lockKey 锁的key
     * @param action 要执行的操作
     */
    public void executeWithLock(String lockKey, VoidLockAction action) throws Exception {
        executeWithLock(lockKey, () -> {
            action.execute();
            return null;
        });
    }
    
    /**
     * 执行带锁的操作（无返回值，自定义参数）
     * 
     * @param lockKey 锁的key
     * @param waitTime 等待时间（毫秒）
     * @param expireTime 锁的过期时间（秒）
     * @param action 要执行的操作
     */
    public void executeWithLock(String lockKey, long waitTime, long expireTime, VoidLockAction action) throws Exception {
        executeWithLock(lockKey, waitTime, expireTime, () -> {
            action.execute();
            return null;
        });
    }
    
    /**
     * 带返回值的锁操作接口
     */
    @FunctionalInterface
    public interface LockAction<T> {
        T execute() throws Exception;
    }
    
    /**
     * 无返回值的锁操作接口
     */
    @FunctionalInterface
    public interface VoidLockAction {
        void execute() throws Exception;
    }
}
