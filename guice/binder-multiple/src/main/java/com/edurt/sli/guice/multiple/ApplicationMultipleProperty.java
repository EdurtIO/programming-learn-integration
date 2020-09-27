package com.edurt.sli.guice.multiple;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

public class ApplicationMultipleProperty {

    @Inject
    @Named("Java")
    public Service java;

    @Inject
    @Named("Guice")
    public Service guice;

    public static void main(String[] args) {
        ApplicationMultipleProperty application = Guice.createInjector(binder -> {
            binder.bind(Service.class).annotatedWith(Names.named("Java")).to(JavaService.class);
            binder.bind(Service.class).annotatedWith(Names.named("Guice")).to(GuiceService.class);
        }).getInstance(ApplicationMultipleProperty.class);
        application.guice.print("sss");
        application.java.print("sss");
    }

}
