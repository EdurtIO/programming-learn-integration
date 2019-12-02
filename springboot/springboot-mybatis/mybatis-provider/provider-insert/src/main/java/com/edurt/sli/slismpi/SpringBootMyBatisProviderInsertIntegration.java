package com.edurt.sli.slismpi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
@Component(value = "com.edurt.sli.slismpi")
public class SpringBootMyBatisProviderInsertIntegration {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMyBatisProviderInsertIntegration.class, args);
    }

}
