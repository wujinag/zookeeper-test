package com.task.schedule.elasticJob.task;

import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class MyElasticJobListener implements ElasticJobListener {

    @Override
    public void beforeJobExecuted(ShardingContexts shardingContexts) {
        log.info("分布式监听器开始……");
    }

    @Override
    public void afterJobExecuted(ShardingContexts shardingContexts) {
        log.info("分布式监听器结束……");
    }
}
