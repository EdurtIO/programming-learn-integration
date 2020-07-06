package com.edurt.sli.guice.sample;

public class GuiceBasicServiceImpl implements GuiceBasicService {

    public void print(String output) {
        System.out.println(String.format("print %s", output));
    }

}
