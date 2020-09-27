package com.edurt.sli.guice.multiple;

import com.google.inject.Inject;

public class ApplicationStatic {

    @Inject
    @Java
    public static Service java;

    @Inject
    @com.edurt.sli.guice.multiple.Guice
    public static Service guice;

    public static void main(String[] args) {
        com.google.inject.Guice.createInjector(binder -> {
            binder.bind(Service.class).annotatedWith(Java.class).to(JavaService.class);
            binder.bind(Service.class).annotatedWith(com.edurt.sli.guice.multiple.Guice.class).to(GuiceService.class);
            binder.requestStaticInjection(ApplicationStatic.class);
        });
        ApplicationStatic.guice.print("sss");
        ApplicationStatic.java.print("sss");
    }

}
