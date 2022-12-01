package com.coc.es.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EsIndexConfig {

    @Value("${spring.elasticsearch.synchroniz_form.system_employ_job}")
    private String indexJobName;

    @Bean(name = "indexJobName")
    public String index() {
        return indexJobName;
    }
}
