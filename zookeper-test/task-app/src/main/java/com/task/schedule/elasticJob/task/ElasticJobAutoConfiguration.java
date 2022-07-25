package com.task.schedule.elasticJob.task;

import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.lite.api.strategy.impl.AverageAllocationJobShardingStrategy;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ElasticJobAutoConfiguration {

    private final ApplicationContext applicationContext;

    @Resource
    private ZookeeperRegistryCenter center;

    @Resource
    private JobEventConfiguration jobEventConfiguration;


    @Bean
    public void initElasticJob() {

        Map<String, SimpleJob> map = applicationContext.getBeansOfType(SimpleJob.class);

        for (Map.Entry<String, SimpleJob> entry : map.entrySet()) {
            SimpleJob simpleJob = entry.getValue();
            ElasticJobConfig elasticSimpleJobAnnotation = simpleJob.getClass().getAnnotation(ElasticJobConfig.class);
            if (elasticSimpleJobAnnotation == null) {
                continue;
            }
            String jobName = StringUtils.defaultIfBlank(elasticSimpleJobAnnotation.name(), simpleJob.getClass().getSimpleName());
            String cron = StringUtils.defaultIfBlank(elasticSimpleJobAnnotation.cron(), elasticSimpleJobAnnotation.value());
            //定义SIMPLE类型配置
            SimpleJobConfiguration simpleJobConfiguration = new SimpleJobConfiguration(
                    JobCoreConfiguration.newBuilder(
                            jobName,
                            cron,
                            elasticSimpleJobAnnotation.shardingTotalCount()
                    ).shardingItemParameters(elasticSimpleJobAnnotation.shardingItemParameters())
                            .build(),
                    simpleJob.getClass().getCanonicalName());
            //定义Lite作业根配置
            LiteJobConfiguration liteJobConfiguration =
                    LiteJobConfiguration.newBuilder(simpleJobConfiguration).overwrite(true)
                    // jobShardingStrategyClass：分片策略
                    .jobShardingStrategyClass(AverageAllocationJobShardingStrategy.class.getCanonicalName())
                    .build();
            SpringJobScheduler jobScheduler = new SpringJobScheduler(simpleJob, center, liteJobConfiguration, jobEventConfiguration);
            jobScheduler.init();
        }
    }
}
