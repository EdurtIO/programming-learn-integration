package com.edurt.sli.slismh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
@Component(value = "com.edurt.sli.slismh")
public class SpringBootMyBatisPageHelperIntegration {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMyBatisPageHelperIntegration.class, args);
    }

}
