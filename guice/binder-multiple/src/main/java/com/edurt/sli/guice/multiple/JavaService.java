package com.edurt.sli.guice.multiple;

public class JavaService implements Service {

    @Override
    public void print(String source) {
        System.out.println("Java Service " + source);
    }

}
