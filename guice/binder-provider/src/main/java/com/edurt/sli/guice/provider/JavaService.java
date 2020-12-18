package com.edurt.sli.guice.provider;

public class JavaService implements Service {

    @Override
    public void println(String input) {
        System.out.println(input);
    }

}
