package com.task.schedule.elasticJob.task;

import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.lite.api.strategy.impl.OdevitySortByNameJobShardingStrategy;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Map;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class ElasticJobAutoConfiguration {

    private final ApplicationContext applicationContext;

    private final ZookeeperRegistryCenter center;

    private final JobEventConfiguration jobEventConfiguration;

    private final Environment environment;


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
            //获取系统配置的定时任务参数
            String cron = StringUtils.defaultIfBlank(
                    environment.getProperty(jobName+".cron").toString(), elasticSimpleJobAnnotation.cron());

            int shardingTotalCount = Objects.nonNull(environment.getProperty(jobName+".shardingTotalCount"))
                        ? Integer.valueOf(environment.getProperty(jobName+".shardingTotalCount")):elasticSimpleJobAnnotation.shardingTotalCount();

            String shardingItemParameters = StringUtils.defaultIfBlank(
                    environment.getProperty(jobName+".shardingItemParameters").toString(),elasticSimpleJobAnnotation.shardingItemParameters());

            //定义SIMPLE类型配置
            SimpleJobConfiguration simpleJobConfiguration = new SimpleJobConfiguration(
                    JobCoreConfiguration.newBuilder(
                            jobName,
                            cron,
                            shardingTotalCount
                    ).shardingItemParameters(shardingItemParameters)
                            .build(),
                    simpleJob.getClass().getCanonicalName());

            //定义Lite作业根配置
            //AverageAllocationJobShardingStrategy的缺点是一旦分片数小于Job实例数，
            // 作业将永远分配至IP地址靠前的Job实例上，导致IP地址靠后的Job实例空闲。
            // 而OdevitySortByNameJobShardingStrategy则可以根据作业名称重新分配Job实例负载。
            LiteJobConfiguration liteJobConfiguration =
                    LiteJobConfiguration.newBuilder(simpleJobConfiguration).overwrite(true)
                    // jobShardingStrategyClass：分片策略
                    .jobShardingStrategyClass(OdevitySortByNameJobShardingStrategy.class.getCanonicalName())
                    .build();
            SpringJobScheduler jobScheduler = new SpringJobScheduler(simpleJob, center, liteJobConfiguration, jobEventConfiguration);
            jobScheduler.init();
        }
    }
}
