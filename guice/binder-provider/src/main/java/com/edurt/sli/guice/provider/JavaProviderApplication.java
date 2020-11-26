package com.edurt.sli.guice.provider;

import com.google.inject.*;

public class JavaProviderApplication {

    @Inject
    private Service service;

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new Module() {
            @Override
            public void configure(Binder binder) {
                binder.bind(Service.class).toProvider(JavaServiceProvider.class);
            }
        });
        JavaProviderApplication application = injector.getInstance(JavaProviderApplication.class);
        application.service.println("Hello Java");
    }

}
