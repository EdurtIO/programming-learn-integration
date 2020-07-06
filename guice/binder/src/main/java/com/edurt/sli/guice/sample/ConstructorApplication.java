package com.edurt.sli.guice.sample;

import com.google.inject.Guice;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;

public class ConstructorApplication {

    private ConstructorService service;

    @Inject
    public ConstructorApplication(ConstructorService service) {
        this.service = service;
    }

    public ConstructorService getService() {
        return service;
    }

    public static void main(String[] args) {
        ConstructorApplication instance = Guice.createInjector().getInstance(ConstructorApplication.class);
        instance.getService().print();
    }

}

@ImplementedBy(ConstructorServiceImpl.class)
interface ConstructorService {

    void print();

}

class ConstructorServiceImpl implements ConstructorService {

    @Override
    public void print() {
        System.out.println("Hello Guice By Constructor");
    }

}