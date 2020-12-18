package com.edurt.sli.guice.scop;

import javax.inject.Singleton;

import java.time.LocalDateTime;

@Singleton
public class ScopeService
        implements com.edurt.sli.guice.seso.Service
{
    @Override
    public void println(String source)
    {
        System.out.println(String.format("%s on %s", source, LocalDateTime.now()));
    }
}
