package com.edurt.sli.guice.sample;

import com.google.inject.Guice;
import com.google.inject.Inject;

public class ApplicationStatic {

    @Inject
    private static Service service;

    public static void main(String[] args) {
        Guice.createInjector(binder -> binder.requestStaticInjection(ApplicationStatic.class));
        ApplicationStatic.service.print("Static");
    }

}
