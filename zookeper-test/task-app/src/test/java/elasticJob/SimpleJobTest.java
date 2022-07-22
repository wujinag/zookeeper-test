package elasticJob;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.elasticjob.infra.handler.sharding.impl.AverageAllocationJobShardingStrategy;
import org.apache.shardingsphere.elasticjob.reg.base.CoordinatorRegistryCenter;
import org.apache.shardingsphere.elasticjob.reg.zookeeper.ZookeeperConfiguration;
import org.apache.shardingsphere.elasticjob.reg.zookeeper.ZookeeperRegistryCenter;

public class SimpleJobTest {
    public static void main(String[] args) {
        CoordinatorRegistryCenter registryCenter = new ZookeeperRegistryCenter(
                new ZookeeperConfiguration("localhost:2181","ejob-standalone"));
        registryCenter.init();
        // 数据源 , 事件执行持久化策略
        HikariDataSource dataSource =new HikariDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://192.168.1.101:3306/zookeper?useUnicode=true&characterEncoding=utf-8");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        JobEventConfiguration jobEventConfig = new JobEventRdbConfiguration(dataSource);
        // 定义作业核心配置
        JobCoreConfiguration coreConfig = JobCoreConfiguration
                .newBuilder("MySimpleJob", "0/20 * * * * ?", 4)
                .shardingItemParameters("0=RDP, 1=CORE, 2=SIMS, 3=ECIF").failover(true).build();
        // 定义SIMPLE类型配置
        SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(
                coreConfig, MySimpleJob.class.getCanonicalName());
        // 作业分片策略
        // 基于平均分配算法的分片策略
        String jobShardingStrategyClass = AverageAllocationJobShardingStrategy.class.getCanonicalName();
//        // 定义Lite作业根配置
//         LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).jobShardingStrategyClass(jobShardingStrategyClass).build();
////        LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).overwrite(true).build();
//
//        // 构建Job
//        new JobScheduler(registryCenter, simpleJobRootConfig).init();
//        new JobScheduler(registryCenter, simpleJobRootConfig, jobEventConfig).init();

    }
}
