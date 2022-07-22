package com.task.schedule.elasticJob.task;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@ConditionalOnExpression("'${zookeeper.address}'.length() > 0")
public class RegistryCenterConfig {

    @Value("${zookeeper.address}")
    private   String address;

    @Value("${zookeeper.namespace}")
    private  String namespace;

    @Value("${zookeeper.connectionTimeout}")
    private  int connectionTimeout;

    @Value("${zookeeper.sessionTimeout}")
    private  int sessionTimeout;

    @Value("${zookeeper.maxRetries}")
    private  int maxRetries;
    
    /** 
     *  把注册中心加载到spring容器
     * @author wujiang
     * @date 2022/7/22 13:51
     * @return ZookeeperRegistryCenter
     */
    @Bean(initMethod = "init" )
   public ZookeeperRegistryCenter registryCenter(){
       ZookeeperConfiguration configuration = new ZookeeperConfiguration(address,namespace);
       configuration.setConnectionTimeoutMilliseconds(connectionTimeout);
       configuration.setSessionTimeoutMilliseconds(sessionTimeout);
       configuration.setMaxRetries(maxRetries);
       return new ZookeeperRegistryCenter(configuration);
   }
}
