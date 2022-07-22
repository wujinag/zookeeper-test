package com.task.schedule.elasticJob.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import org.springframework.stereotype.Component;

/**
 * 自定义业务job
 * @author wujiang
 * @date 2022/7/22 14:03

 */
@Component
public class MySimpleJob implements SimpleJob {

    @Override
    public void execute(ShardingContext shardingContext) {
        System.out.println(shardingContext.getShardingParameter());
    }
}
