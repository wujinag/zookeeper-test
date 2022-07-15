package com.redis.app.zk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@EnableConfigurationProperties
@SpringBootApplication(scanBasePackages={"com"})
@MapperScan("com.wuj.mapper")
public class ZookeeperTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZookeeperTestApplication.class, args);
    }

}
