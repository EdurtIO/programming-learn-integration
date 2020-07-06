package com.edurt.sli.guice.sample;

import com.google.inject.AbstractModule;

public class GuiceBasicModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(GuiceBasicService.class).to(GuiceBasicServiceImpl.class);
    }

}
