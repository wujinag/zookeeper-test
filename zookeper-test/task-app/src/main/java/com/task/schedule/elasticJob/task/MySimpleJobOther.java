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
@ElasticJobConfig(
        cron = "0/2 * * * * ?",
        shardingTotalCount=2,
        shardingItemParameters = "0=北京,1=上海"
)
public class MySimpleJobOther implements SimpleJob {

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("=====MySimpleJob job excute ====start ========>"+shardingContext.getShardingParameter());
            log.info(String.format("----MySimpleJobOther --Thread ID: %s, 任務總片數: %s, " +
                            "当前分片項: %s.当前參數: %s," +
                            "当前任務名稱: %s.当前任務參數: %s",
                    Thread.currentThread().getId(),
                    shardingContext.getShardingTotalCount(),
                    shardingContext.getShardingItem(),
                    shardingContext.getShardingParameter(),
                    shardingContext.getJobName(),
                    shardingContext.getJobParameter()

            ));

        log.info("=====MySimpleJob job excute ====end ========>"+shardingContext.getShardingParameter());
    }
}
