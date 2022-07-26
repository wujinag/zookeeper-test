package com.task.schedule.elasticJob.task;

import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.api.strategy.impl.AverageAllocationJobShardingStrategy;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.internal.schedule.JobRegistry;
import com.dangdang.ddframe.job.lite.internal.schedule.JobScheduleController;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ElasticJobHandler {

    private final ZookeeperRegistryCenter zookeeperRegistryCenter;


    /**
     * 添加、更新任务
     * <p>
     * 传参可以封装成一个对象
     *
     * @param jobName:任务名称，注意不要带#特殊字符。
     * @param simpleJob:实现simpleJob自己的任务类
     * @param cron
     * @param shardingTotalCount：分片总数
     * @param shardingItemParameters：个性化参数，可以和分片项匹配对应关系，用于将分片项的数字转换为更加可读的业务代码。
     */
    public void addJob(final String jobName, final SimpleJob simpleJob, final String cron, final int shardingTotalCount,
                       final String shardingItemParameters) {
        simpleJobScheduler(jobName, simpleJob, cron, shardingTotalCount, shardingItemParameters).init();
    }


    private JobScheduler simpleJobScheduler(final String jobName,
                                            final SimpleJob simpleJob,
                                            final String cron,
                                            final int shardingTotalCount,
                                            final String shardingItemParameters) {
        LiteJobConfiguration liteJobConfiguration = getLiteJobConfiguration(jobName, simpleJob.getClass(), cron, shardingTotalCount, shardingItemParameters);
        return new SpringJobScheduler(simpleJob, zookeeperRegistryCenter, liteJobConfiguration);
    }

    private LiteJobConfiguration getLiteJobConfiguration(final String jobName,
                                                         final Class<? extends SimpleJob> jobClass,
                                                         final String cron,
                                                         final int shardingTotalCount,
                                                         final String shardingItemParameters) {

        /**
         * JobCoreConfiguration.newBuilder(String jobName, String cron, int shardingTotalCount)
         * 其中jobName是任务名称
         * cron是cron表达式
         * shardingTotalCount是分片总数
         * 这样就可以把jobClass.getName()变成我们自己命名的jobName
         */
//        JobCoreConfiguration jobCoreConfiguration = JobCoreConfiguration.newBuilder(
//                jobClass.getName(), cron, shardingTotalCount).shardingItemParameters(shardingItemParameters).build();

        JobCoreConfiguration jobCoreConfiguration = JobCoreConfiguration.newBuilder(
                jobName, cron, shardingTotalCount)
                .shardingItemParameters(shardingItemParameters)
                //开启失效转移
                .misfire(true)
                .build();


        JobTypeConfiguration jobTypeConfiguration = new SimpleJobConfiguration(jobCoreConfiguration, jobClass.getCanonicalName());

        return LiteJobConfiguration.newBuilder(jobTypeConfiguration)
                // overwrite：本地配置是否可覆盖注册中心配置
                .overwrite(true)
                // jobShardingStrategyClass：分片策略
                .jobShardingStrategyClass(AverageAllocationJobShardingStrategy.class.getCanonicalName())
                .build();


    }

    /**
     * 删除定时任务
     *
     * @param jobName
     */
    public void removeJob(String jobName) {
        JobScheduleController jobScheduleController = JobRegistry.getInstance().getJobScheduleController(jobName);
        if (jobScheduleController != null) {
            //暂停任务
            jobScheduleController.pauseJob();
            //关闭调度器
            jobScheduleController.shutdown();
            //删除节点
            zookeeperRegistryCenter.remove("/" + jobName);

        }

    }

    /**
     * 暂停任务
     *
     * @param jobName
     */
    public void pauseJob(String jobName) {
        JobScheduleController jobScheduleController = JobRegistry.getInstance().getJobScheduleController(jobName);
        if (jobScheduleController != null) {
            jobScheduleController.pauseJob();
        }
    }


    /**
     * 添加、更新任务,封装对象
     *
     * @param elasticJobPO
     * @param simpleJob
     */
    public void addJobToBean(ElasticJobPO elasticJobPO, final SimpleJob simpleJob) {
        simpleJobScheduler(elasticJobPO.getName(), simpleJob, elasticJobPO.getCron(), elasticJobPO.getShardingTotalCount(), elasticJobPO.getShardingItemParameters()).init();
    }

    /**
     * 立刻启动作业
     *
     * @param jobName
     * @param
     */
    public boolean start(String jobName) {
        JobScheduleController jobScheduleController = JobRegistry.getInstance().getJobScheduleController(jobName);
        if (jobScheduleController != null) {
            jobScheduleController.triggerJob();
            return true;
        }
        return false;
    }
}
