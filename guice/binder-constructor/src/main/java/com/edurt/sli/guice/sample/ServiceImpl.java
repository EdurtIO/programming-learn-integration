package com.edurt.sli.guice.sample;

public class ServiceImpl implements Service {
    @Override
    public void print(String source) {
        System.out.println(String.format("Hello Guice, %s", source));
    }

}
