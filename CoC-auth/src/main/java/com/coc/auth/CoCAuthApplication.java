package com.coc.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan("com.coc")
@MapperScan("com.coc.auth.mapper")
public class CoCAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoCAuthApplication.class, args);
    }

}
