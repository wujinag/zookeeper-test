package com.task.schedule.elasticJob.task;

import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import jodd.time.TimeUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@NoArgsConstructor
public class MyElasticJobListener implements ElasticJobListener {
    private long beginTime = 0;
    @Override
    public void beforeJobExecuted(ShardingContexts shardingContexts) {
        beginTime = System.currentTimeMillis();
        log.info("===>{} JOB BEGIN TIME: {} <===",shardingContexts.getJobName(), TimeUtil.formatHttpDate(beginTime));
    }

    @Override
    public void afterJobExecuted(ShardingContexts shardingContexts) {
        long endTime = System.currentTimeMillis();
        log.info("===>{} JOB END TIME: {},TOTAL CAST: {} <===",shardingContexts.getJobName(), TimeUtil.formatHttpDate(endTime), endTime - beginTime);
    }
}
