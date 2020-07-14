package com.edurt.sli.guice.sample;

import com.google.inject.Guice;
import com.google.inject.Inject;

public class Application {

    private Service service;

    @Inject
    public Application(Service service) {
        this.service = service;
    }

    public Service getService() {
        return service;
    }

    public static void main(String[] args) {
        Application application = Guice.createInjector().getInstance(Application.class);
        application.service.print("Test");
    }

}
