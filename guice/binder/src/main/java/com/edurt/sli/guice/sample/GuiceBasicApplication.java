package com.edurt.sli.guice.sample;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Named;

public class GuiceBasicApplication {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new GuiceBasicModule());
        GuiceBasicService service = injector.getInstance(GuiceBasicService.class);

        service.print("Hello Guice");
    }

}
