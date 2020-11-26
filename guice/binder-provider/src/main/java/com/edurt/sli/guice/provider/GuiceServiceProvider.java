package com.edurt.sli.guice.provider;

import com.google.inject.Provider;

public class GuiceServiceProvider implements Provider<GuiceService> {

    @Override
    public GuiceService get() {
        return new GuiceServiceImpl();
    }

}
