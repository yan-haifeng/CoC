package com.coc.netty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan("com.coc")
public class CoCNettyApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoCNettyApplication.class, args);
    }
}
