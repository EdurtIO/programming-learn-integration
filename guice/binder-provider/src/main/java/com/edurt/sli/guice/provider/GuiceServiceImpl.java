package com.edurt.sli.guice.provider;

public class GuiceServiceImpl implements GuiceService {

    @Override
    public void say(String input) {
        System.out.println(input);
    }

}
