package com.edurt.sli.guice.provider;

import com.google.inject.Guice;
import com.google.inject.Inject;

public class GuiceAutoProviderApplication {

    @Inject
    private GuiceService guiceService;

    public static void main(String[] args) {
        GuiceAutoProviderApplication application = Guice.createInjector().getInstance(GuiceAutoProviderApplication.class);
        application.guiceService.say("Guice Service");
    }

}
