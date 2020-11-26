package com.edurt.sli.guice.provider;

import com.google.inject.Provider;

public class JavaServiceProvider implements Provider<Service> {

    @Override
    public Service get() {
        return new JavaService();
    }

}
