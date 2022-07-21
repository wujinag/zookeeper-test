package com.task.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务配置
 * @author wujiang
 * @date 2022/7/20 14:34
 */

@Component
@Slf4j
public class ScheduledTasks {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    /**
     * 固定频率，每5秒执行一次
     * @author wujiang
     * @date 2022/7/20 14:36
     */
    @Scheduled(fixedRate = 1000L)
    public void reportCurrentTimeWithFixedDelay(){
        try {
            log.debug("定时任务   Task running : The time is now {}", dateFormat.format(new Date()));
            System.out.println("========================== reportCurrentTimeWithFixedDelay===========================");
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            log.info("Fixed Delay Task : The time is now {}", dateFormat.format(new Date()));
        }

    }
}
