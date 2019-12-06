package com.edurt.sli.slismpu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
@Component(value = "com.edurt.sli.slismpu")
public class SpringBootMyBatisProviderUpdateDeleteIntegration {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMyBatisProviderUpdateDeleteIntegration.class, args);
    }

}
