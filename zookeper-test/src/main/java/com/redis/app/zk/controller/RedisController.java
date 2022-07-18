package com.redis.app.zk.controller;

import com.utils.RedissonDistributedLocker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
@Slf4j
public class RedisController {
    @Autowired
    private RedissonDistributedLocker distributedLocker;


    @GetMapping("/test")
    public void testLock() throws InterruptedException {
        final int[] counter = {0};
        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String myLock = "redis_001";
                    boolean isGetLock = distributedLocker.tryLock(myLock, 3L, 1L);
                    log.info("拿到锁?=={}", isGetLock ? "Y" : "N");
                    if (isGetLock) {
                        try {
                            //TODO 执行相应的业务逻辑，防止并发
                            int a = counter[0];
                            counter[0] = a + 1;
                            log.info("currentThread={},======run==={}", Thread.currentThread().getName(), a + "");
                        } finally {
                            distributedLocker.unLock(myLock);
                        }
                    }
                }
            }).start();
        }
        //主线程休眠
        Thread.sleep(5000);
        System.out.println(counter[0]);
        log.info("=============>{}", counter[0] + "");
    }


}
