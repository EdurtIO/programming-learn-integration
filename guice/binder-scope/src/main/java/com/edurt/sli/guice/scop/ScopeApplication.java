package com.edurt.sli.guice.scop;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;

public class ScopeApplication
{
    public static void main(String[] args)
    {
        Injector injector = Guice.createInjector(binder -> binder.bind(com.edurt.sli.guice.seso.Service.class).to(ScopeService.class).in(Scopes.SINGLETON));
        com.edurt.sli.guice.seso.Service service = injector.getInstance(com.edurt.sli.guice.seso.Service.class);
        service.println("Scope");
    }
}
