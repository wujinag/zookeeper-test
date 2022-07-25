package com.task.schedule.elasticJob.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 自定义业务job
 * @author wujiang
 * @date 2022/7/22 14:03

 */
@Component
@Slf4j
public class MySimpleJob implements SimpleJob {

    @Override
    public void execute(ShardingContext shardingContext) {
        //System.out.println(shardingContext.getShardingParameter());
        try {
            log.info(String.format("------Thread ID: %s, 任務總片數: %s, " +
                            "当前分片項: %s.当前參數: %s," +
                            "当前任務名稱: %s.当前任務參數: %s"
                    ,
                    Thread.currentThread().getId(),
                    shardingContext.getShardingTotalCount(),
                    shardingContext.getShardingItem(),
                    shardingContext.getShardingParameter(),
                    shardingContext.getJobName(),
                    shardingContext.getJobParameter()

            ));
            Thread.sleep(3000L);

            log.info("=====elastic job excute ============>"+shardingContext.getShardingParameter()+"<=============");
        } catch (InterruptedException e) {
           log.error("err=======>"+e);
        }
    }
}
