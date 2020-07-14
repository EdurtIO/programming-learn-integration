package com.edurt.sli.guice.sample;

import com.google.inject.ImplementedBy;

@ImplementedBy(ServiceImpl.class)
public interface Service {

    void print(String source);

}
