package com.edurt.sli.guice.multiple;

public class GuiceService implements Service {

    @Override
    public void print(String source) {
        System.out.println("Guice Service " + source);
    }

}
