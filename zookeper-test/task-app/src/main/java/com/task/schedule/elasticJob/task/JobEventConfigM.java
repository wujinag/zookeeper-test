package com.task.schedule.elasticJob.task;

import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * 配置JobEventConfig事件追踪
 * @author wujiang
 * @date 2022/7/25 10:54

 */

@Configuration
public class JobEventConfigM {

    @Resource
    private DataSource dataSource;

    @Bean
    public JobEventConfiguration jobEventConfig(){
        return  new JobEventRdbConfiguration(dataSource);

    }}
