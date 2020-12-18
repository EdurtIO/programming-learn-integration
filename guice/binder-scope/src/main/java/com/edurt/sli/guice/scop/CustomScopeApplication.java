package com.edurt.sli.guice.scop;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class CustomScopeApplication
{
    public static void main(String[] args)
    {
//        Injector injector = Guice.createInjector(binder -> {
//            binder.bind(Service.class).to(ScopeService.class).in(new CustomScopeImpl());
//        });
        Injector injector = Guice.createInjector(new CustomScopeModule(), binder -> {
            binder.bind(com.edurt.sli.guice.seso.Service.class).to(com.edurt.sli.guice.seso.ScopeService.class).in(new com.edurt.sli.guice.seso.CustomScopeImpl());
        });
        for (int i = 0; i < 3; i++) {
            System.out.println(injector.getInstance(com.edurt.sli.guice.seso.Service.class).hashCode());
        }
    }
}
