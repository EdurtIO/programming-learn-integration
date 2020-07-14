package com.edurt.sli.guice.sample;

import com.google.inject.Guice;
import com.google.inject.Inject;

public class ApplicationBinder {

    @Inject
    private Service service;

    public static void main(String[] args) {
        ApplicationBinder applicationBinder = new ApplicationBinder();
        Guice.createInjector(binder -> binder.requestInjection(applicationBinder));
        applicationBinder.service.print("Test");
    }

}
