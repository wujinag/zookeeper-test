package com.task.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * spring Task 定时任务配置
 * 定时任务配置
 * @author wujiang
 * @date 2022/7/20 14:34
 */

@Component
@Slf4j
public class ScheduledTasks {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    /**
     * fixedRate 固定频率，每5秒执行一次
     * @author wujiang
     * @date 2022/7/20 14:36
     */
    @Scheduled(fixedRate = 5000L)
    public void reportCurrentTimeWithFixedRate(){
        try {
            System.out.println("========================== reportCurrentTimeWithFixedRate===========================");
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            log.info("定时任务 Fixed Delay Task : The time is now {}", dateFormat.format(new Date()));
        }
    }

    /** 
     * fixedDelay 固定延迟执行，距离上一次调用成功2s后才执行
     * @author wujiang
     * @date 2022/7/22 9:13
     */
    @Scheduled(fixedDelay = 2000L)
    public void  reportCurrentTimeWithFixedDelay(){
        try {
            log.debug("定时任务  延迟执行  Task running : The time is now {}", dateFormat.format(new Date()));
            System.out.println("========================== reportCurrentTimeWithFixedDelay===========================");
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            log.info("定时任务 Fixed Delay Task : The time is now {}", dateFormat.format(new Date()));
        }
    }


    /**
     * initialDelay:初始延迟。任务的第一次执行将延迟5秒，然后将以5秒的固定间隔执行。
     */
    @Scheduled(initialDelay = 5000L, fixedRate = 5000L)
    public void reportCurrentTimeWithInitialDelay() {
        log.info("定时任务 Fixed Rate Task with Initial Delay : The time is now {}", dateFormat.format(new Date()));
    }

    /**
     * cron：使用Cron表达式。　每分钟的1，2秒运行
     */
    @Scheduled(cron = "0 0/20 * * * ?")
    public void reportCurrentTimeWithCronExpression() {
        log.info("定时任务 表达式Cron Expression: The time is now {}", dateFormat.format(new Date()));
    }
}
