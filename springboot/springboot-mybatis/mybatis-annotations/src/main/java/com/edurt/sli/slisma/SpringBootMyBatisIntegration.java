package com.edurt.sli.slisma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
@Component(value = "com.edurt.sli.slisma")
public class SpringBootMyBatisIntegration {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMyBatisIntegration.class, args);
    }

}
