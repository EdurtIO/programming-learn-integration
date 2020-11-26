package com.edurt.sli.guice.provider;

import com.google.inject.ProvidedBy;

@ProvidedBy(value = GuiceServiceProvider.class)
public interface GuiceService {

    void say(String input);

}
