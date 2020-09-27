package com.edurt.sli.guice.multiple;

import com.google.inject.Guice;
import com.google.inject.Inject;

public class Application {

    @Inject
    @Java
    public Service java;

    @Inject
    @com.edurt.sli.guice.multiple.Guice
    public Service guice;

    public static void main(String[] args) {
        Application application = Guice.createInjector(binder -> {
            binder.bind(Service.class).annotatedWith(Java.class).to(JavaService.class);
            binder.bind(Service.class).annotatedWith(com.edurt.sli.guice.multiple.Guice.class).to(GuiceService.class);
        }).getInstance(Application.class);
        application.guice.print("sss");
        application.java.print("sss");
    }

}
