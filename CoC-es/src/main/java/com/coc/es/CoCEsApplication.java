package com.coc.es;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan("com.coc")
public class CoCEsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoCEsApplication.class, args);
    }

}
