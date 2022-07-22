package com.task.schedule.elasticJob.task;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.reg.zookeeper.ZookeeperConfiguration;
import org.apache.shardingsphere.elasticjob.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SimpleJob {
    ZookeeperRegistryCenter center =
                new ZookeeperRegistryCenter(new ZookeeperConfiguration("localhot:2181","ejob-stanlone"));


}
