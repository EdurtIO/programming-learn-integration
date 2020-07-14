package com.edurt.sli.guice.sample;

import com.google.inject.Inject;

public class ApplicationCustom {

    @Inject
    private Service service;

    public Service getService() {
        return this.service;
    }

    public static void main(String[] args) {
        ApplicationCustom custom = new ApplicationCustom();
        custom.getService().print("Test");
    }

}
