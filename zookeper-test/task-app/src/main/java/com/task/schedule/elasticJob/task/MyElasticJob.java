package com.task.schedule.elasticJob.task;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class MyElasticJob implements SimpleJob {
    @Override
    public void execute(ShardingContext shardingContext) {
        if (shardingContext.equals(0)){
            log.info( String.format("分片项 ShardingItem: %s | 运行时间: %s | 线程ID: %s | 分片参数: %s ",
                    shardingContext.getShardingItem(),
                    new SimpleDateFormat("HH:mm:ss").format(new Date()),
                    Thread.currentThread().getId(),
                    shardingContext.getShardingParameter()));
        }
    }
}
