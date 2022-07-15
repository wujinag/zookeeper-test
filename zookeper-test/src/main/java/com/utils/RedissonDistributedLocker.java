package com.utils;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * redis 基于Redisson的分布式锁实现
 *
 * @author wujiang
 * @date 2022/7/15 14:40
 */

@Component
public class RedissonDistributedLocker {

    @Autowired
    private RedissonClient redissonClient;


    /**
     * 加锁
     *
     * @param lockKey
     * @return RLock
     * @author wujiang
     * @date 2022/7/15 14:46
     */
    public RLock lock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        return lock;
    }

    /**
     * 加锁，过期自动释放
     *
     * @param lockKey
     * @param leaseTime 自动释放时间
     * @return RLock
     * @author wujiang
     * @date 2022/7/15 14:49
     */
    public RLock lock(String lockKey, long leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(leaseTime, TimeUnit.SECONDS);
        return lock;
    }

    /**
     * 加锁，过期自动释放,时间单位传入
     *
     * @param lockKey
     * @param leaseTime
     * @param timeUnit
     * @return RLock
     * @author wujiang
     * @date 2022/7/15 14:50
     */
    public RLock lock(String lockKey, long leaseTime, TimeUnit timeUnit) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(leaseTime, timeUnit);
        return lock;
    }

    /**
     * 尝试获取锁
     *
     * @param lockKey
     * @param waitTime
     * @param leaseTime
     * @return boolean
     * @author wujiang
     * @date 2022/7/15 14:53
     */
    public boolean tryLock(String lockKey, long waitTime, long leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }


    /**
     * 释放锁
     *
     * @param lockKey
     * @author wujiang
     * @date 2022/7/15 14:55
     */
    public void unLock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }

    /**
     * 释放锁
     *
     * @param lock
     * @author wujiang
     * @date 2022/7/15 14:56
     */
    public void unLock(RLock lock) {
        lock.unlock();
    }

}
