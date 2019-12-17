package com.edurt.sli.slismps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
@Component(value = "com.edurt.sli.slismps")
public class SpringBootMyBatisProviderSelectIntegration {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMyBatisProviderSelectIntegration.class, args);
    }

}
