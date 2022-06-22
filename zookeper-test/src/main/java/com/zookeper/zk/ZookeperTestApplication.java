package com.zookeper.zk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@EnableConfigurationProperties
@SpringBootApplication(scanBasePackages={"config"})
public class ZookeperTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZookeperTestApplication.class, args);
    }

}
