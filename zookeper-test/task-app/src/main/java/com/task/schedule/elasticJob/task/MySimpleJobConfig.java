package com.task.schedule.elasticJob.task;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class MySimpleJobConfig {

    @Resource
    private ZookeeperRegistryCenter center;

    @Resource
    private JobEventConfiguration jobEventConfiguration;


    @Bean
    public SimpleJob simpleJob(){
        return  new MySimpleJob();
    }

    @Bean(initMethod = "init")
    public JobScheduler simpleJobScheduler(
            final SimpleJob simpleJob,
            @Value("${simpleJob.cron}") final String cron,
            @Value("${simpleJob.shardingTotalCount}") final int shardingTotalCount,
            @Value("${simpleJob.shardingItemParameters}") final String shardingItemParameters,
            @Value("${simpleJob.jobParameter}") final String jobParameter,
            @Value("${simpleJob.failover}") final boolean failover,
            @Value("${simpleJob.monitorExecution}") final boolean monitorExecution,
            @Value("${simpleJob.monitorPort}") final int monitorPort,
            @Value("${simpleJob.maxTimeDiffSeconds}") final int maxTimeDiffSeconds,
            @Value("${simpleJob.jobShardingStrategyClass}") final String jobShardingStrategyClass
    ){
        return new SpringJobScheduler(simpleJob,
                center,
                getLiteJobConfiguration(
                        simpleJob.getClass(),cron,
                        shardingTotalCount,
                        shardingItemParameters,
                        jobParameter,
                        failover,
                        monitorExecution,
                        monitorPort,
                        maxTimeDiffSeconds,
                        jobShardingStrategyClass),
                jobEventConfiguration,
                new MyElasticJobListener());

    }

    private LiteJobConfiguration getLiteJobConfiguration(Class<? extends SimpleJob> jobClass, String cron,
                                                         int shardingTotalCount, String shardingItemParameters, String jobParameter, boolean failover,
                                                         boolean monitorExecution, int monitorPort, int maxTimeDiffSeconds, String jobShardingStrategyClass) {

        //定义作业核心配置
        JobCoreConfiguration jobCoreConfiguration = JobCoreConfiguration
                .newBuilder(jobClass.getName(), cron, shardingTotalCount)
                .misfire(true)
                .failover(failover)
                .jobParameter(jobParameter)
                .shardingItemParameters(shardingItemParameters)
                .build();

        //定义SIMPLE类型配置
        SimpleJobConfiguration simpleJobConfiguration = new SimpleJobConfiguration(jobCoreConfiguration, jobClass.getCanonicalName());

        //定义Lite作业根配置
        LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration.newBuilder(simpleJobConfiguration)
                .jobShardingStrategyClass(jobShardingStrategyClass)
                .monitorExecution(monitorExecution)
                .monitorPort(monitorPort)
                .maxTimeDiffSeconds(maxTimeDiffSeconds)
                .overwrite(true)
                .build();

        return liteJobConfiguration;
    }
}


