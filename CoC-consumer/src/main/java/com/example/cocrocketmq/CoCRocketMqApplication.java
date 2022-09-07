package com.example.cocrocketmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CoCRocketMqApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoCRocketMqApplication.class, args);
    }

}
