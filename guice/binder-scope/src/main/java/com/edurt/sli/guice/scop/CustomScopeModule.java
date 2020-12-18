package com.edurt.sli.guice.scop;

import com.google.inject.AbstractModule;

public class CustomScopeModule
        extends AbstractModule
{
    @Override
    protected void configure()
    {
        bindScope(com.edurt.sli.guice.seso.CustomScope.class, new com.edurt.sli.guice.seso.CustomScopeImpl());
    }
}
