package com.zookeper.zk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;


@EnableConfigurationProperties
@SpringBootApplication(scanBasePackages={"com"})
@MapperScan("com.wuj.mapper")
public class ZookeeperTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZookeeperTestApplication.class, args);
    }

}
