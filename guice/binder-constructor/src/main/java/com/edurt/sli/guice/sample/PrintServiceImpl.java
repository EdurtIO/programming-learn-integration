package com.edurt.sli.guice.sample;

public class PrintServiceImpl implements PrintService {

    @Override
    public void print() {
        System.out.println("print Service");
    }

}
